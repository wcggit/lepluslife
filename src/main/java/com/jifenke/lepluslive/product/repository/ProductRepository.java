package com.jifenke.lepluslive.product.repository;

import com.jifenke.lepluslive.product.domain.entities.Product;
import com.jifenke.lepluslive.product.domain.entities.ProductType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by wcg on 16/3/9.
 */
public interface ProductRepository extends JpaRepository<Product,Long>{

  
  Page<Product> findByStateAndProductType(Pageable pageable,Integer state,ProductType productType);

  @Query(value="select count(*) from product", nativeQuery=true)
  Long getTotalCount();

}
