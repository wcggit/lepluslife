package com.jifenke.lepluslive.order.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 快递物流信息 存储已完成的物流信息 Created by zhangwen on 2016/5/6.
 */
@Entity
@Table(name = "EXPRESS_INFO")
public class ExpressInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne
  @JsonIgnore
  private OnLineOrder onLineOrder;

  private String expressNumber;  //快递单号

  private String expressCompany;

  private Integer status;  //快递状态  1 在途中 2 派件中 3 已签收 4 派送失败(拒签等)

  private String content;   //内容

  private Date freshDate;

  public Date getFreshDate() {
    return freshDate;
  }

  public void setFreshDate(Date freshDate) {
    this.freshDate = freshDate;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public OnLineOrder getOnLineOrder() {
    return onLineOrder;
  }

  public void setOnLineOrder(OnLineOrder onLineOrder) {
    this.onLineOrder = onLineOrder;
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

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}
