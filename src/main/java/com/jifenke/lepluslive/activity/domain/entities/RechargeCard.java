package com.jifenke.lepluslive.activity.domain.entities;

import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * 充值卡 充值记录 Created by tqy on 2017/1/3.
 */
@Entity
@Table(name = "RECHARGE_CARD")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RechargeCard {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotNull
  @Column(length = 80)
  private String exchangeCode;          //充值兑换码

  @ManyToOne
  private WeiXinUser weiXinUser;        //微信用户

  private Date createTime = new Date(); //创建时间(兑换时间)
  private Date completeTime;            //完成时间(处理时间)
  private Integer rechargeStatus = 0;   //充值状态, 1充值中 2已充值 3充值失败

  @Column(length = 100)
  private String note;                  //备注


  public Date getCompleteTime() {
    return completeTime;
  }

  public void setCompleteTime(Date completeTime) {
    this.completeTime = completeTime;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public String getExchangeCode() {
    return exchangeCode;
  }

  public void setExchangeCode(String exchangeCode) {
    this.exchangeCode = exchangeCode;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public Integer getRechargeStatus() {
    return rechargeStatus;
  }

  public void setRechargeStatus(Integer rechargeStatus) {
    this.rechargeStatus = rechargeStatus;
  }

  public WeiXinUser getWeiXinUser() {
    return weiXinUser;
  }

  public void setWeiXinUser(WeiXinUser weiXinUser) {
    this.weiXinUser = weiXinUser;
  }

}
