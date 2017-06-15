package com.jifenke.lepluslive.merchant.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.merchant.controller.dto.MerchantDto;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.service.MerchantService;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Created by wcg on 16/3/17.
 */
@RestController
@RequestMapping("/merchant")
public class MerchantController {

  @Inject
  private MerchantService merchantService;

  //分页 todo:待删除
  @RequestMapping(value = "/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public List<MerchantDto> findPageMerchant(
      @RequestParam(value = "page", required = false) Integer offset,
      @RequestParam(required = false) String cityName,
      @RequestParam(required = false) Integer status, @RequestParam(required = false) Long type,
      @RequestParam(required = false) Integer condition,
      @RequestParam(required = false) Integer partnership,
      @RequestParam(required = false) Double lat, @RequestParam(required = false) Double lon) {
    if (offset == null) {
      offset = 1;
    }
    return merchantService
        .findWxMerchantListByCustomCondition(status, lat, lon, offset, type, cityName,
                                             condition, partnership);
  }

  @ApiOperation(value = "商家列表")
  @RequestMapping(value = "/reload", method = RequestMethod.POST)
  public LejiaResult merchantList(
      @ApiParam(value = "是否获取到经纬度1=是,0=否") @RequestParam(required = true) Integer status,
      @ApiParam(value = "经度(保留六位小数)") @RequestParam(required = false) Double longitude,
      @ApiParam(value = "纬度(保留六位小数)") @RequestParam(required = false) Double latitude,
      @ApiParam(value = "第几页") @RequestParam(required = true) Integer page,
      @ApiParam(value = "商家类别") @RequestParam(required = false) Long type,
      @ApiParam(value = "城市id") @RequestParam(required = false) Long cityId,
      @ApiParam(value = "排序条件0=离我最近,2=送积分最多,3=评价最高") @RequestParam(required = false) Integer condition,
      @ApiParam(value = "搜索框输入的文字") @RequestParam(required = false) String value) {

    List<Map> merchantList = merchantService.findMerchantListByCustomCondition(status, latitude,
                                                                               longitude, page,
                                                                               type, cityId,
                                                                               condition, value);

    return LejiaResult.ok(merchantList);
  }

  @ApiOperation(value = "进入商家详情页")
  @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
  public LejiaResult detail(@ApiParam(value = "商家Id") @PathVariable Long id) {
    Map map = merchantService.findMerchant(id);
    return LejiaResult.build(200, "ok", map);
  }

  /**
   * 线下支付成功页面获取商家列表数据 16/09/13
   *
   * @param id 商家ID
   */
  @RequestMapping(value = "/payList/{id}", method = RequestMethod.GET)
  public LejiaResult payPageInfo(@PathVariable Long id) {
    Merchant merchant = merchantService.findMerchantById(id);
    List<Map> list = merchantService.findPaySuccessMerchantList(merchant);
    String status = "0";
    if (merchant.getLat() != null && merchant.getLat() != 0) {
      status = "1";
    }
    return LejiaResult.build(200, status, list);
  }

}
