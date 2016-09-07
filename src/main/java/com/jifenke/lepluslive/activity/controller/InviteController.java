package com.jifenke.lepluslive.activity.controller;

import com.jifenke.lepluslive.activity.controller.dto.InviteDto;
import com.jifenke.lepluslive.activity.domain.entities.LeJiaUserInfo;
import com.jifenke.lepluslive.activity.service.LeJiaUserInfoService;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;
import com.jifenke.lepluslive.weixin.service.DictionaryService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

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

  @Inject
  private DictionaryService dictionaryService;

  /**
   * 打开邀请好友页面
   */
  @ApiOperation(value = "打开邀请好友页面")
  @RequestMapping(value = "/open", method = RequestMethod.GET)
  public
  @ResponseBody
  LejiaResult openSportPage(
      @ApiParam(value = "用户标识token") @RequestParam(required = false) String token) {
    Map<String, String> map = dictionaryService.findInvite();
    if (token == null) {
      return LejiaResult
          .build(200, "ok",
                 new InviteDto(0, 0, 0,
                               map.get("pic"), map.get("title"), map.get("content"),
                               map.get("url")));
    } else {
      LeJiaUserInfo info = leJiaUserInfoService.findByToken(token);
      if (info == null) {  //创建新用户信息
        LeJiaUser leJiaUser = leJiaUserService.findUserByUserSid(token);
        if (leJiaUser != null) {
          info = leJiaUserInfoService.createUserInfo(leJiaUser);
        }
      }
      if (info != null) {
        return LejiaResult
            .build(200, "ok",
                   new InviteDto(info.getInviteA(), info.getInviteB(), info.getTotalInvite(),
                                 map.get("pic"), map.get("title"), map.get("content"),
                                 map.get("url")));
      } else {
        return LejiaResult.build(201, "未登陆或token有误", null);
      }
    }
  }
}
