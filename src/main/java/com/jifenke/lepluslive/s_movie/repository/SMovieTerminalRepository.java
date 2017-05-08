package com.jifenke.lepluslive.s_movie.repository;

import com.jifenke.lepluslive.s_movie.domain.entities.SMovieTerminal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by xf on 2017/4/27.
 */
public interface SMovieTerminalRepository extends JpaRepository<SMovieTerminal, Long> {
    List<SMovieTerminal> findByState(Integer state);
    SMovieTerminal findByTerminalNo(String terminalNo);
}
