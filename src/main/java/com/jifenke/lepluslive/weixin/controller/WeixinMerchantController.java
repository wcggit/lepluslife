package com.jifenke.lepluslive.weixin.controller;

import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantScroll;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.score.service.ScoreAService;
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
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by wcg on 16/3/18.
 */
@RestController
@RequestMapping("/weixin/merchant")
public class WeixinMerchantController {

  @Inject
  private WeiXinService weiXinService;

  @Inject
  private ScoreAService scoreAService;

  @Inject
  private MerchantService merchantService;

  @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
  public ModelAndView goMerchantPage(HttpServletRequest request, Model model,
                                     @PathVariable Long id,
                                     @RequestParam(required = false) String distance,
                                     @RequestParam(required = false) Integer status)
      throws IOException {
    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
    model.addAttribute("scoreA", scoreAService.findScoreAByLeJiaUser(weiXinUser.getLeJiaUser()));

    Merchant merchant = merchantService.findMerchantById(id);
    List<MerchantScroll> scrolls = merchantService.findAllScorllPicture(merchant);
    model.addAttribute("merchant", merchant);
    if (scrolls == null || scrolls.size() < 1) {
      model.addAttribute("hasScroll", 0);
    } else {
      model.addAttribute("hasScroll", 1);
    }
    model.addAttribute("scrolls", scrolls);

    model.addAttribute("distance", distance);
    model.addAttribute("status", status);
    model.addAttribute("wxConfig", weiXinService.getWeiXinConfig(request));
    return MvUtil.go("/weixin/merchantInfo");
  }
}
