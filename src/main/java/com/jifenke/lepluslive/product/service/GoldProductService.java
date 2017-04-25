package com.jifenke.lepluslive.product.service;

import com.jifenke.lepluslive.product.repository.ProductRepository;
import com.jifenke.lepluslive.statistics.service.RedisCacheService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * 金币商品 Created by zhangwen on 2017/2/20.
 */
@Service
@Transactional(readOnly = true)
public class GoldProductService {

  @Inject
  private ProductRepository productRepository;

  @Inject
  private RedisCacheService redisCacheService;

  /**
   * 分页获取金币商品列表 2017/4/1
   *
   * @param currPage 第几页
   * @param pageSize 每页获取数量
   * @param type     类别  0=所有分类
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public String findHotProductListByPage(Integer currPage, Integer pageSize, Integer type) {
    String sql;
    String key = "gold:list:" + type + ":" + currPage;
    if (type == 0) {
      sql =
          "SELECT id,`name`,price,picture,mark_id AS markType FROM product WHERE type=4 AND state=1 ORDER BY sid DESC LIMIT "
          + (currPage - 1) * 10 + "," + pageSize;
    } else {
      sql =
          "SELECT id,`name`,price,picture,mark_id AS markType FROM product WHERE type=4 AND state=1 AND product_type_id = "
          + type + " ORDER BY sid DESC LIMIT "
          + (currPage - 1) * 10 + "," + pageSize;
    }
    long timeout = 600;
    return redisCacheService.findBySqlAndCache(sql, key, timeout);
  }

}
