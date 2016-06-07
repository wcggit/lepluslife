package com.jifenke.lepluslive.product.repository;

import com.jifenke.lepluslive.product.domain.entities.ProductRecommend;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/**
 * Created by wcg on 16/3/25.
 */
public interface ProductRecommendRepository extends JpaRepository<ProductRecommend, Integer> {

  List<ProductRecommend> findAllByStateOrderBySidAsc(Integer state);
}
