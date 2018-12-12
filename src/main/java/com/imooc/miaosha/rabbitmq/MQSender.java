package com.imooc.miaosha.rabbitmq;

import com.imooc.miaosha.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 谭皓文 on 2018/11/16.
 */
@Service
public class MQSender {

    Logger log = LoggerFactory.getLogger(MQSender.class);

    @Autowired
    AmqpTemplate amqpTemplate;

//    public void send(Object message){
//        String msg = RedisService.beanToString(message);
//        log.info("send message" + msg);
//        amqpTemplate.convertAndSend(MQConfig.QUEUE,msg);
//    }
//
//    public void sendTopic(Object message){
//        String msg = RedisService.beanToString(message);
//        log.info("send topic message" + msg);
//        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,"topic.key1",msg+"1");
//        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,"topic.key2",msg+"2");
//    }
//
//    public void sendFanout(Object message){
//        String msg = RedisService.beanToString(message);
//        log.info("send fanout message" + msg);
//        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE,"",msg);
//    }
//
//    public void sendHeader(Object message){
//        String msg = RedisService.beanToString(message);
//        MessageProperties messageProperties = new MessageProperties();
//        messageProperties.setHeader("header1","value1");
//        messageProperties.setHeader("header2","value2");
//        Message m = new Message(msg.getBytes(),messageProperties);
//        log.info("send header message" + msg);
//        amqpTemplate.convertAndSend(MQConfig.HEADERS_EXCHANGE,"",m);
//    }

    public void send(Object message){
        String msg = RedisService.beanToString(message);
        log.info("send message" + msg);
        amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE,msg);
    }

}
