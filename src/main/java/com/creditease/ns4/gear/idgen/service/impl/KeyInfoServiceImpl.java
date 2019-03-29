package com.creditease.ns4.gear.idgen.service.impl;

import com.creditease.ns4.gear.idgen.cache.KeyInfo;
import com.creditease.ns4.gear.idgen.common.NsLogger;
import com.creditease.ns4.gear.idgen.service.BaseDao;
import com.creditease.ns4.gear.idgen.service.KeyInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service("keyInfoService")
public class KeyInfoServiceImpl implements KeyInfoService {

    private static final ConcurrentHashMap<String, KeyInfo> CACHEKEYBANK = new ConcurrentHashMap<String, KeyInfo>();
    /**
     * 首次初始化锁（启动时未初始化的）
     */
    private static Lock initLock = new ReentrantLock();

    @Autowired
    private BaseDao baseDao;

    @Override
    public String createKey(String name) throws Exception {
        return getKeyInfo(name).createKey();
    }

    @Override
    public String createKey(String name, String prefix, String suffix, int length) throws Exception {
        return getKeyInfo(name).createKey(prefix, suffix, length, 0);
    }

    @Override
    public long createKeyAsLong(String name) throws Exception {
        return getKeyInfo(name).createKeyAsLong();
    }

    /**
     * 从缓存取得KeyInfo。如果没有则初始化并添加到缓存。
     *
     * @param name keyName
     * @return keyInfo
     */
    protected KeyInfo getKeyInfo(String name) throws Exception {
        NsLogger.logger.info("KeyInfoServiceImpl.getKeyInfo name: {}", name);
        KeyInfo result = CACHEKEYBANK.get(name);
        if (result == null) {
            NsLogger.logger.info("应用初始化无此数据，需要初始化keyInfo：name:{}", name);
            KeyInfo keyInfo = null;
            initLock.lock();
            if (CACHEKEYBANK.get(name) == null) {
                keyInfo = new KeyInfo(name, baseDao);
                CACHEKEYBANK.put(name, keyInfo);
            } else {
                keyInfo = CACHEKEYBANK.get(name);
            }
            initLock.unlock();
            NsLogger.logger.info("初始化keyInfo：name:{}", name);
            synchronized (keyInfo) {
                if (keyInfo.isNotInit()) {
                    if (keyInfo.call()) {
                        result = keyInfo;
                    } else {
                        throw new Exception("初始化错误");
                    }
                }
            }
        }
        NsLogger.logger.info("result :{} ", result);
        return result;
    }

    @Override
    public void putKeyInfo(String key, KeyInfo keyInfo) {
        CACHEKEYBANK.put(key, keyInfo);
    }
}
