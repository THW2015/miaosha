package com.imooc.miaosha.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 谭皓文 on 2018/11/16.
 */
@Configuration
public class MQConfig {

    public static final String MIAOSHA_QUEUE = "miaosha.queue";
//    public static final String QUEUE = "queue";
//    public static final String TOPIC_QUEUE1 = "topic_queue1";
//    public static final String TOPIC_QUEUE2 = "topic_queue2";
//    public static final String TOPIC_EXCHANGE = "topic_exchange";
//    public static final String FANOUT_EXCHANGE = "fanoutxchage";
//    public static final String HEADER_QUEUE = "header.queue";
//    public static final String HEADERS_EXCHANGE = "headersExchage";

    /**
     * direct模式
     */
//    @Bean
//    public Queue queue(){
//        return new Queue(QUEUE,true);
//    }
//
//    /**
//     * topic模式
//     */
//    @Bean
//    public Queue topicQueue1() {
//        return new Queue(TOPIC_QUEUE1, true);
//    }
//    @Bean
//    public Queue topicQueue2() {
//        return new Queue(TOPIC_QUEUE2, true);
//    }
//
//    @Bean
//    public TopicExchange topicExchange(){
//        return new TopicExchange(TOPIC_EXCHANGE);
//    }
//
//    @Bean
//    public Binding topicBinding1(){
//        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with("topic.key1");
//    }
//
//    @Bean
//    public Binding topicBinding2(){
//        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with("topic.#");
//    }
//
//    /**
//     * fanout(广播)
//     */
//    @Bean
//    public FanoutExchange fanoutExchange(){
//        return new FanoutExchange(FANOUT_EXCHANGE);
//    }
//
//    @Bean
//    public Binding fanoutBinding1(){
//        return BindingBuilder.bind(topicQueue1()).to(fanoutExchange());
//    }
//
//    @Bean
//    public Binding fanoutBinding2(){
//        return BindingBuilder.bind(topicQueue2()).to(fanoutExchange());
//    }
//
//    /**
//     * header模式
//     */
//
//    @Bean
//    public HeadersExchange headersExchage(){
//        return new HeadersExchange(HEADERS_EXCHANGE);
//    }
//
//    @Bean
//    public Queue headerQueue1(){
//        return new Queue(HEADER_QUEUE,true);
//    }
//
//    @Bean
//    public Binding headerBinding(){
//        Map<String,Object> map = new HashMap<String, Object>();
//        map.put("header1","value1");
//        map.put("header2","value2");
//        return BindingBuilder.bind(headerQueue1()).to(headersExchage()).whereAll(map).match();
//    }
    @Bean
    public Queue queue(){
        return new Queue(MIAOSHA_QUEUE,true);
    }
}
