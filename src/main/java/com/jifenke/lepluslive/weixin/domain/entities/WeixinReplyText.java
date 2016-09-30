package com.jifenke.lepluslive.weixin.domain.entities;

import java.util.Map;

/**
 * Created by zhangwen on 2016/5/27.
 */
public class WeixinReplyText extends WeixinReply {

  private String content;

  public WeixinReplyText(Map m) {
    super(m);
    this.content = m.get("Content").toString();
  }

  public WeixinReplyText(Map m, AutoReplyRule rule) {
    super(m);
    this.content = rule.getReplyText();
    this.msgType = "text";
  }

  @Override
  public String buildReplyXmlString(Map map) {
    StringBuffer buffer = new StringBuffer();
    buffer.append("<xml>");
    buffer.append("<ToUserName><![CDATA[" + this.getToUserName() + "]]></ToUserName>");
    buffer.append("<FromUserName><![CDATA[" + this.getFromUserName() + "]]></FromUserName>");
    buffer.append("<CreateTime>" + this.getCreateTime() + "</CreateTime>");
    buffer.append("<MsgType><![CDATA[" + this.getMsgType() + "]]></MsgType>");
    buffer.append("<Content><![CDATA[" + this.getContent() + "]]></Content>");
    buffer.append("</xml>");
    return buffer.toString();
  }

  String getContent() {
    return content;
  }

  void setContent(String content) {
    this.content = content;
  }
}
