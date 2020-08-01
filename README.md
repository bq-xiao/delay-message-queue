# delay-message-queue
延时消息队列

## delay-message-queue-redis
基于Redis实现的延时消息队列
+ Reis ZSet的排序功能
+ 循环从ZSet中拿取一个值，如果该值为空，则线程休眠

## delay-message-queue-rabbitmq
基于RabbitMQ实现的延时消息队列
+ 整个队列设置TTL
+ 单个消息设置TTL

## delay-message-queue-redisson
基于Redisson实现的延时消息队列
+ offer向队列放入一个不同延时的元素
+ take每次从队列头部拿取一个元素
