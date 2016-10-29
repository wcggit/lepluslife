package com.jifenke.lepluslive.activity.repository;

import com.jifenke.lepluslive.activity.domain.entities.ActivityPhoneOrder;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by zhangwen on 16/10/26.
 */
public interface ActivityPhoneOrderRepository extends JpaRepository<ActivityPhoneOrder, String> {

  ActivityPhoneOrder findByOrderSid(String orderSid);

  Page findAll(Specification<ActivityPhoneOrder> whereClause, Pageable pageable);

  /**
   * 话费订单数据统计  16/10/27
   */
  @Query(value = "SELECT SUM(worth) AS totalWorth,SUM(true_price) AS totalPrice,SUM(true_scoreb) AS totalScore,COUNT(DISTINCT(le_jia_user_id)) AS totalUser,COUNT(*) AS totalNumber,SUM(pay_back_score) AS totalBack FROM activity_phone_order WHERE pay_state = 1", nativeQuery = true)
  List<Object[]> orderCount();

  /**
   * 今日已使用话费池  16/10/27
   */
  @Query(value = "SELECT SUM(worth) FROM activity_phone_order WHERE pay_state = 1 AND pay_date BETWEEN DATE_FORMAT(NOW(),'%Y-%m-%d') AND DATE_ADD(DATE_FORMAT(NOW(),'%Y-%m-%d'),INTERVAL 1 DAY)", nativeQuery = true)
  Integer todayUsedWorth();
}
