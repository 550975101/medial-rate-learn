package com.cctv.rabbitmq;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

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

  @RabbitListener(queues = "order.pay")
  public void consumerOrderPayTest(Message message, Channel channel) {
    System.out.println(new String(message.getBody()));
    try {
      channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void consumerOrderPayTest2() {
    for (; ; ) {
      Message receive = rabbitTemplate.receive("order.pay");
      System.out.println(new String(receive.getBody()));
    }
  }
}
