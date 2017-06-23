package com.jifenke.lepluslive.global.service;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * sql查询
 * Created by zhangwen on 2017/6/19.
 */
@Service
public class SqlService {

  @Inject
  private EntityManager em;

  /**
   * 获取list 2017/6/19
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List listBySql(String sql) {

    Query query = em.createNativeQuery(sql);
    //设置返回每行数据格式为MAP
    query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

    return query.getResultList();
  }

  /**
   * 获取一行数据 2017/6/22
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public Object StringBySql(String sql) {

    Query query = em.createNativeQuery(sql);
    //设置返回每行数据格式为MAP
    query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

    return query.getSingleResult();
  }

}
