package com.jifenke.lepluslive.product.service;

import com.jifenke.lepluslive.global.util.JsonUtils;
import com.jifenke.lepluslive.product.repository.ProductModuleRepository;
import com.jifenke.lepluslive.statistics.service.RedisCacheService;
import com.jifenke.lepluslive.weixin.domain.entities.Category;
import com.jifenke.lepluslive.weixin.service.CategoryService;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * 商品模块 Created by zhangwen on 2017/3/30.
 */
@Service
@Transactional(readOnly = true)
public class ProductModuleService {

  @Inject
  private ProductModuleRepository repository;

  @Inject
  private CategoryService categoryService;

  @Inject
  private EntityManager entityManager;

  @Inject
  private RedisCacheService redisCacheService;

  /**
   * 金币商城首页获取推荐数据 2017/3/30
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public String findList() {

    //先从redis缓存中查找，没有读数据库返回并写入redis缓存、设置有效期
    String key = "gold:module";
    String cache = redisCacheService.getByKey(key);
    if (cache != null) {
      return cache;
    }

    String
        sql =
        "SELECT m.module_id AS moduleId,m.picture AS pic,m.product_id AS pruductId,p.price AS price,p.`name` AS name FROM product_module m INNER JOIN product p ON m.product_id = p.id WHERE m.state = 1 ORDER BY m.rank DESC";

    Query query = entityManager.createNativeQuery(sql);
    //设置返回每行数据格式为MAP
    query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

    List<Map<String, Object>> list = query.getResultList();
    List<Category> categories = categoryService.findByCategoryAndState(4, 1);

    Map<String, Object> result = new HashMap<>();
    for (Category category : categories) {
      List<Map<String, Object>> list1 = new ArrayList<>();
      Map<String, Object> map = new HashMap<>();
      list.forEach(e -> {
        if (String.valueOf(e.get("moduleId")).equals(category.getId().toString())) {
          e.remove("moduleId");
          list1.add(e);
        }
      });
      list.removeAll(list1);
      map.put("title", category.getValue());
      map.put("detail", category.getTypeExplain());
      map.put("banner", category.getCategoryExplain());
      map.put("list", list1);
      result.put("key" + category.getType(), map);
    }

    String val = JsonUtils.objectToJson(result);
    redisCacheService.setStringValue(key, val, 300);

    return val;
  }

}
