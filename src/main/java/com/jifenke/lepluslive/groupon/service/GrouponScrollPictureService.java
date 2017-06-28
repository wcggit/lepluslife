package com.jifenke.lepluslive.groupon.service;

import com.jifenke.lepluslive.global.service.SqlService;
import com.jifenke.lepluslive.groupon.repository.GrouponScrollPictureRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.inject.Inject;

/**
 * 轮播图
 * Created by zhangwen on 2017/6/16.
 */
@Service
@Transactional(readOnly = true)
public class GrouponScrollPictureService {

  @Inject
  private GrouponScrollPictureRepository repository;

  @Inject
  private SqlService sqlService;

  /**
   * 获取轮播图列表 2017/6/19
   *
   * @param grouponProductId 团购商品ID
   */
  public List listSqlByGrouponProduct(Long grouponProductId) {
    String
        sql =
        "SELECT sid,picture FROM groupon_scroll_picture WHERE groupon_product_id = "
        + grouponProductId + " ORDER BY sid ASC";
    return sqlService.listBySql(sql);
  }

}
