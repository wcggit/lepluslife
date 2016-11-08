package com.jifenke.lepluslive.weixin.service;

import com.jifenke.lepluslive.activity.domain.entities.ActivityCodeBurse;
import com.jifenke.lepluslive.activity.domain.entities.ActivityJoinLog;
import com.jifenke.lepluslive.activity.service.ActivityCodeBurseService;
import com.jifenke.lepluslive.activity.service.ActivityJoinLogService;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.weixin.domain.entities.AutoReplyRule;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.domain.entities.WeixinMessage;
import com.jifenke.lepluslive.weixin.domain.entities.WeixinReply;
import com.jifenke.lepluslive.weixin.domain.entities.WeixinReplyImageText;
import com.jifenke.lepluslive.weixin.domain.entities.WeixinReplyText;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
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
  private LeJiaUserService leJiaUserService;

  @Inject
  private WeixinMessageService weixinMessageService;

  @Inject
  private ActivityCodeBurseService activityCodeBurseService;

  @Inject
  private ActivityJoinLogService activityJoinLogService;

  @Inject
  private MerchantService merchantService;

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public String routeWeixinEvent(Map map) {

    String str = "";
    switch ((String) map.get("Event")) {
      case "subscribe":
//                关注公众号的事件，包括手动关注和二维码关注两种
        str = buildFocusMessageReply(map, 1);
        break;
      case "unsubscribe"://取消关注公众号的事件
        unSubscribeWeiXinUser(map);
        break;
      case "MASSSENDJOBFINISH": //群发任务即将完成的时候，推送群发结果
        massEndJobFinish(map);
        break;
      case "SCAN":  //用户已关注后扫描带参数二维码
//                关注公众号的事件，包括手动关注和二维码关注两种
        str = buildFocusMessageReply(map, 2); //关注公众号后查询数据库有没有该用户信息，没有的话主动获取
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
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  private String buildFocusMessageReply(Map map, Integer eventType) {
    WeiXinUser
        user =
        weiXinUserService.findWeiXinUserByOpenId(map.get("FromUserName").toString());
    AutoReplyRule rule = autoReplyService.findByReplyType("focusReply");
    String str = "";
    ActivityCodeBurse codeBurse = null;
    int subType = 1;  //二维码类型  1=普通|2=活动|3=充值
    String subSource = "0_0_0";
    if (rule != null) {
      WeixinReply reply = null;
      if (null != rule.getReplyText() && (!"".equals(rule.getReplyText()))) {
        reply = new WeixinReplyText(map, rule);
        str = reply.buildReplyXmlString(null);
      } else {
        reply = new WeixinReplyImageText(map, rule);
        HashMap<String, String> buildMap = new HashMap<>();
        //判断是不是永久二维码
        if (map.get("Ticket") != null && (!"".equals(map.get("Ticket").toString()))) {//是永久二维码
          //判断是活动二维码还是商家二维码还是充值二维码
          String parameter;
          if (eventType == 1) {
            parameter = map.get("EventKey").toString().split("_")[1];
          } else {
            parameter = map.get("EventKey").toString();
          }
          if (parameter.startsWith("M")) { //商户二维码
            Object[]
                o =
                merchantService
                    .findMerchantIdByParameter(parameter);//o[0]=merchantId|o[1]=partnership
            if (o != null) { //绑定注册来源,判断绑定流程
              Map result = subscribeByMerchantQrCode(map, user, Long.valueOf(o[0].toString()));
              str = reply.buildReplyXmlString(result);
              return str;
            }
          } else if (parameter.startsWith("Y")) { //活动二维码
            subType = 2;
            codeBurse = activityCodeBurseService.findCodeBurseByTicket(
                map.get("Ticket").toString());
            if (codeBurse != null) {
              //活动优先级最高(已结束||暂停||派发完毕)
              if (codeBurse.getEndDate().getTime() < new Date().getTime()
                  || codeBurse.getState() == 0
                  || codeBurse.getBudget() < codeBurse.getTotalMoney()) {
                buildMap.put("title", "活动已结束，期待下一次吧！");
                buildMap.put("description", "↑↑↑红包被人抢光了");
              } else {
                //判断是否已参与过活动（走到这儿肯定是未关注的用户）
                if (user == null || user.getSubState() == 0) {//1.数据库没有数据，肯定没关注
                  buildMap.put("title", "点击领取红包，鞍山56店通用，花多少都能用");
                  buildMap.put("description", "↑↑↑戳这里，累计5000人领取");
                } else {//2.判断是否参与过该种活动
                  ActivityJoinLog
                      joinLog =
                      activityJoinLogService
                          .findLogBySubActivityAndOpenId(codeBurse.getType(), user);
                  if (joinLog == null) {//未参与
                    buildMap.put("title", "点击领取红包，鞍山56店通用，花多少都能用");
                    buildMap.put("description", "↑↑↑戳这里，累计5000人领取");
                  } else {
                    buildMap.put("title", "您已经领取过红包了");
                    buildMap.put("description", "↑↑↑点击查看怎么花红包");
                  }
                }
              }
              buildMap.put("url",
                           "http://www.lepluslife.com/weixin/activity/" + codeBurse.getType()
                           + "_" + codeBurse.getId());
              str = reply.buildReplyXmlString(buildMap);
            } else {
              str = reply.buildReplyXmlString(null);
            }
          } else if (parameter.startsWith("P")) { //充值二维码
            subType = 3;
            subSource = "5_0_0";
            buildMap.put("title", "感谢您的关注，恭喜您获得乐＋红包一个");
            buildMap.put("description", "↑↑↑戳这里，累计5000人领取");
            buildMap.put("url",
                         "http://www.lepluslife.com/weixin/subPage");
            str = reply.buildReplyXmlString(buildMap);
          }
        } else {
          buildMap.put("title", "感谢您的关注，恭喜您获得乐＋红包一个");
          buildMap.put("description", "↑↑↑戳这里，累计5000人领取");
          buildMap.put("url",
                       "http://www.lepluslife.com/weixin/subPage");
          str = reply.buildReplyXmlString(buildMap);
        }
      }
    }
    //关注公众号后查询数据库有没有该用户信息，没有的话主动获取
    subscribeWeiXinUser(map, codeBurse, subType, subSource);
    return str;
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
      String str = reply.buildReplyXmlString(null);
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
      String str = reply.buildReplyXmlString(null);
      return str;
    }
    return "";
  }

  /**
   * 上报地理位置时发送的信息
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  private String buildLocationReply(Map map) {
    // weiXinUserInfoService.saveWeiXinUserInfo(map);
    return "success";
  }

  /**
   * 关注公众号后查询数据库有没有该用户信息，没有的话主动获取
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  private void subscribeWeiXinUser(Map map, ActivityCodeBurse codeBurse, int subType,
                                   String subSource) {
    String openId = map.get("FromUserName").toString();
    Map<String, Object> userDetail = weiXinService.getWeiXinUserInfo(openId);
    //拼接关注来源并添加该活动的关注人数
    if (subType == 1 || subType == 3) {
      userDetail.put("subSource", subSource);
    } else if (subType == 2) {
      if (codeBurse != null) {
        if (codeBurse.getType() == 1) {
          userDetail.put("subSource", codeBurse.getType() + "_" + codeBurse.getId() + "_0");
        } else if (codeBurse.getType() == 2) { //裂变临时二维码 为weiXinUserId
          String key = map.get("EventKey").toString();
          String[] keys = key.split("_");
          if (keys.length > 1) {
            userDetail
                .put("subSource", codeBurse.getType() + "_" + codeBurse.getId() + "_" + keys[1]);
          } else {
            userDetail
                .put("subSource", codeBurse.getType() + "_" + codeBurse.getId() + "_" + key);
          }

        } else {
          userDetail.put("subSource", codeBurse.getType() + "_" + codeBurse.getId() + "_1");
        }
        //添加该活动的关注人数
        codeBurse.setScanInviteNumber(codeBurse.getScanInviteNumber() + 1);
        activityCodeBurseService.saveActivityCodeBurse(codeBurse);
      }
    }
    if (null == userDetail.get("errcode")) {
      try {
        weiXinUserService.saveWeiXinUserBySubscribe(userDetail);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

  }

  /**
   * 商户二维码关注处理流程 16/09/27
   *
   * @param map        关注时微信发送的数据
   * @param weiXinUser 从数据库查找的用户信息(没有时为null)
   * @param merchantId 永久二维码对应的商户id
   * @return 图文消息的内容
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  private Map subscribeByMerchantQrCode(Map map, WeiXinUser weiXinUser, Long merchantId) {
    String openId = map.get("FromUserName").toString();
    Map<String, Object> userDetail = weiXinService.getWeiXinUserInfo(openId);
    if (null == userDetail.get("errcode")) {
      try {
        userDetail.put("subSource", "4_0_" + merchantId);
        weiXinUserService.saveWeiXinUserBySubscribe(userDetail);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    if (weiXinUser == null) { //数据库中没有该用户
      //拼接关注来源,将用户信息及关注来源保存到数据库
      map.put("title", "[转账]邀请您成为乐加会员，点击领取新手大礼包!");
      map.put("description", "↑↑↑戳这里");
      map.put("url",
              "http://www.lepluslife.com/weixin/subPage");
    } else {
      if (weiXinUser.getState() == 0) {//非会员
        map.put("title", "[转账]邀请您成为乐加会员，点击领取新手大礼包!");
        map.put("description", "↑↑↑戳这里");
        map.put("url",
                "http://www.lepluslife.com/weixin/subPage");
      } else {
        if (weiXinUser.getLeJiaUser().getBindMerchant() == null) {//未绑上商户|绑定商户但不修改关注来源
          leJiaUserService.memberSubBindMerchant(weiXinUser, merchantId);
        }
        map.put("title", "您已领取过新手礼包，不能再领了哦~");
        map.put("description", "点击查看如何使用积分和红包");
        map.put("url",
                "http://www.lepluslife.com/resource/active2.html");
      }
    }

    return map;
  }

  /**
   * 取消关注公众号后查询数据库有没有该用户信息，有的话取消关注
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  private void unSubscribeWeiXinUser(Map map) {
    String openId = map.get("FromUserName").toString();
    WeiXinUser weiXinUser = weiXinUserService.findWeiXinUserByOpenId(openId);
    if (weiXinUser != null) {
      weiXinUser.setSubState(2);
      try {
        weiXinUserService.saveWeiXinUser(weiXinUser);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * 群发任务即将完成的时候，推送群发结果 将结果保存到数据库
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  private void massEndJobFinish(Map map) {
    String msgID = map.get("MsgID").toString();
    WeixinMessage weixinMessage = weixinMessageService.findMessageByMsgID(msgID);
    if (weixinMessage != null) {
      try {
        weixinMessage.setToUserName(String.valueOf(map.get("ToUserName")));
        weixinMessage.setFromUserName(String.valueOf(map.get("FromUserName")));
        weixinMessage.setCreateTime(String.valueOf(map.get("CreateTime")));
        weixinMessage.setMsgType(String.valueOf(map.get("MsgType")));
        weixinMessage.setEvent(String.valueOf(map.get("Event")));
        weixinMessage.setStatus(String.valueOf(map.get("Status")));
        weixinMessage.setTotalCount(Integer.valueOf(String.valueOf(map.get("TotalCount"))));
        weixinMessage.setFilterCount(Integer.valueOf(String.valueOf(map.get("FilterCount"))));
        weixinMessage.setSentCount(Integer.valueOf(String.valueOf(map.get("SentCount"))));
        weixinMessage.setErrorCount(Integer.valueOf(String.valueOf(map.get("ErrorCount"))));
        weixinMessageService.saveNews(weixinMessage);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
