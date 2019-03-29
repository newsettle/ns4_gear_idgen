package com.creditease.ns4.gear.idgen.cache;

import com.creditease.ns4.gear.idgen.common.Constant;
import com.creditease.ns4.gear.idgen.common.NsLogger;
import com.creditease.ns4.gear.idgen.entity.BaseKeys;
import com.creditease.ns4.gear.idgen.service.BaseDao;
import com.creditease.ns4.gear.idgen.util.ConversionUtil;
import com.creditease.ns4.gear.idgen.util.StringDealUtil;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 缓存对象
 */
public class KeyInfo implements Callable<Boolean> {
    /**
     * keyName
     */
    private String name;
    /**
     * 数据库信息
     */
    private BaseKeys basKeysVO = null;
    /**
     * 数据库操作
     */
    private BaseDao basDAO;
    /**
     * 当前缓存最大值
     */
    private long keyMax;
    /**
     * 更新最大值缓存锁
     */
    private Lock updateMaxLock = new ReentrantLock();
    private BlockingQueue<Long> seqQueue = new ArrayBlockingQueue<>(1000);
    private SeqGenerator seqGenerator = null;

    public KeyInfo(String name, BaseDao basDAO) {
        this.name = name;
        this.basDAO = basDAO;
    }

    public KeyInfo(String name, BaseDao basDAO, BaseKeys basKeysVO) {
        this.name = name;
        this.basKeysVO = basKeysVO;
        this.basDAO = basDAO;
    }

    public String createKey() throws Exception {
        return createKey(basKeysVO.getKeyPrefix(), basKeysVO.getKeySuffix(), basKeysVO.getKeyLength(), 1);
    }

    public String createKey(String prefix, String suffix, int length, int flag) throws Exception {
        NsLogger.logger.info("KeyInfoServiceImpl.createKey prefix = {} suffix = {} length = {} flag = {}", prefix , suffix , length
                , flag);
        String strKey = String.valueOf(createKeyAsLong());
        NsLogger.logger.info("createKeyAsLong return key = {}" , strKey);
        if (Constant.KEY_DIGIT_36.equals(basKeysVO.getKeyDigit()) && flag == 1) {
            strKey = ConversionUtil._10_to_36(Long.parseLong(strKey));
        }

        if (Constant.KEY_DIGIT_62.equals(basKeysVO.getKeyDigit()) && flag == 1) {
            strKey = ConversionUtil._10_to_62(Long.parseLong(strKey));
        }

        NsLogger.logger.info("类型处理后 key = {} ", strKey);
        prefix = prefix == null ? Constant.EMPTY : prefix.trim();
        suffix = suffix == null ? Constant.EMPTY : suffix.trim();
        prefix = StringDealUtil.parseValue(prefix);
        suffix = StringDealUtil.parseValue(suffix);
        int maxLen = length - prefix.length() - suffix.length();
        if (strKey.length() > maxLen) {
            strKey = strKey.substring(strKey.length() - maxLen);
        }
        NsLogger.logger.info("长度处理后 key = {}" , strKey);
        int zeroLen = length - prefix.length() - strKey.length() - suffix.length();
        StringBuffer sb = new StringBuffer(length);
        sb.append(prefix);
        for (int i = 0; i < zeroLen; i++) {
            sb.append("0");
        }
        sb.append(strKey);
        sb.append(suffix);
        NsLogger.logger.info("createKey返回值:{}" , sb.toString());
        return sb.toString();
    }

    public long createKeyAsLong() throws Exception {
        NsLogger.logger.info("KeyInfoServiceImpl.createKeyAsLong begin");
        //获取序列号
        Long seq = getSeq();
        NsLogger.logger.info("createKeyAsLong -- seq:{}", seq);
        if (seq == null) {
            throw new Exception("当前无可用的序列号！");
        }
        //判断是否需要更新DB 预分配序列号
        if (needUpdate(seq)) {
            //提交更新DB任务
            NsLogger.logger.info("KeyInfoServiceImpl提交更新任务");
            CacheUpdateJob.getInstance().submitJob(this);
        }
        NsLogger.logger.info("KeyInfoServiceImpl.createKeyAsLong end. seq = {}" , seq);
        return seq;
    }


    @Override
    public Boolean call() {
        NsLogger.logger.info("KeyInfoServiceImpl.call keyMax = {}", keyMax);
        updateMaxLock.lock();

        // 乐观锁获取数据库记录,并更新
        NsLogger.logger.info("KeyInfoServiceImpl.call 获取数据库信息");
        List<BaseKeys> baseKeys = null;
        try {
            baseKeys = basDAO.findKeyInfoByName(name);
        } catch (Exception e) {
            // 出现异常
            NsLogger.logger.error("获取数据库记录异常", e);
        }

        if (CollectionUtils.isEmpty(baseKeys)) {
            updateMaxLock.unlock();
            NsLogger.logger.info("KeyInfoServiceImpl.call baseKeys is Empty");
            return true;
        }

        NsLogger.logger.info("KeyInfoServiceImpl.call 获取数据库信息完毕 keyName:{},baseKeys size:{}", name, baseKeys.size());

        try {

            BaseKeys baseKey = baseKeys.get(0);
            long currentMax = Long.parseLong(baseKey.getKeyValue());
            NsLogger.logger.info("KeyInfoServiceImpl.call currentMax：{}", currentMax);
            long max;
            if (Long.MAX_VALUE - currentMax < baseKey.getKeyCache()) {
                // 超过了最大值 需要重新设置值
                max = baseKey.getKeyCache();
            } else {
                max = currentMax + baseKey.getKeyCache();
            }
            NsLogger.logger.info("KeyInfoServiceImpl.call 准备更新数据库信息 max: {}", max);
            int updateNum = basDAO.updateKeyInfoValue(max, name, baseKey.getVersion());
            NsLogger.logger.info("KeyInfoServiceImpl.call 记录更新条数 updateNum: {}", updateNum);
            if (updateNum == 1) {
                // 更新成功 设置对应的值
                updateFields(max, baseKey);
                updateMaxLock.unlock();
                NsLogger.logger.info("KeyInfoServiceImpl.call 记录更新成功");
                return true;
            }

        } catch (Exception e) {
            // 出现异常
            NsLogger.logger.error("乐观锁更新数据库异常", e);
        }
        updateMaxLock.unlock();
        NsLogger.logger.info("KeyInfoServiceImpl.call 重新加入提交队列 keyName: {}", this.name);
        // 重新的加入队列
        CacheUpdateJob.getInstance().submitJob(this);

        return false;
    }

    public boolean isNotInit() {
        return this.basKeysVO == null;
    }

    public Long getSeq() {
        try {
            return seqQueue.poll(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
        }
        return null;
    }

    private void updateFields(long max, BaseKeys baseKey) {
        // 首次初始化
        if (seqGenerator == null) {
            seqGenerator = new SeqGenerator(max - baseKey.getKeyCache(), max);
            seqGenerator.start();
        }
        keyMax = max;
        basKeysVO = baseKey;
        basKeysVO.setKeyValue(String.valueOf(max));
        basKeysVO.setVersion(baseKey.getVersion() + 1);
        synchronized (seqGenerator) {
            try {
                seqGenerator.notifyAll();
            } catch (Exception e) {
            }
        }
    }

    // 根据步长计算应该提前多少预申请序列号
    private long getSize(Integer size) {
        if (size <= 100) {
            return size / 2;
        }
        if (size <= 1000) {
            return size / 4;
        }
        if (size <= 10000) {
            return size / 8;
        }
        return size / 10;
    }

    private boolean needUpdate(Long seq) {
        return seq == seqGenerator.currentKeyMax - getSize(basKeysVO.getKeyCache());
    }

    private class SeqGenerator extends Thread {
        /**
         * // 当前缓存值
         */
        private long keyCurrent;
        /**
         * // 当前缓存最大值
         */
        private long currentKeyMax;

        public SeqGenerator(long keyCurrent, long currentKeyMax) {
            this.keyCurrent = keyCurrent;
            this.currentKeyMax = currentKeyMax;
        }

        @Override
        public void run() {
            NsLogger.logger.info("启动生成序列号线程=====:{}" , name);
            if (currentKeyMax != 0) {
                while (true) {
                    NsLogger.logger.info("生成序列号线程 name: {} keyCurrent: {} currentKeyMax: {}" , name , keyCurrent , currentKeyMax);
                    if (keyCurrent < currentKeyMax) {
                        try {
                            keyCurrent++;
                            seqQueue.put(keyCurrent);
                        } catch (InterruptedException e) {
                        }
                    }
                    //更新缓存最大值
                    if (keyCurrent == currentKeyMax) {
                        if (currentKeyMax != keyMax) {
                            currentKeyMax = keyMax;
                            keyCurrent = keyMax - basKeysVO.getKeyCache();
                            NsLogger.logger.info("生成序列号线程 name: {} keyMax: {} currentKeyMax :{} keyCurrent: {}" , name , keyMax , currentKeyMax , keyCurrent);
                        } else {
                            synchronized (this) {
                                try {
                                    this.wait();
                                } catch (InterruptedException e) {
                                }
                            }
                        }
                    }
                }
            }
            throw new RuntimeException("序号生成线程启动错误: " + name);
        }
    }
}