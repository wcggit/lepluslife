package com.jifenke.lepluslive.global.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间相关工具类 Created by zhangwen on 16/10/31.
 */
public class DateUtils {

  private static SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");

  /**
   * 格式化时间为yyyyMMdd
   */
  public static String formatYYYYMMDD(Date date) {
    return yyyyMMdd.format(date);
  }

  /**
   * 获取今日的零点零分
   */
  public static Date getCurrDayBeginDate() {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    return calendar.getTime();
  }

  /**
   * 获取当前月的零点零分
   */
  public static Date getCurrMonthBeginDate() {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    calendar.set(Calendar.DAY_OF_MONTH, 1);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    return calendar.getTime();
  }

  /**
   * 获取本周一的零点零分
   */
  public static Date getCurrWeekBeginDate() {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    return calendar.getTime();
  }


}
