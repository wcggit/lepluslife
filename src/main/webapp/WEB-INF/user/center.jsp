<%--
  Created by IntelliJ IDEA.
  User: zhangwen
  Date: 16/9/8
  Time: 下午8:58
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/WEB-INF/commen.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>

<title></title>
<head>
    <meta charset="utf-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <!--App自定义的css-->
    <link rel="stylesheet" href="${resourceUrl}/frontRes/user/center/css/swiper.min.css">
    <link rel="stylesheet" href="${resourceUrl}/frontRes/user/center/css/wallet.css">
    <link rel="stylesheet" href="${resourceUrl}/frontRes/css/reset.css">
</head>
<body>
<c:if test="${check.yd == 0}">
    <section class="shade yd" style="display: none">
        <div class="swiper-container swiper-shade">
            <div class="swiper-wrapper">
                <div class="swiper-slide">
                    <img src="${resourceUrl}/frontRes/user/center/img/guide1.jpg" alt="">

                    <div class="nextPage swiperNext">
                        <img src="${resourceUrl}/frontRes/user/center/img/next.png" alt="">
                    </div>
                </div>
                <div class="swiper-slide">
                    <img src="${resourceUrl}/frontRes/user/center/img/guide2.jpg" alt="">

                    <div class="nextPage swiperNext position">
                        <img src="${resourceUrl}/frontRes/user/center/img/next.png" alt="">
                    </div>
                </div>
                <div class="swiper-slide">
                    <img src="${resourceUrl}/frontRes/user/center/img/guide3.jpg" alt="">

                    <div class="nextPage know position">
                        <img src="${resourceUrl}/frontRes/user/center/img/know.png" alt="">
                    </div>
                </div>
            </div>
            <!-- Add Pagination -->
            <div class="swiper-pagination shadeColor"></div>
        </div>
    </section>
</c:if>
<c:if test="${check.hasPhone == 0}">
    <section class="shade hb" style="display: none">
        <div>
            <div class="esc">
                <div onclick="showYd()">
                    <img src="${resourceUrl}/frontRes/user/center/img/esc.png" alt="">
                </div>
            </div>
            <div class="swiper-container swiper-hb">
                <div class="swiper-wrapper swiper-bg">
                    <div class="swiper-slide">
                        <div class="w-text">
                            <img src="${resourceUrl}/frontRes/user/center/img/text1.png" alt="">
                        </div>
                    </div>
                    <div class="swiper-slide">
                        <div class="w-text" style="width: 100%;padding-top: 0;">
                            <img src="${resourceUrl}/frontRes/user/center/img/text3.png" alt="">
                        </div>
                    </div>
                </div>
                <!-- Add Pagination -->
                <div class="swiper-pagination shadeColor"></div>
            </div>
            <div><img style="width: 100%;"
                      src="${resourceUrl}/frontRes/user/center/img/hb-bg2.png"
                      alt="">
            </div>
            <div class="w-input">
                <input type="number" id="phoneNumber" placeholder="请输入手机号"/>

                <p id="warning"
                   style="display: none;color: red;text-align: left;padding: 0px 0 0px 7%;margin: -11px 0"></p>
                <br/>
                <button onclick="register()">立即领取</button>
            </div>
        </div>
    </section>
</c:if>
<section class="user">
    <div>
        <div class="headImg">
            <img src="${user.headImageUrl}" alt="">
        </div>
        <div>
            <p>${user.nickname}</p>
            <c:if test="${check.hasPhone == 1}">
                <p class="phoneFix">${user.leJiaUser.phoneNumber}</p>
            </c:if>
            <c:if test="${check.hasPhone == 0}">
                <p class="phoneFix">未绑定手机号</p>
            </c:if>
        </div>
        <div onclick="showPhone()">
            <img src="${resourceUrl}/frontRes/user/center/img/to.png" alt="">
        </div>
    </div>
</section>
<section class="own">
    <div class="ownMain" onclick="goPage('weixin/scoreDetail?type=0')">
        <div><span>${scoreA.score/100}</span>元</div>
        <div>
            <div>
                <img src="${resourceUrl}/frontRes/user/center/img/hongbao.png" alt="">
            </div>
            <div>红包</div>
        </div>
    </div>
    <div class="ownMain" onclick="goPage('weixin/scoreDetail?type=1')">
        <div><span>${scoreB.score}</span>积分</div>
        <div>
            <div>
                <img src="${resourceUrl}/frontRes/user/center/img/jifen.png" alt="">
            </div>
            <div>积分</div>
        </div>
    </div>
    <div class="ownMain msg">
        <div><span>0</span>张</div>
        <div>
            <div>
                <img src="${resourceUrl}/frontRes/user/center/img/youhuiquan.png" alt="">
            </div>
            <div>卡券</div>
        </div>
    </div>
</section>
<section class="module">
    <div onclick="goPage('merchant/index')">
        <div>
            <img src="${resourceUrl}/frontRes/user/center/img/zhoubian.png" alt="">
        </div>
        <p>周边好店</p>
    </div>
    <div onclick="goPage('front/product/weixin/productIndex')">
        <div>
            <img src="${resourceUrl}/frontRes/user/center/img/zhenpin.png" alt="">
        </div>
        <p>臻品商城</p>
    </div>
    <div class="msg">
        <div>
            <img src="${resourceUrl}/frontRes/user/center/img/jifenzhuanxiang.png" alt="">
        </div>
        <p>积分专享</p>
    </div>
</section>
<section class="module">
    <div class="msg">
        <div>
            <img src="${resourceUrl}/frontRes/user/center/img/lingquanzhongxin.png" alt="">
        </div>
        <p>领券中心</p>
    </div>
    <div class="msg">
        <div>
            <img src="${resourceUrl}/frontRes/user/center/img/yinhangka.png" alt="">
        </div>
        <p>绑定银行卡</p>
    </div>
    <div class="msg">
        <div>
            <img src="${resourceUrl}/frontRes/user/center/img/jiaoyijilu.png" alt="">
        </div>
        <p>交易记录</p>
    </div>
</section>
<section class="banner">
    <div class="swiper-container swiper-banner">
        <div class="swiper-wrapper" id="banner">
        </div>
        <div class="swiper-pagination"></div>
    </div>
</section>
</body>
<script src="${resourceUrl}/js/jquery-2.0.3.min.js"></script>
<script src="${resourceUrl}/frontRes/user/center/js/swiper.min.js"></script>
<script src="${resourceUrl}/frontRes/user/center/js/layer.js"></script>
<script>
    function ydLive() {
        var swiper1 = new Swiper('.swiper-shade', {
            pagination: '.swiper-pagination',
            nextButton: '.swiperNext',
            paginationClickable: true
        });
    }

    function showSwiper() {
        var swiper = new Swiper('.swiper-banner', {
            pagination: '.swiper-pagination',
            paginationClickable: true,
            spaceBetween: 30,
            centeredSlides: true,
            autoplay: 2500,
            autoplayDisableOnInteraction: false
        });
    }

    function showPhoneModel() {
        var swiper2 = new Swiper('.swiper-hb', {
            pagination: '.swiper-pagination',
            paginationClickable: true,
            spaceBetween: 30,
            centeredSlides: true,
            autoplayDisableOnInteraction: false
        });
    }

</script>
<script>
    var yd = '${check.yd}'; //是否引导过 1=引导过，无需出现引导页
    var phone = '${check.phone}'; //7天内是否出现过手机号弹窗 1=出现过，无需出现
    //    $(".hb").hide();
    $(".yd .swiper-container").css("height", $(window).height() + "px");
    $(".hb").css("height", $(window).height() - 30 + "px");
    $(".headImg").css("height", $(".headImg").width() + "px");
    $(".swiper-hb").css("height", $(window).width() * 0.8 * 516 / 666 + 18 + "px");
    $(".know").click(function () {
        $(".yd").hide();
        if (phone == 0) {
            $(".hb").show();  //手机号弹窗
            showPhoneModel();
        }
    });
    $(".hb img").click(function () {
        $(".hb").hide();
    });
</script>

<script type="application/javascript">
    document.title = "我的钱包";
    function getBanners() {
        $.ajax({
                   type: "get",
                   url: "/app/banner/home/1",
                   success: function (data) {
                       var list = data.data, i = 0, banner = $("#banner"), content = "";

                       for (i; i < list.length; i++) {
                           var image = '<div class="swiper-slide">' +
                                       '<input type="hidden" value="' + list[i].merchantId + '"/>' +
                                       '<input type="hidden" value="' + list[i].productId + '"/>' +
                                       '<input type="hidden" value="' + list[i].url + '"/>' +
                                       '<input type="hidden" value="' + list[i].afterType + '"/>' +
                                       '<img onclick="clickPic(this)" src="' + list[i].picture
                                       + '" alt=""></div>';
                           content += image;
                       }
                       banner.html(content);
                       showSwiper();
                   }
               });
    }
    window.onload = getBanners();
    //    var phone = 0;
    //    var yd = 0;
    if (yd == 0) {
        $(".yd").show();
        ydLive();
        yd = 1;
    } else if (phone == 0) {
        $(".hb").show();  //手机号弹窗
        showPhoneModel();
    }

    function showYd() {
        if (yd == 0) {
            $(".yd").show();
            ydLive();
            yd = 1;
        }
    }

    function register() {
        var phoneNumber = $("#phoneNumber").val();
        if ((!(/^1[3|4|5|6|7|8]\d{9}$/.test(phoneNumber)))) {
            alert("请输入正确的手机号");
            return false;
        }
        $.ajax({
                   type: "get",
                   url: "/weixin//subPage/open?phoneNumber=" + phoneNumber,
                   success: function (data) {
                       if (data.status == 200) {
                           $(".hb").hide();
                           $(".phoneFix").html(phoneNumber);
                           if (yd == 0) {
                               $(".yd").show();
                               ydLive();
                               yd = 1;
                           }
                       } else {
                           $("#warning").show();
                           $("#warning").html("手机号已被占用");
                       }
                   }
               });
    }
    $(".msg").click(function () {
        msg();
    });
    function msg() {
        layer.msg('暂未开通，敬请期待');
    }
    function showPhone() {
        $(".hb").show();
        showPhoneModel();
    }
    function goPage(page) {
        //  location.href = "${wxRootUrl}/" + page;
        location.href = "/" + page;
    }

    function clickPic(o) {
        var afterType = $(o).prev().val();
        if (afterType == 1) {
            var url = $(o).prev().prev().val();
            window.location.href = "http://" + url;
        } else if (afterType == 2) {
            var productId = $(o).prev().prev().prev().val();
            location.href = "/weixin/product/" + productId;
        } else if (afterType == 3) {
            var merchantId = $(o).prev().prev().prev().prev().val();
            location.href = "merchant/info/" + merchantId + "?status=0";
        }
    }
</script>
</html>
