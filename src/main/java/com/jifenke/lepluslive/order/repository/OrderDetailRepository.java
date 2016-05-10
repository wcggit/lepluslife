package com.jifenke.lepluslive.order.repository;

import com.jifenke.lepluslive.order.domain.entities.OrderDetail;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by wcg on 16/3/21.
 */
public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long>{


}
