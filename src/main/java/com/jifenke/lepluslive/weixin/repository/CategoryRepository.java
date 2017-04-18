package com.jifenke.lepluslive.weixin.repository;

import com.jifenke.lepluslive.weixin.domain.entities.Category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 数据字典 Created by zhangwen on 17/3/30.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

  List<Category> findByCategory(Integer category);

  List<Category> findByCategoryAndState(Integer category,Integer state);

}
