package com.cctv.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * @author by 封心
 * @classname RedisService
 * @description TODO
 * @date 2022/4/26 9:47
 */
@Service
public class RedisService {

  @Autowired
  private RedisTemplate<String, Object> redisTemplate;

  /**
   * bitmap 基本使用
   * 日活统计应用场景中 bitmap 使用姿势
   * 点赞去重应用场景中 bitmap 使用姿势
   * 布隆过滤器 bloomfilter 基本原理及体验 case
   * 日活统计：主要借助bitcount来获取总数（后面会介绍，在日活十万百万以上时，使用 hyperLogLog 更优雅）
   * 点赞: 主要借助setbit/getbit来判断用户是否赞过，从而实现去重
   * bloomfilter: 基于 bitmap 实现的布隆过滤器，广泛用于去重的业务场景中（如缓存穿透，爬虫 url 去重等）
   */
  /**
   * 签到统计
   * key  举例  offset 就是每个月的天数 设置为 setBit  sign:userId:yyyymm
   * 统计这个月1号 有没有签到 getBit  sign:userId:yyyymm  1
   * 统计这个月一共签到   BitCount    sign:userId:yyyymm
   * <p>
   * 点赞统计
   * key为  like:articleId offset为：userId
   * 点赞   setBit  like:articleId  userId true
   * 取消点赞 setBit  like:articleId  userId false
   * 判断有没有点赞 getBit like:articleId  userId
   * userId 不均匀 不是自增 就会产生大量的空白数据  不是数字的话 就会比较坑
   */
  public Boolean bitMapMark(String key, long offset, boolean tag) {
    return redisTemplate.opsForValue().setBit(key, offset, tag);
  }

  public Boolean bitMapGet(String key, long offset) {
    return redisTemplate.opsForValue().getBit(key, offset);
  }

  public Long bitMapCount(String key) {
    return redisTemplate.execute((RedisCallback<Long>) connection -> connection.bitCount(key.getBytes(StandardCharsets.UTF_8)));
  }

}
