package com.jifenke.lepluslive.weixin.domain.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 微信用户临时信息 经纬度、 Created by zhangwen on 2016/6/8.
 */
@Entity
@Table(name = "WEI_XIN_USER_INFO")
public class WeiXinUserInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Date createDate;     //创建时间

  @OneToOne(fetch = FetchType.LAZY)
  private WeiXinUser weiXinUser;

  private String version;    //引导页版本

  private Date lastOpenDate;     //最近一次打开时间|判断是否出现手机号弹窗|>7天出现

  private Integer yinDao = 1;    //该版本是否引导过  1=引导过|0=未引导过

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public Date getLastOpenDate() {
    return lastOpenDate;
  }

  public void setLastOpenDate(Date lastOpenDate) {
    this.lastOpenDate = lastOpenDate;
  }

  public Integer getYinDao() {
    return yinDao;
  }

  public void setYinDao(Integer yinDao) {
    this.yinDao = yinDao;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public WeiXinUser getWeiXinUser() {
    return weiXinUser;
  }

  public void setWeiXinUser(WeiXinUser weiXinUser) {
    this.weiXinUser = weiXinUser;
  }
}
