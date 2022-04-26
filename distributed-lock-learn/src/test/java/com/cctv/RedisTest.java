package com.cctv;

import com.cctv.pojo.CityInfo;
import com.cctv.redis.RedisGeoService;
import com.cctv.redis.RedisLockService;
import com.cctv.redis.RedisService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author by 封心
 * @classname RedisTest
 * @description TODO
 * @date 2022/4/26 9:59
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class RedisTest {

  @Autowired
  private RedisService redisService;

  @Autowired
  private RedisGeoService redisGeoService;

  ObjectMapper objectMapper = new ObjectMapper();


  @Test
  public void test() {
    for (int i = 0; i < 10; i++) {
      redisService.bitMapMark("user:1:202204", ThreadLocalRandom.current().nextInt(29) + 1, true);
    }
  }

  @Test
  public void test1() {
    System.out.println(redisService.bitMapCount("user:1:202204"));
  }

  @Test
  public void test2() {
    List<String> strings = new ArrayList<>();
    for (int i = 0; i < 30; i++) {
      strings.add(redisService.bitMapGet("user:1:202204", i) ? "1" : "0");
    }
    System.out.println(strings);
  }

  /**
   * saveCityInfoToRedis
   */
  @Test
  public void init() {
    List<CityInfo> cityInfos;
    cityInfos = new ArrayList<>();
    cityInfos.add(new CityInfo("hefei", 117.17, 31.52));
    cityInfos.add(new CityInfo("anqing", 117.02, 30.31));
    cityInfos.add(new CityInfo("huaibei", 116.47, 33.57));
    cityInfos.add(new CityInfo("suzhou", 116.58, 33.38));
    cityInfos.add(new CityInfo("fuyang", 115.48, 32.54));
    cityInfos.add(new CityInfo("bengbu", 117.21, 32.56));
    cityInfos.add(new CityInfo("huangshan", 118.18, 29.43));
    System.out.println(redisGeoService.saveCityInfoToRedis(cityInfos));
  }


  @Test
  public void testGetCityPos() {
    List<Point> cityPos = redisGeoService.getCityPos(Arrays.asList("anqing", "suzhou", "xxx").toArray(new String[3]));
    try {
      System.out.println(objectMapper.writeValueAsString(cityPos));
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testGetTwoCityDistance() {
    System.out.println(redisGeoService.getTwoCityDistance("anqing", "suzhou", null));
    System.out.println(redisGeoService.getTwoCityDistance("anqing", "suzhou", Metrics.KILOMETERS));
  }

  @Test
  public void testGetPointRadius() throws JsonProcessingException {
    CityInfo hefei = new CityInfo("hefei", 117.17, 31.52);
    Point point = new Point(hefei.getLongitude(), hefei.getLatitude());
    Distance distance = new Distance(200, Metrics.KILOMETERS);
    Circle circle = new Circle(point, distance);
    // order by 距离 limit 2, 同时返回距离中心点的距离
    RedisGeoCommands.GeoRadiusCommandArgs args =
      RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().limit(2).sortAscending();
    GeoResults<RedisGeoCommands.GeoLocation<String>> pointRadius = redisGeoService.getPointRadius(circle, args);
    System.out.println(objectMapper.writeValueAsString(pointRadius));
  }

  @Test
  public void testGetMemberRadius() throws JsonProcessingException {
    Distance radius = new Distance(200, Metrics.KILOMETERS);
    GeoResults<RedisGeoCommands.GeoLocation<String>> suzhou = redisGeoService.getMemberRadius("suzhou", radius, null);
    // order by 距离 limit 2, 同时返回距离中心点的距离
    RedisGeoCommands.GeoRadiusCommandArgs args =
      RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().limit(2).sortAscending();
    GeoResults<RedisGeoCommands.GeoLocation<String>> suzhou1 = redisGeoService.getMemberRadius("suzhou", radius, args);
    System.out.println(objectMapper.writeValueAsString(suzhou1));
  }

  @Test
  public void testGetCityGeoHash() throws JsonProcessingException {

    System.out.println(objectMapper.writeValueAsString(redisGeoService.getCityGeoHash(
      Arrays.asList("anqing", "suzhou", "xxx").toArray(new String[3])
    )));
  }

  @Autowired
  private RedisLockService redisLockService;

  @Test
  public void test11() {
    redisLockService.testLock();
  }
}
