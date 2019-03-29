package com.creditease.ns4.gear.idgen.server;

import com.creditease.ns4.gear.idgen.cache.CacheUpdateJob;
import com.creditease.ns4.gear.idgen.cache.KeyInfo;
import com.creditease.ns4.gear.idgen.common.NsLogger;
import com.creditease.ns4.gear.idgen.entity.BaseKeys;
import com.creditease.ns4.gear.idgen.service.BaseDao;
import com.creditease.ns4.gear.idgen.service.KeyInfoService;
import com.creditease.ns4.gear.idgen.util.SpringContextHolder;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * server
 */
public class ServerManager {

    private static CopyOnWriteArrayList<KeyInfo> failList = new CopyOnWriteArrayList<>();

    public static void runJob() {

        try {
            if (cacheInitData()) {
                //启动异步更新线程
                CacheUpdateJob.getInstance().start();
            }
        } catch (Exception e) {
            NsLogger.logger.error(e.getMessage(), e);
            //异常情况下 关闭异步更新线程
            CacheUpdateJob.getInstance().shutDown();
        }
    }

    private static boolean cacheInitData() throws Exception {
        NsLogger.logger.info("cacheInitData()...");
        KeyInfoService keyInfoService = SpringContextHolder.getBean("keyInfoService");
        BaseDao dao = SpringContextHolder.getBean("baseDao");
        List<BaseKeys> keyList = dao.findAllKeyInfo();
        //初始化缓存
        String keyName;
        for (BaseKeys basekey : keyList) {
            keyName = basekey.getKeyName();
            NsLogger.logger.info("cacheInitData()... keyName: {}", keyName);
            KeyInfo keyInfo = new KeyInfo(keyName, dao, basekey);
            if (!keyInfo.call()) {
                failList.add(keyInfo);
                NsLogger.logger.info("cacheInitData()... keyName: {} 初始化失败", keyName);
            }
            //重试失败的初始化缓存 
            int loopCount = 0;
            while (!failList.isEmpty() && loopCount++ <= 3) {
                Thread.sleep(loopCount * 100);
                for (KeyInfo key : failList) {
                    if (!key.isNotInit()) {
                        failList.remove(key);
                    }
                }
            }
            if (!failList.isEmpty()) {
                throw new Exception("Key : " + keyName + " 初始化失败");
            }
            keyInfoService.putKeyInfo(keyName, keyInfo);
        }
        return true;
    }

}
