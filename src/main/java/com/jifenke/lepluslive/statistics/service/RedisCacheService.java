package com.jifenke.lepluslive.statistics.service;

import com.jifenke.lepluslive.global.util.JsonUtils;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * redis缓存 Created by zhangwen on 2017/3/30.
 */
@Service
public class RedisCacheService {

  @Inject
  private RedisTemplate<String, String> redisTemplate;

  @Resource(name = "stringRedisTemplate")
  private ValueOperations<String, String> valueOperations;

  @Inject
  private EntityManager entityManager;

  /**
   * 获取字符类型的key对应的值  2017/03/30
   *
   * @param key key
   * @return value
   */
  public String getByKey(String key) {

    return valueOperations.get(key);
  }

  /**
   * 设置字符类型的key,value,并设置过期时间  2017/03/30
   *
   * @param key     key
   * @param value   value
   * @param timeout 有效期 =0 永久有效
   */
  public void setStringValue(String key, String value, long timeout) {
    valueOperations.set(key, value);
    if (timeout != 0) {
      redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }
  }

  /**
   * 获取各种json数据并缓存 2017/3/30
   */
  @SuppressWarnings(value = "unchecked")
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public String findBySqlAndCache(String sql, String key, long timeout) {

    //先从redis缓存中查找，没有读数据库返回并写入redis缓存、设置有效期
    String cache = valueOperations.get(key);
    if (cache != null) {
      return cache;
    }

    Query query = entityManager.createNativeQuery(sql);
    //设置返回每行数据格式为MAP
    query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

    List<Map<String, Object>> list = query.getResultList();

    String val = JsonUtils.objectToJson(list);
    //保存到redis并设置缓存时间
    setStringValue(key, val, timeout);

    return val;
  }

  /**
   * 获取各种List数据 2017/4/7
   */
  @SuppressWarnings(value = "unchecked")
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<Map<String, Object>> findBySql(String sql) {

    Query query = entityManager.createNativeQuery(sql);
    query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
    return query.getResultList();
  }

}
