package com.jifenke.lepluslive.coupon.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.weixin.domain.entities.Category;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * 商家优惠券 Created by zhangwen on 2016/11/29.
 */
@Entity
@Table(name = "MERCHANT_COUPON")
public class MerchantCoupon {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  private Category couponType;  //优惠券类型

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  private Merchant merchant;

  private String merchantName;  //商户名称

  private String picture;  //优惠券图片，暂用商家小图

  @Column(nullable = false)
  private Date beginDate;  //有效期起始时间 例:2016/11/24 00:00:00表示生效于2016/11/24

  @Column(nullable = false)
  private Date endDate;    //有效期截止时间 例:2016/11/29 23:59:59表示截止到2016/11/29

  @Column(nullable = false, length = 50)
  private String describe1;  //描述主题1

  @Column(length = 50)
  private String describe2;  //描述主题2

  @Column(nullable = false, length = 10)
  private String title;     //优惠主题(限5字)

  @JsonIgnore
  private Integer state = 1;   //状态 1=上架|0=下架对已领取的无约束

  private Integer repository = 0;  //库存

  private Integer getNum = 0;  //已领取人数

  private Integer payType = 0;   //支付方式 0=免费领取|1=纯积分

  private Integer scoreB = 0;  //所需积分

  private Integer sid = 0;  //序号

  private Date createDate = new Date();   //创建时间

  @JsonIgnore
  private Date lastUpdate;   //最后修改时间

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Merchant getMerchant() {
    return merchant;
  }

  public void setMerchant(Merchant merchant) {
    this.merchant = merchant;
  }

  public String getPicture() {
    return picture;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }

  public Category getCouponType() {
    return couponType;
  }

  public String getMerchantName() {
    return merchantName;
  }

  public void setMerchantName(String merchantName) {
    this.merchantName = merchantName;
  }

  public void setCouponType(Category couponType) {
    this.couponType = couponType;
  }

  public Date getBeginDate() {
    return beginDate;
  }

  public void setBeginDate(Date beginDate) {
    this.beginDate = beginDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public String getDescribe1() {
    return describe1;
  }

  public void setDescribe1(String describe1) {
    this.describe1 = describe1;
  }

  public String getDescribe2() {
    return describe2;
  }

  public void setDescribe2(String describe2) {
    this.describe2 = describe2;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public Integer getRepository() {
    return repository;
  }

  public void setRepository(Integer repository) {
    this.repository = repository;
  }

  public Integer getGetNum() {
    return getNum;
  }

  public void setGetNum(Integer getNum) {
    this.getNum = getNum;
  }

  public Integer getPayType() {
    return payType;
  }

  public void setPayType(Integer payType) {
    this.payType = payType;
  }

  public Integer getScoreB() {
    return scoreB;
  }

  public void setScoreB(Integer scoreB) {
    this.scoreB = scoreB;
  }

  public Integer getSid() {
    return sid;
  }

  public void setSid(Integer sid) {
    this.sid = sid;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Date getLastUpdate() {
    return lastUpdate;
  }

  public void setLastUpdate(Date lastUpdate) {
    this.lastUpdate = lastUpdate;
  }
}
