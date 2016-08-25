package com.jifenke.lepluslive.activity.domain.entities;

import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 运动日志 Created by zhangwen on 2016/8/18.
 */
@Entity
@Table(name = "ACTIVITY_SPORT_LOG")
public class ActivitySportLog {

  @Id
  @GeneratedValue(generator = "system-uuid")
  @GenericGenerator(name = "system-uuid", strategy = "uuid")
  private String id;

  private Date createDate;   //创建时间

  private String sportTime;  //运动时间yyyy-MM-dd

  private Integer distance = 0;   //运动步数

  private Integer getScore = 0;   //今日是否获得红包积分


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

  public String getSportTime() {
    return sportTime;
  }

  public void setSportTime(String sportTime) {
    this.sportTime = sportTime;
  }

  public Integer getDistance() {
    return distance;
  }

  public void setDistance(Integer distance) {
    this.distance = distance;
  }

  public LeJiaUserInfo getLeJiaUserInfo() {
    return leJiaUserInfo;
  }

  public void setLeJiaUserInfo(LeJiaUserInfo leJiaUserInfo) {
    this.leJiaUserInfo = leJiaUserInfo;
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

  public Integer getGetScore() {
    return getScore;
  }

  public void setGetScore(Integer getScore) {
    this.getScore = getScore;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
