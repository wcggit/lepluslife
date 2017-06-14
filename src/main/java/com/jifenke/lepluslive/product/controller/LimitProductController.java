package com.jifenke.lepluslive.product.controller;


import com.jifenke.lepluslive.global.util.JsonUtils;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.order.service.OrderDetailService;
import com.jifenke.lepluslive.product.domain.entities.Product;
import com.jifenke.lepluslive.product.domain.entities.ProductDetail;
import com.jifenke.lepluslive.product.domain.entities.ProductSpec;
import com.jifenke.lepluslive.product.domain.entities.ProductType;
import com.jifenke.lepluslive.product.domain.entities.ScrollPicture;
import com.jifenke.lepluslive.product.service.ProductService;
import com.jifenke.lepluslive.product.service.ScrollPictureService;
import com.jifenke.lepluslive.score.service.ScoreCService;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
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
 * 爆品 Created by zhangwen on 16/9/18.
 */
@RestController
@RequestMapping("/front/product")
public class LimitProductController {

  @Inject
  private ProductService productService;

  @Inject
  private ScrollPictureService scrollPictureService;


  @Inject
  private ScoreCService scoreCService;

  @Inject
  private OrderDetailService orderDetailService;

  @Inject
  private WeiXinService weiXinService;

  /**
   * 公众号 商品首页 16/09/20
   */
  @RequestMapping(value = "/weixin/productIndex", method = RequestMethod.GET)
  public ModelAndView productIndex(HttpServletRequest request, Model model) {
    //商品分类
    List<ProductType> typeList = productService.findAllProductType();
    //主打爆品
//    Map product = productService.findMainHotProduct();
    model.addAttribute("scoreC", scoreCService
        .findScoreCByLeJiaUser(weiXinService.getCurrentWeiXinUser(request).getLeJiaUser()));
//    model.addAttribute("product", product);
    model.addAttribute("typeList", typeList);
    return MvUtil.go("/product/productIndex");
  }

  /**
   * 爆品详情页 16/09/19
   *
   * @param productId 商品id
   */
  @RequestMapping(value = "/weixin/limitDetail", method = RequestMethod.GET)
  public ModelAndView goBannerPage(HttpServletRequest request,
                                   @RequestParam(required = true) Long productId, Model model) {
    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
    Product product = null;
    List<ProductDetail> detailList = null;
    List<ProductSpec> specList = null;
    List<ScrollPicture> scrollPictureList = null;
    int canBuy = 1;
    if (productId != null) {
      product = productService.findOneProduct(productId);
    }
    if (product != null) {
      //查询某个用户已购买某个商品的数量 判断是否可以购买
      Integer
          payState =
          orderDetailService
              .getCurrentUserBuyProductCount(weiXinUser.getLeJiaUser().getId(), productId);
      if (product.getBuyLimit() != 0 && (payState >= product.getBuyLimit())) {
        canBuy = 0;
      }
      model.addAttribute("canBuy", canBuy);
      model.addAttribute("product", product);
      detailList = productService.findAllProductDetailsByProduct(product);
      specList = productService.findAllProductSpec(product);
      scrollPictureList = scrollPictureService.findAllByProduct(product);
      model.addAttribute("detailList", JsonUtils.objectToJson(detailList));
      model.addAttribute("specList", JsonUtils.objectToJson(specList));
      model.addAttribute("scrollList", scrollPictureList);
    }
    return MvUtil.go("/product/limitProductDetail");
  }

  /**
   * 获取秒杀列表  16/09/21
   *
   * @param page 当前页码
   */
  @RequestMapping(value = "/hotList", method = RequestMethod.GET)
  public LejiaResult productList(@RequestParam(required = true) Integer page) {
    if (page == null || page < 1) {
      page = 1;
    }
    List<Map> list = productService.findHotProductListByPage(page);
    return LejiaResult.ok(list);
  }

}
