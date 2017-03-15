package com.jifenke.lepluslive.product.controller;

import com.jifenke.lepluslive.global.util.JsonUtils;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.product.domain.entities.Product;
import com.jifenke.lepluslive.product.domain.entities.ProductDetail;
import com.jifenke.lepluslive.product.domain.entities.ProductSpec;
import com.jifenke.lepluslive.product.domain.entities.ScrollPicture;
import com.jifenke.lepluslive.product.service.GoldProductService;
import com.jifenke.lepluslive.product.service.ProductService;
import com.jifenke.lepluslive.product.service.ScrollPictureService;
import com.jifenke.lepluslive.score.service.ScoreCService;
import com.jifenke.lepluslive.weixin.service.WeiXinPayService;
import com.jifenke.lepluslive.weixin.service.WeiXinService;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * 金币商品 Created by zhangwen on 17/2/20.
 */
@RestController
@RequestMapping("/front/gold")
public class GoldProductController {

  @Inject
  private ProductService productService;

  @Inject
  private ScrollPictureService scrollPictureService;

  @Inject
  private WeiXinService weiXinService;

  @Inject
  private ScoreCService scoreCService;

  @Inject
  private GoldProductService goldProductService;

  @Inject
  private WeiXinPayService weiXinPayService;

  /**
   * 公众号 金币商品首页 17/2/20
   */
  @RequestMapping(value = "/weixin", method = RequestMethod.GET)
  public ModelAndView productIndex(HttpServletRequest request, Model model) {
    Long score = scoreCService
        .findScoreCByLeJiaUser(weiXinService.getCurrentWeiXinUser(request).getLeJiaUser())
        .getScore();
    model.addAttribute("score", score);
    Long a = score % 100;
    model.addAttribute("score2", a < 10 ? ("0" + a) : a);
    if (a > 0) {
      model.addAttribute("score3", score + 100);
    } else {
      model.addAttribute("score3", score);
    }
    List<Map> list = goldProductService.findHotProductListByPage(1, 100);

    for (Map map : list) {
      model.addAttribute("p_" + map.get("id"), map);
    }
    model.addAttribute("wxConfig", weiXinPayService.getWeiXinPayConfig(request));
    return MvUtil.go("/gold/product/index");
  }

  /**
   * 金币商品详情页 17/2/20
   *
   * @param productId 商品ID
   */
  @RequestMapping(value = "/weixin/p", method = RequestMethod.GET)
  public ModelAndView goBannerPage(@RequestParam Long productId, HttpServletRequest request,
                                   Model model) {
    model.addAttribute("scoreC", scoreCService
        .findScoreCByLeJiaUser(weiXinService.getCurrentWeiXinUser(request).getLeJiaUser()));
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
      scrollPictureList = scrollPictureService.findAllByProduct(product);
      model.addAttribute("detailList", JsonUtils.objectToJson(detailList));
      model.addAttribute("specList", specList);
      model.addAttribute("scrollList", scrollPictureList);
    }
    return MvUtil.go("/gold/product/detail");
  }

  /**
   * 分页获取金币商品列表  17/2/20
   *
   * @param page 当前页码
   */
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public LejiaResult productList(@RequestParam Integer page) {
    if (page == null || page < 1) {
      page = 1;
    }
    List<Map> list = goldProductService.findHotProductListByPage(page, 100);
    return LejiaResult.ok(list);
  }

}
