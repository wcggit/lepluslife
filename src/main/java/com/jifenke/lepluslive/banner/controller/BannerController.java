package com.jifenke.lepluslive.banner.controller;

import com.jifenke.lepluslive.banner.domain.criteria.BannerCriteria;
import com.jifenke.lepluslive.banner.service.BannerService;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.weixin.service.DictionaryService;

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
 * 各种轮播图 Created by zhangwen on 16/8/26.
 */
@RestController
@RequestMapping("/app/banner")
public class BannerController {

  @Inject
  private BannerService bannerService;

  @Inject
  private DictionaryService dictionaryService;

  @ApiOperation(value = "1=首页推荐,2=臻品轮播图,3=新品首发")
  @RequestMapping(value = "/home/{id}", method = RequestMethod.GET)
  public LejiaResult homeImage(@PathVariable Integer id) {
    List<Map> list = bannerService.findByType123(id);
    return LejiaResult.ok(list);
  }

  @ApiOperation(value = "9=首页轮播图,10=首页好店推荐,11=首页臻品推荐")
  @RequestMapping(value = "/homePage/{id}", method = RequestMethod.GET)
  public LejiaResult homePage(@PathVariable Integer id) {

    BannerCriteria bc = new BannerCriteria();
    bc.setType(id);//bannerType
    bc.setStatus(1);//状态   1=正常  0=下架
    bc.setPageSize(3);//每页数量
    bc.setOffset(1);//起始页
    List<Map> list = bannerService.findHomePageByType(bc);
    return LejiaResult.ok(list);
  }



  @ApiOperation(value = "当期好“店”推荐")
  @RequestMapping(value = "/newShop", method = RequestMethod.GET)
  public LejiaResult newShop(@RequestParam(required = false) Long cityId) {
    List<Map> list = bannerService.findByNewShop(cityId);
    return LejiaResult.ok(list);
  }

  @ApiOperation(value = "往期好“店”推荐")
  @RequestMapping(value = "/oldShop", method = RequestMethod.GET)
  public LejiaResult oldShopByPage(
      @ApiParam(value = "第几页，从1开始") @RequestParam(required = true) Integer page,
      @ApiParam(value = "城市id") @RequestParam(required = false) Long cityId) {
    if (page == null || page < 1) {
      page = 1;
    }
    List<Map> list = bannerService.findByOldShop(cityId, page);
    return LejiaResult.ok(list);
  }

  @ApiOperation(value = "当期好“货”推荐")
  @RequestMapping(value = "/newProduct", method = RequestMethod.GET)
  public LejiaResult newProduct() {
    List<Map> list = bannerService.findByNewProduct();
    return LejiaResult.ok(list);
  }

  @ApiOperation(value = "往期好“货”推荐")
  @RequestMapping(value = "/oldProduct/{id}", method = RequestMethod.GET)
  public LejiaResult oldProductByPage(@ApiParam(value = "第几页，从1开始") @PathVariable Integer id) {
    if (id == null || id < 1) {
      id = 1;
    }
    List<Map> list = bannerService.findByOldProduct(id);
    return LejiaResult.ok(list);
  }

  @ApiOperation(value = "公告")
  @RequestMapping(value = "/notice", method = RequestMethod.GET)
  public LejiaResult notice() {
    return LejiaResult.ok(dictionaryService.findDictionaryById(21L).getValue());
  }

  @ApiOperation(value = "热门关键词")
  @RequestMapping(value = "/hot/{id}", method = RequestMethod.GET)
  public LejiaResult hotWord(@ApiParam(value = "城市Id") @PathVariable Long id) {
    String hotWord = bannerService.hotWord(id);
    return LejiaResult.ok(hotWord);
  }

  @ApiOperation(value = "好店推荐页顶部图片")
  @RequestMapping(value = "/niceShopImage", method = RequestMethod.GET)
  public LejiaResult findNiceShopImage() {
    return LejiaResult.ok(dictionaryService.findDictionaryById(26L).getValue());
  }

  @ApiOperation(value = "通用获取数据接口")
  @RequestMapping(value = "/common/{id}", method = RequestMethod.GET)
  public LejiaResult common(@PathVariable Long id) {
    return LejiaResult.ok(dictionaryService.findDictionaryById(id).getValue());
  }

}
