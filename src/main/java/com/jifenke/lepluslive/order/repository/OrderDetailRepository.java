package com.jifenke.lepluslive.order.repository;

import com.jifenke.lepluslive.order.domain.entities.OrderDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by wcg on 16/3/21.
 */
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

  /**
   * 查询某个用户已购买某个商品的数量 16/09/22
   */
  @Query(value = "SELECT SUM(d.product_number) FROM on_line_order o,order_detail d,product p WHERE d.on_line_order_id=o.id AND d.product_id=p.id AND o.le_jia_user_id=?1 AND o.pay_state=1 AND p.id=?2", nativeQuery = true)
  Integer getCurrentUserBuyProductCount(Long leJiaUserId, Long productId);

  /**
   * 查询某个用户待付款的某个商品的数量 16/09/22
   */
  @Query(value = "SELECT SUM(d.product_number) FROM on_line_order o INNER JOIN order_detail d ON d.on_line_order_id=o.id INNER JOIN product p ON d.product_id=p.id WHERE o.le_jia_user_id=?1 AND o.state=0 AND p.id=?2", nativeQuery = true)
  Integer getCurrentUserOrderProductCount(Long leJiaUserId, Long productId);

}
