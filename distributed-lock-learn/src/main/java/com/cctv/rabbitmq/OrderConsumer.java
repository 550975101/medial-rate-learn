package com.cctv.rabbitmq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author by 封心
 * @classname OrderConsumer
 * @description TODO
 * @date 2022/4/22 11:10
 */
@Service
public class OrderConsumer {

  @Autowired
  private RabbitTemplate rabbitTemplate;

  public void consumerOrderPayTest() {
    Message receive = rabbitTemplate.receive("order.pay");
    if (receive != null) {
      System.out.println(new String(receive.getBody()));
    }
  }

  public void consumerOrderPayTest2() {
    for (; ; ) {
      Message receive = rabbitTemplate.receive("order.pay");
      System.out.println(new String(receive.getBody()));
    }
  }
}
