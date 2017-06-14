package com.jifenke.lepluslive.s_movie.service;

import com.jifenke.lepluslive.s_movie.domain.entities.SMovieProduct;
import com.jifenke.lepluslive.s_movie.repository.SMovieProductRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by xf on 2017/4/27.
 */
@Service
@Transactional(readOnly = true)
public class SMovieProductService {

    @Inject
    private SMovieProductRepository productRepository;
    /**
     *  获取所有上架状态的电影产品
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<SMovieProduct> findAllOnSale() {
      return  productRepository.findByState(1);
    }
}
