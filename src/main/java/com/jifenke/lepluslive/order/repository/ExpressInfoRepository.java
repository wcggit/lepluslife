package com.jifenke.lepluslive.order.repository;

import com.jifenke.lepluslive.order.domain.entities.ExpressInfo;
import com.jifenke.lepluslive.order.domain.entities.OnLineOrder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;

/**
 * Created by wcg on 16/3/21.
 */
public interface ExpressInfoRepository extends JpaRepository<ExpressInfo,Long>{

  @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
  ExpressInfo findOneByOnLineOrder(OnLineOrder onLineOrder);

}
