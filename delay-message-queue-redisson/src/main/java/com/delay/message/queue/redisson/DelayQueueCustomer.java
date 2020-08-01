package com.delay.message.queue.redisson;

import io.netty.util.internal.StringUtil;
import org.redisson.Redisson;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.Date;

public class DelayQueueCustomer {
    private static long time = new Date().getTime();

    public static void main(String[] args) {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://10.0.0.50:6379");
        RedissonClient redissonClient = Redisson.create(config);
        RBlockingQueue<String> rBlockingQueue = redissonClient.getBlockingQueue("delay_queue");
        RDelayedQueue<String> rDelayedQueue = redissonClient.getDelayedQueue(rBlockingQueue);
        while (true) {
            try {
                String order = rBlockingQueue.take();
                if (!StringUtil.isNullOrEmpty(order)) {
                    long nowTime = new Date().getTime();
                    long expired = (nowTime - time) / 1000;
                    time = nowTime;
                    System.out.println("delay:" + expired + "s, order:" + order);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                rDelayedQueue.destroy();
            }
        }
    }
}
