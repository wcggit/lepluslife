package com.jifenke.lepluslive.activity.controller;

import com.jifenke.lepluslive.activity.domain.entities.ActivityPhoneOrder;
import com.jifenke.lepluslive.activity.domain.entities.RechargeCard;
import com.jifenke.lepluslive.activity.service.ActivityPhoneOrderService;
import com.jifenke.lepluslive.activity.service.ActivityPhoneRuleService;
import com.jifenke.lepluslive.activity.service.RechargeCardService;
import com.jifenke.lepluslive.activity.service.RechargeService;
import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.global.service.MessageService;
import com.jifenke.lepluslive.global.util.JsonUtils;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;
import com.jifenke.lepluslive.score.domain.entities.ScoreB;
import com.jifenke.lepluslive.score.service.ScoreBService;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.service.DictionaryService;
import com.jifenke.lepluslive.weixin.service.WeiXinPayService;
import com.jifenke.lepluslive.weixin.service.WeiXinService;

import org.jdom.JDOMException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 充值卡兑换 Created by tqy on 2017/1/4.
 */
@RestController
@RequestMapping("/front/rechargeCard")
public class RechargeCardController {

  @Inject
  private WeiXinService weiXinService;
  @Inject
  private RechargeCardService rechargeCardService;

  /**
   * 查询充值卡 兑换记录
   * @return
   */
  @RequestMapping(value = "/exchange/list", method = RequestMethod.POST)
  public LejiaResult rechargeCardList(HttpServletRequest request) {
    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
    List<RechargeCard> list = rechargeCardService.findRechargeCardByWeiXinUser(weiXinUser);
    return LejiaResult.ok(list);
  }

}
