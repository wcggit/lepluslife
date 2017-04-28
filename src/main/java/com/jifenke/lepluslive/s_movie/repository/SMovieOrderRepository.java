package com.jifenke.lepluslive.s_movie.repository;

import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.s_movie.domain.entities.SMovieOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by xf on 2017/4/28.
 */
public interface SMovieOrderRepository extends JpaRepository<SMovieOrder, Long> {

    SMovieOrder findByOrderSid(String orderSid);

    List<SMovieOrder> findByLeJiaUserAndState(LeJiaUser leJiaUser, Integer state);
}
