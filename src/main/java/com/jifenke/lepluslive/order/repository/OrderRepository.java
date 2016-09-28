package com.jifenke.lepluslive.order.repository;

import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.order.domain.entities.OnLineOrder;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by wcg on 16/3/21.
 */
public interface OrderRepository extends JpaRepository<OnLineOrder, Long> {

  OnLineOrder findByOrderSid(String orderSid);

  List<OnLineOrder> findAllByLeJiaUserAndStateNotInOrderByCreateDateDesc(LeJiaUser leJiaUser,
                                                                         List<Integer> states);

  /**
   * 分页查询所有订单 16/09/23
   */
  Page<OnLineOrder> findAllByLeJiaUserAndStateNotInOrderByCreateDateDesc(Pageable pageable,
                                                                         LeJiaUser leJiaUser,
                                                                         List<Integer> states);

  List<OnLineOrder> findAllByLeJiaUserAndStateOrderByCreateDateDesc(LeJiaUser leJiaUser,
                                                                    Integer status);

  /**
   * 分页查询某种状态的订单 16/09/23
   */
  Page<OnLineOrder> findAllByLeJiaUserAndStateOrderByCreateDateDesc(Pageable pageable,
                                                                    LeJiaUser leJiaUser,
                                                                    Integer status);

  @Query(value = "select count(*) from on_line_order where le_jia_user_id = ?1 and state = 0", nativeQuery = true)
  Long getCurrentUserObligationOrdersCount(Long leJiaUserId);

  @Query(value = "SELECT state,COUNT(state) FROM on_line_order WHERE le_jia_user_id=?1 AND state IN(0,1,2) GROUP BY state", nativeQuery = true)
  List<Object[]> getOrdersCount(Long leJiaUserId);

}
