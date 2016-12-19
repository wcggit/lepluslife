package com.jifenke.lepluslive.activity.domain.entities;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 话费活动规则 Created by zhangwen on 2016/10/26.
 */
@Entity
@Table(name = "ACTIVITY_PHONE_RULE")
public class ActivityPhoneRule {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Integer state = 1;   //状态 1=上架 0=下架不可使用

  private Date createDate = new Date();   //创建时间

  private Date lastUpdate; //最后一次修改时间

  private Integer worth = 0; //充值面额 单位/元

  private Integer payType = 1;  //付费方式  1=现金+积分|2=纯现金|3=纯积分

  private Integer price = 0; //所需现金 type!=3时使用  单位/分

  private Integer score = 0;  //所需积分 type!=2时使用

  private Integer cheap = 0;  //是否是特惠活动（是的话没有累计购买限制和分类限购，属于全局限购）

  private Integer repositoryLimit = 0;  //是否有库存限制  0=无|1=有

  private Integer repository = 0;  //库存

  private Integer totalLimit = 0;  //累计购买限制  0=无限制

  private Integer limitType = 1;  //分类限购(和totalLimit联合限制，无冲突)  0=无分类限制|1=每日限购|2=每周限购|3=每月限购

  private Integer buyLimit = 0;  //分类限购数量   0=无限制

  private Integer rebateType = 0;  //红包返利方式    0=固定返利|1=随机返利

  private Integer rebate = 0;   //分为单位  返红包金额 固定返利金额或随机返利金额最大值

  private Integer minRebate = 0;  //随机返利金额最小值

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Date getLastUpdate() {
    return lastUpdate;
  }

  public void setLastUpdate(Date lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  public Integer getWorth() {
    return worth;
  }

  public void setWorth(Integer worth) {
    this.worth = worth;
  }

  public Integer getPayType() {
    return payType;
  }

  public void setPayType(Integer payType) {
    this.payType = payType;
  }

  public Integer getRepositoryLimit() {
    return repositoryLimit;
  }

  public void setRepositoryLimit(Integer repositoryLimit) {
    this.repositoryLimit = repositoryLimit;
  }

  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  public Integer getScore() {
    return score;
  }

  public void setScore(Integer score) {
    this.score = score;
  }

  public Integer getTotalLimit() {
    return totalLimit;
  }

  public void setTotalLimit(Integer totalLimit) {
    this.totalLimit = totalLimit;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public Integer getLimitType() {
    return limitType;
  }

  public void setLimitType(Integer limitType) {
    this.limitType = limitType;
  }

  public Integer getBuyLimit() {
    return buyLimit;
  }

  public void setBuyLimit(Integer buyLimit) {
    this.buyLimit = buyLimit;
  }

  public Integer getRebateType() {
    return rebateType;
  }

  public Integer getCheap() {
    return cheap;
  }

  public void setCheap(Integer cheap) {
    this.cheap = cheap;
  }

  public Integer getRepository() {
    return repository;
  }

  public void setRepository(Integer repository) {
    this.repository = repository;
  }

  public void setRebateType(Integer rebateType) {
    this.rebateType = rebateType;
  }

  public Integer getRebate() {
    return rebate;
  }

  public void setRebate(Integer rebate) {
    this.rebate = rebate;
  }

  public Integer getMinRebate() {
    return minRebate;
  }

  public void setMinRebate(Integer minRebate) {
    this.minRebate = minRebate;
  }
}
