package com.jifenke.lepluslive.activity.domain.entities;

import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 用户运动 Created by zhangwen on 2016/8/18.
 */
@Entity
@Table(name = "LE_JIA_USER_INFO")
public class LeJiaUserInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Date createDate = new Date();   //创建时间

  private Integer sportA = 0;   //运动累计获得红包 单位=分

  private Integer sportB = 0;   //运动累计获得积分

  private Long totalSport = 0L;   //运动累计步数

  private Integer rockA = 0;     //摇一摇累计获得红包

  private Integer rockB = 0;      //摇一摇累计获得积分

  private String rockDate;        //最近一个摇一摇时间yyyy-MM-dd

  private Integer rockTime = 0;       //最近一个摇一摇时间的已摇次数

  private Integer inviteA = 0;        //邀请累计获得红包

  private Integer inviteB = 0;        //邀请累计获得积分

  private Integer totalInvite = 0;      //累计邀请人数

  private String token;         //用户token=leJiaUser中token

  @OneToOne
  private LeJiaUser leJiaUser;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getRockA() {
    return rockA;
  }

  public void setRockA(Integer rockA) {
    this.rockA = rockA;
  }

  public Integer getRockB() {
    return rockB;
  }

  public void setRockB(Integer rockB) {
    this.rockB = rockB;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Integer getSportA() {
    return sportA;
  }

  public void setSportA(Integer sportA) {
    this.sportA = sportA;
  }

  public Integer getSportB() {
    return sportB;
  }

  public void setSportB(Integer sportB) {
    this.sportB = sportB;
  }

  public Long getTotalSport() {
    return totalSport;
  }

  public void setTotalSport(Long totalSport) {
    this.totalSport = totalSport;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public LeJiaUser getLeJiaUser() {
    return leJiaUser;
  }

  public String getRockDate() {
    return rockDate;
  }

  public void setRockDate(String rockDate) {
    this.rockDate = rockDate;
  }

  public Integer getRockTime() {
    return rockTime;
  }

  public Integer getInviteA() {
    return inviteA;
  }

  public void setInviteA(Integer inviteA) {
    this.inviteA = inviteA;
  }

  public Integer getInviteB() {
    return inviteB;
  }

  public void setInviteB(Integer inviteB) {
    this.inviteB = inviteB;
  }

  public Integer getTotalInvite() {
    return totalInvite;
  }

  public void setTotalInvite(Integer totalInvite) {
    this.totalInvite = totalInvite;
  }

  public void setRockTime(Integer rockTime) {
    this.rockTime = rockTime;
  }

  public void setLeJiaUser(LeJiaUser leJiaUser) {
    this.leJiaUser = leJiaUser;
  }
}
