package com.cctv.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author by 封心
 * @classname NoPersistentQueue
 * @description TODO
 * @date 2022/4/25 17:09
 */
@Configuration
public class NoPersistentQueueConfig {

  @Bean
  public Queue noPersistentQueue() {
    return new Queue("noPersistentQueue", false);
  }
}
