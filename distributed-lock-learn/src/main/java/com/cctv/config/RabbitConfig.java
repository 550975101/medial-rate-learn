package com.cctv.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author by 封心
 * @classname RabbitConfig
 * @description Rabbit配置
 * @date 2022/4/22 16:43
 */
@Configuration
@Slf4j
public class RabbitConfig {

  @Bean
  public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
    RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    //开启Mandatory 才能触发回调函数 无论消息结果怎样都强制回调
    //强制性的; 强制的
    /*
     * 当mandatory标志位设置为true时
     * 如果exchange根据自身类型和消息routingKey无法找到一个合适的queue存储消息
     * 那么broker会调用basic.return方法将消息返还给生产者
     * 当mandatory设置为false时，出现上述情况broker会直接将消息丢弃
     */
    rabbitTemplate.setMandatory(true);
    //到交换机的情况
    rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
      log.info("ConfirmCallback: 相关数据: {}", correlationData);
      log.info("ConfirmCallback: 确认情况: {}", ack);
      log.info("ConfirmCallback: 原因: {}", cause);
    });
    //到队列的情况
    rabbitTemplate.setReturnsCallback(returnedMessage -> {
      log.info("ReturnsCallback: 消息: {}", returnedMessage.getMessage());
      log.info("ReturnsCallback: 回应码: {}", returnedMessage.getReplyCode());
      log.info("ReturnsCallback: 回应信息: {}", returnedMessage.getReplyText());
      log.info("ReturnsCallback: 交换机: {}", returnedMessage.getExchange());
      log.info("ReturnsCallback: 路由键: {}", returnedMessage.getRoutingKey());
    });
    return rabbitTemplate;
  }

}
