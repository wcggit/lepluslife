package com.jifenke.lepluslive.lejiauser.domain.entities;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by zhangwen on 2016/4/25.
 */
@Entity
@Table(name = "VALIDATE_CODE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ValidateCode {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String phoneNumber;

  private String code;

  private Integer status = 0;  //0表示有效，1表示失效

  private String ipAddr;

  private Date createDate = new Date();

  private String sendCode;  //发送状态（成功时为success,否则为运营商错误码）

  private String subMsg;  //错误时有值，中文错误原因

  private String subCode;  //错误时有值，阿里错误码

  public String getIpAddr() {
    return ipAddr;
  }

  public void setIpAddr(String ipAddr) {
    this.ipAddr = ipAddr;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getSendCode() {
    return sendCode;
  }

  public void setSendCode(String sendCode) {
    this.sendCode = sendCode;
  }

  public String getSubMsg() {
    return subMsg;
  }

  public void setSubMsg(String subMsg) {
    this.subMsg = subMsg;
  }

  public String getSubCode() {
    return subCode;
  }

  public void setSubCode(String subCode) {
    this.subCode = subCode;
  }
}
