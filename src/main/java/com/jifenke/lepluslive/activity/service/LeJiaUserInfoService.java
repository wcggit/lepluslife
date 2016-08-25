package com.jifenke.lepluslive.activity.service;

import com.jifenke.lepluslive.activity.domain.entities.ActivitySportLog;
import com.jifenke.lepluslive.activity.domain.entities.LeJiaUserInfo;
import com.jifenke.lepluslive.activity.repository.ActivitySportLogRepository;
import com.jifenke.lepluslive.activity.repository.LeJiaUserInfoRepository;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.score.domain.entities.ScoreA;
import com.jifenke.lepluslive.score.domain.entities.ScoreADetail;
import com.jifenke.lepluslive.score.domain.entities.ScoreB;
import com.jifenke.lepluslive.score.domain.entities.ScoreBDetail;
import com.jifenke.lepluslive.score.repository.ScoreADetailRepository;
import com.jifenke.lepluslive.score.repository.ScoreARepository;
import com.jifenke.lepluslive.score.repository.ScoreBDetailRepository;
import com.jifenke.lepluslive.score.repository.ScoreBRepository;
import com.jifenke.lepluslive.score.service.ScoreAService;
import com.jifenke.lepluslive.score.service.ScoreBService;
import com.jifenke.lepluslive.weixin.repository.DictionaryRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by zhangwen on 2016/8/18.
 */
@Service
@Transactional(readOnly = true)
public class LeJiaUserInfoService {

  @Inject
  private LeJiaUserInfoRepository leJiaUserInfoRepository;


  //获取sportUser
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public LeJiaUserInfo findByToken(String token) {
    List<LeJiaUserInfo> list = leJiaUserInfoRepository.findByToken(token);
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  //新建sportUser
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public LeJiaUserInfo createSportUser(LeJiaUser leJiaUser) {

    LeJiaUserInfo leJiaUserInfo = new LeJiaUserInfo();
    leJiaUserInfo.setLeJiaUser(leJiaUser);
    leJiaUserInfo.setToken(leJiaUser.getToken());
    leJiaUserInfoRepository.save(leJiaUserInfo);

    return leJiaUserInfo;
  }
}
