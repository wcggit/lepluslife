package com.jifenke.lepluslive.activity.controller;

import com.jifenke.lepluslive.activity.domain.entities.ActivityCodeBurse;
import com.jifenke.lepluslive.activity.domain.entities.ActivityJoinLog;
import com.jifenke.lepluslive.activity.domain.entities.RechargeCard;
import com.jifenke.lepluslive.activity.service.ActivityCodeBurseService;
import com.jifenke.lepluslive.activity.service.ActivityJoinLogService;
import com.jifenke.lepluslive.activity.service.ActivityShareLogService;
import com.jifenke.lepluslive.activity.service.RechargeCardService;
import com.jifenke.lepluslive.global.service.MessageService;
import com.jifenke.lepluslive.global.util.CookieUtils;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.partner.service.PartnerService;
import com.jifenke.lepluslive.score.service.ScoreAService;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.service.DictionaryService;
import com.jifenke.lepluslive.weixin.service.WeiXinService;
import com.jifenke.lepluslive.weixin.service.WeiXinUserService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 活动 Created by zhangwen on 16/9/2.
 */
@RestController
@RequestMapping("/weixin")
public class ActivityCodeBurseController {

  @Value("${weixin.appId}")
  private String appId;
  @Inject
  private ActivityCodeBurseService activityCodeBurseService;
  @Inject
  private WeiXinUserService weiXinUserService;
  @Inject
  private ActivityJoinLogService activityJoinLogService;

  @Inject
  private ScoreAService scoreAService;

  @Inject
  private DictionaryService dictionaryService;

  @Inject
  private WeiXinService weiXinService;

  @Inject
  private LeJiaUserService leJiaUserService;

  @Inject
  private PartnerService partnerService;

  @Inject
  private ActivityShareLogService activityShareLogService;

  @Inject
  private MessageService messageService;
  @Inject
  private RechargeCardService rechargeCardService;

  //分享页面 06/09/02
  @RequestMapping(value = "/share/{id}", method = RequestMethod.GET)
  public ModelAndView sharePage(@PathVariable String id, HttpServletRequest request,
                                HttpServletResponse response,
                                Model model) {
    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
    model.addAttribute("token", id); //记录邀请人的token
    //判断是否有手机号码
    LeJiaUser leJiaUser = weiXinUser.getLeJiaUser();
    int flag = 0;
    if (leJiaUser != null) {
      if (leJiaUser.getPhoneNumber() == null || leJiaUser.getPhoneNumber().equals("")) {
        //判断是否被邀请过，防止手机号互相挤掉刷红包
        flag = activityShareLogService.findLogByLeJiaUser(leJiaUser.getId());
      } else {
        flag = 1;
      }
      if (flag == 1) {
        //已被邀请过或有手机号
        try {
          response.sendRedirect("/resource/frontRes/activity/share2/register.html");
          return null;
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      //判断是否是自己打开的自己的分享页面
      if (leJiaUser.getUserSid().equals(id)) {
        model.addAttribute("self", 1);
      }
    }
    return MvUtil.go("/activity/share2");
  }

  /**
   * 分享页面提交  16/09/07
   *
   * @param phoneNumber 被邀请的手机号码
   * @param token       邀请人的token
   * @param request     请求
   * @return 状态
   */
  @RequestMapping(value = "/share/submit", method = RequestMethod.GET)
  public LejiaResult shareSubmit(@RequestParam String phoneNumber, @RequestParam String token,
                                 HttpServletRequest request) {
    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);

    //给双方派发红包和积分,填充手机号码成为会员，修改邀请人邀请记录(info)并记录shareLog
    try {
      activityShareLogService.giveScoreByShare(weiXinUser, token, phoneNumber);
      return LejiaResult.ok();
    } catch (Exception e) {
      return LejiaResult.build(202, "服务器异常");
    }
  }

  //关注图文链接页面
  @RequestMapping("/subPage")
  public ModelAndView subPage(HttpServletRequest request, Model model) {
    String openId = CookieUtils.getCookieValue(request, appId + "-user-open-id");
    WeiXinUser weiXinUser = weiXinUserService.findWeiXinUserByOpenId(openId);
    model.addAttribute("wxConfig", weiXinService.getWeiXinConfig(request));
    //判断是否获得过红包
    ActivityJoinLog joinLog = activityJoinLogService.findLogBySubActivityAndOpenId(0, weiXinUser);
    if (joinLog == null) {//未参与
      if (weiXinUser.getLeJiaUser().getPhoneNumber() != null && !""
          .equals(weiXinUser.getLeJiaUser().getPhoneNumber())) {
        model.addAttribute("status", 1);
        model.addAttribute("scoreA", 200);
      } else {
        model.addAttribute("status", 0);
      }
    } else {
      model.addAttribute("scoreA", joinLog.getDetail());
      model.addAttribute("status", 1);
    }
    return MvUtil.go("/activity/subPage");
  }

  /**
   * 关注图文链接页面 输入手机号,点击领取红包 16/09/20
   *
   * @param phoneNumber 手机号
   */
  @RequestMapping(value = "/subPage/open")
  public LejiaResult subPageOpen(@RequestParam String phoneNumber, HttpServletRequest request) {
    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
    LeJiaUser leJiaUser = leJiaUserService.findUserByPhoneNumber(phoneNumber);  //是否已注册
    ActivityJoinLog joinLog = activityJoinLogService.findLogBySubActivityAndOpenId(0, weiXinUser);
    if (leJiaUser != null && !weiXinUser.getLeJiaUser().getId().equals(leJiaUser.getId())) {
      leJiaUser.setPhoneNumber(null);
      leJiaUserService.saveUser(leJiaUser);
    }
    if (joinLog == null) {
      //判断是否需要绑定商户 4_0_123
      Merchant merchant = leJiaUserService.checkUserBindMerchant(weiXinUser);

      //派发红包和积分,填充手机号码成为会员
      try {
        Map<String, Integer> map = null;
        if (merchant != null && merchant.getPartnership() == 2) { //虚拟商户由合伙人发放红包金额
          map = partnerService.lockGiveScoreToUser(weiXinUser, phoneNumber, merchant);
          if (map.get("return").equals(0)) { //合伙人红包不足,由乐加生活发放红包
            map = weiXinUserService.giveScoreAByDefault(weiXinUser, phoneNumber);
          }
        } else {
          map = weiXinUserService.giveScoreAByDefault(weiXinUser, phoneNumber);
        }
        //添加参加记录
        activityJoinLogService.addCodeBurseLogByDefault(weiXinUser, map.get("scoreA"));
        return LejiaResult.ok(map);
      } catch (Exception e) {
        e.printStackTrace();
        return LejiaResult.build(500, "服务器异常");
      }
    }
    return LejiaResult.build(6005, messageService.getMsg("6005"));
  }

  //关注永久二维码领取红包
  @RequestMapping(value = "/activity/{id}", method = RequestMethod.GET)
  public ModelAndView goActivityPage(HttpServletRequest request, @PathVariable String id,
                                     Model model) {
    String openId = CookieUtils.getCookieValue(request, appId + "-user-open-id");
    WeiXinUser weiXinUser = weiXinUserService.findWeiXinUserByOpenId(openId);
    String[] str = id.split("_");
    if ("0".equals(str[0])) { //普通关注
      //判断是否获得过红包
      ActivityJoinLog joinLog = activityJoinLogService.findLogBySubActivityAndOpenId(0, weiXinUser);
      int defaultScoreA = Integer.valueOf(dictionaryService.findDictionaryById(18L).getValue());
      if (joinLog == null) {//未参与
        //派发红包,获取默认派发红包金额
        int
            status =
            scoreAService
                .giveScoreAByDefault(weiXinUser.getLeJiaUser(), defaultScoreA, "关注送红包", 0,
                                     "0_" + defaultScoreA);
        //添加参加记录
        if (status == 1) {
          activityJoinLogService.addCodeBurseLogByDefault(weiXinUser, defaultScoreA);
        }
      }
      model.addAttribute("singleMoney", defaultScoreA);
      model.addAttribute("status", 200);
    } else {
      //判断活动是否失效
      ActivityCodeBurse
          codeBurse =
          activityCodeBurseService.findCodeBurseById(Long.valueOf(str[1]));
      if (codeBurse != null) {
        //活动优先级最高(已结束||暂停||派发完毕)
        if (codeBurse.getEndDate().getTime() < new Date().getTime() || codeBurse.getState() == 0
            || codeBurse.getBudget().intValue() < codeBurse.getTotalMoney().intValue()) {
          model.addAttribute("status", 201);
          model.addAttribute("singleMoney", codeBurse.getSingleMoney());
        } else {
          //判断是否参与过该种活动
          ActivityJoinLog
              joinLog =
              activityJoinLogService
                  .findLogBySubActivityAndOpenId(codeBurse.getType(), weiXinUser);
          if (joinLog == null) {//未参与
            int status = scoreAService.giveScoreAByActivity(codeBurse, weiXinUser.getLeJiaUser());
            //添加参加记录
            if (status == 1) {
              activityJoinLogService.addCodeBurseLog(codeBurse, weiXinUser);
            }
            model.addAttribute("singleMoney", codeBurse.getSingleMoney());
          } else {
            model.addAttribute("singleMoney", joinLog.getDetail());
          }
          model.addAttribute("status", 200);
        }
      } else {
        model.addAttribute("status", 404);
      }
    }
    // model.addAttribute("wxConfig", weiXinService.getWeiXinConfig(request));
    return MvUtil.go("/activity/codeBurse");
  }

  /**
   * 跳转到当前临时活动页面  16/10/18
   *
   * @param id 活动版本
   */
  @RequestMapping(value = "/activity/short/{id}", method = RequestMethod.GET)
  public void shortPage(HttpServletResponse response, @PathVariable String id) {
    try {
      response.sendRedirect("/resource/frontRes/activity/short/version" + id + "/index.html");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 临时活动页面加载访问  16/10/18
   *
   * @param version 活动对应的版本
   */
  @RequestMapping(value = "/activity", method = RequestMethod.POST)
  public LejiaResult activityPage(@RequestParam Long version, HttpServletRequest request) {
    WeiXinUser user = weiXinService.getCurrentWeiXinUser(request);
    ActivityJoinLog joinLog = activityJoinLogService.findLogByTypeAndUser(4, version, user);
    Map<Object, Object> result = new HashMap<>();
    result.put("user", user);
    if (joinLog != null) {
      result.put("status", 1);
    } else {
      result.put("status", 0);
    }
    return LejiaResult.ok(result);
  }

  /**
   * 临时活动页面加载访问  16/10/18
   *
   * @param version 活动对应的版本
   * @param scoreA  发放的红包
   * @param scoreB  发放的积分
   */
  @RequestMapping(value = "/short/submit", method = RequestMethod.POST)
  public LejiaResult activitySubmit(@RequestParam Long version, @RequestParam Integer scoreA,
                                    @RequestParam Integer scoreB, @RequestParam String aInfo,
                                    @RequestParam String bInfo, HttpServletRequest request) {
    WeiXinUser user = weiXinService.getCurrentWeiXinUser(request);
    ActivityJoinLog joinLog = activityJoinLogService.findLogByTypeAndUser(4, version, user);
    Map<Object, Object> result = new HashMap<>();
    result.put("user", user.getLeJiaUser());
    if (joinLog != null) {
      result.put("status", 1);
    } else {
      //给红包和积分，并添加领取记录
      try {
        Map<Object, Object> map = weiXinUserService
            .shortActivitySubmit(user, scoreA, scoreB, aInfo, bInfo, 11, 4 + "_" + version, version,
                                 4);
        result.put("map", map);

      } catch (Exception e) {
        e.printStackTrace();
        return LejiaResult.build(500, "服务器异常");
      }
    }
    return LejiaResult.ok(result);
  }

  /**
   * 充值卡 兑换
   * @param exchangeCode 充值兑换码
   * @return 状态
   */
  @RequestMapping(value = "/rechargeCard/exchange", method = RequestMethod.POST)
  public LejiaResult rechargeCardSubmit(@RequestParam String exchangeCode, HttpServletRequest request) {
    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);

    try {
      if (exchangeCode!=null && !exchangeCode.equals("")){
        List<RechargeCard> list1 = rechargeCardService.findRechargeCardByExchangeCode(exchangeCode);
        List<RechargeCard> list2 = rechargeCardService.findRechargeCardByWeiXinUser(weiXinUser);
        if(list1.size()>0){
          return LejiaResult.build(499, "兑换码已使用!");
        }
        if(list2.size()>100){
          return LejiaResult.build(498, "兑换次数超限!");
        }

        RechargeCard rechargeCard = new RechargeCard();
        rechargeCard.setRechargeStatus(1);
        rechargeCard.setCreateTime(new Date());
        rechargeCard.setExchangeCode(exchangeCode);
        rechargeCard.setWeiXinUser(weiXinUser);
        rechargeCardService.saveRechargeCard(rechargeCard);
        return LejiaResult.ok();
      }else {

        return LejiaResult.build(497, "兑换码错误!");
      }

    } catch (Exception e) {
      return LejiaResult.build(202, "服务器异常");
    }
  }

}
