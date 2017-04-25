package com.jifenke.lepluslive.s_movie.domain.entities;

import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 电影订单表 Created by zhangwen on 2017/4/25.
 */
@Entity
@Table(name = "S_MOVIE_ORDER")
public class SMovieOrder {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Date dateCreated = new Date();   //订单创建时间

  private String orderSid = MvUtil.getOrderNumber();

  private Date dateCompleted;  //支付完成时间

  private Date dateUsed;       //核销使用时间

  @ManyToOne
  private SMovieTerminal sMovieTerminal;  //核销的影院

  @ManyToOne
  private SMovieProduct sMovieProduct;   //购买对应的产品

  @ManyToOne
  private LeJiaUser leJiaUser;

  private Long totalPrice = 0L;   //订单金额  单位/分

  private Long trueScore = 0L;   //实际使用金币 单位/分

  private Long truePrice = 0L;   //实际付款金额  单位/分

  private Long commission = 0L;  //支付手续费  默认=实付金额(truePrice)*0.6%四舍五入

  private Long trueIncome = 0L;  //实际入账=truePrice-commission

  private Long payBackA = 0L;   //订单支付后的返鼓励金金额

  private Integer state = 0;   //订单状态   0=待付款|1=已付款待核销|2=已付款已核销|3=已退款

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(Date dateCreated) {
    this.dateCreated = dateCreated;
  }

  public String getOrderSid() {
    return orderSid;
  }

  public void setOrderSid(String orderSid) {
    this.orderSid = orderSid;
  }

  public Date getDateCompleted() {
    return dateCompleted;
  }

  public void setDateCompleted(Date dateCompleted) {
    this.dateCompleted = dateCompleted;
  }

  public Date getDateUsed() {
    return dateUsed;
  }

  public void setDateUsed(Date dateUsed) {
    this.dateUsed = dateUsed;
  }

  public SMovieTerminal getsMovieTerminal() {
    return sMovieTerminal;
  }

  public void setsMovieTerminal(SMovieTerminal sMovieTerminal) {
    this.sMovieTerminal = sMovieTerminal;
  }

  public SMovieProduct getsMovieProduct() {
    return sMovieProduct;
  }

  public void setsMovieProduct(SMovieProduct sMovieProduct) {
    this.sMovieProduct = sMovieProduct;
  }

  public LeJiaUser getLeJiaUser() {
    return leJiaUser;
  }

  public void setLeJiaUser(LeJiaUser leJiaUser) {
    this.leJiaUser = leJiaUser;
  }

  public Long getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(Long totalPrice) {
    this.totalPrice = totalPrice;
  }

  public Long getTrueScore() {
    return trueScore;
  }

  public void setTrueScore(Long trueScore) {
    this.trueScore = trueScore;
  }

  public Long getTruePrice() {
    return truePrice;
  }

  public void setTruePrice(Long truePrice) {
    this.truePrice = truePrice;
  }

  public Long getCommission() {
    return commission;
  }

  public void setCommission(Long commission) {
    this.commission = commission;
  }

  public Long getTrueIncome() {
    return trueIncome;
  }

  public void setTrueIncome(Long trueIncome) {
    this.trueIncome = trueIncome;
  }

  public Long getPayBackA() {
    return payBackA;
  }

  public void setPayBackA(Long payBackA) {
    this.payBackA = payBackA;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }
}
