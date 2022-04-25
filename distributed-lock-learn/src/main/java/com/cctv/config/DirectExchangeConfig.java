package com.cctv.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author by 封心
 * @classname DirectExchangeConfig
 * @description 直连交换 他是通过交换机 routeKey 精准匹配  现在我设置的routeKey就是队列名字 withQueueName
 * @date 2022/4/25 10:32
 */
@Configuration
public class DirectExchangeConfig {

  /**
   * durable  队列是否持久化.
   * false:队列在内存中,服务器挂掉后,队列就没了;
   * true:服务器重启后,队列将会重新生成.注意:只是队列持久化,不代表队列中的消息持久化!!!!
   * exclusive 队列是否专属,专属的范围针对的是连接,也就是说,一个连接下面的多个信道是可见的.
   * 对于其他连接是不可见的.连接断开后,该队列会被删除.注意,不是信道断开,是连接断开.并且,就算设置成了持久化,也会删除.
   * autoDelete 如果所有消费者都断开连接了,是否自动删除.
   * 如果还没有消费者从该队列获取过消息或者监听该队列,那么该队列不会删除.
   * 只有在有消费者从该队列获取过消息后,该队列才有可能自动删除(当所有消费者都断开连接,不管消息是否获取完)
   * arguments: null 队列的配置
   * <p>
   * 一共10个:
   * <p>
   * Message TTL : 消息生存期  个队列中的消息,在被丢弃之前能够存活多少毫秒
   * Auto expire : 队列生存期  队列在指定的时间内没有被使用(访问)就会被删除. 如果我们同时创建一个消费者,那该队列永远不会被删除.因为虽然它里面没有消息,但一直有消费者在使用(访问)它
   * Max length : 队列可以容纳的消息的最大条数
   * Max length bytes : 队列可以容纳的消息的最大字节数
   * Overflow behaviour : 队列中的消息溢出后如何处理
   * Dead letter exchange : 溢出的消息需要发送到绑定该死信交换机的队列
   * Dead letter routing key : 溢出的消息需要发送到绑定该死信交换机,并且路由键匹配的队列
   * Maximum priority : 最大优先级
   * Lazy mode : 懒人模式
   * Master locator :
   * <p>
   * 队列的参数一经创建 不支持修改 只能删除 重新创建
   *
   * @return
   */
  @Bean
  public Queue directQueueOne() {
    return new Queue("directQueueOne", true, false, false);
  }

  @Bean
  public Queue directQueueTwo() {
    return new Queue("directQueueTwo", true, false, false);
  }

  @Bean
  public Queue directQueueThree() {
    return new Queue("directQueueThree", true, false, false);
  }

  @Bean
  public DirectExchange directExchange() {
    return new DirectExchange("directExchange", true, false);
  }

  @Bean
  public Binding directQueueOneBanding() {
    return BindingBuilder.bind(directQueueOne()).to(directExchange()).withQueueName();
  }

  @Bean
  public Binding directQueueTwoBanding() {
    return BindingBuilder.bind(directQueueTwo()).to(directExchange()).withQueueName();
  }

  @Bean
  public Binding directQueueThreeBanding() {
    return BindingBuilder.bind(directQueueThree()).to(directExchange()).withQueueName();
  }
}
