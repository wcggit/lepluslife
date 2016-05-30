package com.jifenke.lepluslive.weixin.domain.entities;

import java.util.Date;
import java.util.Map;

/**
 * Created by zhangwen on 2016/5/27.
 */
public abstract class WeixinReply {

  public String toUserName;
  public String fromUserName;
  public Long createTime;
  public String msgType;

  public WeixinReply(Map m) {
    this.toUserName = m.get("FromUserName").toString();
    this.fromUserName = m.get("ToUserName").toString();
    this.createTime = new Date().getTime();
    this.msgType = m.get("MsgType").toString();
  }

  public abstract String buildReplyXmlString();

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

  public Long getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Long createTime) {
    this.createTime = createTime;
  }

  public String getMsgType() {
    return msgType;
  }

  public void setMsgType(String msgType) {
    this.msgType = msgType;
  }
}
