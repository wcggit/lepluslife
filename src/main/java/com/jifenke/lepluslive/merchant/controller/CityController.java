package com.jifenke.lepluslive.merchant.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.merchant.domain.entities.Area;
import com.jifenke.lepluslive.merchant.domain.entities.City;
import com.jifenke.lepluslive.merchant.service.CityService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

import java.util.List;

/**
 * Created by wcg on 16/3/17.
 */
@RestController
@RequestMapping("/city")
public class CityController {

  @Inject
  private CityService cityService;

  @ApiOperation(value = "获取所有的城市列表")
  @RequestMapping(value = "/list", method = RequestMethod.POST)
  public
  @ResponseBody
  LejiaResult list(@ApiParam(value = "第几页") @RequestParam(required = false) Integer page) {
//    List<City> cities = cityService.findCitiesByPage(PaginationUtil.generatePageRequest(page, 10));
    List<City> cities = cityService.findAllCity();
    return LejiaResult.build(200, "ok", cities);
  }

  @ApiOperation(value = "获取某个城市的地区列表")
  @RequestMapping(value = "/areas", method = RequestMethod.POST)
  public
  @ResponseBody
  LejiaResult areas(@ApiParam(value = "城市Id") @RequestParam(required = false) Long id) {
    List<Area> areaList = cityService.findAreaListByCity(id);
    return LejiaResult.build(200, "ok", areaList);
  }

  @ApiOperation(value = "获取热门城市列表")
  @RequestMapping(value = "/city/hotList", method = RequestMethod.GET)
  public LejiaResult hotList() {
    return LejiaResult.build(200, "ok", cityService.findHotCity());
  }
}
