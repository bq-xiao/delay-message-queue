package com.delay.message.queue.redis.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Calendar;
import java.util.Random;

@Slf4j
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ProducerServiceTest {
    @Autowired
    private ProducerService producerService;

    @Test
    public void produce() {
        Random random = new Random(1);
        for (int i = 0; i < 10; i++) {
            Calendar calendar = Calendar.getInstance();
            // 产生一个随机数
            int time = random.nextInt(100);
            calendar.add(Calendar.SECOND, time);
            String orderId = "order-id-" + i;
            long expired = calendar.getTimeInMillis();
            log.info("order id:{}, expired:{}", orderId, time);
            producerService.produce(orderId, expired);
            try {
                //log.info("no data will sleep");
                Thread.sleep(500);
            } catch (InterruptedException e) {
                log.error("InterruptedException", e);
            }
        }
    }
}