package com.jifenke.lepluslive.weixin.repository;

import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.List;
import java.util.Optional;

import javax.persistence.QueryHint;

/**
 * Created by wcg on 16/3/18.
 */
public interface WeiXinUserRepository extends JpaRepository<WeiXinUser,Long> {

  @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
  List<WeiXinUser> findByOpenId(String openId);

  @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
  WeiXinUser findByUnionId(String unionId);
}
