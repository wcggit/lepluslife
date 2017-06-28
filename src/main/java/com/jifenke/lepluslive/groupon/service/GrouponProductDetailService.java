package com.jifenke.lepluslive.groupon.service;

import com.jifenke.lepluslive.global.service.SqlService;
import com.jifenke.lepluslive.groupon.repository.GrouponProductDetailRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.inject.Inject;

/**
 * 图文详情
 * Created by zhangwen on 2017/6/16.
 */
@Service
@Transactional(readOnly = true)
public class GrouponProductDetailService {

  @Inject
  private GrouponProductDetailRepository repository;

  @Inject
  private SqlService sqlService;

  /**
   * 获取详情图列表 2017/6/19
   *
   * @param grouponProductId 团购商品ID
   */
  public List listSqlByGrouponProduct(Long grouponProductId) {
    String
        sql =
        "SELECT sid,picture FROM groupon_product_detail WHERE groupon_product_id = "
        + grouponProductId + " ORDER BY sid ASC";
    return sqlService.listBySql(sql);
  }

}
