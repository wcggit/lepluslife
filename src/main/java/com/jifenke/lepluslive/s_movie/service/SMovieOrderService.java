package com.jifenke.lepluslive.s_movie.service;

import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.order.service.OnLineOrderShareService;
import com.jifenke.lepluslive.s_movie.domain.entities.SMovieOrder;
import com.jifenke.lepluslive.s_movie.domain.entities.SMovieProduct;
import com.jifenke.lepluslive.s_movie.domain.entities.SMovieTerminal;
import com.jifenke.lepluslive.s_movie.repository.SMovieOrderRepository;
import com.jifenke.lepluslive.s_movie.repository.SMovieProductRepository;
import com.jifenke.lepluslive.s_movie.repository.SMovieTerminalRepository;
import com.jifenke.lepluslive.score.domain.entities.ScoreC;
import com.jifenke.lepluslive.score.service.ScoreCService;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by xf on 2017/4/28.
 */
@Service
@Transactional(readOnly = true)
public class SMovieOrderService{

    @Inject
    private SMovieProductRepository productRepository;
    @Inject
    private SMovieOrderRepository movieOrderRepository;
    @Inject
    private ScoreCService scoreCService;
    @Inject
    private OnLineOrderShareService orderShareService;
    @Inject
    private SMovieTerminalRepository terminalRepository;

    @Transactional(readOnly = false,propagation = Propagation.REQUIRED)
    public Map<Object,Object> createSMoiveOrder(Long productId, WeiXinUser weiXinUser) {
        Map result = new HashMap();                         //  结果消息
        //   创建电影订单
        SMovieProduct product = productRepository.findOne(productId);
        SMovieOrder sMovieOrder = new SMovieOrder();
        sMovieOrder.setLeJiaUser(weiXinUser.getLeJiaUser());
        sMovieOrder.setsMovieProduct(product);
        sMovieOrder.setState(0);                            //  订单状态   0=待付款|1=已付款待核销|2=已付款已核销|3=已退款
        sMovieOrder.setTotalPrice(product.getPrice());      //  订单金额
        sMovieOrder.setDateCreated(new Date());
        sMovieOrder.setPayBackA(product.getPayBackA());
        //  是否是纯金币支付
        ScoreC scoreC = scoreCService.findScoreCByLeJiaUser(weiXinUser.getLeJiaUser());
        try {
            if(scoreC.getScore()>=product.getPrice()) {         //  纯金币支付 - 完成支付流程
                sMovieOrder.setTrueScore(product.getPrice());
                sMovieOrder.setTruePrice(0L);
                sMovieOrder.setTrueIncome(0L);
                result.put("status",2000);
                result.put("msg","本次消费使用纯金币支付。");
            }else if(scoreC.getScore()>0){                      //  部分使用金币支付
                sMovieOrder.setTrueIncome(product.getPrice()-scoreC.getScore());
                sMovieOrder.setTrueScore(scoreC.getScore());
                sMovieOrder.setTruePrice(product.getPrice()-scoreC.getScore());
                result.put("status",200);
                result.put("msg","本次消费使用金币抵扣部分金额。");
            }else {                                             //  纯现金支付
                sMovieOrder.setTrueIncome(product.getPrice());
                sMovieOrder.setTrueScore(0L);
                sMovieOrder.setTruePrice(product.getPrice());
                result.put("status",200);
                result.put("msg","本次消费使用现金支付。");
            }
            result.put("data",sMovieOrder);
            movieOrderRepository.save(sMovieOrder);
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return result;
    }

    /**
     * 电影订单微信支付成功后处理  17/4/28
     *
     * @param orderSid 订单Sid
     */

    /**
     * 微信支付成功后，充值成功回调 17/5/3
     *
     * @param orderSid   订单SID
     */
    @Transactional(readOnly = false,propagation = Propagation.REQUIRED)
    public void paySuccess(String orderSid) {
        SMovieOrder order = movieOrderRepository.findByOrderSid(orderSid);
        if(order!=null) {
            order.setState(1);              //  订单状态   0=待付款|1=已付款待核销|2=已付款已核销|3=已退款
            paySuccessByGold(order);        //  金币变更
            order.setDateCompleted(new Date());
            movieOrderRepository.save(order);
        }
    }
    /**
     * 金币支付后处理 17/4/28
     * @param movieOrder 电影订单
     */
    @Transactional(readOnly = false,propagation = Propagation.REQUIRED)
    public void paySuccessByGold(SMovieOrder movieOrder) {
        try{
            //  支付完成后，减金币
            LeJiaUser leJiaUser = movieOrder.getLeJiaUser();
            ScoreC scoreC = scoreCService.findScoreCByLeJiaUser(leJiaUser);
            scoreCService.saveScoreC(scoreC, 0, movieOrder.getTrueScore());
            scoreCService.saveScoreCDetail(scoreC, 0, movieOrder.getTrueScore(), 15003, "购买电影特权消耗金币",
                            movieOrder.getOrderSid());
            //  分润
            try{
                orderShareService.sMovieOrderShare(movieOrder);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * 查找当前用户未核销的电影  17/4/28
     */
    @Transactional(readOnly = true,propagation = Propagation.REQUIRED)
    public List<SMovieOrder> findVaildMovies(LeJiaUser leJiaUser) {   //订单状态   0=待付款|1=已付款待核销|2=已付款已核销|3=已退款
       return movieOrderRepository.findByLeJiaUserAndState(leJiaUser,1);
    }
    /**
     * 查找当前设备已核销的电影  17/4/28
     */
    @Transactional(readOnly = true,propagation = Propagation.REQUIRED)
    public List<SMovieOrder> findUsedMoviesByTerminal(String terminalNo) {   //订单状态   0=待付款|1=已付款待核销|2=已付款已核销|3=已退款
        SMovieTerminal sMovieTerminal =  terminalRepository.findByTerminalNo(terminalNo);
        List<SMovieOrder> orders = null;
        if(sMovieTerminal!=null) {
            orders =movieOrderRepository.findBySMovieTerminalAndStateOrderByDateUsedDesc(sMovieTerminal, 2);
        }else {
            throw  new RuntimeException();
        }
        return orders;
    }
    /**
     * 查找当前用户已核销的电影  17/4/28
     */
    @Transactional(readOnly = true,propagation = Propagation.REQUIRED)
    public List<SMovieOrder> findUsedMovies(LeJiaUser leJiaUser) {   //订单状态   0=待付款|1=已付款待核销|2=已付款已核销|3=已退款
        return movieOrderRepository.findByLeJiaUserAndState(leJiaUser,2);
    }

    /**
     *  根据 orderSid 查询订单
     */
    @Transactional(readOnly = true,propagation = Propagation.REQUIRED)
    public SMovieOrder findByOrderSid(String orderSid) {
        return movieOrderRepository.findByOrderSid(orderSid);
    }

    /**
     *  核销订单
     */
    @Transactional(readOnly = false,propagation = Propagation.REQUIRED)
    public Map<Object,Object> updateOrderState(String orderSid,String phoneNumber,String terminalNo) {
        try{
            Map result = new HashMap();
            SMovieOrder order  =  findByOrderSid(orderSid);
            SMovieTerminal sMovieTerminal =  terminalRepository.findByTerminalNo(terminalNo);
            if(order!=null && order.getState()==1 && order.getLeJiaUser().getPhoneNumber().equals(phoneNumber)) {
                order.setState(2);
                order.setDateUsed(new Date());
                order.setsMovieTerminal(sMovieTerminal);
                movieOrderRepository.save(order);
                result.put("status",200);
                result.put("data",order);
                result.put("msg","核销成功！");
            }else if(order == null) {
                result.put("status",501);
                result.put("msg","核销失败,订单号有误！");
            }else if(order.getState()!=1) {
                result.put("status",502);
                result.put("msg","核销失败,该订单已经核销过了哦～");
            }else {
                result.put("status",503);
                result.put("msg","您的手机号码有误,所以不能核销！");
            }
            return result;
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     *  统计用户当前可用的电影特权
     *  17/05/02
     */
    @Transactional(readOnly = true,propagation = Propagation.REQUIRED)
    public Long countVaildMovie(Long  userID) {
        return movieOrderRepository.countVaild(userID);
    }

    @Transactional(readOnly = true,propagation = Propagation.REQUIRED)
    public SMovieOrder findById(Long orderId) {
        return movieOrderRepository.findOne(orderId);
    }
}
