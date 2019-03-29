package com.creditease.ns4.gear.idgen.action;

import com.creditease.ns4.gear.idgen.common.*;
import com.creditease.ns4.gear.idgen.service.CounterService;
import com.creditease.framework.exception.NSException;
import com.creditease.framework.pojo.ServiceMessage;
import com.creditease.framework.scope.SystemOutKey;
import com.creditease.framework.util.StringUtil;
import com.creditease.framework.work.ActionWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IdGenAction extends ActionWorker {

    @Autowired
    private CounterService counterService;

    @Override
    public void doWork(ServiceMessage serviceMessage) throws NSException {

        long start = System.currentTimeMillis();
        //参数准备
        String keyType = serviceMessage.getExchangeByType(DataExchangeKey.keyType, String.class);
        if (StringUtil.isEmpty(keyType)) {
            keyType = serviceMessage.getParameter(Constant.PARAM_KEY_TYPE);
        }
        String keyName = serviceMessage.getExchangeByType(DataExchangeKey.keyName, String.class);
        if (StringUtil.isEmpty(keyName)) {
            keyName = serviceMessage.getParameter(Constant.PARAM_KEY_NAME);
        }
        NsLogger.logger.info("IdGenAction dowork... keyType:{},keyName:{}", keyType, keyName);

        //业务处理
        String keyValue = Constant.EMPTY;
        try {
            if (!StringUtil.isEmpty(keyType) && !StringUtil.isEmpty(keyName)) {
                keyValue = counterService.generator(keyType.trim(), keyName.trim());
            }
            NsLogger.logger.info("KeyValue:{} keyType:{} keyName:{}", keyValue, keyType, keyName);
        } catch (Exception e) {
            NsLogger.logger.error("response key is empty,error occur");
        }

        //输出结果
        serviceMessage.setOut(InterfaceOutKey.result, keyValue);

        String retCode = InnerMsgEnum.GOT_SUCCESS.getRetCode();
        String retInfo = InnerMsgEnum.GOT_SUCCESS.getRetInfo();

        if (Constant.EMPTY.equals(keyValue)) {
            retCode = InnerMsgEnum.GOT_FAIL.getRetCode();
            retInfo = InnerMsgEnum.GOT_FAIL.getRetInfo();
        }
        serviceMessage.setOut(SystemOutKey.RETURN_CODE, new GeneralRetInfo(retCode, retInfo));

        long end = System.currentTimeMillis();
        NsLogger.logger.info("IdGenAction dowork 共耗时{} ms", (end - start));

    }
}
