package com.jifenke.lepluslive.weixin.controller;

import com.jifenke.lepluslive.Address.domain.entities.Address;
import com.jifenke.lepluslive.Address.service.AddressService;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.order.domain.entities.OnLineOrder;
import com.jifenke.lepluslive.order.service.OrderService;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.service.WeiXinService;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by wcg on 16/3/20.
 */
@RestController
@RequestMapping("/weixin")
public class WeixinOrderController {

  @Inject
  private OrderService orderService;

  @Inject
  private WeiXinService weiXinService;

  @Inject
  private AddressService addressService;

  /**
   * 从支付页面跳转到地址修改页面
   */
  @RequestMapping("/order/addressEdit/{orderId}")
  public ModelAndView goAddressEditPage(@PathVariable Long orderId, Model model,
                                        HttpServletRequest request) {
    Address
        address =
        addressService.findAddressByLeJiaUserAndState(
            weiXinService.getCurrentWeiXinUser(request).getLeJiaUser());
    model.addAttribute("address", address);
    model.addAttribute("orderId", orderId);
    return MvUtil.go("/weixin/address");
  }

  /**
   * 修改地址并重定向到支付页面
   */
  @RequestMapping(value = "/order/{orderId}", method = RequestMethod.POST)
  public void editAddress(Address address, @PathVariable Long orderId,
                          HttpServletRequest request, HttpServletResponse response) {
    OnLineOrder onLineOrder = orderService.findOnLineOrderById(orderId);
    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
    addressService.editAddress(address, weiXinUser, onLineOrder);
    try {
      response.sendRedirect("/front/order/weixin/confirmOrder?orderId=" + orderId);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
