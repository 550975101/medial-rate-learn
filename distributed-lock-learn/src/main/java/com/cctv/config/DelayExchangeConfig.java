package com.cctv.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author by 封心
 * @classname DelayExchangeConfig
 * @description rabbitmq 本身不支持延迟交换 得通过插件
 * 安装插件
 * rabbitmq-plugins enable rabbitmq_delayed_message_exchange
 * 这个延迟交换器 比自带延迟队列好处在于  他的过期时间可以自定义
 * 比如 先进去一个40秒过期的消息  又进去一个10过期的消息  他10秒的消息 会先出去
 * 但是自带的延迟队列不行 他只能等前面的40秒消息出去才能出去 也就是10秒的那个得等40秒才能出去
 * <p>
 * 这个是先到达交换器存起来 完事时间到了 扔进去绑定的队列
 * @date 2022/4/25 14:13
 * https://blog.csdn.net/weixin_50616848/article/details/123593198
 */
@Configuration
public class DelayExchangeConfig {

  @Bean
  public Queue delayExchangeQueue() {
    return new Queue("delayExchangeQueue", true, false, false);
  }

  @Bean
  public Exchange delayExchangeOne() {
    Map<String, Object> arguments = new HashMap<>();
    arguments.put("x-delayed-type", "topic");
    Exchange exchange = new CustomExchange(
      "delayExchangeOne",
      "x-delayed-message",
      true,
      false,
      arguments
    );
    return exchange;
  }

  @Bean
  public Binding delayExchangeBindQueue() {
    return BindingBuilder.bind(delayExchangeQueue()).to(delayExchangeOne()).with("delayed.#").noargs();
  }

}
