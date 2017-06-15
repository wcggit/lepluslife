package com.jifenke.lepluslive.lejiauser.controller;

import com.jifenke.lepluslive.global.service.MessageService;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.lejiauser.domain.entities.BankCard;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.service.BankCardService;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;
import com.jifenke.lepluslive.weixin.service.WeiXinService;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import io.swagger.annotations.ApiOperation;

/**
 * 用户银行卡号相关接口 Created by zhangwen on 2016/11/28.
 */
@RestController
@RequestMapping("/front")
public class BankCardController {

  @Inject
  private BankCardService bankCardService;

  @Inject
  private LeJiaUserService leJiaUserService;

  @Inject
  private MessageService messageUtil;

  @Inject
  private WeiXinService weiXinService;

  /**
   * 用户银行卡列表页  2017/4/19
   */
  @RequestMapping(value = "/user/weixin/cardList", method = RequestMethod.GET)
  public ModelAndView cardList(Model model, HttpServletRequest request) {
    model.addAttribute("list", cardConvert(bankCardService.findByLeJiaUser(
        weiXinService.getCurrentWeiXinUser(request).getLeJiaUser())));

    return MvUtil.go("/user/cardList");
  }


  /**
   * 添加银行卡页面  2017/4/19
   */
  @RequestMapping(value = "/user/weixin/addCard", method = RequestMethod.GET)
  public ModelAndView addCard(Model model, HttpServletRequest request) {
    model.addAttribute("user",
                       weiXinService.getCurrentWeiXinUser(request).getLeJiaUser());
    return MvUtil.go("/user/addCard");
  }

  /**
   * 调用第三方获取银行卡相关信息  2017/4/19
   *
   * @param cardNum 银行卡号
   */
  @RequestMapping(value = "/card/cardCheck", method = RequestMethod.GET)
  public LejiaResult cardCheck(@RequestParam String cardNum) {
    if (cardNum == null || cardNum.length() < 15) {
      return LejiaResult.build(2011, messageUtil.getMsg("2011"));
    }
    return LejiaResult.ok(bankCardService.cardCheck(cardNum));
  }

  /**
   * 新版获取银行卡列表  2017/4/20
   */
  @RequestMapping(value = "/card/user/list", method = RequestMethod.POST)
  public LejiaResult list(HttpServletRequest request) {
    return LejiaResult
        .ok(cardConvert(bankCardService.findByLeJiaUser(leJiaUserService.findUserById(
            Long.valueOf("" + request.getAttribute("leJiaUserId"))))));
  }

  @ApiOperation(value = "删除绑定的银行卡")
  @RequestMapping(value = "/user/card/del", method = RequestMethod.POST)
  public LejiaResult delCard(@RequestParam Long id, @RequestParam String token) {
    try {
      bankCardService.deleteBankCard(id, token);
    } catch (Exception e) {
      e.printStackTrace();
      return LejiaResult.build(2009, messageUtil.getMsg("2009"));
    }
    return LejiaResult.ok();
  }

  /**
   * 绑定银行卡  2017/4/20
   *
   * @param token       用户TOKEN
   * @param number      银行卡号
   * @param cardType    卡类型
   * @param prefixNum   卡bin
   * @param cardName    卡名称
   * @param bankName    发卡行
   * @param phoneNum    手机号
   * @param registerWay 注册途径
   */
  @RequestMapping(value = "/card/user/add", method = RequestMethod.POST)
  public LejiaResult addCard(@RequestParam String token, @RequestParam String number,
                             @RequestParam String cardType,
                             @RequestParam String prefixNum, @RequestParam String cardName,
                             @RequestParam String bankName,
                             @RequestParam(required = false) String phoneNum,
                             @RequestParam(required = false) Integer registerWay) {
    LeJiaUser leJiaUser = leJiaUserService.findUserByUserSid(token);
    if (registerWay == null) {
      registerWay = 1;
    }
    if (phoneNum == null) {
      phoneNum = "15555555555";
    }
    try {
      String
          status =
          bankCardService
              .addBankCard(leJiaUser, number, cardType, prefixNum, cardName,
                           bankName, phoneNum, registerWay);
      if (!"200".equals(status)) {
        return LejiaResult.build(Integer.valueOf(status), messageUtil.getMsg(status));
      }
    } catch (Exception e) {
      e.printStackTrace();
      return LejiaResult.build(500, messageUtil.getMsg("500"));
    }
    return LejiaResult.ok();
  }

  /**
   * 银行卡信息转换  2017/4/19
   *
   * @param list 银行卡列表
   */
  private List<Map<String, Object>> cardConvert(List<BankCard> list) {

    if (list != null && list.size() > 0) {
      List<Map<String, Object>> result = new ArrayList<>();

      for (BankCard card : list) {
        Map<String, Object> map = new HashMap<>();
        map.put("number",
                "**** **** **** " + card.getNumber().substring(card.getNumber().length() - 4));
        map.put("cardType", card.getCardType());
        map.put("bankName", card.getBankName());
        map.put("id", card.getId());
        result.add(map);
      }
      return result;
    }
    return null;
  }


}
