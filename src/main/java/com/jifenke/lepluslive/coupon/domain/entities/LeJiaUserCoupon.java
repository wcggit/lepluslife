package com.jifenke.lepluslive.coupon.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;

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
 * 用户领取的商家优惠券 Created by zhangwen on 2016/11/29.
 */
@Entity
@Table(name = "LE_JIA_USER_COUPON")
public class LeJiaUserCoupon {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  private LeJiaUser leJiaUser;

  @NotNull
  @ManyToOne
  private MerchantCoupon merchantCoupon;

  private String merchantName;  //商家名称

  @Column(nullable = false)
  private Date beginDate;  //有效期起始时间 例:2016/11/24 00:00:00表示生效于2016/11/24

  @Column(nullable = false)
  private Date endDate;    //有效期截止时间 例:2016/11/29 23:59:59表示截止到2016/11/29

  private Integer state = 1;   //状态 1=待使用|2=已使用|3=已过期

  private Date createDate = new Date();   //创建及领取时间

  private Date useDate;  //使用时间

  private Integer payType = 0;   //领取方式 0=免费领取|1=纯积分

  private Integer useScore = 0;  //领取消耗积分

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public LeJiaUser getLeJiaUser() {
    return leJiaUser;
  }

  public void setLeJiaUser(LeJiaUser leJiaUser) {
    this.leJiaUser = leJiaUser;
  }

  public MerchantCoupon getMerchantCoupon() {
    return merchantCoupon;
  }

  public void setMerchantCoupon(MerchantCoupon merchantCoupon) {
    this.merchantCoupon = merchantCoupon;
  }

  public String getMerchantName() {
    return merchantName;
  }

  public void setMerchantName(String merchantName) {
    this.merchantName = merchantName;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public Integer getPayType() {
    return payType;
  }

  public void setPayType(Integer payType) {
    this.payType = payType;
  }

  public Integer getUseScore() {
    return useScore;
  }

  public void setUseScore(Integer useScore) {
    this.useScore = useScore;
  }

  public Date getUseDate() {
    return useDate;
  }

  public void setUseDate(Date useDate) {
    this.useDate = useDate;
  }
}
