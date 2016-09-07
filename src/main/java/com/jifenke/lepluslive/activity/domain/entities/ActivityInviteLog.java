package com.jifenke.lepluslive.activity.domain.entities;

import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;

import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 邀请日志 Created by zhangwen on 2016/8/18.
 */
@Entity
@Table(name = "ACTIVITY_INVITE_LOG")
public class ActivityInviteLog {

  @Id
  @GeneratedValue(generator = "system-uuid")
  @GenericGenerator(name = "system-uuid", strategy = "uuid")
  private String id;

  private Date createDate;   //创建时间

  @ManyToOne
  private LeJiaUser leJiaUser;    //邀请人
  private String token;           //邀请人token
  private Integer scoreA = 0;     //邀请人获得的红包
  private Integer scoreB = 0;     //邀请人获得的积分

  @ManyToOne
  private LeJiaUser beLeJiaUser;  //被邀请人
  private Integer beScoreA = 0;   //被邀请人获得的红包
  private Integer beScoreB = 0;   //被邀请人获得的积分

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

  public LeJiaUser getLeJiaUser() {
    return leJiaUser;
  }

  public void setLeJiaUser(LeJiaUser leJiaUser) {
    this.leJiaUser = leJiaUser;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Integer getScoreA() {
    return scoreA;
  }

  public void setScoreA(Integer scoreA) {
    this.scoreA = scoreA;
  }

  public LeJiaUser getBeLeJiaUser() {
    return beLeJiaUser;
  }

  public void setBeLeJiaUser(LeJiaUser beLeJiaUser) {
    this.beLeJiaUser = beLeJiaUser;
  }

  public Integer getScoreB() {
    return scoreB;
  }

  public void setScoreB(Integer scoreB) {
    this.scoreB = scoreB;
  }

  public Integer getBeScoreB() {
    return beScoreB;
  }

  public void setBeScoreB(Integer beScoreB) {
    this.beScoreB = beScoreB;
  }

  public Integer getBeScoreA() {
    return beScoreA;
  }

  public void setBeScoreA(Integer beScoreA) {
    this.beScoreA = beScoreA;
  }
}
