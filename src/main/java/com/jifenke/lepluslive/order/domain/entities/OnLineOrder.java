package com.jifenke.lepluslive.order.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.Address.domain.entities.Address;

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

  private Long orderPrice = 0L;  //对应商品的price和，虚拟总价，不包括邮费

  private Long totalPrice = 0L;  //对应商品的minPrice和，最低需付金额，包括邮费

  private Long totalScore = 0L;  //订单可用积分

  private Long truePrice = 0L;  //不包括积分抵扣

  private Long trueScore = 0L;  //订单实际使用积分

  private Long freightPrice = 0L;     //运费，选择线下自提时不变但没有计算价值(实际为0)

  private Long payBackA = 0L;   //订单支付后的返红包额度

  @ManyToOne
  @JsonIgnore
  private LeJiaUser leJiaUser;

  private Date createDate = new Date();

  private Date payDate;      //付款时间

  private Date deliveryDate; //发货时间

  private Date confirmDate;  //确认收货时间

  private String orderSid = MvUtil.getOrderNumber();

  @ManyToOne(cascade = CascadeType.DETACH)
  private Address address;

  private Integer state;//0 未支付 1 已支付 2 已发货 3已收获 4 订单取消

  private Integer payState = 0;    //支付状态 0=未支付|1=已支付

  private Integer transmitWay;    //取货方式  1=线下自提|2=快递

  @ManyToOne
  private PayOrigin payOrigin;    //支付方式及订单来源

  private String expressNumber;  //快递单号

  private String expressCompany; //快递公司名称

  public Date getDeliveryDate() {
    return deliveryDate;
  }

  public void setDeliveryDate(Date deliveryDate) {
    this.deliveryDate = deliveryDate;
  }

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

  public PayOrigin getPayOrigin() {
    return payOrigin;
  }

  public void setPayOrigin(PayOrigin payOrigin) {
    this.payOrigin = payOrigin;
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

  public Integer getTransmitWay() {
    return transmitWay;
  }

  public void setTransmitWay(Integer transmitWay) {
    this.transmitWay = transmitWay;
  }

  public Date getPayDate() {
    return payDate;
  }

  public void setPayDate(Date payDate) {
    this.payDate = payDate;
  }

  public Long getPayBackA() {
    return payBackA;
  }

  public void setPayBackA(Long payBackA) {
    this.payBackA = payBackA;
  }

  public Long getOrderPrice() {
    return orderPrice;
  }

  public void setOrderPrice(Long orderPrice) {
    this.orderPrice = orderPrice;
  }

  public Integer getPayState() {
    return payState;
  }

  public void setPayState(Integer payState) {
    this.payState = payState;
  }
}
