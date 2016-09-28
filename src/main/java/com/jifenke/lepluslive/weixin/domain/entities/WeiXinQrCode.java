package com.jifenke.lepluslive.weixin.domain.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 乐加生活永久二维码  Created by zhangwen on 2016/9/26.
 */
@Entity
@Table(name = "WEI_XIN_QR_CODE")
public class WeiXinQrCode {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  private Date createDate = new Date();     //创建时间

  private String parameter;  //二维码参数(1-32位)随机生成  最多2万个

  private String ticket;      //获取的二维码ticket，凭借此ticket可以在有效时间内换取二维码

  //1=商户永久二维码(merchantInfo表)|2=活动二维码(ActivityCodeBurse表)
  private Integer type;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public String getParameter() {
    return parameter;
  }

  public void setParameter(String parameter) {
    this.parameter = parameter;
  }

  public String getTicket() {
    return ticket;
  }

  public void setTicket(String ticket) {
    this.ticket = ticket;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }
}
