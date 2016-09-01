package com.jifenke.lepluslive.activity.controller;

import com.jifenke.lepluslive.activity.controller.dto.SportDto;
import com.jifenke.lepluslive.activity.domain.entities.LeJiaUserInfo;
import com.jifenke.lepluslive.activity.service.ActivitySportLogService;
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

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Created by zhangwen on 2016/8/18.
 */
@Controller
@RequestMapping("/app/sport")
public class SportController {

  @Inject
  private LeJiaUserService leJiaUserService;

  @Inject
  private LeJiaUserInfoService leJiaUserInfoService;

  @Inject
  private ActivitySportLogService activitySportLogService;

  @Inject
  private DictionaryService dictionaryService;

  /**
   * 打开运动页
   */
  @ApiOperation(value = "打开运动页")
  @RequestMapping(value = "/open", method = RequestMethod.POST)
  public
  @ResponseBody
  LejiaResult openSportPage(
      @ApiParam(value = "用户标识token") @RequestParam(required = true) String token) {

    LeJiaUserInfo leJiaUserInfo = leJiaUserInfoService.findByToken(token);
    if (leJiaUserInfo == null) {  //创建新运动信息
      LeJiaUser leJiaUser = leJiaUserService.findUserByUserSid(token);
      if (leJiaUser != null) {
        leJiaUserInfo = leJiaUserInfoService.createUserInfo(leJiaUser);
      }
    }
    //返回数据
    String rule = dictionaryService.findDictionaryById(19L).getValue();
    String hour = dictionaryService.findDictionaryById(20L).getValue();
    String currDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    //查询今日是否已添加运动数据
    int flag = activitySportLogService.findBySportUserAndCurrDate(leJiaUserInfo, currDate);

    if (leJiaUserInfo != null) {
      return LejiaResult
          .build(200, "ok",
                 new SportDto(leJiaUserInfo.getSportA(), leJiaUserInfo.getSportB(), rule, hour,
                              flag));
    } else {
      return LejiaResult.build(200, "未登陆", new SportDto(0, 0, rule, hour, flag));
    }
  }

  /**
   * 提交运动数据
   */
  @ApiOperation(value = "提交运动数据")
  @RequestMapping(value = "/submit", method = RequestMethod.POST)
  public
  @ResponseBody
  LejiaResult submitSport(@ApiParam(value = "运动步数") @RequestParam(required = true) Integer distance,
                          @ApiParam(value = "用户标识token") @RequestParam(required = true) String token) {

    LeJiaUserInfo leJiaUserInfo = leJiaUserInfoService.findByToken(token);
    if (leJiaUserInfo == null) {  //创建新运动信息
      LeJiaUser leJiaUser = leJiaUserService.findUserByUserSid(token);
      if (leJiaUser != null) {
        leJiaUserInfo = leJiaUserInfoService.createUserInfo(leJiaUser);
      }
    }
    //处理数据
    if (leJiaUserInfo != null) {
      try {
        activitySportLogService.addSportLogAndScore(token, leJiaUserInfo, distance);
      } catch (Exception e) {
        e.printStackTrace();
        return LejiaResult.build(201, "保存失败");
      }
    }
    return LejiaResult.build(200, "ok");
  }
}
