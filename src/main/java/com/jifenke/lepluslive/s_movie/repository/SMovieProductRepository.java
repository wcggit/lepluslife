package com.jifenke.lepluslive.s_movie.repository;

import com.jifenke.lepluslive.s_movie.domain.entities.SMovieProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by xf on 2017/4/27.
 */
public interface SMovieProductRepository extends JpaRepository<SMovieProduct, Long> {
    List<SMovieProduct> findByState(Integer state);
}
