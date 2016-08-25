package com.jifenke.lepluslive.activity.repository;

import com.jifenke.lepluslive.activity.domain.entities.LeJiaUserInfo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by zhangwen on 16/8/18.
 */
public interface LeJiaUserInfoRepository extends JpaRepository<LeJiaUserInfo, Long> {

  List<LeJiaUserInfo> findByToken(String token);

}
