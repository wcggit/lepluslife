package com.jifenke.lepluslive.s_movie.domain.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 电影产品 Created by zhangwen on 2017/4/25.
 */
@Entity
@Table(name = "S_MOVIE_PRODUCT")
public class SMovieProduct {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Date dateCreated = new Date();

  private Integer state = 1;   //状态  1=上架|0=下架

  private Integer sid = 1;  //显示顺序  越大越靠前

  private String name;  //产品名称

  private String picture;  //产品图片

  private String introduce;  //图片下文案  分行符& 示例：aaa&uu

  private Long price = 0L;  //原价 单位/分

  private Long costPrice = 0L;  //电影票采购的成本价  单位/分

  private Long payBackA = 0L;   //订单支付后的返鼓励金金额

  private Long toMerchant = 0L;   //锁定门店分润

  private Long toPartner = 0L;   //锁定天使合伙人分润

  private Long toPartnerManager = 0L;   //锁定城市合伙人分润

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(Date dateCreated) {
    this.dateCreated = dateCreated;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public Integer getSid() {
    return sid;
  }

  public void setSid(Integer sid) {
    this.sid = sid;
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

  public String getIntroduce() {
    return introduce;
  }

  public void setIntroduce(String introduce) {
    this.introduce = introduce;
  }

  public Long getPrice() {
    return price;
  }

  public void setPrice(Long price) {
    this.price = price;
  }

  public Long getCostPrice() {
    return costPrice;
  }

  public void setCostPrice(Long costPrice) {
    this.costPrice = costPrice;
  }

  public Long getPayBackA() {
    return payBackA;
  }

  public void setPayBackA(Long payBackA) {
    this.payBackA = payBackA;
  }

  public Long getToMerchant() {
    return toMerchant;
  }

  public void setToMerchant(Long toMerchant) {
    this.toMerchant = toMerchant;
  }

  public Long getToPartner() {
    return toPartner;
  }

  public void setToPartner(Long toPartner) {
    this.toPartner = toPartner;
  }

  public Long getToPartnerManager() {
    return toPartnerManager;
  }

  public void setToPartnerManager(Long toPartnerManager) {
    this.toPartnerManager = toPartnerManager;
  }
}
