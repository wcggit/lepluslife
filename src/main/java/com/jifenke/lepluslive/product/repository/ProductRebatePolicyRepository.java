package com.jifenke.lepluslive.product.repository;

import com.jifenke.lepluslive.product.domain.entities.Product;
import com.jifenke.lepluslive.product.domain.entities.ProductRebatePolicy;
import com.jifenke.lepluslive.product.domain.entities.ProductSpec;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 规格分润、返鼓励金规则
 * Created by zhangwen on 2017/6/26.
 */
public interface ProductRebatePolicyRepository extends JpaRepository<ProductRebatePolicy, Long> {

  ProductRebatePolicy findByProductSpec(ProductSpec productSpec);

  List<ProductRebatePolicy> findAllByProductSpecProduct(Product product);

}
