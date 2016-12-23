package com.jifenke.lepluslive.banner.service;

import com.jifenke.lepluslive.banner.domain.criteria.BannerCriteria;
import com.jifenke.lepluslive.banner.domain.entities.Banner;
import com.jifenke.lepluslive.banner.domain.entities.BannerType;
import com.jifenke.lepluslive.banner.repository.BannerRepository;
import com.jifenke.lepluslive.merchant.domain.entities.City;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.service.CityService;
import com.jifenke.lepluslive.product.domain.entities.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
   * 9=首页轮播图,10=首页好店推荐,11=首页臻品推荐
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<Map> findHomePageByType(BannerCriteria bannerCriteria) {
    List<Map> mapList = new ArrayList<>();

    Sort sort = new Sort(Sort.Direction.ASC, "sid");
    Page page = bannerRepository.findAll(getWhereClause(bannerCriteria),
                                         new PageRequest(bannerCriteria.getOffset() - 1,
                                                         bannerCriteria.getPageSize(), sort));
    List<Banner> listb = page.getContent();
    if (listb.size() > 0) {
      for (Banner b : listb) {
        Map<String, Object> map = new HashMap<>();
        map.put("sid", b.getSid());
        map.put("picture", b.getPicture());
        map.put("afterType", b.getAfterType());
        map.put("url", b.getUrl());
        map.put("urlTitle", b.getUrlTitle());
        map.put("introduce", b.getIntroduce());
        Merchant merchant = b.getMerchant();
        if (merchant != null) {
          map.put("merchantId", merchant.getId());
          map.put("merchantName", merchant.getName());
        }
        Product product = b.getProduct();
        if (product != null) {
          map.put("productId", product.getId());
          map.put("productName", product.getName());
          map.put("productPrice", product.getPrice());
          map.put("productPriceTrue", product.getMinPrice());
          map.put("productScore", product.getMinScore());
        }
        mapList.add(map);
      }
    }

    return mapList;

  }

  private static Specification<Banner> getWhereClause(BannerCriteria criteria) {
    return new Specification<Banner>() {
      @Override
      public Predicate toPredicate(Root<Banner> r, CriteriaQuery<?> q,
                                   CriteriaBuilder cb) {
        Predicate predicate = cb.conjunction();
        if (criteria.getType() != null) {   //banner类型
          predicate.getExpressions().add(
              cb.equal(r.<BannerType>get("bannerType").get("id"), criteria.getType()));
        }
        predicate.getExpressions().add(//1上架、0下架
                                       cb.equal(r.get("status"), 1));

        return predicate;
      }
    };
  }

  /**
   * 当期好店推荐 16/09/14
   *
   * @param cityId 城市Id
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<Map> findByNewShop(Long cityId) {
    List<Map> mapList = new ArrayList<>();
    List<Object[]> list = null;
    City city = null;
    if (cityId != null) {
      city = cityService.findCityById(cityId);
    }
    if (city != null) {
      list = bannerRepository.findNewShopByCity(cityId);
    } else {
      list = bannerRepository.findByNewShop();
    }
    if (list != null) {
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
        map.put("afterType", o[8]);
        mapList.add(map);
      }
    }
    return mapList;
  }

  /**
   * 往期好店推荐 16/09/14
   *
   * @param cityId   城市ID
   * @param startNum 起始记录
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<Map> findByOldShop(Long cityId, Integer startNum) {
    List<Map> mapList = new ArrayList<>();
    List<Object[]> list = null;
    City city = null;
    if (cityId != null) {
      city = cityService.findCityById(cityId);
    }
    if (city != null) {
      list = bannerRepository.findOldShopByCity(cityId, (startNum - 1) * 10, 10);
    } else {
      list = bannerRepository.findByOldShop((startNum - 1) * 10, 10);
    }
    if (list != null) {
      for (Object[] o : list) {
        Map<String, Object> map = new HashMap<>();
        map.put("sid", o[0]);
        map.put("picture", o[1]);
        map.put("url", o[2]);
        map.put("merchantId", o[3]);
        map.put("urlTitle", o[4]);
        map.put("afterType", o[5]);
        mapList.add(map);
      }
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
