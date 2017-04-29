package com.jifenke.lepluslive.s_movie.controller;

import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;
import com.jifenke.lepluslive.s_movie.domain.entities.SMovieOrder;
import com.jifenke.lepluslive.s_movie.service.SMovieOrderService;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.service.WeiXinPayService;
import com.jifenke.lepluslive.weixin.service.WeiXinService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

/**
 * Created by xf on 2017/4/28.
 * 乐加电影特权订单
 */
@RestController
@RequestMapping("/front/movie")
public class SMovieOrderController {

    @Inject
    private WeiXinService weiXinService;
    @Inject
    private SMovieOrderService sMovieOrderService;
    @Inject
    private WeiXinPayService weiXinPayService;
    @Inject
    private LeJiaUserService leJiaUserService;

    /**
     *  电影特权生成 设置订单参数   17/04/28
     *  @param  smovieId  电影特权产品 ID
     */
    @RequestMapping(value="/weixin/moviePay",method = RequestMethod.POST)
    public LejiaResult moviePay(Long smovieId, HttpServletRequest request) {
        WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
        Map result = null;
        try{
            result = sMovieOrderService.createSMoiveOrder(smovieId, weiXinUser);
            //  封装订单参数
            SMovieOrder order = (SMovieOrder) result.get("data");
            //  纯金币支付
            if ("2000".equals("" + result.get("status"))) {
                sMovieOrderService.paySuccess(order.getOrderSid());
                return LejiaResult
                        .build((Integer) result.get("status"),(String)result.get("msg"));
            }
            SortedMap<String, Object>
                    map =
                    weiXinPayService
                            .buildOrderParams(request, "电影票购买", order.getOrderSid(), "" + order.getTruePrice(),
                                    Constants.PHONEORDER_NOTIFY_URL);
            // 获取微信预支付
            Map<String, Object> unifiedOrder = weiXinPayService.createUnifiedOrder(map);
            if (unifiedOrder.get("prepay_id") != null) {
                //返回前端页面
                SortedMap<String, Object>
                        params =
                        weiXinPayService.buildJsapiParams(unifiedOrder.get("prepay_id").toString());
                params.put("orderId", order.getId());
                return LejiaResult.ok(params);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return LejiaResult.build(500, "出现未知错误,请联系管理员或稍后重试");
    }

    /**
     *  电影特权订单展示 - 未核销  17/04/28
     */
    @RequestMapping(value="/vaildMovies",method = RequestMethod.GET)
    public ModelAndView showVaildMovies(Long lejiaUserId,Model model) {
        LeJiaUser leJiaUser = leJiaUserService.findUserById(lejiaUserId);
        List<SMovieOrder> vaildMovies = sMovieOrderService.findVaildMovies(leJiaUser);
        model.addAttribute("vaildMovies",vaildMovies);
        return MvUtil.go("/...");
    }

    /**
     *  电影特权订单展示 - 已核销  17/04/28
     */
    @RequestMapping(value="/usedMovies",method = RequestMethod.GET)
    public ModelAndView showUsedMovies(Long lejiaUserId, Model model) {
        LeJiaUser leJiaUser = leJiaUserService.findUserById(lejiaUserId);
        List<SMovieOrder> usedMovies = sMovieOrderService.findUsedMovies(leJiaUser);
        model.addAttribute("usedMovies",usedMovies);
        return MvUtil.go("/...");
    }



    /***
     *  商米核销模块 - 根据手机号查询用户有效特权及用户信息
     *  17/4/29
     */
    @RequestMapping(value="/shangmi/searchVaildOrderByPhoneNum",method = RequestMethod.GET)
    @ResponseBody
    public LejiaResult loadOrderByPhoneNum(String phoneNumber) {
        LejiaUser lejiaUser = leJiaUserService.findUserByPhoneNumber(phoneNumber);
        List<SMovieOrder> vaildMovies = sMovieOrderService.findVaildMovies(leJiaUser);
        Map result = new HashMap();
        map.put("lejiaUser",lejiaUser);
        map.put("orderList",vaildMovies);
        return LejiaResult.ok(result);
    }

    /***
     *  商米核销模块 - 根据手机号查询用户已核销的订单
     *  17/4/29
     */
    @RequestMapping(value="/shangmi/searchCheckedOrderByPhoneNum",method = RequestMethod.GET)
    @ResponseBody
    public LejiaResult loadOrderByPhoneNum(String phoneNumber) {
        LejiaUser lejiaUser = leJiaUserService.findUserByPhoneNumber(phoneNumber);
        List<SMovieOrder> usedMovies = sMovieOrderService.findUsedMovies(leJiaUser);
        Map result = new HashMap();
        map.put("lejiaUser",lejiaUser);
        map.put("orderList",usedMovies);
        return LejiaResult.ok(result);
    }

    /**
     * 商米核销模块 - 核销电影票
     */
    @RequestMapping(value="/shangmi/doCheckedMovie",method = RequestMethod.POST)
    @ResponseBody
    public LejiaResult doCheckedMovie(@RequestBody String orderSid,RequestBody String phoneNumber) {
        try {
            Map result = sMovieOrderService.updateOrderState(orderSid,phoneNumber);
            return LejiaResult.ok(result);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return LejiaResult.build(500, "您的手机号码有误,所以不能核销");
    }

}
