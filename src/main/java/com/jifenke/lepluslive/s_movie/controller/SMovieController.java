package com.jifenke.lepluslive.s_movie.controller;

import com.jifenke.lepluslive.banner.service.BannerService;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.s_movie.domain.entities.SMovieProduct;
import com.jifenke.lepluslive.s_movie.service.SMovieProductService;
import com.jifenke.lepluslive.score.domain.entities.ScoreC;
import com.jifenke.lepluslive.score.service.ScoreCService;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.service.WeiXinService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by xf on 2017/4/27.
 * 乐加观影特权模块
 */
@RestController
@RequestMapping("/front/movie")
public class SMovieController {

    @Inject
    private SMovieProductService productService;
    @Inject
    private BannerService bannerService;
    @Inject
    private WeiXinService weiXinService;
    @Inject
    private ScoreCService scoreCService;

    /**
     * 进入乐加电影首页
     */
    @RequestMapping(value = "/weixin/page", method = RequestMethod.GET)
    public ModelAndView toMoviePage(HttpServletRequest request, Model model) {
        WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
        ScoreC scoreC = scoreCService.findScoreCByLeJiaUser(weiXinUser.getLeJiaUser());
        List<SMovieProduct> products = productService.findAllOnSale();
        model.addAttribute("products", products);
        model.addAttribute("scoreC",scoreC);
        return MvUtil.go("/movie/moviePage");
    }

    /**
     * 电影首页上方轮播图
     */
    @RequestMapping(value = "/topBanner", method = RequestMethod.GET)
    @ResponseBody
    public LejiaResult getTopBanner(Model model) {
        List topBanner = bannerService.findTopBanner();
        return LejiaResult.ok(topBanner);
    }

    /**
     * 热门电影列表图
     */
    @RequestMapping(value = "/hotMovieBanner", method = RequestMethod.GET)
    @ResponseBody
    public LejiaResult getHotMovieBanner(Model model) {
        List hotMovieBanner = bannerService.findHotMovieBanner();
        return LejiaResult.ok(hotMovieBanner);
    }

}
