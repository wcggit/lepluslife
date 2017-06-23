package com.jifenke.lepluslive.groupon.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.groupon.service.GrouponMerchantService;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;

/**
 * 可用门店列表
 * Created by zhangwen on 2017/6/19.
 */
@RestController
@RequestMapping("/groupon")
public class GrouponMerchantController {

  @Inject
  private GrouponMerchantService grouponMerchantService;


  /**
   * 获取团购商品可使用的商家列表数据 WEB  2017/6/22
   *
   * @param id     商品ID
   * @param status 是否获取到经纬度  1=是
   * @param lat    纬度
   * @param lon    经度
   */
  @RequestMapping(value = "/ajaxMerchant", method = RequestMethod.POST)
  public LejiaResult merchantList(@RequestParam Long id,
                                  @RequestParam Integer status,
                                  @RequestParam(required = false) Double lat,
                                  @RequestParam(required = false) Double lon) {

    return LejiaResult.ok(grouponMerchantService.listSqlByGrouponProduct(id, status, lat, lon));
  }


  /**
   * 团购商品可用门店列表页 WEB  2017/6/19
   *
   * @param id     商品ID
   * @param mId    商户ID
   * @param status 是否获取到经纬度  1=是
   * @param lat    纬度
   * @param lon    经度
   */
  @RequestMapping(value = "/merchantList", method = RequestMethod.GET)
  public ModelAndView merchantList(Model model, @RequestParam Long id,
                                   @RequestParam Long mId,
                                   @RequestParam Integer status,
                                   @RequestParam(required = false) Double lat,
                                   @RequestParam(required = false) Double lon) {
    model.addAttribute("status", status);
    model.addAttribute("name", grouponMerchantService.findMerchantUserName(mId));
    model
        .addAttribute("list", grouponMerchantService.listSqlByGrouponProduct(id, status, lat, lon));
    return MvUtil.go("/groupon/product/merchantList");
  }
}
