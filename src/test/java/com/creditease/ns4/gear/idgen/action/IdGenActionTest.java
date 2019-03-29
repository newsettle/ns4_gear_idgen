package com.creditease.ns4.gear.idgen.action;

import com.creditease.framework.exception.NSException;
import com.creditease.ns.framework.MockMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.CountDownLatch;


@RunWith(SpringJUnit4ClassRunner.class) //使用junit4进行测试
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"}) //加载配置文件
public class IdGenActionTest {

    @Autowired
    IdGenAction idGenAction;

    private static int threadNum = 100;

    private static int loopTimes = 100;

    private static CountDownLatch countDownLatch = new CountDownLatch(threadNum);

    @Test
    public void doWork() throws Exception {

        long start = System.currentTimeMillis();
        MockMessage message = new MockMessage();

        message.setParameter("keyType", "2");
        message.setParameter("keyName", "TEST-KEY");

        for (int i = 0; i < threadNum; i++) {
            new MyThread(message, loopTimes, countDownLatch).start();
        }

        countDownLatch.await();
        System.out.println("总时间" + (System.currentTimeMillis() - start) / 1000.0f);
    }

    class MyThread extends Thread {

        MockMessage mockMessage;
        private long loopTimes;
        private CountDownLatch countDownLatch;

        public MyThread(MockMessage mockMessage, long loopTimes, CountDownLatch countDownLatch) {
            this.mockMessage = mockMessage;
            this.loopTimes = loopTimes;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            for (int j = 0; j < loopTimes; j++) {
                try {
                    idGenAction.doWork(mockMessage);
                } catch (NSException e) {
                    e.printStackTrace();
                }
            }
            countDownLatch.countDown();
        }
    }
}