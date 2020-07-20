package com.delay.message.queue.rabbitmq.service;

import com.delay.message.queue.rabbitmq.config.RabbitmqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProducerService {
    @Autowired
    private AmqpTemplate amqpTemplate;

    public void produceByQueueTTL(String orderId, long expiredTime) {
        amqpTemplate.convertAndSend(RabbitmqConfig.DELAY_EXCHANGE_NAME, RabbitmqConfig.DELAY_QUEUE_ROUTING_KEY, orderId);
        log.info("[produceByQueueTTL]order id:{}, delay {}s send success", orderId, expiredTime);
    }

    public void produceByMessageTTL(String orderId, long expiredTime) {
        //rabbit默认为毫秒级
        MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setExpiration(String.valueOf(expiredTime));
                return message;
            }
        };
        amqpTemplate.convertAndSend(RabbitmqConfig.DELAY_EXCHANGE_NAME, RabbitmqConfig.DELAY_QUEUE_ROUTING_KEY, orderId, messagePostProcessor);
        log.info("[produceByMessageTTL]order id:{}, delay {}s send success", orderId, expiredTime);
    }
}
