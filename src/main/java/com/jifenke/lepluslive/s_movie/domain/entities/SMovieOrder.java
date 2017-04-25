package com.jifenke.lepluslive.s_movie.domain.entities;

import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;

import java.util.Date;

import javax.inject.Inject;
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


}
