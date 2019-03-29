package com.creditease.ns4.gear.idgen.common;


import com.creditease.framework.pojo.ServiceMessage;
import com.creditease.ns.log.NsLog;

import java.util.HashMap;
import java.util.Map;


public class NsLogger {

    public static final NsLog logger = NsLog.getBizLog(Constant.ServiceId.LOGGER, "日志输出");

    public static Map<String, NsLog> log = new HashMap<String, NsLog>();

    static {
        log.put(Constant.ServiceId.LOGGER, logger);
    }

    public static NsLog getLog(String key) {
        return log.get(key);
    }

    public static void debug(String serviceId, ServiceMessage serviceMessage, String logMsg) {
        getLog(serviceId).debug(makeLog(serviceMessage) + "-{}", logMsg);
    }

    public static void debug(String serviceId, String logMsg) {
        getLog(serviceId).debug(logMsg);
    }

    public static void info(String serviceId, ServiceMessage serviceMessage, String logMsg) {
        getLog(serviceId).info(makeLog(serviceMessage) + "-{}", logMsg);
    }

    public static void info(String serviceId, String logMsg) {
        getLog(serviceId).info(logMsg);
    }

    public static void error(String serviceId, ServiceMessage serviceMessage, String logMsg) {
        getLog(serviceId).error(makeLog(serviceMessage) + "-{}", logMsg);
    }

    public static void error(String serviceId, String logMsg) {
        getLog(serviceId).error(logMsg);
    }

    public static void error(String serviceId, Throwable e, String logMsg) {
        getLog(serviceId).error(e, logMsg);
    }

    public static void error(String serviceId, Throwable e, ServiceMessage serviceMessage, String logMsg) {
        getLog(serviceId).error(e, makeLog(serviceMessage) + "-{}", logMsg);
    }

    private static String makeLog(ServiceMessage serviceMessage) {
        String fixLog = Constant.EMPTY;
        try {
            String retCode = serviceMessage.getExchange(DataExchangeKey.retCode) == null ? Constant.EMPTY : serviceMessage
                    .getExchange(DataExchangeKey.retCode);
            String retInfo = serviceMessage.getExchange(DataExchangeKey.retInfo) == null ? Constant.EMPTY : serviceMessage
                    .getExchange(DataExchangeKey.retInfo);
            fixLog = retCode + "|" + retInfo
                    + "|" ;
        } catch (Exception e) {
            NsLogger.logger.error("NsLogger.makeLog(); ### 异常", e);
        }
        return fixLog;
    }

}
