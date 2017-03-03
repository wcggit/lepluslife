package com.jifenke.lepluslive.activity.controller;

import com.jifenke.lepluslive.activity.domain.entities.RechargeCard;
import com.jifenke.lepluslive.activity.service.RechargeCardService;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.service.WeiXinService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

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
