package com.jifenke.lepluslive.merchant.controller.dto;


import com.jifenke.lepluslive.merchant.domain.entities.MerchantDetail;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by zhangwen on 2016/4/26.
 */
public class MerchantDto {

  private Long id;

  private int sid;

  private String location;

  private String phoneNumber;

  private String name;

  private String picture;

//  private Integer discount; //折扣
//
//  private Integer rebate;  //返利

  private Double distance;

  private Double lng;

  private Double lat;  //纬度

  private String typeName;

  private String cityName;

  private String areaName;

  private BigDecimal star;   //星级

  private Integer perSale;  //客单价

  private Integer park;   //有停车? 0=无

  private Integer wifi;   //有wifi? 0=无

  private Integer card;   //可刷卡? 0=不可

  private List<MerchantDetail> detailList;

  public List<MerchantDetail> getDetailList() {
    return detailList;
  }

  public void setDetailList(List<MerchantDetail> detailList) {
    this.detailList = detailList;
  }

  public Double getLng() {
    return lng;
  }

  public void setLng(Double lng) {
    this.lng = lng;
  }

  public Double getLat() {
    return lat;
  }

  public void setLat(Double lat) {
    this.lat = lat;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public int getSid() {
    return sid;
  }

  public void setSid(int sid) {
    this.sid = sid;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPicture() {
    return picture;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }

  public Double getDistance() {
    return distance;
  }

  public void setDistance(Double distance) {
    this.distance = distance;
  }

  public String getTypeName() {
    return typeName;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  public String getCityName() {
    return cityName;
  }

  public void setCityName(String cityName) {
    this.cityName = cityName;
  }

  public BigDecimal getStar() {
    return star;
  }

  public void setStar(BigDecimal star) {
    this.star = star;
  }

  public Integer getPerSale() {
    return perSale;
  }

  public void setPerSale(Integer perSale) {
    this.perSale = perSale;
  }

  public Integer getPark() {
    return park;
  }

  public void setPark(Integer park) {
    this.park = park;
  }

  public Integer getWifi() {
    return wifi;
  }

  public void setWifi(Integer wifi) {
    this.wifi = wifi;
  }

  public Integer getCard() {
    return card;
  }

  public void setCard(Integer card) {
    this.card = card;
  }

  public String getAreaName() {
    return areaName;
  }

  public void setAreaName(String areaName) {
    this.areaName = areaName;
  }
}
