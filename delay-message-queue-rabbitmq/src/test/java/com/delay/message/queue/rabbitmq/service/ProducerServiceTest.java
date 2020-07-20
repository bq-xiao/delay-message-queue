package com.delay.message.queue.rabbitmq.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.Calendar;
import java.util.Random;

@Slf4j
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ProducerServiceTest {
    @Autowired
    private ProducerService producerService;

    @Test
    public void produceByQueueTTL() throws IOException {
        Random random = new Random(1);
        for (int i = 0; i < 10; i++) {
            String orderId = "order-id-" + i;
            //log.info("order id:{}", orderId);
            int delay = random.nextInt(10);
            try {
                Thread.sleep(delay * 1000);
            } catch (InterruptedException e) {
                log.error("InterruptedException", e);
            }
            producerService.produceByQueueTTL(orderId, delay);
        }
        System.in.read();
    }

    @Test
    public void produceByMessageTTL() throws IOException {
        Random random = new Random(1);
        for (int i = 0; i < 10; i++) {
            Calendar calendar = Calendar.getInstance();
            // 3秒后执行
            int time = random.nextInt(100);
            calendar.add(Calendar.SECOND, time);
            String orderId = "order-id-" + i;
            long expired = calendar.getTimeInMillis();
            log.info("order id:{}, expired:{}", orderId, time);
            producerService.produceByMessageTTL(orderId, expired);
            try {
                //log.info("no data will sleep");
                Thread.sleep(500);
            } catch (InterruptedException e) {
                log.error("InterruptedException", e);
            }
        }
        System.in.read();
    }
}