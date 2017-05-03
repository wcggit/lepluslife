package com.jifenke.lepluslive.s_movie.controller;

import com.jifenke.lepluslive.activity.domain.entities.ActivityPhoneOrder;
import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.global.util.WeixinPayUtil;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;
import com.jifenke.lepluslive.s_movie.domain.entities.SMovieOrder;
import com.jifenke.lepluslive.s_movie.domain.entities.SMovieTerminal;
import com.jifenke.lepluslive.s_movie.service.SMovieOrderService;
import com.jifenke.lepluslive.weixin.controller.WeixinPayController;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.service.WeiXinPayService;
import com.jifenke.lepluslive.weixin.service.WeiXinService;
import com.jifenke.lepluslive.weixin.service.WeixinPayLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by xf on 2017/4/28.
 * 乐加电影特权订单
 */
@RestController
@RequestMapping("/front/movie")
public class SMovieOrderController {

    private static Logger log = LoggerFactory.getLogger(WeixinPayController.class);

    @Inject
    private WeiXinService weiXinService;
    @Inject
    private SMovieOrderService sMovieOrderService;
    @Inject
    private WeiXinPayService weiXinPayService;
    @Inject
    private LeJiaUserService leJiaUserService;
    @Inject
    private WeixinPayLogService weixinPayLogService;

    /**
     * 电影特权生成 设置订单参数   17/04/28
     *
     * @param productId 电影特权产品 ID
     */
    @RequestMapping(value = "/weixin/moviePay", method = RequestMethod.POST)
    public LejiaResult moviePay(Long productId, HttpServletRequest request) {
        WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
        Map result = null;
        try {
            result = sMovieOrderService.createSMoiveOrder(productId, weiXinUser);
            //  封装订单参数
            SMovieOrder order = (SMovieOrder) result.get("data");
            //  纯金币支付
            if ("2000".equals("" + result.get("status"))) {
                sMovieOrderService.paySuccess(order.getOrderSid());
                return LejiaResult
                        .build((Integer) result.get("status"), (String) result.get("msg"),order.getOrderSid());
            }
            SortedMap<String, Object>
                    map =
                    weiXinPayService
                            .buildOrderParams(request, "电影票购买", order.getOrderSid(), "" + order.getTruePrice(),
                                    Constants.MOVIE_NOTIFY_URL);
            // 获取微信预支付
            Map<String, Object> unifiedOrder = weiXinPayService.createUnifiedOrder(map);
            if (unifiedOrder.get("prepay_id") != null) {
                //返回前端页面
                SortedMap<String, Object>
                        params =
                        weiXinPayService.buildJsapiParams(unifiedOrder.get("prepay_id").toString());
                params.put("orderSid", order.getOrderSid());
                return LejiaResult.ok(params);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return LejiaResult.build(500, "出现未知错误,请联系管理员或稍后重试");
    }

    /**
     * 电影特权订单展示 - 未核销  17/04/28
     */
    @RequestMapping(value = "/vaildMovies", method = RequestMethod.GET)
    public ModelAndView showVaildMovies(Long lejiaUserId, Model model) {
        LeJiaUser leJiaUser = leJiaUserService.findUserById(lejiaUserId);
        List<SMovieOrder> vaildMovies = sMovieOrderService.findVaildMovies(leJiaUser);
        model.addAttribute("vaildMovies", vaildMovies);
        return MvUtil.go("/...");
    }

    /**
     * 电影特权订单展示 - 已核销  17/04/28
     */
    @RequestMapping(value = "/usedMovies", method = RequestMethod.GET)
    public ModelAndView showUsedMovies(Long lejiaUserId, Model model) {
        LeJiaUser leJiaUser = leJiaUserService.findUserById(lejiaUserId);
        List<SMovieOrder> usedMovies = sMovieOrderService.findUsedMovies(leJiaUser);
        model.addAttribute("usedMovies", usedMovies);
        return MvUtil.go("/...");
    }


    /***
     *  商米核销模块 - 根据手机号查询用户有效特权及用户信息
     *  17/4/29
     */
    @RequestMapping(value = "/shangmi/searchVaild", method = RequestMethod.GET)
    @ResponseBody
    public LejiaResult loadVaildByPhoneNum(String phoneNumber, String lejiaUserSid) {
        Map result = new HashMap();
        LeJiaUser lejiaUser = null;
        if (phoneNumber != null) {
            lejiaUser = leJiaUserService.findUserByPhoneNumber(phoneNumber);
        } else if (lejiaUserSid != null) {
            lejiaUser = leJiaUserService.findUserByUserSid(lejiaUserSid);
        }
        if (lejiaUser == null) {
            result.put("status", 201);
            result.put("msg", "手机号或二维码有误,用户不存在!");
        } else {
            List<SMovieOrder> vaildMovies = sMovieOrderService.findVaildMovies(lejiaUser);
            result.put("status", 200);
            result.put("lejiaUser", lejiaUser);
            WeiXinUser wx = lejiaUser.getWeiXinUser();
            WeiXinUser weiXinUser = new WeiXinUser();
            weiXinUser.setHeadImageUrl(wx.getHeadImageUrl());
            weiXinUser.setId(wx.getId());
            weiXinUser.setNickname(wx.getNickname());
            result.put("weiXinUser", weiXinUser);
            result.put("orderList", vaildMovies);
        }
        return LejiaResult.ok(result);
    }

    /***
     *  商米核销模块 - 根据手机号查询当前设备核销过的订单
     *  17/4/29
     */
    @RequestMapping(value = "/shangmi/searchChecked", method = RequestMethod.GET)
    @ResponseBody
    public LejiaResult loadCheckedByPhoneNum(String terminalNo) {
        Map result = new HashMap();
        try {
            List<SMovieOrder> usedMovies = sMovieOrderService.findUsedMoviesByTerminal(terminalNo);
            result.put("status", 200);
            result.put("orderList", usedMovies);
        } catch (Exception e) {
            result.put("status", 202);
            result.put("msg", "终端号有误！");
            e.printStackTrace();
        }
        return LejiaResult.ok(result);
    }

    /**
     * 商米核销模块 - 核销电影票
     */
    @RequestMapping(value = "/shangmi/doCheckMovie", method = RequestMethod.POST)
    @ResponseBody
    public LejiaResult doCheckedMovie(@RequestParam String orderSid, @RequestParam String phoneNumber, @RequestParam String terminalNo) {
        try {
            Map result = sMovieOrderService.updateOrderState(orderSid, phoneNumber, terminalNo);
            return LejiaResult.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return LejiaResult.build(201, "您的手机号码有误,所以不能核销");
    }


    /**
     * 购买电影票微信回调函数
     */
    @RequestMapping(value = "/afterPay", method = RequestMethod.POST)
    @ResponseBody
    public void afterPay(HttpServletRequest request, HttpServletResponse response) throws Exception {
        InputStreamReader inputStreamReader = new InputStreamReader(request.getInputStream(), "utf-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String str = null;
        String success = "SUCCESS";
        StringBuilder buffer = new StringBuilder();
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
        }
        Map<String, Object> map = WeixinPayUtil.doXMLParse(buffer.toString());
        if (map != null) {
            //验签
            String sign = weiXinPayService.createSign("UTF-8", map, String.valueOf(map.get("trade_type")));
            if (map.get("sign") != null && String.valueOf(map.get("sign")).equals(sign)) {
                //保存微信支付日志
                weixinPayLogService.savePayLog(map, "MovieOrder", 1);
                String orderSid = (String) map.get("out_trade_no");
                String returnCode = (String) map.get("return_code");
                String resultCode = (String) map.get("result_code");
                //操作订单
                if (success.equals(returnCode) && success.equals(resultCode)) {
                    try {
                        sMovieOrderService.paySuccess(orderSid);
                    } catch (Exception ex) {
                        log.error(ex.getMessage());
                        buffer.delete(0, buffer.length());
                        buffer.append("<xml>");
                        buffer.append("<return_code>FAIL</" + "return_code" + ">");
                        buffer.append("</xml>");
                        String s = buffer.toString();
                        response.setContentType("application/xml");
                        response.getWriter().write(s);
                        return;
                    }
                }
                //返回微信的信息
                buffer.delete(0, buffer.length());
                buffer.append("<xml>");
                buffer.append("<return_code>" + returnCode + "</" + "return_code" + ">");
                buffer.append("</xml>");
                String s = buffer.toString();
                response.setContentType("application/xml");
                response.getWriter().write(s);
            }
        }

    }


    /**
     * 进入支付成功页面
     */
    @RequestMapping(value = "/pay/successPage", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView toPaySuccessPage(String orderSid, Model model) {
        SMovieOrder order = sMovieOrderService.findByOrderSid(orderSid);
        model.addAttribute("order", order);
        return MvUtil.go("/movie/paySuccess");
    }
}
