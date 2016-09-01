package com.jifenke.lepluslive.activity.controller;

import com.jifenke.lepluslive.activity.domain.entities.LeJiaUserInfo;
import com.jifenke.lepluslive.activity.service.LeJiaUserInfoService;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * APP邀请好友 Created by zhangwen on 2016/9/1.
 */
@Controller
@RequestMapping("/app/invite")
public class InviteController {

  @Inject
  private LeJiaUserService leJiaUserService;

  @Inject
  private LeJiaUserInfoService leJiaUserInfoService;

  /**
   * 打开邀请好友页面
   */
  @ApiOperation(value = "打开邀请好友页面")
  @RequestMapping(value = "/open", method = RequestMethod.GET)
  public
  @ResponseBody
  LejiaResult openSportPage(
      @ApiParam(value = "用户标识token") @RequestParam(required = true) String token) {

    LeJiaUserInfo leJiaUserInfo = leJiaUserInfoService.findByToken(token);
    if (leJiaUserInfo == null) {  //创建新用户信息
      LeJiaUser leJiaUser = leJiaUserService.findUserByUserSid(token);
      if (leJiaUser != null) {
        leJiaUserInfo = leJiaUserInfoService.createUserInfo(leJiaUser);
      }
    }

    if (leJiaUserInfo != null) {

      return LejiaResult
          .build(200, "ok");
    } else {
      return LejiaResult.build(201, "未登陆", null);
    }
  }
}
