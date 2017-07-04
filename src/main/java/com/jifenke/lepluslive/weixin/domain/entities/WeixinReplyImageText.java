package com.jifenke.lepluslive.weixin.domain.entities;

import java.util.Map;

/**
 * Created by zhangwen on 2016/5/27.
 */
public class WeixinReplyImageText extends WeixinReply {

  private AutoReplyRule autoReplyRule;

  public WeixinReplyImageText(Map m) {
    super(m);
  }

  public WeixinReplyImageText(Map m, AutoReplyRule rule) {
    super(m);
    this.msgType = "news";
    this.autoReplyRule = rule;
  }

  @Override
  public String buildReplyXmlString(Map map) {
    StringBuffer buffer = new StringBuffer();
    buffer.append("<xml>");
    buffer.append("<ToUserName><![CDATA[" + this.getToUserName() + "]]></ToUserName>");
    buffer.append("<FromUserName><![CDATA[" + this.getFromUserName() + "]]></FromUserName>");
    buffer.append("<CreateTime>" + this.getCreateTime() + "</CreateTime>");
    buffer.append("<MsgType><![CDATA[" + this.getMsgType() + "]]></MsgType>");
    buffer.append("<ArticleCount>1</ArticleCount>");
    buffer.append("<Articles>");
    buffer.append("<item>");
    if (map == null) {
      buffer.append("<Title><![CDATA[" + this.getAutoReplyRule().getReplyImageText0().getTextTitle()
                    + "]]></Title>");
      buffer.append(
          "<Description><![CDATA[" + this.getAutoReplyRule().getReplyImageText0().getTextNote()
          + "]]></Description>");
      buffer.append("<PicUrl><![CDATA[" + this.getAutoReplyRule().getReplyImageText0().getImageUrl()
                    + "]]></PicUrl>");
      buffer.append("<Url><![CDATA[" + this.getAutoReplyRule().getReplyImageText0().getTextLink()
                    + "]]></Url>");
    } else {
      buffer.append("<Title><![CDATA[" + (map.get("title") == null ? this.getAutoReplyRule()
          .getReplyImageText0().getTextTitle() : map.get("title"))
                    + "]]></Title>");
      buffer.append(
          "<Description><![CDATA[" + (map.get("description") == null ? this.getAutoReplyRule()
              .getReplyImageText0().getTextNote() : map.get("description"))
          + "]]></Description>");
      buffer.append("<PicUrl><![CDATA[" + this.getAutoReplyRule().getReplyImageText0().getImageUrl()
                    + "]]></PicUrl>");
      buffer.append(
          "<Url><![CDATA[" + (map.get("url") == null ? this.getAutoReplyRule().getReplyImageText0()
              .getTextLink() : map.get("url"))
          + "]]></Url>");
    }

    buffer.append("</item>");
    buffer.append("</Articles>");
    buffer.append("</xml>");
    return buffer.toString();
  }

  public AutoReplyRule getAutoReplyRule() {
    return autoReplyRule;
  }

  public void setAutoReplyRule(AutoReplyRule autoReplyRule) {
    this.autoReplyRule = autoReplyRule;
  }
}
