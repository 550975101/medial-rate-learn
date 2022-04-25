package com.cctv.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


/**
 * @author by 封心
 * @classname DeadLetterExchange
 * @description TODO
 * @date 2022/4/25 13:04
 */
@Configuration
public class DeadLetterQueueConfig {

  /**
   * 申明队列的时候 给绑定一个 扇形死信交换机
   *  不绑定交换 会有一个普通交换机 普通交换机 routeKey设置成队列名  就会发到这个队列
   *  完事因为设置了 消息的存活时间是20秒
   *  20秒时候 会由绑定的x-dead-letter-exchange 死信交换机  转发到他的队列 因为绑定的是扇形交换机
   *  会把消息转发到所有绑定这个交换机的队列
   * @return
   */
  @Bean
  public Queue deadLetterQueueOne() {
    Map<String, Object> params = new HashMap<>(4);
    params.put("x-dead-letter-exchange", "fanoutExchange");
    params.put("x-message-ttl", 10000);
    return new Queue("deadLetterQueue", true, false, false, params);
  }

  @Bean
  public Queue deadLetterQueueTwo() {
    Map<String, Object> params = new HashMap<>(4);
    params.put("x-dead-letter-exchange", "topicExchange");
    params.put("x-dead-letter-routing-key", "topic.queue.three");
    params.put("x-message-ttl", 10000);
    return new Queue("deadLetterQueueTwo", true, false, false, params);
  }
}
