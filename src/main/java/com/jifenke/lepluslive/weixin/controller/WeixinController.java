package com.jifenke.lepluslive.weixin.controller;

import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.global.util.CookieUtils;
import com.jifenke.lepluslive.global.util.JsonUtils;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;
import com.jifenke.lepluslive.product.controller.dto.ProductDto;
import com.jifenke.lepluslive.product.domain.entities.Product;
import com.jifenke.lepluslive.product.domain.entities.ProductDetail;
import com.jifenke.lepluslive.product.domain.entities.ProductType;
import com.jifenke.lepluslive.product.domain.entities.ScrollPicture;
import com.jifenke.lepluslive.product.service.ProductService;
import com.jifenke.lepluslive.product.service.ScrollPictureService;
import com.jifenke.lepluslive.score.domain.entities.ScoreB;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.score.service.ScoreAService;
import com.jifenke.lepluslive.score.service.ScoreBService;
import com.jifenke.lepluslive.weixin.service.WeiXinService;
import com.jifenke.lepluslive.weixin.service.WeiXinUserService;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by wcg on 16/3/18.
 */
@RestController
@RequestMapping("/weixin")
public class WeixinController {

  private static Logger log = LoggerFactory.getLogger(WeixinController.class);

  @Value("${weixin.appId}")
  private String appId;

  @Value("${weixin.weixinRootUrl}")
  private String weixinRootUrl;

  @Inject
  private WeiXinUserService weiXinUserService;

  @Inject
  private WeiXinService weiXinService;

  @Inject
  private ProductService productService;

  @Inject
  private ScoreBService scoreBService;

  @Inject
  private ScoreAService scoreAService;

  @Inject
  private ScrollPictureService scrollPictureService;

  @Inject
  private LeJiaUserService leJiaUserService;


  @RequestMapping("/shop")
  public ModelAndView goProductPage(HttpServletRequest request, HttpServletResponse response,
                                    Model model) {
    model.addAttribute("productTypes", productService.findAllProductType());
    return MvUtil.go("/weixin/product");
  }

  @RequestMapping("/product/{id}")
  public ModelAndView goProductDetailPage(@PathVariable Long id, Model model,
                                          HttpServletRequest request,
                                          HttpServletResponse response) {
    Product product = productService.findOneProduct(id);
    ProductType productType = product.getProductType();
    List<ScrollPicture> scrollPictureList = scrollPictureService.findAllScorllPicture(product);
    List<ProductDetail>
        productDetails =
        productService.findAllProductDetailsByProduct(product);
    ProductDto productDto = new ProductDto();
    productDto.setProductSpecs(productService.findAllProductSpec(product));
    productDto.setFreePrice(Constants.FREIGHT_FREE_PRICE);
    try {
      BeanUtils.copyProperties(productDto, product);
      productDto.setScrollPictures(scrollPictureList.stream().map(scrollPicture -> {
        scrollPicture.setProduct(null);
        return scrollPicture;
      }).collect(
          Collectors.toList()));
      WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
      ScoreB scoreB = null;
      scoreB = scoreBService.findScoreBByWeiXinUser(weiXinUser.getLeJiaUser());
      model.addAttribute("product", productDto);
      model.addAttribute("scoreB", scoreB.getScore());
      model.addAttribute("productdetails", JsonUtils.objectToJson(productDetails));
      CookieUtils
          .setCookie(request, response, "currentType", productType.getId() + " " + product.getId(),
                     Constants.COOKIE_DISABLE_TIME);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return MvUtil.go("/weixin/productDetail");
  }


  @RequestMapping("/userRegister")
  public String userRegister(@RequestParam String action, @RequestParam String code,
                             HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    if (code == null) {
      return "禁止授权无法访问app";
    }
    Map<String, Object> map = weiXinService.getSnsAccessToken(code);
    String openid = map.get("openid").toString();
    new Thread(() -> {
      //获取accessToken与openid
      if (map.get("errcode") != null) {
        log.error(map.get("errcode").toString() + map.get("errmsg").toString());
      }
      WeiXinUser weiXinUser = weiXinUserService.findWeiXinUserByOpenId(openid);
      String accessToken = map.get("access_token").toString();
      //2种情况 当用户不存在时,当上次登录距离此次已经经过了3天
      if (weiXinUser == null || new Date(
          weiXinUser.getLastUserInfoDate().getTime() + 3 * 24 * 60 * 60 * 1000)
          .before(new Date())) {
        Map<String, Object> userDetail = weiXinService.getDetailWeiXinUser(accessToken, openid);
        if (userDetail.get("errcode") != null) {
          log.error(userDetail.get("errcode").toString() + userDetail.get("errmsg").toString());
        }
        try {
          weiXinUserService.saveWeiXinUser(userDetail, map);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }).start();
    try {
      CookieUtils.setCookie(request, response, appId + "-user-open-id", openid,
                            Constants.COOKIE_DISABLE_TIME);
      response.sendRedirect(action);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }


  @RequestMapping("/hongbao")
  public ModelAndView goHongbaoPage(HttpServletRequest request) {
    String openId = CookieUtils.getCookieValue(request, appId + "-user-open-id");
    WeiXinUser weiXinUser = weiXinUserService.findWeiXinUserByOpenId(openId);
    if (weiXinUser != null && weiXinUser.getHongBaoState() == 1) {
      return MvUtil.go("/weixin/hongbaoOpen");
    }
    return MvUtil.go("/weixin/hongbao");
  }

  @RequestMapping("/hongbao/open")
  public ModelAndView goHongbaoOpenPage(@RequestParam String phoneNumber,
                                        HttpServletRequest request, HttpServletResponse response,
                                        Model model) {
    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
    LeJiaUser leJiaUser = leJiaUserService.findUserByPhoneNumber(phoneNumber);  //是否已注册
    if (leJiaUser == null && weiXinUser.getHongBaoState() == 0) {
      weiXinUserService.openHongBao(weiXinUser,phoneNumber);
    }

    return MvUtil.go("/weixin/hongbaoOpen");
  }


  @RequestMapping("/user")
  public ModelAndView goUserPage(Model model, HttpServletRequest request,
                                 HttpServletResponse response) throws IOException {

    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
    if (weiXinUser.getLeJiaUser().getOneBarCodeUrl() == null) {
      weiXinUser = weiXinUserService.saveBarCodeForUser(weiXinUser);
    }
    model.addAttribute("scoreA", scoreAService.findScoreAByLeJiaUser(weiXinUser.getLeJiaUser()));
    model.addAttribute("user", weiXinUser);
    model.addAttribute("scoreB", scoreBService.findScoreBByWeiXinUser(weiXinUser.getLeJiaUser()));
    return MvUtil.go("/weixin/user");
  }


  @RequestMapping("/load")
  public ModelAndView load() {
    return MvUtil.go("/weixin/load");
  }

  @RequestMapping("/city")
  public ModelAndView goMerchantPage() {
    return MvUtil.go("/weixin/merchant");
  }

}
