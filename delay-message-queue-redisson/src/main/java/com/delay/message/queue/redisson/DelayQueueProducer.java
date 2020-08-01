package com.delay.message.queue.redisson;

import org.redisson.Redisson;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class DelayQueueProducer {
    public static void main(String[] args) throws IOException {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://10.0.0.50:6379");
        RedissonClient redissonClient = Redisson.create(config);
        RBlockingQueue<String> rBlockingQueue = redissonClient.getBlockingQueue("delay_queue");
        RDelayedQueue<String> rDelayedQueue = redissonClient.getDelayedQueue(rBlockingQueue);
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int delay = random.nextInt(100);
            String orderId = "order-" + i;
            rDelayedQueue.offer(orderId, delay, TimeUnit.SECONDS);
            System.out.println("order:" + orderId + ", delay:" + delay + "s");
        }
        rDelayedQueue.destroy();
        System.in.read();
    }
}
