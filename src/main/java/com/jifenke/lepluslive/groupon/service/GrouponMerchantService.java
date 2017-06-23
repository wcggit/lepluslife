package com.jifenke.lepluslive.groupon.service;

import com.jifenke.lepluslive.global.service.SqlService;
import com.jifenke.lepluslive.groupon.repository.GrouponMerchantRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * 团购产品对应门店
 * Created by zhangwen on 2017/6/16.
 */
@Service
@Transactional(readOnly = true)
public class GrouponMerchantService {

  @Inject
  private GrouponMerchantRepository repository;

  @Inject
  private SqlService sqlService;

  /**
   * 获取团购商品可用的门店列表  2017/6/19
   *
   * @param grouponProductId 商品ID
   * @param status           是否获取到经纬度  1=是
   * @param lat              纬度
   * @param lon              经度
   */
  public List listSqlByGrouponProduct(Long grouponProductId,
                                      Integer status,
                                      Double lat,
                                      Double lon) {
    //需要获取的信息：id,电话，名称，地址，经纬度，图片，距离
    StringBuffer sql = new StringBuffer();
    if (status == 1) {
      sql.append(
          "SELECT m.id AS merchantId,m.`name` AS name,m.picture AS picture,m.phone_number AS phone,m.location AS location,m.lat AS lat,m.lng AS lon,ROUND( 6378.138 * 2 * ASIN(SQRT(POW(SIN((")
          .append(lat).append(
          " * PI() / 180 - m.lat * PI() / 180) / 2),2) + COS(").append(lat).append(
          " * PI() / 180) * COS(m.lat * PI() / 180) * POW(SIN((").append(lon).append(
          " * PI() / 180 - m.lng * PI() / 180) / 2),2))) * 1000) AS distance FROM groupon_merchant g LEFT OUTER JOIN merchant m ON g.merchant_id = m.id WHERE groupon_product_id = ")
          .append(grouponProductId).append(" ORDER BY distance ASC");
    } else {
      sql.append(
          "SELECT m.id AS merchantId,m.`name` AS name,m.picture AS picture,m.phone_number AS phone,m.location AS location,m.lat AS lat,m.lng AS lon,0 AS distance FROM groupon_merchant g LEFT OUTER JOIN merchant m ON g.merchant_id = m.id WHERE groupon_product_id = ")
          .append(grouponProductId);
    }
    return sqlService.listBySql(sql.toString());
  }

  /**
   * 获取团购商品商户名称  2017/6/22
   *
   * @param merchantUserId 商户ID
   */
  public String findMerchantUserName(Long merchantUserId) {
    //需要获取的信息：id,电话，名称，地址，经纬度，图片，距离
    String sql = "SELECT merchant_name AS name FROM merchant_user WHERE id = " + merchantUserId;
    Object result = sqlService.StringBySql(sql);

    return String.valueOf(((Map) result).get("name"));
  }

}
