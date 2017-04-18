package com.jifenke.lepluslive.merchant.controller;

import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantScroll;
import com.jifenke.lepluslive.merchant.service.CityService;
import com.jifenke.lepluslive.merchant.service.MerchantRebatePolicyService;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.score.service.ScoreAService;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.service.WeiXinService;

import org.springframework.ui.Model;
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
 * Created by zhangwen on 2017/4/16.
 */
@RestController
@RequestMapping("/front/shop")
public class ShopController {

  @Inject
  private WeiXinService weiXinService;

  @Inject
  private ScoreAService scoreAService;

  @Inject
  private MerchantService merchantService;

  @Inject
  private MerchantRebatePolicyService policyService;

  @Inject
  private CityService cityService;

  /**
   * 周边好店首页  2017/4/17
   */
  @RequestMapping(value = "/weixin", method = RequestMethod.GET)
  public ModelAndView index(HttpServletRequest request, Model model)
      throws IOException {
    model.addAttribute("wxConfig", weiXinService.getWeiXinConfig(request));
    return MvUtil.go("/shop/index");
  }

  /**
   * 商家推荐页  2017/4/17
   *
   * @param cityId 推荐的城市ID
   */
  @RequestMapping(value = "/weixin/more", method = RequestMethod.GET)
  public ModelAndView more(HttpServletRequest request, Long cityId, Model model)
      throws IOException {
    model.addAttribute("wxConfig", weiXinService.getWeiXinConfig(request));
    model.addAttribute("city", cityService.findCityById(cityId));
    return MvUtil.go("/shop/more");
  }

  @RequestMapping(value = "/weixin/m", method = RequestMethod.GET)
  public ModelAndView goMerchantPage(HttpServletRequest request, Model model,
                                     @RequestParam Long id,
                                     @RequestParam(required = false) String distance,
                                     @RequestParam(required = false) Integer status)
      throws IOException {
    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
    model.addAttribute("score",
                       scoreAService.findScoreAByLeJiaUser(weiXinUser.getLeJiaUser()).getScore());

    Merchant merchant = merchantService.findMerchantById(id);
    List<MerchantScroll> scrolls = merchantService.findAllScorllPicture(merchant);
    model.addAttribute("merchant", merchant);
    model.addAttribute("policy", policyService.findByMerchant(id));
    List<Merchant> list = merchantService.countByMerchantUser(merchant.getMerchantUser());
    model.addAttribute("list", list);
    if (list != null) {
      model.addAttribute("shopNum", list.size() - 1);
    } else {
      model.addAttribute("shopNum", 0);
    }
    if (scrolls == null || scrolls.size() < 1) {
      model.addAttribute("hasScroll", 0);
    } else {
      model.addAttribute("hasScroll", 1);
    }
    model.addAttribute("scrolls", scrolls);
    model.addAttribute("distance", distance);
    model.addAttribute("status", status);
    model.addAttribute("wxConfig", weiXinService.getWeiXinConfig(request));
    return MvUtil.go("/shop/detail");
  }
}
