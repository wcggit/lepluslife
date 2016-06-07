package com.jifenke.lepluslive.product.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.product.domain.entities.ProductRecommend;
import com.jifenke.lepluslive.product.service.ProductRecommendService;
import com.jifenke.lepluslive.weixin.domain.entities.Dictionary;
import com.jifenke.lepluslive.weixin.service.DictionaryService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.inject.Inject;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Created by zhangwen on 16/6/6.
 */
@RestController
@RequestMapping("/shop")
public class ProductRecommendController {

  @Inject
  private ProductRecommendService recommendService;

  @Inject
  private DictionaryService dictionaryService;

  @ApiOperation(value = "首页加载推荐商品列表1.1")
  @RequestMapping(value = "/recommend", method = RequestMethod.GET)
  public LejiaResult recommend(
      @ApiParam(value = "商品列表时间戳") @RequestParam(required = false) String timestamp) {

    Dictionary dictionary = dictionaryService.findDictionaryById(5L);
    if (timestamp != null) {
      if (timestamp.equals(dictionary.getValue())) {
        return LejiaResult.build(304, "equal");
      }
    }
    List<ProductRecommend> list = recommendService.findAllProductRecommend();

    return LejiaResult.build(200, dictionary.getValue(), list);
  }
}
