package com.jifenke.lepluslive.weixin.service;

import com.jifenke.lepluslive.weixin.domain.entities.AutoReplyRule;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.domain.entities.WeixinReply;
import com.jifenke.lepluslive.weixin.domain.entities.WeixinReplyImageText;
import com.jifenke.lepluslive.weixin.domain.entities.WeixinReplyText;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import javax.inject.Inject;

/**
 * Created by zhangwen on 2016/5/27.
 */
@Service
@Transactional(readOnly = true)
public class WeixinReplyService {

  @Inject
  private AutoReplyService autoReplyService;

  @Inject
  private WeiXinService weiXinService;

  @Inject
  private WeiXinUserService weiXinUserService;

  @Inject
  private WeiXinUserInfoService weiXinUserInfoService;

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public String routeWeixinEvent(Map map) {

    String str = "";
    switch ((String) map.get("Event")) {
      case "subscribe":
//                关注公众号的事件，包括手动关注和二维码关注两种
        str = buildFocusMessageReply(map);
        //关注公众号后查询数据库有没有该用户信息，没有的话主动获取
        subscribeWeiXinUser(map);
        break;
      case "unsubscribe":
        break;
      case "MASSSENDJOBFINISH": //群发任务即将完成的时候，推送群发结果
        massEndJobFinish(map);
        break;
      case "SCAN":
        break;
      case "LOCATION":
        //上报地理位置
        str = buildLocationReply(map);
        break;
      case "CLICK":
//                用户点击菜单事件
        str = buildMenuMessageReply(map);
        break;
      case "VIEW":
//                点击菜单跳转链接时的事件推送
        break;
      default:
        break;
    }
    return str;
  }

  public String routeWeixinMessage(Map map) {

    String str = "";
    switch ((String) map.get("MsgType")) {
      case "text":
//              用户在微信中发送文本消息
        str = buildTextMessageReply(map);
        break;
      case "image":
//              用户在微信中发送图片消息
        break;
      case "voice":
//                用户在微信中发送语音消息
        break;
      case "video":
//                用户在微信中发送视频消息
        break;
      case "location":
//                用户在微信中发送地理位置消息
        break;
      case "link":
//                用户在微信中发送链接消息
        break;
      default:
//                用户在微信中发送其他未定义的消息
        break;
    }
    return str;
  }

  /**
   * 关注公众号时发送的信息
   */
  private String buildFocusMessageReply(Map map) {

    AutoReplyRule rule = autoReplyService.findByReplyType("focusReply");
    if (rule != null) {
      WeixinReply reply = null;
      if (null != rule.getReplyText() && (!"".equals(rule.getReplyText()))) {
        reply = new WeixinReplyText(map, rule);
      } else {
        reply = new WeixinReplyImageText(map, rule);
      }
      String str = reply.buildReplyXmlString();
      return str;
    }
    return "";
  }

  /**
   * 点击自定义菜单发送信息
   */
  private String buildMenuMessageReply(Map map) {
    AutoReplyRule rule = autoReplyService.findByKeyword((String) map.get("EventKey"));
    if (rule == null) {
      rule = autoReplyService.findByReplyType("defaultReply");
    }
    if (rule != null) {
      WeixinReply reply = null;
      if (null != rule.getReplyText() && (!"".equals(rule.getReplyText()))) {
        reply = new WeixinReplyText(map, rule);
      } else {
        reply = new WeixinReplyImageText(map, rule);
      }
      String str = reply.buildReplyXmlString();
      return str;
    }
    return "";
  }

  /**
   * 回复用户直接给公众号发送的信息
   */
  private String buildTextMessageReply(Map map) {
    AutoReplyRule rule = autoReplyService.findByKeyword((String) map.get("Content"));
    if (rule == null) {
      rule = autoReplyService.findByReplyType("defaultReply");
    }
    if (rule != null) {
      WeixinReply reply = null;
      if (null != rule.getReplyText() && (!"".equals(rule.getReplyText()))) {
        reply = new WeixinReplyText(map, rule);
      } else {
        reply = new WeixinReplyImageText(map, rule);
      }
      String str = reply.buildReplyXmlString();
      return str;
    }
    return "";
  }

  /**
   * 上报地理位置时发送的信息
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  private String buildLocationReply(Map map) {
    weiXinUserInfoService.saveWeiXinUserInfo(map);
    return "success";
  }

  /**
   * 关注公众号后查询数据库有没有该用户信息，没有的话主动获取
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  private void subscribeWeiXinUser(Map map) {
    String openId = map.get("FromUserName").toString();
    WeiXinUser weiXinUser = weiXinUserService.findWeiXinUserByOpenId(openId);
    if (weiXinUser == null || weiXinUser.getState() == 0) {
      Map<String, Object> userDetail = weiXinService.getWeiXinUserInfo(openId);
      if (null == userDetail.get("errcode")) {
        try {
          weiXinUserService.saveWeiXinUserBySubscribe(userDetail);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * 群发任务即将完成的时候，推送群发结果 将结果保存到数据库
   */
  private void massEndJobFinish(Map map) {

  }
}
