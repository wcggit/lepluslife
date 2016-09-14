package com.jifenke.lepluslive.weixin.repository;

import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUserInfo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by zhangwen on 16/5/25.
 */
public interface WeiXinUserInfoRepository extends JpaRepository<WeiXinUserInfo, Long> {

  List<WeiXinUserInfo> findByWeiXinUser(WeiXinUser weiXinUser);
}
