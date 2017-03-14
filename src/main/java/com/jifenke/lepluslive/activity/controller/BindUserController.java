package com.jifenke.lepluslive.activity.controller;

import com.jifenke.lepluslive.activity.service.BindUserService;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.service.WeiXinService;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 商户注册码 用于消费者绑定手机号并绑定商户 Created by zhangwen on 2017/3/7.
 */
@RestController
@RequestMapping("/front/bind")
public class BindUserController {

  @Inject
  private WeiXinService weiXinService;

  @Inject
  private MerchantService merchantService;

  @Inject
  private BindUserService bindUserService;

  /**
   * 进入绑定页面 17/3/7
   */
  @RequestMapping(value = "/weixin/{merchantSid}", method = RequestMethod.GET)
  public ModelAndView index(HttpServletRequest request, HttpServletResponse response, Model model,
                            @PathVariable String merchantSid) {
    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
    LeJiaUser leJiaUser = weiXinUser.getLeJiaUser();
    Merchant merchant = merchantService.findByMerchantSid(merchantSid);
    if (leJiaUser != null && merchant != null) {
      if (leJiaUser.getPhoneNumber() == null || "".equals(leJiaUser.getPhoneNumber())) {
        model.addAttribute("merchantSid", merchant.getMerchantSid());
        model.addAttribute("merchantName", merchant.getName());
        model.addAttribute("subState", weiXinUser.getSubState());
        return MvUtil.go("/activity/bind/index");
      }
    }
    try {
      response
          .sendRedirect(
              "http://www.lepluslife.com/resource/frontRes/activity/bind/hasRegister.html");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 绑定页面提交 17/3/7
   */
  @RequestMapping(value = "/weixin/submit", method = RequestMethod.POST)
  public LejiaResult submit(HttpServletRequest request, @RequestParam String merchantSid,
                            @RequestParam String code,
                            @RequestParam String phoneNumber) {
    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
    Merchant merchant = merchantService.findByMerchantSid(merchantSid);
    bindUserService.bindUserSubmit(weiXinUser, merchant, code, phoneNumber);
    return LejiaResult.ok();
  }

}
