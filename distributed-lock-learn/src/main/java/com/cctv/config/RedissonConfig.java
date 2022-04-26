package com.cctv.config;

import io.micrometer.core.instrument.util.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.ReadMode;
import org.redisson.config.SentinelServersConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author by 封心
 * @classname RedissonConfig
 * @description TODO
 * @date 2022/4/26 15:49
 */
@Configuration
public class RedissonConfig {
  @Value("${spring.redis.sentinel.nodes}")
  private String nodes;

  @Value("${spring.redis.sentinel.master}")
  private String master;

  @Value("${spring.redis.password}")
  private String password;

  /**
   * 哨兵模式配置
   */
  @Bean
  RedissonClient redissonSentinel() {
    Config config = new Config();
    String[] nodeStr = nodes.split(",");
    List<String> newNodes = new ArrayList(nodeStr.length);
    Arrays.stream(nodeStr).forEach((index) -> newNodes.add(
      index.startsWith("redis://") ? index : "redis://" + index));

    SentinelServersConfig serverConfig = config.useSentinelServers()
      .addSentinelAddress(newNodes.toArray(new String[0]))
      .setMasterName(master)
      .setReadMode(ReadMode.SLAVE);

    if (StringUtils.isNotBlank(password)) {
      serverConfig.setPassword(password);
    }
    return Redisson.create(config);
  }
}
