package com.jifenke.lepluslive.weixin.domain.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 微信支付记录 Created by zhangwen on 2016/5/26.
 */
@Entity
@Table(name = "WEIXIN_PAY_LOG")
public class WeixinPayLog {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String outTradeNo;
  private String returnCode;
  private String resultCode;


  public WeixinPayLog(String outTradeNo, String returnCode, String resultCode) {
    this.outTradeNo = outTradeNo;
    this.returnCode = returnCode;
    this.resultCode = resultCode;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getOutTradeNo() {
    return outTradeNo;
  }

  public void setOutTradeNo(String outTradeNo) {
    this.outTradeNo = outTradeNo;
  }

  public String getReturnCode() {
    return returnCode;
  }

  public void setReturnCode(String returnCode) {
    this.returnCode = returnCode;
  }

  public String getResultCode() {
    return resultCode;
  }

  public void setResultCode(String resultCode) {
    this.resultCode = resultCode;
  }

}
