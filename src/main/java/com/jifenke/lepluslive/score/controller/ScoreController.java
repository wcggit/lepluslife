package com.jifenke.lepluslive.score.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;
import com.jifenke.lepluslive.score.controller.dto.ScoreDto;
import com.jifenke.lepluslive.score.domain.entities.ScoreA;
import com.jifenke.lepluslive.score.domain.entities.ScoreADetail;
import com.jifenke.lepluslive.score.domain.entities.ScoreB;
import com.jifenke.lepluslive.score.domain.entities.ScoreBDetail;
import com.jifenke.lepluslive.score.domain.entities.ScoreC;
import com.jifenke.lepluslive.score.domain.entities.ScoreCDetail;
import com.jifenke.lepluslive.score.service.ScoreAService;
import com.jifenke.lepluslive.score.service.ScoreBService;
import com.jifenke.lepluslive.score.service.ScoreCService;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.service.WeiXinService;
import com.jifenke.lepluslive.weixin.service.WeiXinUserService;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import io.swagger.annotations.ApiOperation;

/**
 * 积分和红包的查询 Created by zhangwen on 2016/5/11.
 */
@RestController
@RequestMapping("/score")
public class ScoreController {

  @Inject
  private ScoreAService scoreAService;

  @Inject
  private ScoreBService scoreBService;

  @Inject
  private ScoreCService scoreCService;

  @Inject
  private LeJiaUserService leJiaUserService;

  @Inject
  private WeiXinUserService weiXinUserService;

  @Inject
  private WeiXinService weiXinService;

  @RequestMapping("/scoreDetail")
  public ModelAndView goScoreDetailPage(HttpServletRequest request, Model model,
                                        @RequestParam Integer type) {
    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
    model.addAttribute("openId", weiXinUser.getOpenId());
    model.addAttribute("type", type);
    return MvUtil.go("/weixin/scoreDetail");
  }

  @ApiOperation(value = "红包列表")
  @RequestMapping(value = "/listA", method = RequestMethod.POST)
  public LejiaResult listA(@RequestParam(required = true) String token) {

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

  @ApiOperation(value = "C金币列表")
  @RequestMapping(value = "/listB", method = RequestMethod.POST)
  public LejiaResult listB(@RequestParam(required = true) String token) {

    LeJiaUser leJiaUser = leJiaUserService.findUserByUserSid(token);
    ScoreC scoreC = scoreCService.findScoreCByLeJiaUser(leJiaUser);

    List<ScoreCDetail> cDetails = scoreCService.findAllScoreCDetailByScoreC(scoreC);

    if ((cDetails != null) && (cDetails.size() > 0)) {
      Map result = formatCDetail(cDetails, scoreC);
      return LejiaResult.build(200, "ok", result);
    } else {
      return LejiaResult.build(200, "ok", null);
    }
  }

  @RequestMapping(value = "/scoreList", method = RequestMethod.POST)
  public LejiaResult scoreList(@RequestParam(required = false) String openId,
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
      } else if (type == 2) {
        ScoreC scoreC = scoreCService.findScoreCByLeJiaUser(leJiaUser);

        List<ScoreCDetail> cDetails = scoreCService.findAllScoreCDetailByScoreC(scoreC);

        if ((cDetails != null) && (cDetails.size() > 0)) {
          Map result = formatCDetail(cDetails, scoreC);
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
    aDetails.forEach(e -> {
      if (map.containsKey(sdf.format(e.getDateCreated()))) {
        map.get(sdf.format(e.getDateCreated())).add(e);
      } else {
        ArrayList<Object> aDetailList = new ArrayList<>();
        aDetailList.add(e);
        map.put(sdf.format(e.getDateCreated()), aDetailList);
      }
    });
    map.forEach((key, val) -> {
      ScoreDto scoreDto = new ScoreDto();
      scoreDto.setCreateDate(key);
      scoreDto.setList(val);
      result.add(scoreDto);
    });
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
    bDetails.forEach(e -> {
      if (map.containsKey(sdf.format(e.getDateCreated()))) {
        map.get(sdf.format(e.getDateCreated())).add(e);
      } else {
        ArrayList<Object> aDetailList = new ArrayList<>();
        aDetailList.add(e);
        map.put(sdf.format(e.getDateCreated()), aDetailList);
      }
    });
    map.forEach((key, val) -> {
      ScoreDto scoreDto = new ScoreDto();
      scoreDto.setCreateDate(key);
      scoreDto.setList(val);
      result.add(scoreDto);
    });
    resultMap.put("list", result);
    return resultMap;
  }

  private Map formatCDetail(List<ScoreCDetail> cDetails, ScoreC scoreC) {
    Map<String, Object> resultMap = new HashMap<>();
    Map<String, Object> map1 = new HashMap<>();
    map1.put("totalScore", scoreC.getTotalScore());
    map1.put("currScore", scoreC.getScore());
    resultMap.put("scoreA", map1);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
    LinkedHashMap<String, ArrayList<Object>> map = new LinkedHashMap<>();
    List<ScoreDto> result = new ArrayList<>();
    cDetails.forEach(e -> {
      if (map.containsKey(sdf.format(e.getDateCreated()))) {
        map.get(sdf.format(e.getDateCreated())).add(e);
      } else {
        ArrayList<Object> aDetailList = new ArrayList<>();
        aDetailList.add(e);
        map.put(sdf.format(e.getDateCreated()), aDetailList);
      }
    });
    map.forEach((key, val) -> {
      ScoreDto scoreDto = new ScoreDto();
      scoreDto.setCreateDate(key);
      scoreDto.setList(val);
      result.add(scoreDto);
    });
    resultMap.put("list", result);
    return resultMap;
  }

}
