package com.jifenke.lepluslive.activity.repository;

import com.jifenke.lepluslive.activity.domain.entities.ActivityCodeBurse;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by zhangwen on 16/8/4.
 */
public interface ActivityCodeBurseRepository extends JpaRepository<ActivityCodeBurse, Long> {

  List<ActivityCodeBurse> findByTicket(String ticket);
}
