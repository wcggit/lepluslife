package com.jifenke.lepluslive.order.controller.dto;

import com.jifenke.lepluslive.Address.domain.entities.Address;
import com.jifenke.lepluslive.order.domain.entities.OrderDetail;

import java.util.Date;
import java.util.List;


/**
 * 生成的订单信息及金币信息
 * Created by zhangwen on 2016/5/9.
 */
public class OnLineOrderDto {

  private Long id; //订单id

  private Long scoreB;  //用户可用金币

  private Long orderPrice; //订单虚拟总价

  private Long totalPrice;  //包括邮费

  private Long totalScore;  //该订单可使用金币

  private Long truePrice;  //不包括邮费

  private Long freightPrice;     //运费

  private Date createDate;

  private String orderSid; //订单编号

  private Address address; //默认收货地址

  private Integer minPrice;   //免运费最低价格

  private List<OrderDetail> orderDetails;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public List<OrderDetail> getOrderDetails() {
    return orderDetails;
  }

  public void setOrderDetails(List<OrderDetail> orderDetails) {
    this.orderDetails = orderDetails;
  }

  public Long getScoreB() {
    return scoreB;
  }

  public void setScoreB(Long scoreB) {
    this.scoreB = scoreB;
  }

  public Long getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(Long totalPrice) {
    this.totalPrice = totalPrice;
  }

  public Long getTotalScore() {
    return totalScore;
  }

  public void setTotalScore(Long totalScore) {
    this.totalScore = totalScore;
  }

  public Long getTruePrice() {
    return truePrice;
  }

  public void setTruePrice(Long truePrice) {
    this.truePrice = truePrice;
  }

  public Long getFreightPrice() {
    return freightPrice;
  }

  public void setFreightPrice(Long freightPrice) {
    this.freightPrice = freightPrice;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public String getOrderSid() {
    return orderSid;
  }

  public void setOrderSid(String orderSid) {
    this.orderSid = orderSid;
  }

  public Integer getMinPrice() {
    return minPrice;
  }

  public void setMinPrice(Integer minPrice) {
    this.minPrice = minPrice;
  }

  public Long getOrderPrice() {
    return orderPrice;
  }

  public void setOrderPrice(Long orderPrice) {
    this.orderPrice = orderPrice;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }
}
