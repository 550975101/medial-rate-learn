package com.cctv.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author by 封心
 * @classname DirectExchangeConfig
 * @description 直连交换 他是通过交换机 routeKey 精准匹配  现在我设置的routeKey就是队列名字 withQueueName
 * @date 2022/4/25 10:32
 */
@Configuration
public class DirectExchangeConfig {

  @Bean
  public Queue directQueueOne() {
    return new Queue("directQueueOne", true, false, false);
  }

  @Bean
  public Queue directQueueTwo() {
    return new Queue("directQueueTwo", true, false, false);
  }

  @Bean
  public Queue directQueueThree() {
    return new Queue("directQueueThree", true, false, false);
  }

  @Bean
  public DirectExchange directExchange() {
    return new DirectExchange("directExchange", true, false);
  }

  @Bean
  public Binding directQueueOneBanding() {
    return BindingBuilder.bind(directQueueOne()).to(directExchange()).withQueueName();
  }

  @Bean
  public Binding directQueueTwoBanding() {
    return BindingBuilder.bind(directQueueTwo()).to(directExchange()).withQueueName();
  }

  @Bean
  public Binding directQueueThreeBanding() {
    return BindingBuilder.bind(directQueueThree()).to(directExchange()).withQueueName();
  }
}
