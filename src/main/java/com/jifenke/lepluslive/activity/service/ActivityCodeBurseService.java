package com.jifenke.lepluslive.activity.service;

import com.jifenke.lepluslive.activity.domain.entities.ActivityCodeBurse;
import com.jifenke.lepluslive.activity.repository.ActivityCodeBurseRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by wcg on 16/3/17.
 */
@Service
@Transactional(readOnly = true)
public class ActivityCodeBurseService {

  @Inject
  private ActivityCodeBurseRepository activityCodeBurseRepository;

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void saveActivityCodeBurse(ActivityCodeBurse codeBurse) {
    activityCodeBurseRepository.save(codeBurse);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public ActivityCodeBurse findCodeBurseById(Long id) {
    return activityCodeBurseRepository.findOne(id);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public ActivityCodeBurse findCodeBurseByTicket(String ticket) {
    List<ActivityCodeBurse> list = activityCodeBurseRepository.findByTicket(ticket);
    if (list.size() > 0) {
      return list.get(0);
    }
    return null;
  }


}
