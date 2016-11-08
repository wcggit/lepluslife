package com.jifenke.lepluslive.order.repository;

import com.jifenke.lepluslive.order.domain.entities.ExpressInfo;
import com.jifenke.lepluslive.order.domain.entities.OnLineOrder;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by zhangwen on 16/11/04.
 */
public interface ExpressInfoRepository extends JpaRepository<ExpressInfo, Long> {

  List<ExpressInfo> findOneByOnLineOrder(OnLineOrder onLineOrder);

}
