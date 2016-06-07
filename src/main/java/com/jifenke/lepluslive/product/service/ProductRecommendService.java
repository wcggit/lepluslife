package com.jifenke.lepluslive.product.service;

import com.jifenke.lepluslive.product.domain.entities.ProductRecommend;
import com.jifenke.lepluslive.product.repository.ProductRecommendRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.inject.Inject;

/**
 * 商品推荐管理 Created by zhangwen on 2016/6/6.
 */
@Service
@Transactional(readOnly = true)
public class ProductRecommendService {

  @Inject
  private ProductRecommendRepository recommendRepository;

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<ProductRecommend> findAllProductRecommend() {

    return recommendRepository.findAllByStateOrderBySidAsc(1);
  }

}
