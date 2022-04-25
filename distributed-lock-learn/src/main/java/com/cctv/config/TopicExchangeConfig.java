package com.cctv.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author by 封心
 * @classname TopicExchangeConfig
 * @description TODO
 * @date 2022/4/25 10:45
 */
@Configuration
public class TopicExchangeConfig {

  @Bean
  public Queue topicQueueOne() {
    return new Queue("topic.queue.one", true, false, false);
  }

  @Bean
  public Queue topicQueueTwo() {
    return new Queue("topic.queue.two", true, false, false);
  }

  @Bean
  public Queue topicQueueThree() {
    return new Queue("topic.queue.three", true, false, false);
  }

  /**
   * 故意写成这样的
   * @return
   */
  @Bean
  public Queue topicQueueFour() {
    return new Queue("topic.queueFour", true, false, false);
  }

  @Bean
  public TopicExchange topicExchange() {
    return new TopicExchange("topicExchange", true, false);
  }

  /**
   * topic.queue.one 这个就只是精准匹配
   * @return
   */
  @Bean
  public Binding topicQueueOneBinding() {
    return BindingBuilder.bind(topicQueueOne()).to(topicExchange()).with("topic.queue.one");
  }

  /**
   * topic.#  会匹配到 topic.queue.one  topic.queue.one topic.queue.three topic.queueFour
   * topic.#  #是匹配后面多个
   * @return
   */
  @Bean
  public Binding topicQueueTwoBinding() {
    return BindingBuilder.bind(topicQueueTwo()).to(topicExchange()).with("topic.#");
  }

  /**
   * topic.*  会匹配到 topic.queueFour
   * @return
   */
  @Bean
  public Binding topicQueueThreeBinding() {
    return BindingBuilder.bind(topicQueueThree()).to(topicExchange()).with("topic.*");
  }

  @Bean
  public Binding topicQueueFourBinding() {
    return BindingBuilder.bind(topicQueueFour()).to(topicExchange()).with("topic.#");
  }
}
