package com.jifenke.lepluslive.order.domain.entities;

import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.Address.domain.entities.Address;

import org.hibernate.annotations.Cascade;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Created by wcg on 16/3/20.
 */
@Entity
@Table(name = "ON_LINE_ORDER")
public class OnLineOrder {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "onLineOrder")
  private List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();


  private Long totalPrice;  //包括邮费

  private Long totalScore;

  private Long truePrice;  //不包括积分抵扣

  private Long trueScore;

  private Long freightPrice;     //运费

  @ManyToOne
  private LeJiaUser leJiaUser;

  private Date createDate = new Date();

  private Date confirmDate;

  private String orderSid = MvUtil.getOrderNumber();

  @ManyToOne
  private Address address;

  private Integer state;//0 未支付 1 已支付 2 已发货 3已收获 4 订单取消

  private String payWay;

  private String expressNumber;  //快递单号

  private String expressCompany; //快递公司名称

  public Date getConfirmDate() {
    return confirmDate;
  }

  public void setConfirmDate(Date confirmDate) {
    this.confirmDate = confirmDate;
  }

  public Long getFreightPrice() {
    return freightPrice;
  }

  public void setFreightPrice(Long freightPrice) {
    this.freightPrice = freightPrice;
  }

  public String getExpressNumber() {
    return expressNumber;
  }

  public void setExpressNumber(String expressNumber) {
    this.expressNumber = expressNumber;
  }

  public String getExpressCompany() {
    return expressCompany;
  }

  public void setExpressCompany(String expressCompany) {
    this.expressCompany = expressCompany;
  }

  public Long getTruePrice() {
    return truePrice;
  }

  public void setTruePrice(Long truePrice) {
    this.truePrice = truePrice;
  }

  public Long getTrueScore() {
    return trueScore;
  }

  public void setTrueScore(Long trueScore) {
    this.trueScore = trueScore;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public String getPayWay() {
    return payWay;
  }

  public void setPayWay(String payWay) {
    this.payWay = payWay;
  }

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

  public LeJiaUser getLeJiaUser() {
    return leJiaUser;
  }

  public void setLeJiaUser(LeJiaUser leJiaUser) {
    this.leJiaUser = leJiaUser;
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

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

}
