package com.jifenke.lepluslive.product.repository;

import com.jifenke.lepluslive.product.domain.entities.ProductType;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by wcg on 16/3/11.
 */
public interface ProductTypeRepository extends JpaRepository<ProductType, Integer> {

  List<ProductType> findAll();

}
