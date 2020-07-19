package com.delay.message.queue.redis.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProducerService {
    private static final String QUEUE_NAME = "delay_order_queue";
    @Autowired
    private RedisTemplate redisTemplate;

    public void produce(String orderId, long expiredTime) {
        redisTemplate.opsForZSet().add(QUEUE_NAME, orderId, expiredTime);
        //log.info("order id:{} set success", orderId);
        long length = redisTemplate.opsForZSet().size(QUEUE_NAME);
        //log.info("[produce]{} length:{}", QUEUE_NAME, length);
    }
}
