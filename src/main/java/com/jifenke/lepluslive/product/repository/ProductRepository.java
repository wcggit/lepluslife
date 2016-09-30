package com.jifenke.lepluslive.product.repository;

import com.jifenke.lepluslive.product.domain.entities.Product;
import com.jifenke.lepluslive.product.domain.entities.ProductType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by wcg on 16/3/9.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {


  Page<Product> findByStateAndProductTypeAndType(Pageable pageable, Integer state,
                                                 ProductType productType, Integer type);

  /**
   * 不分类 分页获取臻品列表 16/09/21
   */
  @Query(value = "SELECT p.id,p.name,p.price,p.min_price,p.min_score,p.picture,p.thumb,p.custom_sale,p.sale_num,p.hot_style,p.postage,p.buy_limit,t.type FROM product p,product_type t WHERE p.product_type_id=t.id AND p.state=1 AND p.type=1 ORDER BY p.sid ASC LIMIT ?1,?2", nativeQuery = true)
  List<Object[]> findProductListByPage(Integer startNum, Integer pageSize);

  /**
   * 分类  分页获取臻品列表 16/09/21
   */
  @Query(value = "SELECT p.id,p.name,p.price,p.min_price,p.min_score,p.picture,p.thumb,p.custom_sale,p.sale_num,p.hot_style,p.postage,p.buy_limit,t.type FROM product p,product_type t WHERE p.product_type_id=t.id AND p.state=1 AND p.type=1 AND t.id=?1 ORDER BY p.sid ASC LIMIT ?2,?3", nativeQuery = true)
  List<Object[]> findProductListByTypeAndPage(Integer typeId, Integer startNum, Integer pageSize);

  /**
   * 获取主打爆款 16/09/21
   */
  @Query(value = "SELECT p.id,p.name,p.price,p.min_price,p.min_score,p.picture,p.thumb,p.custom_sale,p.sale_num,p.hot_style,p.postage,p.buy_limit,SUM(s.repository) FROM product p,product_spec s WHERE s.product_id=p.id AND p.state=1 AND p.hot_style=1 AND p.type=2 GROUP BY p.id ORDER BY p.sid ASC", nativeQuery = true)
  List<Object[]> findMainHotProductList();

  /**
   * 分页获取爆款列表 16/09/21
   */
  @Query(value = "SELECT p.id,p.name,p.price,p.min_price,p.min_score,p.picture,p.thumb,p.custom_sale,p.sale_num,p.hot_style,p.postage,p.buy_limit,SUM(s.repository) FROM product p,product_spec s WHERE s.product_id=p.id AND p.state=1 AND p.type=2 GROUP BY p.id ORDER BY p.sid ASC LIMIT ?1,?2", nativeQuery = true)
  List<Object[]> findHotProductListByPage(Integer startNum, Integer pageSize);

  @Query(value = "select count(*) from product", nativeQuery = true)
  Long getTotalCount();

}
