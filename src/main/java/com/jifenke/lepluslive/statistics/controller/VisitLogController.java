package com.jifenke.lepluslive.statistics.controller;

import com.jifenke.lepluslive.global.util.CookieUtils;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.statistics.domain.entities.VisitLog;
import com.jifenke.lepluslive.statistics.service.VisitLogRedisService;
import com.jifenke.lepluslive.statistics.service.VisitLogService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * 统计 Created by zhangwen on 2017/3/9.
 */
@RestController
@RequestMapping("/front/visit")
public class VisitLogController {

  private static Logger log = LoggerFactory.getLogger(VisitLogController.class);

  @Inject
  private VisitLogService visitLogService;

  @Inject
  private VisitLogRedisService redisService;


  /**
   * 金币商品详情页统计 17/3/9
   */
  @RequestMapping(value = "/product/{productId}", method = RequestMethod.GET)
  public LejiaResult submit(HttpServletRequest request, @PathVariable String productId) {
    String unionId = CookieUtils.getCookieValue(request, "leJiaUnionId");
    if (unionId == null) {
      unionId = "null";
    }
    log.debug("unionId=" + unionId + "&&productId=" + productId);
    visitLogService.saveLog(new VisitLog(unionId, "product", productId, "goldIndex"));

    redisService.addClickLog(unionId, "product:", "productId:" + productId);

    return LejiaResult.ok();
  }

}
