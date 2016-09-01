package com.jifenke.lepluslive.banner.controller;

import com.jifenke.lepluslive.banner.service.BannerService;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.weixin.service.DictionaryService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
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
  public
  @ResponseBody
  LejiaResult homeImage(@PathVariable Integer id) {
    List<Map> list = bannerService.findByType123(id);
    return LejiaResult.ok(list);
  }

  @ApiOperation(value = "当期好“店”推荐")
  @RequestMapping(value = "/newShop", method = RequestMethod.GET)
  public
  @ResponseBody
  LejiaResult newShop() {
    List<Map> list = bannerService.findByNewShop();
    return LejiaResult.ok(list);
  }

  @ApiOperation(value = "往期好“店”推荐")
  @RequestMapping(value = "/oldShop/{id}", method = RequestMethod.GET)
  public
  @ResponseBody
  LejiaResult oldShopByPage(@ApiParam(value = "第几页，从1开始") @PathVariable Integer id) {
    if (id == null || id < 1) {
      id = 1;
    }
    List<Map> list = bannerService.findByOldShop(id);
    return LejiaResult.ok(list);
  }

  @ApiOperation(value = "当期好“货”推荐")
  @RequestMapping(value = "/newProduct", method = RequestMethod.GET)
  public
  @ResponseBody
  LejiaResult newProduct() {
    List<Map> list = bannerService.findByNewProduct();
    return LejiaResult.ok(list);
  }

  @ApiOperation(value = "往期好“货”推荐")
  @RequestMapping(value = "/oldProduct/{id}", method = RequestMethod.GET)
  public
  @ResponseBody
  LejiaResult oldProductByPage(@ApiParam(value = "第几页，从1开始") @PathVariable Integer id) {
    if (id == null || id < 1) {
      id = 1;
    }
    List<Map> list = bannerService.findByOldProduct(id);
    return LejiaResult.ok(list);
  }

  @ApiOperation(value = "公告")
  @RequestMapping(value = "/notice", method = RequestMethod.GET)
  public
  @ResponseBody
  LejiaResult notice() {
    return LejiaResult.ok(dictionaryService.findDictionaryById(21L).getValue());
  }

  @ApiOperation(value = "热门关键词")
  @RequestMapping(value = "/hot/{id}", method = RequestMethod.GET)
  public
  @ResponseBody
  LejiaResult hotWord(@ApiParam(value = "城市Id") @PathVariable Long id) {
    String hotWord = bannerService.hotWord(id);
    return LejiaResult.ok(hotWord);
  }
}
