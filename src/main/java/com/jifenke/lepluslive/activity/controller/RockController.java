package com.jifenke.lepluslive.activity.controller;

import com.jifenke.lepluslive.activity.controller.dto.RockDto;
import com.jifenke.lepluslive.activity.domain.entities.LeJiaUserInfo;
import com.jifenke.lepluslive.activity.service.ActivityRockLogService;
import com.jifenke.lepluslive.activity.service.LeJiaUserInfoService;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.inject.Inject;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Created by zhangwen on 2016/8/18.
 */
@Controller
@RequestMapping("/app/rock")
public class RockController {

  @Inject
  private LeJiaUserService leJiaUserService;

  @Inject
  private LeJiaUserInfoService leJiaUserInfoService;

  @Inject
  private ActivityRockLogService activityRockLogService;

  /**
   * 打开摇一摇页面
   */
  @ApiOperation(value = "打开摇一摇页面")
  @RequestMapping(value = "/open", method = RequestMethod.POST)
  public
  @ResponseBody
  LejiaResult openSportPage(
      @ApiParam(value = "用户标识token") @RequestParam(required = true) String token) {

    LeJiaUserInfo leJiaUserInfo = leJiaUserInfoService.findByToken(token);
    if (leJiaUserInfo == null) {  //创建新运动信息
      LeJiaUser leJiaUser = leJiaUserService.findUserByUserSid(token);
      if (leJiaUser != null) {
        leJiaUserInfo = leJiaUserInfoService.createSportUser(leJiaUser);
      }
    }

    if (leJiaUserInfo != null) {
      //获取剩余摇一摇次数
      Date date = new Date();
      String currDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
      int flag;
      if (currDate.equals(leJiaUserInfo.getRockDate())) {
        flag = 3 - leJiaUserInfo.getRockTime();
      } else {
        flag = 3;
      }
      return LejiaResult
          .build(200, "ok",
                 new RockDto(flag, leJiaUserInfo.getRockA(), leJiaUserInfo.getRockB(), 0, 0));
    } else {
      return LejiaResult.build(201, "未登陆", null);
    }
  }

  /**
   * 摇一摇发放红包和积分
   */
  @ApiOperation(value = "摇一摇接口")
  @RequestMapping(value = "/submit", method = RequestMethod.POST)
  public
  @ResponseBody
  LejiaResult submitSport(
      @ApiParam(value = "用户标识token") @RequestParam(required = true) String token) {

    LeJiaUserInfo leJiaUserInfo = leJiaUserInfoService.findByToken(token);
    if (leJiaUserInfo == null) {  //创建新摇一摇信息
      LeJiaUser leJiaUser = leJiaUserService.findUserByUserSid(token);
      if (leJiaUser != null) {
        leJiaUserInfo = leJiaUserInfoService.createSportUser(leJiaUser);
      }
    }
    //处理数据
    if (leJiaUserInfo != null) {
      try {
        HashMap<String, Integer>
            map =
            activityRockLogService.addRockLogAndScore(token, leJiaUserInfo);
        if (map != null) {
          return LejiaResult.build(200, "ok",
                                   new RockDto(map.get("times"), map.get("totalA"),
                                               map.get("totalB"), map.get("a"),
                                               map.get("b")));
        } else {
          return LejiaResult.build(302, "已摇三次");
        }
      } catch (Exception e) {
        e.printStackTrace();
        return LejiaResult.build(201, "保存失败");
      }
    } else {
      return LejiaResult.build(500, "服务器繁忙");
    }
  }
}
