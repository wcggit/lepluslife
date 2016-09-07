package com.jifenke.lepluslive.banner.service;

import com.jifenke.lepluslive.banner.domain.entities.Banner;
import com.jifenke.lepluslive.banner.repository.BannerRepository;
import com.jifenke.lepluslive.merchant.domain.entities.City;
import com.jifenke.lepluslive.merchant.service.CityService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * app广告轮播 Created by zhangwen on 16/8/26.
 */
@Service
@Transactional(readOnly = true)
public class BannerService {

  @Inject
  private BannerRepository bannerRepository;

  @Inject
  private CityService cityService;

  /**
   * 首页，臻品轮播，新品首发
   *
   * @param type 轮播图类型
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<Map> findByType123(Integer type) {
    List<Map> mapList = new ArrayList<>();
    List<Object[]> list = bannerRepository.findByType123(type);
    for (Object[] o : list) {
      Map<String, Object> map = new HashMap<>();
      map.put("sid", o[0]);
      map.put("picture", o[1]);
      map.put("afterType", o[2]);
      map.put("url", o[3]);
      map.put("merchantId", o[4]);
      map.put("productId", o[5]);
      map.put("urlTitle", o[6]);
      mapList.add(map);
    }
    return mapList;
  }

  /**
   * 当期好店推荐
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<Map> findByNewShop() {
    List<Map> mapList = new ArrayList<>();
    List<Object[]> list = bannerRepository.findByNewShop();
    for (Object[] o : list) {
      Map<String, Object> map = new HashMap<>();
      map.put("sid", o[0]);
      map.put("picture", o[1]);
      map.put("url", o[2]);
      map.put("merchantId", o[3]);
      map.put("title", o[4]);
      map.put("content", o[5]);
      map.put("shopName", o[6]);
      map.put("urlTitle", o[7]);
      mapList.add(map);
    }
    return mapList;
  }

  /**
   * 往期好店推荐
   *
   * @param startNum 起始记录
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<Map> findByOldShop(Integer startNum) {
    List<Map> mapList = new ArrayList<>();
    List<Object[]> list = bannerRepository.findByOldShop((startNum - 1) * 10, 10);
    for (Object[] o : list) {
      Map<String, Object> map = new HashMap<>();
      map.put("sid", o[0]);
      map.put("picture", o[1]);
      map.put("url", o[2]);
      map.put("merchantId", o[3]);
      map.put("urlTitle", o[4]);
      mapList.add(map);
    }
    return mapList;
  }

  /**
   * 当期好货推荐
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<Map> findByNewProduct() {
    List<Map> mapList = new ArrayList<>();
    List<Object[]> list = bannerRepository.findByNewProduct();
    for (Object[] o : list) {
      Map<String, Object> map = new HashMap<>();
      map.put("sid", o[0]);
      map.put("picture", o[1]);
      map.put("afterType", o[2]);
      map.put("url", o[3]);
      map.put("productId", o[4]);
      map.put("title", o[5]);
      map.put("content", o[6]);
      map.put("price", o[7]);
      map.put("urlTitle", o[8]);
      mapList.add(map);
    }
    return mapList;
  }

  /**
   * 往期好货推荐
   *
   * @param startNum 起始记录
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<Map> findByOldProduct(Integer startNum) {
    List<Map> mapList = new ArrayList<>();
    List<Object[]> list = bannerRepository.findByOldProduct((startNum - 1) * 10, 10);
    for (Object[] o : list) {
      Map<String, Object> map = new HashMap<>();
      map.put("sid", o[0]);
      map.put("picture", o[1]);
      map.put("afterType", o[2]);
      map.put("url", o[3]);
      map.put("productId", o[4]);
      map.put("title", o[5]);
      map.put("content", o[6]);
      map.put("price", o[7]);
      map.put("urlTitle", o[8]);
      mapList.add(map);
    }
    return mapList;
  }

  /**
   * 往期好货推荐
   *
   * @param id 城市id
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public String hotWord(Long id) {
    //获取所在城市
    City city = cityService.findCityById(id);
    if (city == null) {
      city = new City();
      city.setId(1L);
    }
    //获取该城市的热门关键词集合
    List<Banner> list = bannerRepository.findByCityOrderByCreateDateDesc(city);
    String hot = "";
    for (Banner banner : list) {
      hot += banner.getTitle() + "_";
    }
    return hot;
  }

}
