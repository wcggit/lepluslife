package com.jifenke.lepluslive.weixin.domain.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 主动发送的内容 微信发送过来的消息 Created by zhangwen on 2016/5/25.
 */
@Entity
@Table(name = "WEI_XIN_MESSAGE")
public class WeixinMessage {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  //主动记录的信息
  private String type;   //媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb），次数为news，即图文消息
  private Date createDate = new Date();  //创建时间

  //被动接受的信息
  private String toUserName;
  private String fromUserName;
  private String createTime;
  private String msgType;
  private String event;

  //以下是调用群发时成功的数据
  private String msgID;               //群发的消息ID
  private String msgDataId;       //消息的数据ID,该字段只有在群发图文消息时，才会出现。可以用于在图文分析数据接口中

  //以下是调用群发接口成功后推送的数据
  private String status;              //群发的结构，为“send success”或“send fail”或“err(num)”
  private Integer totalCount = 0;          //tag_id下粉丝数；或者openid_list中的粉丝数
  //过滤（过滤是指用户设置拒收的过滤，用户接收已超4条等的过滤）后，准备发送的粉丝数，原则上，FilterCount = SentCount + ErrorCount
  private Integer filterCount = 0;
  private Integer sentCount = 0;           //发送成功的粉丝数
  private Integer errorCount = 0;          //发送失败的粉丝数

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getToUserName() {
    return toUserName;
  }

  public void setToUserName(String toUserName) {
    this.toUserName = toUserName;
  }

  public String getFromUserName() {
    return fromUserName;
  }

  public void setFromUserName(String fromUserName) {
    this.fromUserName = fromUserName;
  }

  public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }

  public String getMsgType() {
    return msgType;
  }

  public void setMsgType(String msgType) {
    this.msgType = msgType;
  }

  public String getEvent() {
    return event;
  }

  public void setEvent(String event) {
    this.event = event;
  }

  public String getMsgID() {
    return msgID;
  }

  public void setMsgID(String msgID) {
    this.msgID = msgID;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Integer getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(Integer totalCount) {
    this.totalCount = totalCount;
  }

  public Integer getFilterCount() {
    return filterCount;
  }

  public void setFilterCount(Integer filterCount) {
    this.filterCount = filterCount;
  }

  public Integer getSentCount() {
    return sentCount;
  }

  public void setSentCount(Integer sentCount) {
    this.sentCount = sentCount;
  }

  public Integer getErrorCount() {
    return errorCount;
  }

  public void setErrorCount(Integer errorCount) {
    this.errorCount = errorCount;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public String getMsgDataId() {
    return msgDataId;
  }

  public void setMsgDataId(String msgDataId) {
    this.msgDataId = msgDataId;
  }
}
