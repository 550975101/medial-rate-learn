package com.cctv;


import com.cctv.rabbitmq.OrderConsumer;
import com.cctv.rabbitmq.OrderProvider;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/**
 * Unit test for simple App.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class AppTest {


  @Autowired
  private OrderProvider orderProvider;

  @Autowired
  private OrderConsumer orderConsumer;

  @Test
  public void test() {
    String s = orderProvider.declareQueue("order.pay");
    System.out.println(s);
  }

  @Test
  public void deleteQueue() {
    orderProvider.deleteQueue("order.pay");
  }

  @Test
  public void sendMessage() {
    for (int i = 0; i < 100; i++) {
      orderProvider.publishMessage("hell word");
    }
  }

  @Test
  public void consumerTest() {
  }

  @Test
  public void consumerTest2() {
    orderConsumer.consumerOrderPayTest2();
  }

  /**
   * ReturnsCallback: 消息: (Body:'[B@7d676953(byte[3])' MessageProperties [headers={}, contentType=application/octet-stream, contentLength=0, receivedDeliveryMode=PERSISTENT, priority=0, deliveryTag=0])
   * ReturnsCallback: 回应码: 312
   * ReturnsCallback: 回应信息: NO_ROUTE
   * ReturnsCallback: 交换机:
   * ReturnsCallback: 路由键: order.pay.not.exists
   * ConfirmCallback: 相关数据: null
   * ConfirmCallback: 确认情况: true
   * ConfirmCallback: 原因: null
   * 用默认交换机
   * <p>
   * confirm机制是只保证消息到达exchange，并不保证消息可以路由到正确的queue
   * 当前的exchange不存在或者指定的路由key路由不到才会触发return机制
   */
  @Test
  public void publishMessageNotExistsQueue() {
    orderProvider.publishMessageNotExistsQueue("111");
  }

  /**
   * 用默认交换机
   * ConfirmCallback: 相关数据: null
   * ConfirmCallback: 确认情况: true
   * ConfirmCallback: 原因: null
   */
  @Test
  public void publishMessageExistsQueue() {
    orderProvider.publishMessage("hell word");
  }


  @Autowired
  private RabbitTemplate rabbitTemplate;

  @Test
  public void test22() {
    rabbitTemplate.convertAndSend("fanoutExchange", null, "fanout message");
  }

  @Test
  public void test23() {
    rabbitTemplate.convertAndSend("directExchange", "directQueueTwo", "direct message");
  }

  /**
   * 这个是 topic.queue.one  、topic.queue.two  会收到
   */
  @Test
  public void test24() {
    rabbitTemplate.convertAndSend("topicExchange", "topic.queue.one", "topic message");
  }

  /**
   * 这个是  topic.queue.two   topic.queue.four 会收到
   */
  @Test
  public void test25() {
    rabbitTemplate.convertAndSend("topicExchange", "topic.renyi", "topic message");
  }

  /**
   * 这个是  topic.queue.two   topic.queue.four 会收到
   */
  @Test
  public void test26() {
    rabbitTemplate.convertAndSend("topicExchange", "topic.renyi", "topic message");
  }

  @Test
  public void test27() {
    ConnectionFactory connectionFactory = rabbitTemplate.getConnectionFactory();
    RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
    //净化  purge 婆着 删除队列全部消息
    rabbitAdmin.purgeQueue("directQueueOne");
    rabbitAdmin.purgeQueue("directQueueThree");
    rabbitAdmin.purgeQueue("directQueueTwo");
    rabbitAdmin.purgeQueue("fanoutQueueOne");
    rabbitAdmin.purgeQueue("fanoutQueueTwo");
    rabbitAdmin.purgeQueue("fanoutQueueThree");
    rabbitAdmin.purgeQueue("topic.queue.one");
    rabbitAdmin.purgeQueue("topic.queue.two");
    rabbitAdmin.purgeQueue("topic.queue.three");
    rabbitAdmin.purgeQueue("topic.queueFour");
  }

  @Test
  public void test28() {
    rabbitTemplate.convertAndSend("directExchange", "TTLQueueOne", "TTLQueueOne message");
  }

  /**
   * 这个队列创建20s会自动删除
   * 但是,如果我们同时创建一个消费者,那该队列永远不会被删除.因为虽然它里面没有消息,但一直有消费者在使用(访问)它
   */
  @Test
  public void test29() {
    ConnectionFactory connectionFactory = rabbitTemplate.getConnectionFactory();
    RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
    Map<String, Object> params = new HashMap<>();
    params.put("x-expires", 20000);
    rabbitAdmin.declareQueue(new Queue("auto.expire", true, false, false, params));
  }

  /**
   * 先进先出 剩下的是auto.expire9 这个可以我丢 很迷
   */
  @Test
  public void test30() {
    ConnectionFactory connectionFactory = rabbitTemplate.getConnectionFactory();
    Connection connection = connectionFactory.createConnection();
    Channel channel = connection.createChannel(true);
    RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
    Map<String, Object> params = new HashMap<>();
    params.put("x-expires", 60000);
    params.put("x-max-length", 1);
    rabbitAdmin.declareQueue(new Queue("auto.expire", true, false, false, params));
    for (int i = 0; i < 10; i++) {
      rabbitTemplate.convertAndSend("", "auto.expire", "auto.expire" + i);
    }
  }

  @Test
  public void test31() {
    rabbitTemplate.convertAndSend("", "deadLetterQueue", "auto.expire");
  }

  @Test
  public void test32() {
    rabbitTemplate.convertAndSend("delayExchangeOne", "delayed.www", "message 10000", new MessagePostProcessor() {
      @Override
      public Message postProcessMessage(Message message) throws AmqpException {
        message.getMessageProperties().setDelay(10000);
        return message;
      }
    });

  }
}

