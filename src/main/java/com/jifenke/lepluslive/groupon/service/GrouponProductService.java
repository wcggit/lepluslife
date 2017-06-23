package com.jifenke.lepluslive.groupon.service;

import com.jifenke.lepluslive.groupon.domain.entities.GrouponProduct;
import com.jifenke.lepluslive.groupon.repository.GrouponProductRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * 团购商品
 * Created by zhangwen on 2017/6/16.
 */
@Service
@Transactional(readOnly = true)
public class GrouponProductService {

  @Inject
  private GrouponProductRepository repository;

  @Inject
  private GrouponScrollPictureService grouponScrollPictureService;

  @Inject
  private GrouponProductDetailService grouponProductDetailService;

  @Inject
  private GrouponMerchantService grouponMerchantService;

  public GrouponProduct findById(Long id) {
    return repository.findOne(id);
  }

  /**
   * 获取团购商品详情页信息  2017/6/19
   *
   * @param id     商品ID
   * @param status 是否获取到经纬度  1=是
   * @param lat    纬度
   * @param lon    经度
   */
  public Map<String, Object> detail(Long id,
                                    Integer status,
                                    Double lat,
                                    Double lon) {
    Map<String, Object> result = new HashMap<>();
    GrouponProduct product = repository.findOne(id);

    result.put("id", product.getId());
    result.put("name", product.getName());
    result.put("description", product.getDescription());
    result.put("sellVolume", product.getSellVolume());
    result.put("reservation", product.getReservation());
    result.put("refundType", product.getRefundType());
    result.put("rebateScorea", product.getRebateScorea());
    result.put("rebateScorec", product.getRebateScorec());
    result.put("explainPicture", product.getExplainPicture());
    result.put("instruction", product.getInstruction());
    result.put("originPrice", product.getOriginPrice());
    result.put("normalPrice", product.getNormalPrice());
    result.put("ljPrice", product.getLjPrice());
    result.put("normalStorage", product.getNormalStorage());
    result.put("ljStorage", product.getLjStorage());
    result.put("merchantName",
               grouponMerchantService.findMerchantUserName(product.getMerchantUser().getId()));
    //轮播图
    result.put("scrollPicList", grouponScrollPictureService.listSqlByGrouponProduct(id));
    //详情图
    result.put("detailPicList", grouponProductDetailService.listSqlByGrouponProduct(id));
    //可用门店
    result.put("shopList", grouponMerchantService.listSqlByGrouponProduct(id, status, lat, lon));

    return result;
  }

}
