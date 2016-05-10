package com.jifenke.lepluslive.product.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wcg on 16/3/10.
 */
@RestController
public class weixinloginin {

  @RequestMapping("/lepluslive/wxSigin/index")
  public void wxLogin(){
    System.out.println(123);
  }
}
