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

  private Double latitude;   //纬度

  private Double longitude;   //经度

  private Date createDate = new Date();     //创建时间

  private String openId;      //等同于WeiXinUser的openId  便于查询

  @OneToOne(fetch = FetchType.LAZY)
  private WeiXinUser weiXinUser;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public String getOpenId() {
    return openId;
  }

  public void setOpenId(String openId) {
    this.openId = openId;
  }

  public WeiXinUser getWeiXinUser() {
    return weiXinUser;
  }

  public void setWeiXinUser(WeiXinUser weiXinUser) {
    this.weiXinUser = weiXinUser;
  }
}
