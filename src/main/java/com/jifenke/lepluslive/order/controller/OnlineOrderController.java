package com.jifenke.lepluslive.order.controller;

import com.jifenke.lepluslive.Address.domain.entities.Address;
import com.jifenke.lepluslive.Address.service.AddressService;
import com.jifenke.lepluslive.banner.service.BannerService;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.groupon.service.GrouponOrderService;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.order.domain.entities.OnLineOrder;
import com.jifenke.lepluslive.order.service.OnlineOrderService;
import com.jifenke.lepluslive.order.service.OrderDetailService;
import com.jifenke.lepluslive.order.service.OrderService;
import com.jifenke.lepluslive.s_movie.domain.entities.SMovieProduct;
import com.jifenke.lepluslive.s_movie.service.SMovieOrderService;
import com.jifenke.lepluslive.s_movie.service.SMovieProductService;
import com.jifenke.lepluslive.score.domain.entities.ScoreC;
import com.jifenke.lepluslive.score.service.ScoreAService;
import com.jifenke.lepluslive.score.service.ScoreCService;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.service.WeiXinPayService;
import com.jifenke.lepluslive.weixin.service.WeiXinService;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * 线上订单 Created by zhangwen on 16/9/18.
 */
@RestController
@RequestMapping("/front/order")
public class OnlineOrderController {

  @Inject
  private OrderService orderService;

  @Inject
  private AddressService addressService;

  @Inject
  private WeiXinService weiXinService;

  @Inject
  private OrderDetailService orderDetailService;

  @Inject
  private OnlineOrderService onlineOrderService;

  @Inject
  private WeiXinPayService weiXinPayService;

  @Inject
  private ScoreCService scoreCService;

  @Inject
  private ScoreAService scoreAService;

  @Inject
  private SMovieOrderService movieOrderService;

  @Inject
  private SMovieProductService productService;

  @Inject
  private BannerService bannerService;

  @Inject
  private GrouponOrderService grouponOrderService;

  /**
   * 爆品详情页点击购买生成订单 16/09/22
   *
   * @param productId 产品ID
   * @param buyNumber 规格数量
   * @param specId    规格ID
   */
  @RequestMapping(value = "/weixin/createHotOrder", method = RequestMethod.POST)
  @ResponseBody
  public LejiaResult createHotOrder(HttpServletRequest request,
                                    @RequestParam Long productId,
                                    @RequestParam Integer buyNumber,
                                    @RequestParam Long specId) {
    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
    LeJiaUser leJiaUser = weiXinUser.getLeJiaUser();
    //查询某个用户待付款的某个商品的数量
    int count = orderDetailService.getCurrentUserOrderProductCount(leJiaUser.getId(), productId);
    if (count > 0) {
      return LejiaResult.build(5002, "请先支付该商品的待支付订单");
    }
    Address address = addressService.findAddressByLeJiaUserAndState(leJiaUser);
    //创建爆品的待支付订单
    try {
      Map
          result =
          onlineOrderService.createHotOrder(productId, specId, buyNumber, leJiaUser, address, 5L);
      return LejiaResult.build((Integer) result.get("status"), "ok", result.get("data"));
    } catch (Exception e) {
      e.printStackTrace();
      return LejiaResult.build(500, "服务器异常");
    }
  }

  /**
   * 订单确认页 16/09/22
   *
   * @param orderId 订单ID
   */
  @RequestMapping(value = "/weixin/confirmOrder", method = RequestMethod.GET)
  public ModelAndView productIndex(HttpServletRequest request, @RequestParam Long orderId,
                                   Model model) {
    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
    OnLineOrder order = orderService.findOnLineOrderById(orderId);
    model.addAttribute("order", order);
    model.addAttribute("wxConfig", weiXinPayService.getWeiXinPayConfig(request));
    model.addAttribute("canUseScore",
                       scoreCService.findScoreCByLeJiaUser(weiXinUser.getLeJiaUser())
                           .getScore()); //用户可用金币
    if (order.getType() != null && order.getType() == 2) {
      return MvUtil.go("/gold/order/confirmOrder");
    }
    return MvUtil.go("/order/confirmOrder");
  }

  /**
   * 个人订单列表页 16/09/23
   */
  @RequestMapping(value = "/weixin/orderList")
  public ModelAndView goOrderListPage() {
    return MvUtil.go("/order/orderList");
  }

  /**
   * 分页获取用户各种类型订单 16/09/23
   */
  @RequestMapping(value = "/weixin/orderList", method = RequestMethod.POST)
  public
  @ResponseBody
  LejiaResult getCurrentUserAllOrder(HttpServletRequest request,
                                     @RequestParam(required = true) Integer currPage,
                                     @RequestParam(required = true) Integer state) {
    if (currPage == null || currPage < 1) {
      currPage = 1;
    }
    List<OnLineOrder>
        onLineOrders =
        onlineOrderService
            .getCurrentUserOrderListByPage(
                weiXinService.getCurrentWeiXinUser(request).getLeJiaUser(), state, currPage);
    return LejiaResult.ok(onLineOrders);
  }

  /**
   * 跳转到金币冲话费首页 17/02/22 在这儿的原因=微信支付目录设置问题
   */
  @RequestMapping(value = "/weixin/recharge", method = RequestMethod.GET)
  public ModelAndView recharge(HttpServletRequest request, Model model) {
    LeJiaUser leJiaUser = weiXinService.getCurrentWeiXinUser(request).getLeJiaUser();
    model.addAttribute("wxConfig", weiXinPayService.getWeiXinPayConfig(request));
    model.addAttribute("phone", leJiaUser.getPhoneNumber());
    model.addAttribute("canUseScore",
                       scoreCService.findScoreCByLeJiaUser(leJiaUser).getScore()); //用户可用金币
    return MvUtil.go("/gold/recharge/index");
  }

  /**
   * 进入乐加电影首页
   */
  @RequestMapping(value = "/weixin/movie", method = RequestMethod.GET)
  public ModelAndView toMoviePage(HttpServletRequest request, Model model) {
    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
    ScoreC scoreC = scoreCService.findScoreCByLeJiaUser(weiXinUser.getLeJiaUser());
    Long vaildCount = movieOrderService.countVaildMovie(weiXinUser.getLeJiaUser().getId());
    List<SMovieProduct> products = productService.findAllOnSale();
    List topBanner = bannerService.findTopBanner();
    List hotMovieBanner = bannerService.findHotMovieBanner();
    model.addAttribute("wxConfig", weiXinPayService.getWeiXinPayConfig(request));
    model.addAttribute("products", products);
    model.addAttribute("scoreC", scoreC);
    model.addAttribute("vaildCount", vaildCount);
    model.addAttribute("topBanner", topBanner);
    model.addAttribute("hotMovieBanner", hotMovieBanner);
    return MvUtil.go("/movie/moviePage");
  }

  /**
   * 跳转到团购订单支付页 17/06/19 在这儿的原因=微信支付目录设置问题
   */
  @RequestMapping(value = "/weixin/groupon", method = RequestMethod.GET)
  public ModelAndView groupon(HttpServletRequest request, @RequestParam Long orderId,
                              Model model) {
    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
    model.addAttribute("state", weiXinUser.getState());
    model.addAttribute("order", grouponOrderService.findById(orderId));
    model.addAttribute("wxConfig", weiXinPayService.getWeiXinPayConfig(request));
    model.addAttribute("canUseScore",
                       scoreAService.findScoreAByLeJiaUser(weiXinUser.getLeJiaUser())
                           .getScore()); //用户可用鼓励金

    return MvUtil.go("/groupon/order/confirmOrder");
  }

}
