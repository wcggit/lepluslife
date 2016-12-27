package com.jifenke.lepluslive.activity.service;

import com.jifenke.lepluslive.activity.domain.entities.LeJiaUserInfo;
import com.jifenke.lepluslive.activity.repository.LeJiaUserInfoRepository;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
  public LeJiaUserInfo findByLeJiaUser(LeJiaUser leJiaUser) {
    List<LeJiaUserInfo> list = leJiaUserInfoRepository.findByLeJiaUser(leJiaUser);
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  //新建sportUser
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public LeJiaUserInfo createUserInfo(LeJiaUser leJiaUser) {

    LeJiaUserInfo leJiaUserInfo = new LeJiaUserInfo();
    leJiaUserInfo.setLeJiaUser(leJiaUser);
    leJiaUserInfo.setToken(leJiaUser.getUserSid());
    leJiaUserInfoRepository.save(leJiaUserInfo);

    return leJiaUserInfo;
  }
}
