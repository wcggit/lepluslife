package com.jifenke.lepluslive.weixin.controller;

import com.jifenke.lepluslive.Address.domain.entities.Address;
import com.jifenke.lepluslive.Address.service.AddressService;
import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.global.util.CookieUtils;
import com.jifenke.lepluslive.global.util.JsonUtils;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.order.service.OrderService;
import com.jifenke.lepluslive.product.domain.entities.ProductSpec;
import com.jifenke.lepluslive.product.service.ProductService;
import com.jifenke.lepluslive.weixin.controller.dto.CartDetailDto;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.service.WeiXinService;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by wcg on 16/4/18.
 */
@RestController
@RequestMapping("/weixin")
public class WeiXinCartController {

  @Inject
  private ProductService productService;

  @Inject
  private OrderService orderService;

  @Inject
  private AddressService addressService;

  @Inject
  private WeiXinService weiXinService;


  @RequestMapping(value = "/cart", method = RequestMethod.GET)
  public ModelAndView goProductPage() {

    return MvUtil.go("/weixin/cart");
  }

  @RequestMapping("/cart/ajax")
  public
  @ResponseBody
  List<CartDetailDto> getCartDetail(HttpServletRequest request) {
    String openId = weiXinService.getCurrentWeiXinUser(request).getOpenId();
    String cart = CookieUtils.getCookieValue(request, openId + "-cart");
    List<CartDetailDto> cartDetailDtos = null;
    if (cart != null) {
      cartDetailDtos = JsonUtils.jsonToList(cart, CartDetailDto.class);
      cartDetailDtos.stream().map(cartDetailDto -> {
        cartDetailDto.setProduct(productService.findOneProduct(cartDetailDto.getProduct().getId()));
        ProductSpec
            productSpec =
            productService.findOneProductSpec(cartDetailDto.getProductSpec().getId());
        cartDetailDto.setProductSpec(productSpec);
        if (cartDetailDto.getProductNumber() >= productSpec.getRepository()) {
          cartDetailDto.setProductNumber(productSpec.getRepository());
        }
        return cartDetailDto;
      }).collect(Collectors.toList());
    }
    return cartDetailDtos;
  }

  @RequestMapping(value = "/cart", method = RequestMethod.POST)
  public
  @ResponseBody
  LejiaResult addToCart(@RequestBody CartDetailDto cartDetailDto, HttpServletRequest request,
                        HttpServletResponse response) {
    String openId = weiXinService.getCurrentWeiXinUser(request).getOpenId();
    String cart = CookieUtils.getCookieValue(request, openId + "-cart");
    List<CartDetailDto> cartDetailDtos = null;
    LejiaResult lejiaResult = new LejiaResult();
    lejiaResult.setStatus(200);
    int cartNumber = 0;
    if (cart != null) {
      cartDetailDtos = JsonUtils.jsonToList(cart, CartDetailDto.class);
      boolean tag = true;
      for (CartDetailDto cartDetail : cartDetailDtos) {

        if (cartDetail.equals(cartDetailDto)) {
          ProductSpec
              productSpec =
              productService.findOneProductSpec(cartDetailDto.getProductSpec().getId());
          if (cartDetail.getProductNumber() + cartDetailDto.getProductNumber() <= productSpec
              .getRepository()) {
            cartDetail
                .setProductNumber(cartDetail.getProductNumber() + cartDetailDto.getProductNumber());
          } else {
            cartDetail
                .setProductNumber(productSpec.getRepository());
            lejiaResult.setData("库存不足");
          }
          tag = false;
        }
        cartNumber += cartDetail.getProductNumber();
      }
      if (tag) {
        cartDetailDtos.add(cartDetailDto);
        cartNumber += cartDetailDto.getProductNumber();
      }
    } else {
      cartDetailDtos = new ArrayList<>();
      cartDetailDtos.add(cartDetailDto);
      cartNumber += cartDetailDto.getProductNumber();
    }
    CookieUtils
        .setCookie(request, response, openId + "-cart", JsonUtils.objectToJson(cartDetailDtos),
                   Constants.COOKIE_DISABLE_TIME);
    lejiaResult.setMsg(cartNumber + "");
    return lejiaResult;

  }

  @RequestMapping(value = "/cart/cartNumber", method = RequestMethod.GET)
  public
  @ResponseBody
  LejiaResult addToCart(HttpServletRequest request) {
    String openId = weiXinService.getCurrentWeiXinUser(request).getOpenId();
    String cart = CookieUtils.getCookieValue(request, openId + "-cart");
    int cartNumber = 0;
    if (cart != null) {
      List<CartDetailDto> cartDetailDtos = JsonUtils.jsonToList(cart, CartDetailDto.class);
      for (CartDetailDto cartDetail : cartDetailDtos) {
        cartNumber += cartDetail.getProductNumber();
      }
    }
    return LejiaResult.build(200, cartNumber + "");
  }

  @RequestMapping(value = "/cart/deleteCart", method = RequestMethod.GET)
  public
  @ResponseBody
  LejiaResult deleteCart(@RequestParam long product, @RequestParam long productSpec,
                         HttpServletRequest request, HttpServletResponse response) {
    String openId = weiXinService.getCurrentWeiXinUser(request).getOpenId();
    String cart = CookieUtils.getCookieValue(request, openId + "-cart");
    int count = 0;
    List<CartDetailDto> cartDetailDtos = null;
    if (cart != null) {
      cartDetailDtos = JsonUtils.jsonToList(cart, CartDetailDto.class);
      for (CartDetailDto cartDetail : cartDetailDtos) {
        if (cartDetail.getProduct().getId() == product
            && cartDetail.getProductSpec().getId() == productSpec) {
          cartDetailDtos.remove(count);
          break;
        }
        count++;
      }
    }
    CookieUtils
        .setCookie(request, response, openId + "-cart", JsonUtils.objectToJson(cartDetailDtos),
                   Constants.COOKIE_DISABLE_TIME);
    return LejiaResult.build(200, "");
  }

  @RequestMapping(value = "/cart/changeNumber", method = RequestMethod.GET)
  public
  @ResponseBody
  LejiaResult changeProductNumber(@RequestParam long product, @RequestParam long productSpec,
                                  @RequestParam int number,
                                  HttpServletRequest request, HttpServletResponse response) {
    String openId = weiXinService.getCurrentWeiXinUser(request).getOpenId();
    String cart = CookieUtils.getCookieValue(request, openId + "-cart");
    int cartNumber = 0;
    List<CartDetailDto> cartDetailDtos = null;
    if (cart != null) {
      cartDetailDtos = JsonUtils.jsonToList(cart, CartDetailDto.class);
      for (CartDetailDto cartDetail : cartDetailDtos) {
        if (cartDetail.getProduct().getId() == product
            && cartDetail.getProductSpec().getId() == productSpec) {
          cartDetail.setProductNumber(number);
        }
        cartNumber += cartDetail.getProductNumber();
      }
    }
    CookieUtils
        .setCookie(request, response, openId + "-cart", JsonUtils.objectToJson(cartDetailDtos),
                   Constants.COOKIE_DISABLE_TIME);
    return LejiaResult.build(200, cartNumber + "");
  }

  @RequestMapping(value = "/cart/createCartOrder", method = RequestMethod.POST)
  public
  @ResponseBody
  LejiaResult createCartOrder(@RequestBody List<CartDetailDto> cartDetailDtos,
                              HttpServletRequest request, HttpServletResponse response) {
    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);

    Long count = orderService.getCurrentUserObligationOrdersCount(weiXinUser.getLeJiaUser());
    if (count >= 4) {
      return LejiaResult.build(5001, "未支付订单过多,请支付后再下单");
    }
    Address address = addressService.findAddressByLeJiaUserAndState(weiXinUser.getLeJiaUser());
    String cart = CookieUtils.getCookieValue(request, weiXinUser.getOpenId() + "-cart");
    List<CartDetailDto> cartDetailDtoOrigin = null;
    if (cart != null) {
      cartDetailDtoOrigin = JsonUtils.jsonToList(cart, CartDetailDto.class);
      cartDetailDtoOrigin.removeAll(cartDetailDtos);
      Map<String, Object>
          result =
          orderService.createCartOrder(cartDetailDtos, weiXinUser.getLeJiaUser(), address,
                                       5L);
      if ("200".equals(result.get("status").toString())) {
        CookieUtils
            .setCookie(request, response, weiXinUser.getOpenId() + "-cart",
                       JsonUtils.objectToJson(cartDetailDtoOrigin),
                       Constants.COOKIE_DISABLE_TIME);
      }
      return LejiaResult.build((Integer) result.get("status"), "ok", result.get("data"));
    }
    return LejiaResult.build(500, "购物车无数据");
  }
}
