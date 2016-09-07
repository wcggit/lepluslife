package com.jifenke.lepluslive.merchant.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.merchant.controller.dto.MerchantDto;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantScroll;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.weixin.service.WeiXinService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by wcg on 16/3/17.
 */
@RestController
@RequestMapping("/merchant")
public class MerchantController {

  @Inject
  private MerchantService merchantService;

  @Inject
  private WeiXinService weiXinService;

  @RequestMapping("/index")
  public ModelAndView goMerchantPage(HttpServletRequest request, Model model) {
    model.addAttribute("wxConfig", weiXinService.getWeiXinConfig(request));
    return MvUtil.go("/weixin/merchant");
  }

  @RequestMapping("/type")
  public ModelAndView goMerchantTypePage(@RequestParam(required = false) String cityName,
                                         @RequestParam(required = false) Integer status,
                                         @RequestParam(required = false) Integer condition,
                                         @RequestParam(required = false) Long type,
                                         @RequestParam(required = false) Double lat,
                                         @RequestParam(required = false) Double lon, Model model) {
    model.addAttribute("cityName", cityName);
    if (condition == null) {
      condition = 0;
    }
    model.addAttribute("condition", condition);
    model.addAttribute("status", status);
    model.addAttribute("type", type);
    model.addAttribute("lat", lat);
    model.addAttribute("lon", lon);
    return MvUtil.go("/weixin/merchantType");
  }

  //分页
  @RequestMapping(value = "/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public
  @ResponseBody
  List<MerchantDto> findPageMerchant(
      @RequestParam(value = "page", required = false) Integer offset,
      @RequestParam(required = false) String cityName,
      @RequestParam(required = false) Integer status, @RequestParam(required = false) Long type,
      @RequestParam(required = false) Integer condition,
      @RequestParam(required = false) Integer partnership,
      @RequestParam(required = false) Double lat, @RequestParam(required = false) Double lon) {
//    return merchantService.findMerchantsByPage(offset);
    if (offset == null) {
      offset = 1;
    }
    List<MerchantDto>
        merchantDtoList =
        merchantService
            .findWxMerchantListByCustomCondition(status, lat, lon, offset, type, cityName,
                                                 condition, partnership);
    return merchantDtoList;
  }

  @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
  public ModelAndView goMerchantPage(HttpServletRequest request, Model model,
                                     @PathVariable Long id,
                                     @RequestParam(required = false) String distance,
                                     @RequestParam(required = false) Integer status) {
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

  @ApiOperation(value = "APP商家列表")
  @RequestMapping(value = "/reload", method = RequestMethod.POST)
  public
  @ResponseBody
  LejiaResult merchantList(
      @ApiParam(value = "是否获取到经纬度1=是,0=否") @RequestParam(required = true) Integer status,
      @ApiParam(value = "经度(保留六位小数)") @RequestParam(required = false) Double longitude,
      @ApiParam(value = "纬度(保留六位小数)") @RequestParam(required = false) Double latitude,
      @ApiParam(value = "第几页") @RequestParam(required = true) Integer page,
      @ApiParam(value = "商家类别") @RequestParam(required = false) Long type,
      @ApiParam(value = "城市id") @RequestParam(required = false) Long cityId,
      @ApiParam(value = "排序条件0=离我最近,2=送红包最多,3=评价最高") @RequestParam(required = false) Integer condition,
      @ApiParam(value = "搜索框输入的文字") @RequestParam(required = false) String value) {

    List<Map> merchantList = merchantService.findMerchantListByCustomCondition(status, latitude,
                                                                               longitude, page,
                                                                               type, cityId,
                                                                               condition, value);

    return LejiaResult.ok(merchantList);
  }

  @ApiOperation(value = "进入商家详情页")
  @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
  public
  @ResponseBody
  LejiaResult detail(@ApiParam(value = "商家Id") @PathVariable Long id) {
    Map map = merchantService.findMerchant(id);
    return LejiaResult.build(200, "ok", map);
  }

}
