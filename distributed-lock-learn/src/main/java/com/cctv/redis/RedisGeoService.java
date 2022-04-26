package com.cctv.redis;

import com.cctv.pojo.CityInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author by 封心
 * @classname RedisGeoService
 * @description TODO
 * @date 2022/4/26 10:33
 */
@Service
public class RedisGeoService {

  /**
   * Redis 的 Geo 是在 3.2 版本才有的
   * 使用 geohash 保存地理位置的坐标
   * 使用有序集合（zset）保存地理位置的集合
   *
   * GEOADD：增加某个地理位置的坐标
   * GEOPOS：获取某个地理位置的坐标
   * GEODIST：获取两个地理位置的距离
   * GEORADIUS：根据给定地理位置坐标获取指定范围内的地理位置集合
   * GEORADIUSBYMEMBER：根据给定地理位置获取指定范围内的地理位置集合
   * GEOHASH：获取某个地理位置的 geohash 值
   */

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  private final String GEO_kEY = "ah-cities";

  /**
   * 把城市信息保存到 Redis 中
   * @param cityInfos
   * @return
   */
  public Long saveCityInfoToRedis(Collection<CityInfo> cityInfos) {
    Set<RedisGeoCommands.GeoLocation<String>> locations = new HashSet<>();
    cityInfos.forEach(cityInfo -> locations.add(new RedisGeoCommands.GeoLocation<String>(cityInfo.getCity(),
      new Point(cityInfo.getLongitude(), cityInfo.getLatitude()))));
   return redisTemplate.opsForGeo().add(GEO_kEY, locations);
  }

  /**
   * 获取给定城市的坐标
   * @return
   */
  public List<Point> getCityPos(String[] cities) {
    return redisTemplate.opsForGeo().position(GEO_kEY, cities);
  }

  /**
   * 获取两个城市之间的距离
   * @param city1
   * @param city2
   * @param metric
   * @return
   */
  public Distance getTwoCityDistance(String city1, String city2, Metric metric) {
    return metric == null ? redisTemplate.opsForGeo().distance(GEO_kEY, city1, city2) : redisTemplate.opsForGeo().distance(GEO_kEY, city1, city2, metric);
  }

  /**
   * 根据给定地理位置坐标获取指定范围内的地理位置集合
   *
   * @param within
   * @param args
   * @return
   */
  public GeoResults<RedisGeoCommands.GeoLocation<String>> getPointRadius(Circle within, RedisGeoCommands.GeoRadiusCommandArgs args){
    return args==null?redisTemplate.opsForGeo().radius(GEO_kEY,within):redisTemplate.opsForGeo().radius(GEO_kEY,within,args);
  }

  /**
   * 根据给定地理位置获取指定范围内的地理位置集合
   *
   * @param member
   * @param distance
   * @param args
   * @return
   */
  public GeoResults<RedisGeoCommands.GeoLocation<String>> getMemberRadius(String member, Distance distance, RedisGeoCommands.GeoRadiusCommandArgs args){
    return args==null?redisTemplate.opsForGeo().radius(GEO_kEY,member,distance):redisTemplate.opsForGeo().radius(GEO_kEY,member,distance,args);
  }

  /**
   * 获取某个地理位置的 geohash 值
   * @param cities
   * @return
   */
  public List<String> getCityGeoHash(String[] cities){
    return redisTemplate.opsForGeo().hash(GEO_kEY, cities);
  }
}
