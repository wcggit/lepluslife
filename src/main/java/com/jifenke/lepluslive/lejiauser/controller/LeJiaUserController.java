package com.jifenke.lepluslive.lejiauser.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.lejiauser.controller.dto.LeJiaUserDto;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;

import com.jifenke.lepluslive.order.service.OrderService;
import com.jifenke.lepluslive.score.domain.entities.ScoreA;
import com.jifenke.lepluslive.score.domain.entities.ScoreB;
import com.jifenke.lepluslive.score.service.ScoreAService;
import com.jifenke.lepluslive.score.service.ScoreBService;

import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.service.WeiXinUserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

import javax.inject.Inject;

/**
 * Created by wcg on 16/4/21.
 */
@Controller
@RequestMapping("/user")
public class LeJiaUserController {

  @Inject
  private LeJiaUserService leJiaUserService;

  @Inject
  private ScoreAService scoreAService;

  @Inject
  private ScoreBService scoreBService;

  @Inject
  private WeiXinUserService weiXinUserService;

  @Inject
  private OrderService orderService;

  /**
   * APP个人中心1.1版 16/09/05
   */
  @ApiOperation(value = "点击“我的”")
  @RequestMapping(value = "/center", method = RequestMethod.POST)
  public
  @ResponseBody
  LejiaResult userInfo(@RequestParam(required = false) String token) {
    LeJiaUser leJiaUser = leJiaUserService.findUserByUserSid(token);
    if (leJiaUser != null) {
      Map map = orderService.getOrdersCount(leJiaUser.getId());
      return LejiaResult.ok(map);
    }
    return LejiaResult.build(201, "未登录");
  }

  /**
   * token获取个人数据 16/09/07
   */
  @ApiOperation(value = "token获取个人数据")
  @RequestMapping(value = "/open", method = RequestMethod.POST)
  public
  @ResponseBody
  LejiaResult open(@RequestParam(required = false) String token) {
    if (token != null) {//获取个人数据
      Map map = leJiaUserService.getUserInfo(token);
      if (map != null) {
        return LejiaResult.ok(map);
      }
      return LejiaResult.build(202, "token无效");
    }
    return LejiaResult.ok();
  }

  /**
   * app微信登录1.1版
   */
  @ApiOperation(value = "微信登录")
  @RequestMapping(value = "/wxLogin", method = RequestMethod.POST)
  public
  @ResponseBody
  LejiaResult wxLogin(@RequestParam(required = true) String unionid,
                      @RequestParam(required = true) String openid,
                      @RequestParam(required = false) String country,
                      @RequestParam(required = false) String nickname,
                      @RequestParam(required = false) String city,
                      @RequestParam(required = false) String province,
                      @RequestParam(required = false) String language,
                      @RequestParam(required = false) String headimgurl,
                      @RequestParam(required = false) Long sex,
                      @ApiParam(value = "推送token") @RequestParam(required = false) String token) {
    WeiXinUser weiXinUser = weiXinUserService.findWeiXinUserByUnionId(unionid);  //是否已注册
    LeJiaUser leJiaUser = null;
    try {
      leJiaUser =
          weiXinUserService
              .saveWeiXinUserByApp(weiXinUser, unionid, openid, country, city, nickname,
                                   province, language, headimgurl, sex, token);
    } catch (Exception e) {
      e.printStackTrace();
      return LejiaResult.build(500, "服务器异常");
    }
    ScoreA scoreA = scoreAService.findScoreAByLeJiaUser(leJiaUser);
    ScoreB scoreB = scoreBService.findScoreBByWeiXinUser(leJiaUser);
    if (scoreA != null && scoreB != null && leJiaUser != null) {
      return LejiaResult.build(200, "登录成功", new LeJiaUserDto(scoreA.getScore(), scoreB.getScore(),
                                                             null,
                                                             leJiaUser.getUserSid(),
                                                             headimgurl,
                                                             nickname,
                                                             leJiaUser.getPhoneNumber()));
    } else {
      return LejiaResult.build(404, "服务器异常");
    }
  }

  @RequestMapping(value = "/test", method = RequestMethod.GET)
  public ModelAndView gotest() {
    return MvUtil.go("/test/test");
  }

}
