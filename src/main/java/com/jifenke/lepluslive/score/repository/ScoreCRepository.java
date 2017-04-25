package com.jifenke.lepluslive.score.repository;

import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.score.domain.entities.ScoreC;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 金币账户 Created by zhangwen on 17/2/20.
 */
public interface ScoreCRepository extends JpaRepository<ScoreC, Long> {

  Optional<ScoreC> findByLeJiaUser(LeJiaUser leJiaUser);
}
