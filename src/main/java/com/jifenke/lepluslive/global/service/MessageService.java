package com.jifenke.lepluslive.global.service;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

import javax.inject.Inject;

/**
 * 国际化 Created by zhangwen on 2016/10/9.
 */
@Service
public class MessageService {

  @Inject
  private MessageSource messageSource;

  public String getMsg(String status) {
    String msg = messageSource.getMessage(status, null, Locale.getDefault());
    try {
      return new String(msg.getBytes("iso8859-1"), "utf-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return null;
    }
  }

  public String getMsg(String status, String[] o) {
    try {
      for (int i = 0; i < o.length; i++) {
        o[i] = new String(o[i].getBytes("utf-8"), "iso8859-1");
      }
      String msg = messageSource.getMessage(status, o, Locale.getDefault());
      return new String(msg.getBytes("iso8859-1"), "utf-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return null;
    }
  }


}
