package com.jifenke.lepluslive.activity.service;

import com.jifenke.lepluslive.activity.domain.entities.ActivityCodeBurse;
import com.jifenke.lepluslive.activity.repository.ActivityCodeBurseRepository;
import com.jifenke.lepluslive.weixin.service.WeiXinService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created by wcg on 16/3/17.
 */
@Service
@Transactional(readOnly = true)
public class ActivityCodeBurseService {

  @Inject
  private ActivityCodeBurseRepository activityCodeBurseRepository;

  @Inject
  private WeiXinService weiXinService;

  @Value("${bucket.ossBarCodeReadRoot}")
  private String barCodeRootUrl;

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void saveActivityCodeBurse(ActivityCodeBurse codeBurse) {
    activityCodeBurseRepository.save(codeBurse);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public Page findByPage(Integer offset, Integer limit) {
    Sort sort = new Sort(Sort.Direction.DESC, "createDate");
    return activityCodeBurseRepository.findAll(getWhereClause(),
                                               new PageRequest(offset - 1, limit, sort));
  }

  public Specification<ActivityCodeBurse> getWhereClause() {

    return new Specification<ActivityCodeBurse>() {
      @Override
      public Predicate toPredicate(Root<ActivityCodeBurse> r, CriteriaQuery<?> q,
                                   CriteriaBuilder cb) {
        Predicate predicate = cb.conjunction();
        predicate.getExpressions().add(
            cb.equal(r.get("type"), 1));

        return predicate;
      }
    };
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
