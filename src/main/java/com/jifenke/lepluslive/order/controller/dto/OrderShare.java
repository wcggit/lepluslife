package com.jifenke.lepluslive.order.controller.dto;

/**
 * 分润信息
 * Created by zhangwen on 2017/6/27.
 */
public class OrderShare {

  private Long userId;

  private Long merchantId = 0L;

  private Long type = 1L;

  private String orderSid;

  private Long rebateScore = 0L;

  private Long shareMoney = 0L;

  private Long shareToLockMerchant = 0L;

  private Long shareToLockPartner = 0L;

  private Long shareToTradePartner = 0L;

  private Long shareToLockPartnerManager = 0L;

  private Long shareToTradePartnerManager = 0L;

  private Long toLePlusLife = 0L;

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Long getMerchantId() {
    return merchantId;
  }

  public void setMerchantId(Long merchantId) {
    this.merchantId = merchantId;
  }

  public Long getType() {
    return type;
  }

  public void setType(Long type) {
    this.type = type;
  }

  public Long getRebateScore() {
    return rebateScore;
  }

  public void setRebateScore(Long rebateScore) {
    this.rebateScore = rebateScore;
  }

  public Long getShareMoney() {
    return shareMoney;
  }

  public void setShareMoney(Long shareMoney) {
    this.shareMoney = shareMoney;
  }

  public Long getShareToLockMerchant() {
    return shareToLockMerchant;
  }

  public void setShareToLockMerchant(Long shareToLockMerchant) {
    this.shareToLockMerchant = shareToLockMerchant;
  }

  public Long getShareToLockPartner() {
    return shareToLockPartner;
  }

  public void setShareToLockPartner(Long shareToLockPartner) {
    this.shareToLockPartner = shareToLockPartner;
  }

  public Long getShareToTradePartner() {
    return shareToTradePartner;
  }

  public void setShareToTradePartner(Long shareToTradePartner) {
    this.shareToTradePartner = shareToTradePartner;
  }

  public Long getShareToLockPartnerManager() {
    return shareToLockPartnerManager;
  }

  public void setShareToLockPartnerManager(Long shareToLockPartnerManager) {
    this.shareToLockPartnerManager = shareToLockPartnerManager;
  }

  public Long getShareToTradePartnerManager() {
    return shareToTradePartnerManager;
  }

  public void setShareToTradePartnerManager(Long shareToTradePartnerManager) {
    this.shareToTradePartnerManager = shareToTradePartnerManager;
  }

  public Long getToLePlusLife() {
    return toLePlusLife;
  }

  public void setToLePlusLife(Long toLePlusLife) {
    this.toLePlusLife = toLePlusLife;
  }

  public String getOrderSid() {
    return orderSid;
  }

  public void setOrderSid(String orderSid) {
    this.orderSid = orderSid;
  }
}
