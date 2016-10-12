package com.jifenke.lepluslive.score.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;
import com.jifenke.lepluslive.score.controller.dto.ScoreDto;
import com.jifenke.lepluslive.score.domain.entities.ScoreA;
import com.jifenke.lepluslive.score.domain.entities.ScoreADetail;
import com.jifenke.lepluslive.score.domain.entities.ScoreB;
import com.jifenke.lepluslive.score.domain.entities.ScoreBDetail;
import com.jifenke.lepluslive.score.service.ScoreAService;
import com.jifenke.lepluslive.score.service.ScoreBService;
import com.jifenke.lepluslive.weixin.service.WeiXinUserService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.swagger.annotations.ApiOperation;

/**
 * 积分和红包的查询 Created by zhangwen on 2016/5/11.
 */
@Controller
@RequestMapping("/score")
public class ScoreController {

  @Inject
  private ScoreAService scoreAService;

  @Inject
  private ScoreBService scoreBService;

  @Inject
  private LeJiaUserService leJiaUserService;

  @Inject
  private WeiXinUserService weiXinUserService;

  @ApiOperation(value = "红包列表")
  @RequestMapping(value = "/listA", method = RequestMethod.POST)
  public
  @ResponseBody
  LejiaResult listA(@RequestParam(required = true) String token) {

    LeJiaUser leJiaUser = leJiaUserService.findUserByUserSid(token);
    ScoreA scoreA = scoreAService.findScoreAByLeJiaUser(leJiaUser);
    if (scoreA == null) {
      return LejiaResult.build(6003, "未找到红包记录");
    }

    List<ScoreADetail> aDetails = scoreAService.findAllScoreADetailByScoreA(scoreA);

    if ((aDetails != null) && (aDetails.size() > 0)) {
      Map result = formatADetail(aDetails, scoreA);

      return LejiaResult.build(200, "ok", result);
    } else {
      return LejiaResult.build(200, "ok", null);
    }
  }

  @ApiOperation(value = "B积分列表")
  @RequestMapping(value = "/listB", method = RequestMethod.POST)
  public
  @ResponseBody
  LejiaResult listB(@RequestParam(required = true) String token) {

    LeJiaUser leJiaUser = leJiaUserService.findUserByUserSid(token);
    ScoreB scoreB = scoreBService.findScoreBByWeiXinUser(leJiaUser);
    if (scoreB == null) {
      return LejiaResult.build(6001, "未找到积分记录");
    }

    List<ScoreBDetail> bDetails = scoreBService.findAllScoreBDetailByScoreB(scoreB);

    if ((bDetails != null) && (bDetails.size() > 0)) {
      Map result = formatBDetail(bDetails, scoreB);
      return LejiaResult.build(200, "ok", result);
    } else {
      return LejiaResult.build(200, "ok", null);
    }
  }

  @RequestMapping(value = "/scoreList", method = RequestMethod.POST)
  public
  @ResponseBody
  LejiaResult scoreList(@RequestParam(required = false) String openId,
                        @RequestParam(required = false) Integer type) {
    LeJiaUser leJiaUser = weiXinUserService.findWeiXinUserByOpenId(openId).getLeJiaUser();
    if (leJiaUser != null) {
      if (type == 0) {
        ScoreA scoreA = scoreAService.findScoreAByLeJiaUser(leJiaUser);
        if (scoreA == null) {
          return LejiaResult.build(207, "请先登录");
        }

        List<ScoreADetail> aDetails = scoreAService.findAllScoreADetailByScoreA(scoreA);

        if ((aDetails != null) && (aDetails.size() > 0)) {
          Map result = formatADetail(aDetails, scoreA);
          return LejiaResult.build(200, "ok", result);
        } else {
          return LejiaResult.build(200, "ok", null);
        }
      } else if (type == 1) {
        ScoreB scoreB = scoreBService.findScoreBByWeiXinUser(leJiaUser);
        if (scoreB == null) {
          return LejiaResult.build(207, "请先登录");
        }

        List<ScoreBDetail> bDetails = scoreBService.findAllScoreBDetailByScoreB(scoreB);

        if ((bDetails != null) && (bDetails.size() > 0)) {
          Map result = formatBDetail(bDetails, scoreB);
          return LejiaResult.build(200, "ok", result);
        } else {
          return LejiaResult.build(200, "ok", null);
        }
      }
    }
    return LejiaResult.build(201, "未找到用户");
  }

  private Map formatADetail(List<ScoreADetail> aDetails, ScoreA scoreA) {
    Map<String, Object> resultMap = new HashMap<>();
    Map<String, Object> map1 = new HashMap<>();
    map1.put("totalScore", scoreA.getTotalScore());
    map1.put("currScore", scoreA.getScore());
    resultMap.put("scoreA", map1);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
    LinkedHashMap<String, ArrayList<Object>> map = new LinkedHashMap<>();
    List<ScoreDto> result = new ArrayList<>();
    for (int i = 0; i < aDetails.size(); i++) {

      if (map.containsKey(sdf.format(aDetails.get(i).getDateCreated()))) {
        map.get(sdf.format(aDetails.get(i).getDateCreated())).add(aDetails.get(i));
      } else {
        ArrayList<Object> aDetailList = new ArrayList<>();
        aDetailList.add(aDetails.get(i));
        map.put(sdf.format(aDetails.get(i).getDateCreated()), aDetailList);
      }
    }
    Iterator i = map.entrySet().iterator();

    while (i.hasNext()) {
      Map.Entry entry = (java.util.Map.Entry) i.next();
      ScoreDto scoreDto = new ScoreDto();
      scoreDto.setCreateDate(entry.getKey().toString());
      scoreDto.setList((ArrayList<Object>) entry.getValue());
      result.add(scoreDto);
    }
    resultMap.put("list", result);
    return resultMap;
  }

  private Map formatBDetail(List<ScoreBDetail> bDetails, ScoreB scoreB) {
    Map<String, Object> resultMap = new HashMap<>();
    Map<String, Object> map1 = new HashMap<>();
    map1.put("totalScore", scoreB.getTotalScore());
    map1.put("currScore", scoreB.getScore());
    resultMap.put("scoreA", map1);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
    LinkedHashMap<String, ArrayList<Object>> map = new LinkedHashMap<>();
    List<ScoreDto> result = new ArrayList<>();
    for (int i = 0; i < bDetails.size(); i++) {

      if (map.containsKey(sdf.format(bDetails.get(i).getDateCreated()))) {
        map.get(sdf.format(bDetails.get(i).getDateCreated())).add(bDetails.get(i));
      } else {
        ArrayList<Object> aDetailList = new ArrayList<>();
        aDetailList.add(bDetails.get(i));
        map.put(sdf.format(bDetails.get(i).getDateCreated()), aDetailList);
      }
    }
    Iterator i = map.entrySet().iterator();

    while (i.hasNext()) {
      Map.Entry entry = (java.util.Map.Entry) i.next();
      ScoreDto scoreDto = new ScoreDto();
      scoreDto.setCreateDate(entry.getKey().toString());
      scoreDto.setList((ArrayList<Object>) entry.getValue());
      result.add(scoreDto);
    }
    resultMap.put("list", result);
    return resultMap;
  }

}
