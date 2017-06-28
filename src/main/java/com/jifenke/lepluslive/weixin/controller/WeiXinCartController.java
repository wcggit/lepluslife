package com.jifenke.lepluslive.weixin.controller;

import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.global.util.CookieUtils;
import com.jifenke.lepluslive.global.util.JsonUtils;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.product.domain.entities.ProductSpec;
import com.jifenke.lepluslive.product.service.ProductService;
import com.jifenke.lepluslive.weixin.controller.dto.CartDetailDto;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

  @Inject
  private ProductService productService;

  @RequestMapping(value = "/cart", method = RequestMethod.GET)
  public ModelAndView goProductPage() {

    return MvUtil.go("/weixin/cart");
  }

  @RequestMapping("/cart/ajax")
  public List<CartDetailDto> getCartDetail(HttpServletRequest request) {
    String cardCookie = CookieUtils.getCookieValue(request, "leJiaUnionId") + "-cart";
    String cart = CookieUtils.getCookieValue(request, cardCookie);
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
  public LejiaResult addToCart(@RequestBody CartDetailDto cartDetailDto, HttpServletRequest request,
                               HttpServletResponse response) {
    String cardCookie = CookieUtils.getCookieValue(request, "leJiaUnionId") + "-cart";
    String cart = CookieUtils.getCookieValue(request, cardCookie);
    List<CartDetailDto> cartDetailDtos = null;
    LejiaResult lejiaResult = new LejiaResult();
    lejiaResult.setStatus(200);
    int cartNumber = 0;
    if (cart != null && !"null".equalsIgnoreCase(cart)) {
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
        .setCookie(request, response, cardCookie, JsonUtils.objectToJson(cartDetailDtos),
                   Constants.COOKIE_DISABLE_TIME);
    lejiaResult.setMsg(cartNumber + "");
    return lejiaResult;

  }

  @RequestMapping(value = "/cart/cartNumber", method = RequestMethod.GET)
  public LejiaResult addToCart(HttpServletRequest request) {
    String cardCookie = CookieUtils.getCookieValue(request, "leJiaUnionId") + "-cart";
    String cart = CookieUtils.getCookieValue(request, cardCookie);
    int cartNumber = 0;
    if (cart != null && !"null".equals(cart)) {
      List<CartDetailDto> cartDetailDtos = JsonUtils.jsonToList(cart, CartDetailDto.class);
      for (CartDetailDto cartDetail : cartDetailDtos) {
        cartNumber += cartDetail.getProductNumber();
      }
    }
    return LejiaResult.build(200, cartNumber + "");
  }

  @RequestMapping(value = "/cart/deleteCart", method = RequestMethod.GET)
  public LejiaResult deleteCart(@RequestParam long product, @RequestParam long productSpec,
                                HttpServletRequest request, HttpServletResponse response) {
    String cardCookie = CookieUtils.getCookieValue(request, "leJiaUnionId") + "-cart";
    String cart = CookieUtils.getCookieValue(request, cardCookie);
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
        .setCookie(request, response, cardCookie, JsonUtils.objectToJson(cartDetailDtos),
                   Constants.COOKIE_DISABLE_TIME);
    return LejiaResult.build(200, "");
  }

  @RequestMapping(value = "/cart/changeNumber", method = RequestMethod.GET)
  public LejiaResult changeProductNumber(@RequestParam long product, @RequestParam long productSpec,
                                         @RequestParam int number,
                                         HttpServletRequest request, HttpServletResponse response) {
    String cardCookie = CookieUtils.getCookieValue(request, "leJiaUnionId") + "-cart";
    String cart = CookieUtils.getCookieValue(request, cardCookie);
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
        .setCookie(request, response, cardCookie, JsonUtils.objectToJson(cartDetailDtos),
                   Constants.COOKIE_DISABLE_TIME);
    return LejiaResult.build(200, cartNumber + "");
  }
}
