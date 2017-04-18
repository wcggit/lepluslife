package com.jifenke.lepluslive.weixin.service;

import com.jifenke.lepluslive.weixin.domain.entities.Category;
import com.jifenke.lepluslive.weixin.repository.CategoryRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.inject.Inject;

/**
 * 数据字典 Created by zhangwen on 2017/3/30.
 */
@Service
@Transactional(readOnly = true)
public class CategoryService {

  @Inject
  private CategoryRepository repository;

  public List<Category> findByCategory(Integer category) {

    return repository.findByCategory(category);

  }

  public List<Category> findByCategoryAndState(Integer category, Integer state) {

    return repository.findByCategoryAndState(category, state);

  }

}
