package com.jifenke.lepluslive.product.repository;

import com.jifenke.lepluslive.product.domain.entities.Product;
import com.jifenke.lepluslive.product.domain.entities.ProductDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.List;

import javax.persistence.QueryHint;

/**
 * Created by wcg on 16/3/28.
 */
public interface ProductDetailRepository extends JpaRepository<ProductDetail,Integer> {

  @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
  List<ProductDetail> findAllByProductOrderBySid(Product product);
}
