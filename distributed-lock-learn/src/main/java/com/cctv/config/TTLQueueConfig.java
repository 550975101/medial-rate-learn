package com.cctv.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author by 封心
 * @classname TTLQueueConfig
 * @description 过期队列 10秒之后没有确认 消息就会丢弃
 * @date 2022/4/25 11:27
 */
@Configuration
public class TTLQueueConfig {

  @Bean
  public Queue TTLQueueOne() {
    Map<String, Object> params = new HashMap<>(4);
    params.put("x-message-ttl", 10000);
    return new Queue("TTLQueueOne", true, false, false, params);
  }

  @Bean
  public Binding TTLQueueBinding(@Autowired DirectExchange directExchange) {
    return BindingBuilder.bind(TTLQueueOne()).to(directExchange).withQueueName();
  }

}
