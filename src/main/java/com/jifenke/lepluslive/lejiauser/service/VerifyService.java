package com.jifenke.lepluslive.lejiauser.service;

import com.jifenke.lepluslive.lejiauser.domain.entities.Verify;
import com.jifenke.lepluslive.lejiauser.repository.VerifyRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Created by zhangwen on 2017/6/29.
 */
@Service
@Transactional(readOnly = true)
public class VerifyService {

  @Inject
  private VerifyRepository repository;

  @Transactional(propagation = Propagation.REQUIRED)
  public Verify addVerify(String unionId, Integer pageType) {
    Verify verify = new Verify();
    verify.setUnionId(unionId);
    verify.setPageType(pageType);
    repository.save(verify);
    return verify;
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public void saveVerify(Verify verify) {
    repository.save(verify);
  }

  public Verify findByPageSidAndUnionId(String pageSid, String unionId) {

    Verify verify = repository.findByPageSid(pageSid);
    if (verify != null && verify.getState() == 0 && verify.getUnionId().equals(unionId)) {
      return verify;
    }
    return null;
  }

  public Integer countByUnionId(String unionId) {
    return repository.countByUnionIdAndAndState(unionId, 1);
  }

}
