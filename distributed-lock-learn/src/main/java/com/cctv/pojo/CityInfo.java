package com.cctv.pojo;

/**
 * @author by 封心
 * @classname CityInfo
 * @description TODO
 * @date 2022/4/26 10:31
 */
public class CityInfo {

  /**
   * 城市名
   */
  private String city;

  /**
   * 经度
   */
  private Double longitude;

  /**
   * 维度
   */
  private Double latitude;

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  @Override
  public String toString() {
    return "CityInfo{" +
      "city='" + city + '\'' +
      ", longitude=" + longitude +
      ", latitude=" + latitude +
      '}';
  }

  public CityInfo(String city, Double longitude, Double latitude) {
    this.city = city;
    this.longitude = longitude;
    this.latitude = latitude;
  }
}
