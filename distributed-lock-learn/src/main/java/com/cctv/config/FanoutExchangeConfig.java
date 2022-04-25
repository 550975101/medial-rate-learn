package com.cctv.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author by 封心
 * @classname FanoutExchangeConfig
 * @description 扇形交换机 广播模式会给所有的绑定该交换机的队列发送消息 没有路由键 全部转发
 * @date 2022/4/25 10:00
 */
@Configuration
public class FanoutExchangeConfig {

  @Bean
  public Queue fanoutQueueOne() {
    return new Queue("fanoutQueueOne", true, false, false);
  }

  @Bean
  public Queue fanoutQueueTwo() {
    return new Queue("fanoutQueueTwo", true, false, false);
  }

  @Bean
  public Queue fanoutQueueThree() {
    return new Queue("fanoutQueueThree", true, false, false);
  }

  @Bean
  public FanoutExchange fanoutExchange() {
    return new FanoutExchange("fanoutExchange",false,false);
  }

  @Bean
  public Binding fanoutQueueOneBinding() {
    return BindingBuilder.bind(fanoutQueueOne()).to(fanoutExchange());
  }

  @Bean
  public Binding fanoutQueueTwoBinding() {
    return BindingBuilder.bind(fanoutQueueTwo()).to(fanoutExchange());
  }

  @Bean
  public Binding fanoutQueueThreeBinding() {
    return BindingBuilder.bind(fanoutQueueThree()).to(fanoutExchange());
  }
}
