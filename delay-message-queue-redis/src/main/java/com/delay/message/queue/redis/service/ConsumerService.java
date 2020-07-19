package com.delay.message.queue.redis.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
public class ConsumerService {
    private static final String QUEUE_NAME = "delay_order_queue";
    @Autowired
    private RedisTemplate redisTemplate;

    public void consume() {
        while (true) {
            Set<String> set = redisTemplate.opsForZSet().rangeByScore(QUEUE_NAME, 0, System.currentTimeMillis(), 0, 1);
            if (set == null || set.isEmpty()) {
                try {
                    //log.info("no data will sleep");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.error("InterruptedException", e);
                }
                continue;
            }
            String orderId = set.iterator().next();
            if (redisTemplate.opsForZSet().remove(QUEUE_NAME, orderId) > 0) {
                log.info("order id:{} handle success", orderId);
                long length = redisTemplate.opsForZSet().size(QUEUE_NAME);
                log.info("[consume]{} length:{}", QUEUE_NAME, length);
            }
        }
    }
}
