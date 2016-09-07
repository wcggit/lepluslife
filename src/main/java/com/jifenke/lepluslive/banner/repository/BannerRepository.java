package com.jifenke.lepluslive.banner.repository;

import com.jifenke.lepluslive.banner.domain.entities.Banner;
import com.jifenke.lepluslive.merchant.domain.entities.City;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


/**
 * Created by wcg on 16/3/24.
 */
public interface BannerRepository extends JpaRepository<Banner, Long> {

  //首页，臻品轮播，新品首发
  @Query(value = "SELECT b.sid,b.picture,b.after_type,b.url,b.merchant_id,b.product_id,b.url_title FROM banner b,banner_type t WHERE b.banner_type_id = t.id AND t.id = ?1 AND b.`status` =1 ORDER BY b.sid ASC", nativeQuery = true)
  List<Object[]> findByType123(int type);

  //当期好店推荐
  @Query(value = "SELECT b.sid,b.picture,b.url,b.merchant_id,b.title,b.introduce,m.`name`,b.url_title FROM banner b,banner_type t,merchant m WHERE b.banner_type_id = t.id AND b.merchant_id = m.id AND t.id = 5 AND b.`status` =1 AND b.alive = 1 ORDER BY b.sid ASC", nativeQuery = true)
  List<Object[]> findByNewShop();

  //往期好店推荐
  @Query(value = "SELECT b.sid,b.old_picture,b.url,b.merchant_id,b.url_title FROM banner b,banner_type t WHERE b.banner_type_id = t.id AND t.id = 5 AND b.`status` =1 AND b.alive = 0 ORDER BY b.sid ASC LIMIT ?1,?2", nativeQuery = true)
  List<Object[]> findByOldShop(Integer startNum, Integer pageSize);

  //当期好货推荐
  @Query(value = "SELECT b.sid,b.picture,b.after_type,b.url,b.product_id,b.title,b.introduce,b.price,b.url_title FROM banner b,banner_type t WHERE b.banner_type_id = t.id AND t.id = 4 AND b.`status` =1 AND b.alive = 1 ORDER BY b.sid ASC", nativeQuery = true)
  List<Object[]> findByNewProduct();

  //往期好货推荐
  @Query(value = "SELECT b.sid,b.picture,b.after_type,b.url,b.product_id,b.title,b.introduce,b.price,b.url_title FROM banner b,banner_type t WHERE b.banner_type_id = t.id AND t.id = 4 AND b.`status` =1 AND b.alive = 0 ORDER BY b.sid ASC LIMIT ?1,?2", nativeQuery = true)
  List<Object[]> findByOldProduct(Integer startNum, Integer pageSize);

  Page findAll(Specification<Banner> whereClause, Pageable pageRequest);

  List<Banner> findByCityOrderByCreateDateDesc(City city);

}
