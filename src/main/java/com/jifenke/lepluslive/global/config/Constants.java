package com.jifenke.lepluslive.global.config;

/**
 * Application constants.
 */
public final class Constants {

    // Spring profile for development, production and "fast", see http://jhipster.github.io/profiles.html
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    public static final String SPRING_PROFILE_PRODUCTION = "prod";
    public static final String SPRING_PROFILE_FAST = "fast";
    // Spring profile used when deploying with Spring Cloud (used when deploying to CloudFoundry)
    public static final String SPRING_PROFILE_CLOUD = "cloud";
    // Spring profile used when deploying to Heroku
    public static final String SPRING_PROFILE_HEROKU = "heroku";

    public static final String SYSTEM_ACCOUNT = "system";

    public static final String WEI_XIN_TOKEN = "sdjbtq1457162257";

    public static final String BAR_CODE_EXT = "png";

    public static final Long ORDER_EXPIRED = 900000L;
    //public static final Long ORDER_EXPIRED = 90000L;

    public static final Long VALIDATECODE_EXPIRED = 300000L;  //验证码过期时间

    public static final String APPID = "wxe2190d22ce025e4f";

    public static final String WEI_XIN_ROOT_URL = "http://www.lepluslife.com";

    public static final Integer COOKIE_DISABLE_TIME = 604800;

    public static final String SMS_SEND_URL = "https://eco.taobao.com/router/rest";
    public static final String SMS_TEMPLATE_CODE = "SMS_8275442";  //注册模板id

    public static final Integer FREIGHT_PRICE = 1000;  //运费
    public static final Integer FREIGHT_FREE_PRICE = 12800;  //免运费最低价格

    private Constants() {
    }
}
