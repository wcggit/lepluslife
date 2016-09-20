package com.jifenke.lepluslive.activity.controller;

import com.jifenke.lepluslive.activity.domain.entities.ActivityCodeBurse;
import com.jifenke.lepluslive.activity.domain.entities.ActivityJoinLog;
import com.jifenke.lepluslive.activity.service.ActivityCodeBurseService;
import com.jifenke.lepluslive.activity.service.ActivityJoinLogService;
import com.jifenke.lepluslive.activity.service.ActivityShareLogService;
import com.jifenke.lepluslive.global.util.CookieUtils;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.Map;
import java.util.Random;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

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
  private MerchantService merchantService;

  @Inject
  private ActivityShareLogService activityShareLogService;

  //分享页面 06/09/02
  @RequestMapping(value = "/share/{id}", method = RequestMethod.GET)
  public ModelAndView sharePage(@PathVariable String id, HttpServletRequest request,
                                Model model) {
    String openId = CookieUtils.getCookieValue(request, appId + "-user-open-id");
    WeiXinUser weiXinUser = weiXinUserService.findWeiXinUserByOpenId(openId);
    model.addAttribute("token", id); //记录邀请人的token
    //判断是否有手机号码
    LeJiaUser leJiaUser = weiXinUser.getLeJiaUser();
    if (leJiaUser != null) {
      if (leJiaUser.getPhoneNumber() == null || leJiaUser.getPhoneNumber().equals("")) {
        model.addAttribute("status", 0);
      } else {
        model.addAttribute("status", 1);
      }
    } else {
      model.addAttribute("status", 0);
    }
    return MvUtil.go("/activity/share");
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
  public
  @ResponseBody
  LejiaResult shareSubmit(@RequestParam String phoneNumber, @RequestParam String token,
                          HttpServletRequest request) {
    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
    LeJiaUser leJiaUser = leJiaUserService.findUserByPhoneNumber(phoneNumber);
    if (leJiaUser == null) { //手机号是否已注册
      //给双方派发红包和积分,填充手机号码成为会员，修改邀请人邀请记录(info)并记录shareLog
      try {
        activityShareLogService.giveScoreByShare(weiXinUser, token, phoneNumber);
        return LejiaResult.ok();
      } catch (Exception e) {
        return LejiaResult.build(202, "服务器异常");
      }
    } else {
      return LejiaResult.build(201, "手机号已被使用");
    }
  }

  //关注图文链接页面
  @RequestMapping("/subPage")
  public ModelAndView subPage(HttpServletRequest request, Model model) {
    String openId = CookieUtils.getCookieValue(request, appId + "-user-open-id");
    WeiXinUser weiXinUser = weiXinUserService.findWeiXinUserByOpenId(openId);
    model.addAttribute("wxConfig", weiXinService.getWeiXinConfig(request));
    //判断是否获得过红包
    ActivityJoinLog joinLog = activityJoinLogService.findLogBySubActivityAndOpenId(0, weiXinUser
        .getOpenId());
    if (joinLog == null) {//未参与
      model.addAttribute("status", 0);
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
  public
  @ResponseBody
  LejiaResult subPageOpen(@RequestParam String phoneNumber, HttpServletRequest request) {
    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
    LeJiaUser leJiaUser = leJiaUserService.findUserByPhoneNumber(phoneNumber);  //是否已注册
    ActivityJoinLog joinLog = activityJoinLogService.findLogBySubActivityAndOpenId(0, weiXinUser
        .getOpenId());
    if (leJiaUser == null && joinLog == null) {
      //判断是否需要绑定商户 4_0_123
      leJiaUserService.checkUserBindMerchant(weiXinUser);

      //派发红包和积分,填充手机号码成为会员
      try {
        Map<String, Integer> map = weiXinUserService.giveScoreAByDefault(weiXinUser, phoneNumber);
        //添加参加记录
        activityJoinLogService.addCodeBurseLogByDefault(weiXinUser, map.get("scoreA"));
        return LejiaResult.build(200, "" + map.get("scoreA"));
      } catch (Exception e) {
        e.printStackTrace();
        return LejiaResult.build(500, "服务器异常");
      }
    }
    return LejiaResult.build(201, "手机号已被使用或已领取红包");
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
      ActivityJoinLog joinLog = activityJoinLogService.findLogBySubActivityAndOpenId(0, weiXinUser
          .getOpenId());
      int defaultScoreA = Integer.valueOf(dictionaryService.findDictionaryById(18L).getValue());
      if (joinLog == null) {//未参与
        //派发红包,获取默认派发红包金额
        int status = scoreAService.giveScoreAByDefault(weiXinUser, defaultScoreA);
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
                  .findLogBySubActivityAndOpenId(codeBurse.getType(), weiXinUser.getOpenId());
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


}
