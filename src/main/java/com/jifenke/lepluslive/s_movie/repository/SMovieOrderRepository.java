package com.jifenke.lepluslive.s_movie.repository;

import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.s_movie.domain.entities.SMovieOrder;
import com.jifenke.lepluslive.s_movie.domain.entities.SMovieTerminal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by xf on 2017/4/28.
 */
public interface SMovieOrderRepository extends JpaRepository<SMovieOrder, Long> {

    SMovieOrder findByOrderSid(String orderSid);

    List<SMovieOrder> findByLeJiaUserAndState(LeJiaUser leJiaUser, Integer state);

    List<SMovieOrder> findBySMovieTerminalAndStateOrderByDateUsedDesc(SMovieTerminal sMovieTerminal, Integer state);


    @Query(value="select count(*) from s_movie_order where le_jia_user_id = ?1 and state = 1",nativeQuery=true)
    Long countVaild(Long userId);
}
