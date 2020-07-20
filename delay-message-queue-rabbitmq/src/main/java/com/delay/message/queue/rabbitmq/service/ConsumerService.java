package com.delay.message.queue.rabbitmq.service;

import com.delay.message.queue.rabbitmq.config.RabbitmqConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

@Slf4j
@Service
public class ConsumerService {
    private long time = new Date().getTime();

    @RabbitListener(queues = RabbitmqConfig.DEAD_LETTER_QUEUE_NAME)
    @RabbitHandler
    public void consume(Message message, Channel channel) throws IOException {
        String orderId = new String(message.getBody());
        String expiration = message.getMessageProperties().getExpiration();
        if (expiration != null) {
            log.info("[consume]order id:{}, expiration:{}s handle success", orderId, Long.valueOf(expiration) / 1000);
        } else {
            long nowTime = new Date().getTime();
            long expired = (nowTime - time) / 1000;
            time = nowTime;
            log.info("[consume]order id:{}, delay:{}s handle success", orderId, expired);
        }
        //channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
