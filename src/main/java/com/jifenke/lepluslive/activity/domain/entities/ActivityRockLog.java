package com.jifenke.lepluslive.activity.domain.entities;

import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 摇一摇日志 Created by zhangwen on 2016/8/18.
 */
@Entity
@Table(name = "ACTIVITY_ROCK_LOG")
public class ActivityRockLog {

  @Id
  @GeneratedValue(generator = "system-uuid")
  @GenericGenerator(name = "system-uuid", strategy = "uuid")
  private String id;

  private Date createDate;   //创建时间

  private String rockTime;  //摇一摇时间yyyy-MM-dd

  private Integer scoreA = 0;   //本次获得的红包

  private Integer scoreB = 0;   //本次获得的积分

  private String token;         //用户token=leJiaUser中token

  @ManyToOne
  private LeJiaUserInfo leJiaUserInfo;

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

  public String getRockTime() {
    return rockTime;
  }

  public void setRockTime(String rockTime) {
    this.rockTime = rockTime;
  }

  public Integer getScoreA() {
    return scoreA;
  }

  public void setScoreA(Integer scoreA) {
    this.scoreA = scoreA;
  }

  public Integer getScoreB() {
    return scoreB;
  }

  public void setScoreB(Integer scoreB) {
    this.scoreB = scoreB;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public LeJiaUserInfo getLeJiaUserInfo() {
    return leJiaUserInfo;
  }

  public void setLeJiaUserInfo(LeJiaUserInfo leJiaUserInfo) {
    this.leJiaUserInfo = leJiaUserInfo;
  }
}
