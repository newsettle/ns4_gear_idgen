package com.creditease.ns4.gear.idgen.service.impl;

import com.creditease.ns4.gear.idgen.common.Constant;
import com.creditease.ns4.gear.idgen.common.NsLogger;
import com.creditease.ns4.gear.idgen.service.CounterService;
import com.creditease.ns4.gear.idgen.service.KeyInfoService;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 读取key_bank
 *
 * @author 201505130123
 */
@Service("counterService")
public class CounterServiceImpl implements CounterService {

    @Autowired
    private KeyInfoService keyInfoService;
    private final static String BATCH_REGX = "\\$\\$\\$";
    private final static String COMMA = ",";


    @Override
    public String getTableId(String tableName) throws Exception {
        NsLogger.logger.info("CounterServiceImpl.getTableId tableName:{}", tableName);
        return keyInfoService.createKey(tableName);
    }

    @Override
    public String getFlowNo(String keyName) throws Exception {
        NsLogger.logger.info("CounterServiceImpl.getFlowNo keyName:{}", keyName);
        return keyInfoService.createKey(keyName);
    }


    @Override
    public long getLongNo(String keyName) throws Exception {
        NsLogger.logger.info("CounterServiceImpl.getLongNo keyName:{}", keyName);
        return keyInfoService.createKeyAsLong(keyName);
    }

    @Override
    public String batchGetFlowNo(String key) throws Exception {
        NsLogger.logger.info("CounterServiceImpl.batchGetFlowNo key:{}", key);
        String[] keyInfo = this.splitBatchKey(key);
        String result = Constant.EMPTY;
        if (ArrayUtils.isNotEmpty(keyInfo)) {
            String keyName = keyInfo[0];
            String strSize = keyInfo[1];
            int size = 1;
            //长度是否有效
            if (StringUtils.isNotBlank(strSize)
                    && StringUtils.isNumeric(strSize)) {
                size = Integer.valueOf(strSize);
            }
            StringBuffer sbKeyValue = new StringBuffer();
            for (int i = 0; i < size; i++) {
                sbKeyValue.append(this.getFlowNo(keyName)).append(COMMA);
            }
            result = sbKeyValue.toString();
            if (result.endsWith(COMMA)) {
                result = result.substring(0, result.length() - 1);
            }
        }
        NsLogger.logger.info("CounterServiceImpl.batchGetFlowNo result:{}", result);
        return result;
    }

    @Override
    public String generator(String keyType, String keyName) throws Exception {
        String keyValue = Constant.EMPTY;
        if (Constant.KEY_TYPE_STRING.equals(keyType)) {
            keyValue = getFlowNo(keyName);
        } else if (Constant.KEY_TYPE_LONG.equals(keyType)) {
            keyValue = String.valueOf(getLongNo(keyName));
        } else if (Constant.KEY_TYPE_BATCH.equals(keyType)) {
            keyValue = batchGetFlowNo(keyName);
        }
        return keyValue;
    }

    private String[] splitBatchKey(String key) {
        if (StringUtils.isNotBlank(key)) {
            return key.split(BATCH_REGX);
        }
        return null;
    }
}
