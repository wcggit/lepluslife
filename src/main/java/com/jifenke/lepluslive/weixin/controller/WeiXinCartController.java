package com.jifenke.lepluslive.weixin.controller;

import com.jifenke.lepluslive.Address.domain.entities.Address;
import com.jifenke.lepluslive.Address.service.AddressService;
import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.global.util.CookieUtils;
import com.jifenke.lepluslive.global.util.JsonUtils;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.order.domain.entities.OnLineOrder;
import com.jifenke.lepluslive.order.service.OrderService;
import com.jifenke.lepluslive.product.domain.entities.ProductSpec;
import com.jifenke.lepluslive.product.service.ProductService;
import com.jifenke.lepluslive.weixin.controller.dto.CartDetailDto;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.service.DictionaryService;
import com.jifenke.lepluslive.weixin.service.WeiXinUserService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
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

  @Value("${weixin.appId}")
  private String appid;

  @Inject
  private ProductService productService;

  @Inject
  private OrderService orderService;

  @Inject
  private DictionaryService dictionaryService;

  @Inject
  private AddressService addressService;

  @Inject
  private WeiXinUserService weiXinUserService;


  @RequestMapping(value = "/cart", method = RequestMethod.GET)
  public ModelAndView goProductPage(HttpServletRequest request, HttpServletResponse response,
                                    Model model) {

    return MvUtil.go("/weixin/cart");
  }

  @RequestMapping("/cart/ajax")
  public
  @ResponseBody
  List<CartDetailDto> getCartDetail(HttpServletRequest request, HttpServletResponse response) {
    String openId = CookieUtils.getCookieValue(request, appid + "-user-open-id");
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
    String openId = CookieUtils.getCookieValue(request, appid + "-user-open-id");

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
      cartDetailDtos = new ArrayList<CartDetailDto>();
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
  LejiaResult addToCart(HttpServletRequest request, HttpServletResponse response) {
    String openId = CookieUtils.getCookieValue(request, appid + "-user-open-id");
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
    String openId = CookieUtils.getCookieValue(request, appid + "-user-open-id");
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
    String openId = CookieUtils.getCookieValue(request, appid + "-user-open-id");
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
    String openId = CookieUtils.getCookieValue(request, appid + "-user-open-id");
    WeiXinUser weiXinUser = weiXinUserService.findWeiXinUserByOpenId(openId);

    Long count = orderService.getCurrentUserObligationOrdersCount(weiXinUser.getLeJiaUser());
    if (count >= 4) {
      return LejiaResult.build(500, "未支付订单过多,请支付后再下单");
    }
    Address address = addressService.findAddressByLeJiaUserAndState(weiXinUser.getLeJiaUser());
    String cart = CookieUtils.getCookieValue(request, openId + "-cart");
    List<CartDetailDto> cartDetailDtoOrigin = null;
    OnLineOrder onLineOrder = null;
    if (cart != null) {
      cartDetailDtoOrigin = JsonUtils.jsonToList(cart, CartDetailDto.class);
      cartDetailDtoOrigin.removeAll(cartDetailDtos);
      //免运费最低价格
      Integer
          FREIGHT_FREE_PRICE =
          Integer.parseInt(dictionaryService.findDictionaryById(1L).getValue());
      onLineOrder =
          orderService.createCartOrder(cartDetailDtos, weiXinUser.getLeJiaUser(), address,
                                       FREIGHT_FREE_PRICE);
    }

    CookieUtils
        .setCookie(request, response, openId + "-cart", JsonUtils.objectToJson(cartDetailDtoOrigin),
                   Constants.COOKIE_DISABLE_TIME);
    return LejiaResult.build(200, onLineOrder.getId() + "");
  }
}
