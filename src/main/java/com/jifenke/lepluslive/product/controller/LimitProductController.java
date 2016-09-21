package com.jifenke.lepluslive.product.controller;


import com.jifenke.lepluslive.global.util.CookieUtils;
import com.jifenke.lepluslive.global.util.JsonUtils;
import com.jifenke.lepluslive.global.util.MvUtil;

import com.jifenke.lepluslive.product.domain.entities.Product;

import com.jifenke.lepluslive.product.domain.entities.ProductDetail;
import com.jifenke.lepluslive.product.domain.entities.ProductSpec;
import com.jifenke.lepluslive.product.domain.entities.ScrollPicture;

import com.jifenke.lepluslive.product.service.ProductService;

import com.jifenke.lepluslive.product.service.ScrollPictureService;
import com.jifenke.lepluslive.score.domain.entities.ScoreB;
import com.jifenke.lepluslive.score.service.ScoreBService;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.service.WeiXinUserService;


import net.sf.json.JSON;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * 爆品 Created by zhangwen on 16/9/18.
 */
@RestController
@RequestMapping("/front/product")
public class LimitProductController {

  @Inject
  private ProductService productService;

  @Value("${weixin.appId}")
  private String appId;

  @Inject
  private ScrollPictureService scrollPictureService;

  @Inject
  private WeiXinUserService weiXinUserService;

  @Inject
  private ScoreBService scoreBService;

  /**
   * 爆品详情页 16/09/20
   */
  @RequestMapping(value = "/weixin/hotIndex", method = RequestMethod.GET)
  public ModelAndView hotIndex(HttpServletRequest request, Model model) {
    String openId = CookieUtils.getCookieValue(request, appId + "-user-open-id");
    WeiXinUser weiXinUser = weiXinUserService.findWeiXinUserByOpenId(openId);
    ScoreB scoreB = scoreBService.findScoreBByWeiXinUser(weiXinUser.getLeJiaUser());
    model.addAttribute("scoreB", scoreB);
    return MvUtil.go("/product/hotIndex");
  }

  /**
   * 爆品详情页 16/09/19
   *
   * @param productId 商品id
   */
  @RequestMapping(value = "/limitDetail", method = RequestMethod.GET)
  public ModelAndView goBannerPage(@RequestParam(required = true) Long productId, Model model) {
    Product product = null;
    List<ProductDetail> detailList = null;
    List<ProductSpec> specList = null;
    List<ScrollPicture> scrollPictureList = null;
    if (productId != null) {
      product = productService.findOneProduct(productId);
    }
    if (product != null) {
      model.addAttribute("product", product);
      detailList = productService.findAllProductDetailsByProduct(product);
      specList = productService.findAllProductSpec(product);
      scrollPictureList = scrollPictureService.findAllScorllPicture(product);
      model.addAttribute("detailList", JsonUtils.objectToJson(detailList));
      model.addAttribute("specList", JsonUtils.objectToJson(specList));
      model.addAttribute("scrollList", scrollPictureList);
    }
    return MvUtil.go("/product/limitProductDetail");
  }

}
