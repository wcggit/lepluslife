package com.jifenke.lepluslive.product.repository;

import com.jifenke.lepluslive.product.domain.entities.Product;
import com.jifenke.lepluslive.product.domain.entities.ScrollPicture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.List;

import javax.persistence.QueryHint;

/**
 * Created by wcg on 16/3/11.
 */
public interface ScrollPictureRepository extends JpaRepository<ScrollPicture,Integer>{

  List<ScrollPicture> findAllBySidGreaterThan(Integer sid);


  List<ScrollPicture> findAllBySidIsGreaterThanEqual(Integer sid);

  @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
  List<ScrollPicture> findAllByProductOrderBySid(Product product);

}
