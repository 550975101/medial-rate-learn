package com.cctv;


import com.cctv.rabbitmq.OrderConsumer;
import com.cctv.rabbitmq.OrderProvider;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
    orderConsumer.consumerOrderPayTest();
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
   *用默认交换机
   *
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
    rabbitTemplate.convertAndSend("fanoutExchange",null,"fanout message");
  }

  @Test
  public void test23() {
    rabbitTemplate.convertAndSend("directExchange","directQueueTwo","direct message");
  }

  /**
   * 这个是 topic.queue.one  、topic.queue.two  会收到
   */
  @Test
  public void test24() {
    rabbitTemplate.convertAndSend("topicExchange","topic.queue.one","topic message");
  }

  /**
   * 这个是  topic.queue.two   topic.queue.four 会收到
   */
  @Test
  public void test25() {
    rabbitTemplate.convertAndSend("topicExchange","topic.renyi","topic message");
  }

  /**
   * 这个是  topic.queue.two   topic.queue.four 会收到
   */
  @Test
  public void test26() {
    rabbitTemplate.convertAndSend("topicExchange","topic.renyi","topic message");
  }
}
