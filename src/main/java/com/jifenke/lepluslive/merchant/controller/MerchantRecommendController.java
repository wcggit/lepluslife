package com.jifenke.lepluslive.merchant.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantRecommend;
import com.jifenke.lepluslive.merchant.service.MerchantRecommendService;
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
 * 商家首页推荐 Created by zhangwen on 16/6/6.
 */
@RestController
@RequestMapping("/merchant")
public class MerchantRecommendController {

  @Inject
  private MerchantRecommendService recommendService;

  @Inject
  private DictionaryService dictionaryService;

  @ApiOperation(value = "首页加载推荐商家列表1.1")
  @RequestMapping(value = "/recommend", method = RequestMethod.GET)
  public LejiaResult recommend(
      @ApiParam(value = "商家列表时间戳") @RequestParam(required = false) String timestamp) {

    Dictionary dictionary = dictionaryService.findDictionaryById(6L);
    if (timestamp != null) {
      if (timestamp.equals(dictionary.getValue())) {
        return LejiaResult.build(304, "equal");
      }
    }
    List<MerchantRecommend> list = recommendService.findAllMerchantRecommend();

    return LejiaResult.build(200, dictionary.getValue(), list);
  }


}
