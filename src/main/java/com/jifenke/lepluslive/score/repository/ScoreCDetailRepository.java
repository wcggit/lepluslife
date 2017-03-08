package com.jifenke.lepluslive.score.repository;

import com.jifenke.lepluslive.score.domain.entities.ScoreC;
import com.jifenke.lepluslive.score.domain.entities.ScoreCDetail;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 金币记录 Created by zhangwen on 17/2/20.
 */
public interface ScoreCDetailRepository extends JpaRepository<ScoreCDetail, Long> {

  List<ScoreCDetail> findAllByScoreCOrderByIdDesc(ScoreC scoreC);

}
