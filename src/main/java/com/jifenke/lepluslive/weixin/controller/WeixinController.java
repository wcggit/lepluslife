package com.jifenke.lepluslive.weixin.controller;

import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.global.util.CookieUtils;
import com.jifenke.lepluslive.global.util.JsonUtils;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerManager;
import com.jifenke.lepluslive.partner.service.PartnerManagerService;
import com.jifenke.lepluslive.partner.service.PartnerService;
import com.jifenke.lepluslive.product.controller.dto.ProductDto;
import com.jifenke.lepluslive.product.domain.entities.Product;
import com.jifenke.lepluslive.product.domain.entities.ProductDetail;
import com.jifenke.lepluslive.product.domain.entities.ProductType;
import com.jifenke.lepluslive.product.domain.entities.ScrollPicture;
import com.jifenke.lepluslive.product.service.ProductService;
import com.jifenke.lepluslive.product.service.ScrollPictureService;
import com.jifenke.lepluslive.score.domain.entities.ScoreB;
import com.jifenke.lepluslive.score.service.ScoreAService;
import com.jifenke.lepluslive.score.service.ScoreBService;
import com.jifenke.lepluslive.score.service.ScoreCService;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.service.WeiXinService;
import com.jifenke.lepluslive.weixin.service.WeiXinUserInfoService;
import com.jifenke.lepluslive.weixin.service.WeiXinUserService;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
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

  @Inject
  private WeiXinUserService weiXinUserService;

  @Inject
  private WeiXinService weiXinService;

  @Inject
  private ProductService productService;

  @Inject
  private ScoreBService scoreBService;

  @Inject
  private ScoreCService scoreCService;

  @Inject
  private ScoreAService scoreAService;

  @Inject
  private ScrollPictureService scrollPictureService;

  @Inject
  private LeJiaUserService leJiaUserService;

  @Inject
  private WeiXinUserInfoService weiXinUserInfoService;

  @Inject
  private PartnerService partnerService;

  @Inject
  private PartnerManagerService partnerManagerService;

  @RequestMapping("/shop")
  public ModelAndView goProductPage(@CookieValue String leJiaUnionId,
                                    Model model) {
    WeiXinUser weiXinUser = weiXinUserService.findWeiXinUserByUnionId(leJiaUnionId);
    ScoreB scoreB = scoreBService.findScoreBByWeiXinUser(weiXinUser.getLeJiaUser());
    //商品分类
    List<ProductType> typeList = productService.findAllProductType();
    //主打爆品
    Map product = productService.findMainHotProduct();
    model.addAttribute("scoreB", scoreB);
    model.addAttribute("product", product);
    model.addAttribute("typeList", typeList);
    return MvUtil.go("/product/productIndex");
  }

  @RequestMapping("/product/{id}")
  public ModelAndView goProductDetailPage(@PathVariable Long id, Model model,
                                          HttpServletRequest request,
                                          HttpServletResponse response) {
    Product product = productService.findOneProduct(id);
    ProductType productType = product.getProductType();
    List<ScrollPicture> scrollPictureList = scrollPictureService.findAllByProduct(product);
    List<ProductDetail>
        productDetails =
        productService.findAllProductDetailsByProduct(product);
    ProductDto productDto = new ProductDto();
    productDto.setProductSpecs(productService.findAllProductSpec(product));

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

    //获取accessToken与openid
    if (map.get("errcode") != null) {
      log.error(map.get("errcode").toString() + map.get("errmsg").toString());
    }

    if (map.get("openid") != null) {
      String openid = "" + map.get("openid");
      String unionId = "";
      WeiXinUser weiXinUser = weiXinUserService.findWeiXinUserByOpenId(openid);
      if (weiXinUser != null) {
        unionId = weiXinUser.getUnionId();
      }
      //2种情况 当用户不存在时,当上次登录距离此次已经经过了3天
      if (weiXinUser == null || new Date(
          weiXinUser.getLastUpdated().getTime() + 3 * 24 * 60 * 60 * 1000)
          .before(new Date())) {
        String accessToken = "" + map.get("access_token");
        Map<String, Object> userDetail = weiXinService.getDetailWeiXinUser(accessToken, openid);
        if (userDetail.get("errcode") != null) {
          log.error(userDetail.get("errcode").toString() + userDetail.get("errmsg").toString());
        } else {
          try {
            unionId = weiXinUserService.saveWeiXinUser(userDetail, map);
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
      try {
        CookieUtils.setCookie(request, response, "leJiaUnionId", unionId,
                              Constants.COOKIE_DISABLE_TIME);
        response.sendRedirect(action);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return null;
  }

  /**
   * 会员中心页面
   */
  @RequestMapping("/user")
  public ModelAndView goUserPage(Model model, HttpServletRequest request) {
    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
    LeJiaUser leJiaUser = weiXinUser.getLeJiaUser();
    model.addAttribute("scoreA", scoreAService.findScoreAByLeJiaUser(leJiaUser));
    model.addAttribute("user", weiXinUser);
    model.addAttribute("scoreC", scoreCService.findScoreCByLeJiaUser(leJiaUser));
//    model.addAttribute("check", weiXinUserInfoService.checkYdAndWarning(weiXinUser));
    return MvUtil.go("/user/index");
  }


  @RequestMapping("/load")
  public ModelAndView load() {
    return MvUtil.go("/weixin/load");
  }

  @RequestMapping("/partner/bind_wx_user/{sid}")
  public ModelAndView partnerBindUser(@PathVariable String sid, Model model,
                                      HttpServletRequest request) {
    Partner partner = partnerService.findPartnerBySid(sid);
    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
    model.addAttribute("partner", partner);
    model.addAttribute("partnerSid", sid + "?");
    model.addAttribute("openid", weiXinUser.getOpenId());
    model.addAttribute("weiXinUser", weiXinUser);
    if (partner.getWeiXinUser() == null) {
      if (partnerService.findPartnerByWeiXinUser(weiXinUser).isPresent()) {
        model.addAttribute("code", "1"); //该微信号已经绑定合伙人且不是当前合伙人
      } else {
        long partnerUserLimit = leJiaUserService.countPartnerBindLeJiaUser(partner.getId());
        Partner bindPartner = weiXinUser.getLeJiaUser().getBindPartner();
        if (bindPartner != null && bindPartner.getId().equals(partner.getId())) {//已经绑上无须在考虑绑定
          model.addAttribute("code", "4");
        } else {
          if (partner.getUserLimit() <= partnerUserLimit) {
            model.addAttribute("code", "2"); //名额不足
          } else {
            model.addAttribute("code", "4");//正常绑定
          }
        }
      }
    } else {
      // 如果已经绑上当前用户
      if (weiXinUser.getId().equals(partner.getWeiXinUser().getId())) {
        model.addAttribute("code", "5"); //该微信号已经绑定合伙人且是当前合伙人
      } else {
        model.addAttribute("code", "3");//该合伙人已绑定微信号且非该微信号
      }
    }
    return MvUtil.go("/weixin/partnerBind");
  }

  @RequestMapping(value = "/partner/bind/{sid}")
  public LejiaResult bindPartnerConfirm(@PathVariable String sid, HttpServletRequest request) {
    Partner partner = partnerService.findPartnerBySid(sid);
    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
    return partnerService.bindWeiXinUser(partner, weiXinUser) ? LejiaResult.ok()
                                                              : LejiaResult.build(201, "绑定名额已满");
  }


  @RequestMapping(value = "/partnerManager/bind/{sid}")
  public LejiaResult bindPartnerManagerConfirm(@PathVariable String sid, HttpServletRequest request) {
    PartnerManager partnerManager = partnerManagerService.findByPartnerManagerSid(sid);
    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
    return partnerManagerService.bindWeiXinUser(partnerManager, weiXinUser) ? LejiaResult.ok()
            : LejiaResult.build(201, "绑定账户异常");
  }

  @RequestMapping("/partnerManager/bind_wx_user/{sid}")
  public ModelAndView partnerManagerBindUser(@PathVariable String sid, Model model,
                                      HttpServletRequest request) {
    PartnerManager partnerManager = partnerManagerService.findByPartnerManagerSid(sid);
    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
    model.addAttribute("partnerManager", partnerManager);
    model.addAttribute("partnerManagerSid", sid + "?");
    model.addAttribute("openid", weiXinUser.getOpenId());
    model.addAttribute("weiXinUser", weiXinUser);
    if (partnerManager.getWeiXinUser() == null) {
      if (partnerManagerService.findByWeiXinUser(weiXinUser).isPresent()) {
        model.addAttribute("code", "1"); //该微信号已经绑定合伙人且不是当前合伙人
      } else {
        PartnerManager bindPartnerManager = weiXinUser.getLeJiaUser().getBindPartnerManager();
        if (bindPartnerManager != null && bindPartnerManager.getId().equals(partnerManager.getId())) {//已经绑上无须在考虑绑定
          model.addAttribute("code", "4");
        }
      }
    } else {
      // 如果已经绑上当前用户
      if (weiXinUser.getId().equals(partnerManager.getWeiXinUser().getId())) {
        model.addAttribute("code", "5"); //该微信号已经绑定合伙人且是当前合伙人
      } else {
        model.addAttribute("code", "3");//该合伙人已绑定微信号且非该微信号
      }
    }
    return MvUtil.go("/weixin/partnerManagerBind");
  }


}
