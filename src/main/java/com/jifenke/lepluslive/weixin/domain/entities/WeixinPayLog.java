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

  private String outTradeNo; //商户订单号

  private String returnCode; //返回状态码|SUCCESS/FAIL|通信标识，非交易标识

  private String resultCode; //业务结果|SUCCESS/FAIL|交易是否成功

  private String tradeState; //查询时返回的交易状态

  private Integer logType;    //1=支付后微信回调日志|2=掉单查询订单日志

  private String orderType;   //onLineOrder=公众号线上订单|APPOnLineOrder|offLineOrder|APPOffLineOrder|PhoneOrder话费订单

  private String openId; //消费者openId

  private Long totalFee;  //订单金额

  private String transactionId;  //微信订单号

  private String timeEnd;  //微信支付完成时间

  private Date createDate = new Date();

  public WeixinPayLog(){

  }

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

  public String getOpenId() {
    return openId;
  }

  public void setOpenId(String openId) {
    this.openId = openId;
  }

  public Long getTotalFee() {
    return totalFee;
  }

  public void setTotalFee(Long totalFee) {
    this.totalFee = totalFee;
  }

  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  public String getTimeEnd() {
    return timeEnd;
  }

  public void setTimeEnd(String timeEnd) {
    this.timeEnd = timeEnd;
  }
}
