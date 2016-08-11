package com.jifenke.lepluslive.activity.domain.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * 扫描永久二维码送红包 Created by zhangwen on 2016/8/4.
 */
@Entity
@Table(name = "ACTIVITY_CODE_BURSE")
public class ActivityCodeBurse {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Version
  private Long version = 0L;

  private Integer type = 1;    //活动类型 1=关注永久  2=裂变临时

  private Date createDate = new Date();   //创建时间

  private Date beginDate;  //开始时间

  private Date endDate;   //结束时间

  private Integer state = 1; //0=暂停  1=进行中

  private String title;  //活动标题

  private Integer budget = 0;   //活动预算

  private Integer singleMoney = 0;   //单次领取红包额

  private String parameter;  //二维码参数(1-32位)随机生成  最多2万个  Y=送红包 || L=裂变

  private Integer totalNumber = 0;  //扫码领红包人数

  private Integer totalMoney = 0;   //已发放金额

  private String ticket;      //获取的二维码ticket，凭借此ticket可以在有效时间内换取二维码

  private Integer inviteMoney = 0;   //邀请人可获得的红包

  private Integer scanInviteMoney = 0;  //被邀请可获得的红包

  private Integer scanInviteNumber = 0;   //被该活动邀请关注的人数

  public Long getId() {
    return id;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public Date getBeginDate() {
    return beginDate;
  }

  public void setBeginDate(Date beginDate) {
    this.beginDate = beginDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Integer getBudget() {
    return budget;
  }

  public void setBudget(Integer budget) {
    this.budget = budget;
  }

  public Integer getSingleMoney() {
    return singleMoney;
  }

  public void setSingleMoney(Integer singleMoney) {
    this.singleMoney = singleMoney;
  }

  public String getParameter() {
    return parameter;
  }

  public void setParameter(String parameter) {
    this.parameter = parameter;
  }

  public Integer getTotalNumber() {
    return totalNumber;
  }

  public void setTotalNumber(Integer totalNumber) {
    this.totalNumber = totalNumber;
  }

  public Integer getTotalMoney() {
    return totalMoney;
  }

  public void setTotalMoney(Integer totalMoney) {
    this.totalMoney = totalMoney;
  }

  public String getTicket() {
    return ticket;
  }

  public void setTicket(String ticket) {
    this.ticket = ticket;
  }

  public Integer getInviteMoney() {
    return inviteMoney;
  }

  public void setInviteMoney(Integer inviteMoney) {
    this.inviteMoney = inviteMoney;
  }

  public Integer getScanInviteMoney() {
    return scanInviteMoney;
  }

  public void setScanInviteMoney(Integer scanInviteMoney) {
    this.scanInviteMoney = scanInviteMoney;
  }

  public Integer getScanInviteNumber() {
    return scanInviteNumber;
  }

  public void setScanInviteNumber(Integer scanInviteNumber) {
    this.scanInviteNumber = scanInviteNumber;
  }
}
