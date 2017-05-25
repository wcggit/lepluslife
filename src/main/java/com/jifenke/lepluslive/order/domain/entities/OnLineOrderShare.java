package com.jifenke.lepluslive.order.domain.entities;

import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerManager;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 线上订单分润 Created by zhangwen on 16/11/05.
 */
@Entity
@Table(name = "ON_LINE_ORDER_SHARE")
public class OnLineOrderShare {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @OneToOne
  private OnLineOrder onLineOrder;  //即将废弃 不用了 采用type+orderSid

  private Integer type = 0;   //分润单来源   1=商城|2=电影票

  private String orderSid;   //对应的分润来源的订单号

  private Long shareMoney = 0L;  //=toLockMerchant + toLockPartner + toLockPartnerManager


  private Date createDate = new Date();

  @ManyToOne
  private Merchant lockMerchant; //锁定商户

  private Long toLockMerchant = 0L;

  @ManyToOne
  private Partner lockPartner;  //锁定天使合伙人

  private Long toLockPartner = 0L;

  @ManyToOne
  private PartnerManager lockPartnerManager; //锁定城市合伙人

  private Long toLockPartnerManager = 0L;

  private Long toLePlusLife = 0L; //给积分客的

  public Long getShareMoney() {
    return shareMoney;
  }

  public void setShareMoney(Long shareMoney) {
    this.shareMoney = shareMoney;
  }

  public Merchant getLockMerchant() {
    return lockMerchant;
  }

  public void setLockMerchant(Merchant lockMerchant) {
    this.lockMerchant = lockMerchant;
  }

  public Partner getLockPartner() {
    return lockPartner;
  }

  public void setLockPartner(Partner lockPartner) {
    this.lockPartner = lockPartner;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public OnLineOrder getOnLineOrder() {
    return onLineOrder;
  }

  public void setOnLineOrder(OnLineOrder onLineOrder) {
    this.onLineOrder = onLineOrder;
  }

  public Long getToLockMerchant() {
    return toLockMerchant;
  }

  public void setToLockMerchant(Long toLockMerchant) {
    this.toLockMerchant = toLockMerchant;
  }

  public Long getToLockPartner() {
    return toLockPartner;
  }

  public void setToLockPartner(Long toLockPartner) {
    this.toLockPartner = toLockPartner;
  }

  public Long getToLockPartnerManager() {
    return toLockPartnerManager;
  }

  public void setToLockPartnerManager(Long toLockPartnerManager) {
    this.toLockPartnerManager = toLockPartnerManager;
  }

  public PartnerManager getLockPartnerManager() {
    return lockPartnerManager;
  }

  public void setLockPartnerManager(PartnerManager lockPartnerManager) {
    this.lockPartnerManager = lockPartnerManager;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public String getOrderSid() {
    return orderSid;
  }

  public void setOrderSid(String orderSid) {
    this.orderSid = orderSid;
  }

  public Long getToLePlusLife() {
    return toLePlusLife;
  }

  public void setToLePlusLife(Long toLePlusLife) {
    this.toLePlusLife = toLePlusLife;
  }
}
