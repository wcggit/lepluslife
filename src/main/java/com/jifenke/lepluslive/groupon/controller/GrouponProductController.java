package com.jifenke.lepluslive.groupon.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.groupon.service.GrouponProductDetailService;
import com.jifenke.lepluslive.groupon.service.GrouponProductService;
import com.jifenke.lepluslive.groupon.service.GrouponScrollPictureService;
import com.jifenke.lepluslive.weixin.service.WeiXinPayService;
import com.jifenke.lepluslive.weixin.service.WeiXinService;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * 团购商品
 * Created by zhangwen on 2017/6/19.
 */
@RestController
@RequestMapping("/groupon")
public class GrouponProductController {

  @Inject
  private GrouponProductService grouponProductService;

  @Inject
  private GrouponProductDetailService grouponProductDetailService;

  @Inject
  private GrouponScrollPictureService grouponScrollPictureService;

  @Inject
  private WeiXinPayService weiXinPayService;

  @Inject
  private WeiXinService weiXinService;

  /**
   * 团购商品详情页 WEB  2017/6/19
   *
   * @param id 商品ID
   */
  @RequestMapping("/weixin")
  public ModelAndView index(Model model, HttpServletRequest request, @RequestParam Long id) {
    model.addAttribute("id", id);
    model.addAttribute("p", grouponProductService.findById(id));
    model.addAttribute("state", weiXinService.getCurrentWeiXinUser(request).getState());
    //轮播图
    model.addAttribute("scrollPicList", grouponScrollPictureService.listSqlByGrouponProduct(id));
    model.addAttribute("wxConfig", weiXinPayService.getWeiXinPayConfig(request));
    return MvUtil.go("/groupon/product/detail");
  }

  /**
   * 团购商品图文详情页 WEB  2017/6/19
   *
   * @param id 商品ID
   */
  @RequestMapping("/picList")
  public ModelAndView detailPicList(Model model, @RequestParam Long id) {
    model
        .addAttribute("list", grouponProductDetailService.listSqlByGrouponProduct(id));
    return MvUtil.go("/groupon/product/picList");
  }

  /**
   * 获取团购商品详情页信息 APP  2017/6/19
   *
   * @param id     商品ID
   * @param status 是否获取到经纬度  1=是
   * @param lat    纬度
   * @param lon    经度
   */
  @RequestMapping(value = "/detail")
  public LejiaResult productInfo(@RequestParam Long id,
                                 @RequestParam Integer status,
                                 @RequestParam(required = false) Double lat,
                                 @RequestParam(required = false) Double lon) {

    return LejiaResult.ok(grouponProductService.detail(id, status, lat, lon));
  }

}
