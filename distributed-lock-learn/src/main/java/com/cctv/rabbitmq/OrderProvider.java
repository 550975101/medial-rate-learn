package com.cctv.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * @author by 封心
 * @classname OrderProvider
 * @description TODO
 * @date 2022/4/22 11:11
 */
@Service
public class OrderProvider {

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @Autowired
  private ObjectMapper objectMapper;


  /**
   * 声明一个队列 rabbitmq  有一个默认的交换机
   * 默认交换机（default exchange）
   * 实际上是一个由消息代理预先声明好的没有名字（名字为空字符串）的直连交换机（direct exchange）。
   * 它有一个特殊的属性使得它对于简单应用特别有用处：那就是每个新建队列（queue）都会自动绑定到默认交换机上，
   * 绑定的路由键（routing key）名称与队列名称相同。
   * @param queueName
   * @return
   */
  public String declareQueue(String queueName) {
    ConnectionFactory connectionFactory = rabbitTemplate.getConnectionFactory();
    RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
    String result = rabbitAdmin.declareQueue(new Queue(queueName));
    return result;
  }

  /**
   * 删除队列
   * @param queueName
   */
  public void deleteQueue(String queueName) {
    ConnectionFactory connectionFactory = rabbitTemplate.getConnectionFactory();
    RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
    rabbitAdmin.deleteQueue(queueName);
  }

  /**
   * 发送消息
   */
  public void publishMessage(String message) {
    rabbitTemplate.send("order.pay",new Message(message.getBytes(StandardCharsets.UTF_8)));
    //不能每次都设置 Only one ConfirmCallback is supported by each RabbitTemplate
    //得先自己改造一下 rabbitTemplate
    //rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
    //  @Override
    //  public void confirm(CorrelationData correlationData, boolean b, String s) {
    //    try {
    //      System.out.println(objectMapper.writeValueAsString(correlationData));
    //    } catch (JsonProcessingException e) {
    //      throw new RuntimeException(e);
    //    }
    //    System.out.println(b);
    //    System.out.println(s);
    //  }
    //});
    //rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
    //  @Override
    //  public void returnedMessage(ReturnedMessage returnedMessage) {
    //    try {
    //      System.out.println(objectMapper.writeValueAsString(returnedMessage));
    //    } catch (JsonProcessingException e) {
    //      throw new RuntimeException(e);
    //    }
    //  }
    //});
    //rabbitTemplate.setRecoveryCallback(new RecoveryCallback<Object>() {
    //  @Override
    //  public Object recover(RetryContext retryContext) throws Exception {
    //    System.out.println(objectMapper.writeValueAsString(retryContext));
    //    return null;
    //  }
    //});
  }


  public void publishMessageNotExistsQueue(String message) {
    rabbitTemplate.send("order.pay.not.exists",new Message(message.getBytes(StandardCharsets.UTF_8)));
  }

}
