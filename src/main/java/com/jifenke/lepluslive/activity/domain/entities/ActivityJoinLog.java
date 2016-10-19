package com.jifenke.lepluslive.activity.domain.entities;

import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;

import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 参加活动记录 Created by zhangwen on 2016/8/5.
 */
@Entity
@Table(name = "ACTIVITY_JOIN_LOG")
public class ActivityJoinLog {

  @Id
  @GeneratedValue(generator = "system-uuid")
  @GenericGenerator(name = "system-uuid", strategy = "uuid")
  private String id;

  private Date createDate = new Date();   //创建时间

  private String openId;  //直接参与人的openId

  @ManyToOne(fetch = FetchType.EAGER)
  private WeiXinUser user;  //参与人

  @ManyToOne
  private WeiXinUser helpUser;  //助力人

  private String openIdHelp;   //助力人或扫码人的openId

  private Integer type;  // //活动类型 0=关注填充手机号 | 1=永久二维码送红包活动  2=临时二维码关注裂变红包活动  3=APP邀请好友  4=临时活动页面

  private Long activityId;   //活动id或临时活动页面版本ID

  private String detail;    //活动说明(type=3:50_100 分享人获得红包_被分享人获得红包)(type=4:50_100 红包_积分)

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public String getOpenIdHelp() {
    return openIdHelp;
  }

  public void setOpenIdHelp(String openIdHelp) {
    this.openIdHelp = openIdHelp;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public Long getActivityId() {
    return activityId;
  }

  public void setActivityId(Long activityId) {
    this.activityId = activityId;
  }

  public String getOpenId() {
    return openId;
  }

  public void setOpenId(String openId) {
    this.openId = openId;
  }

  public WeiXinUser getUser() {
    return user;
  }

  public void setUser(WeiXinUser user) {
    this.user = user;
  }

  public WeiXinUser getHelpUser() {
    return helpUser;
  }

  public void setHelpUser(WeiXinUser helpUser) {
    this.helpUser = helpUser;
  }

  public String getDetail() {
    return detail;
  }

  public void setDetail(String detail) {
    this.detail = detail;
  }
}
