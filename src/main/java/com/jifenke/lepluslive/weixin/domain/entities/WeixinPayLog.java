package com.jifenke.lepluslive.weixin.domain.entities;

import java.util.Date;

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
  private String tradeState;

  private Integer logType;    //1=支付后微信回调日志|2=掉单查询订单日志

  private String orderType;   //onLineOrder=公众号线上订单|APPOnLineOrder|offLineOrder|APPOffLineOrder

  private Date createDate = new Date();

  public WeixinPayLog(String outTradeNo, String returnCode, String resultCode, Integer logType,
                      String orderType) {
    this.outTradeNo = outTradeNo;
    this.returnCode = returnCode;
    this.resultCode = resultCode;
    this.logType = logType;
    this.orderType = orderType;
  }

  public WeixinPayLog(String outTradeNo, String returnCode, String resultCode,
                      String tradeState, Integer logType, String orderType) {
    this.outTradeNo = outTradeNo;
    this.returnCode = returnCode;
    this.resultCode = resultCode;
    this.tradeState = tradeState;
    this.logType = logType;
    this.orderType = orderType;
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

  public String getTradeState() {
    return tradeState;
  }

  public void setTradeState(String tradeState) {
    this.tradeState = tradeState;
  }

  public Integer getLogType() {
    return logType;
  }

  public void setLogType(Integer logType) {
    this.logType = logType;
  }

  public String getOrderType() {
    return orderType;
  }

  public void setOrderType(String orderType) {
    this.orderType = orderType;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }
}
