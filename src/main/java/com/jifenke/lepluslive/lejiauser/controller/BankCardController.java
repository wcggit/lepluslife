package com.jifenke.lepluslive.lejiauser.controller;

import com.jifenke.lepluslive.global.service.MessageService;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.service.BankCardService;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

import io.swagger.annotations.ApiOperation;

/**
 * 用户银行卡号相关接口 Created by zhangwen on 2016/11/28.
 */
@RestController
@RequestMapping("/front/user")
public class BankCardController {

  @Inject
  private BankCardService bankCardService;

  @Inject
  private LeJiaUserService leJiaUserService;

  @Inject
  private MessageService messageUtil;


  @ApiOperation(value = "获取银行卡列表")
  @RequestMapping(value = "/card/list", method = RequestMethod.POST)
  public
  @ResponseBody
  LejiaResult list(@RequestParam(required = true) String token) {
    return LejiaResult
        .ok(bankCardService.findByLeJiaUser(leJiaUserService.findUserByUserSid(token)));
  }

  @ApiOperation(value = "删除绑定的银行卡")
  @RequestMapping(value = "/card/del", method = RequestMethod.POST)
  public
  @ResponseBody
  LejiaResult delCard(@RequestParam Long id, @RequestParam String token) {
    try {
      bankCardService.deleteBankCard(id, token);
    } catch (Exception e) {
      e.printStackTrace();
      return LejiaResult.build(2009, messageUtil.getMsg("2009"));
    }
    return LejiaResult.ok();
  }

  @ApiOperation(value = "绑定银行卡")
  @RequestMapping(value = "/card/add", method = RequestMethod.POST)
  public
  @ResponseBody
  LejiaResult addCard(@RequestParam String token, @RequestParam String number,
                      @RequestParam String cardType, @RequestParam Integer cardLength,
                      @RequestParam String prefixNum, @RequestParam String cardName,
                      @RequestParam String bankName) {
    LeJiaUser leJiaUser = leJiaUserService.findUserByUserSid(token);
    try {
      String
          status =
          bankCardService.addBankCard(leJiaUser, number, cardLength, cardType, prefixNum, cardName,
                                      bankName);
      if (!"200".equals(status)) {
        return LejiaResult.build(Integer.valueOf(status), messageUtil.getMsg(status));
      }
    } catch (Exception e) {
      e.printStackTrace();
      return LejiaResult.build(500, messageUtil.getMsg("500"));
    }
    return LejiaResult.ok();
  }


}
