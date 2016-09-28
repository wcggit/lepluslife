package com.jifenke.lepluslive.product.repository;

import com.jifenke.lepluslive.product.domain.entities.ProductType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.List;

import javax.persistence.QueryHint;

/**
 * Created by wcg on 16/3/11.
 */
public interface ProductTypeRepository extends JpaRepository<ProductType, Integer> {

  @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
  List<ProductType> findAll();

}
