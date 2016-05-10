package com.jifenke.lepluslive.order.repository;

import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.order.domain.entities.OnLineOrder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by wcg on 16/3/21.
 */
public interface OrderRepository extends JpaRepository<OnLineOrder,Long>{

  OnLineOrder findByOrderSid(String orderSid);

  List<OnLineOrder> findAllByLeJiaUserAndStateNotInOrderByCreateDateDesc(LeJiaUser leJiaUser,List<Integer> states);

  @Query(value = "select count(*) from on_line_order where le_jia_user_id = ?1 and state = 0",nativeQuery = true)
  Long getCurrentUserObligationOrdersCount(Long  leJiaUserId);

}
