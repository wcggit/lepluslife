<%--
  Created by IntelliJ IDEA.
  User: zhangwen
  Date: 2017/4/16
  Time: 16:31
  商家详情页
--%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/commen.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="viewport" id="viewport" content="width=device-width, initial-scale=1">
    <meta name="format-detection" content="telephone=no">
    <meta name="format-detection" content="telephone=yes"/>
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="black"/>
    <title>${merchant.name}</title>
    <link rel="stylesheet" href="${resourceUrl}/frontRes/css/reset.css">
    <link rel="stylesheet" href="${resourceUrl}/frontRes/shop/detail/css/shopInfo.css">
    <link rel="stylesheet" href="${resourceUrl}/frontRes/css/swiper.min.css">
</head>
<body>
<section>
    <!-- Swiper -->
    <div class="swiper-container">
        <div class="swiper-wrapper">
            <c:if test="${hasScroll == 0}">
                <div class="swiper-slide"><img
                        src="${resourceUrl}/frontRes/merchant/img/infoLogo.jpg" alt=""></div>
            </c:if>
            <c:if test="${hasScroll != 0}">
                <c:forEach items="${scrolls}" var="scroll">
                    <div class="swiper-slide"><img src="${scroll.picture}" alt=""></div>
                </c:forEach>
            </c:if>
        </div>
        <!-- Add Pagination -->
        <div class="swiper-pagination"></div>
    </div>
</section>
<section class="title">
    <p>${merchant.name}</p>

    <div class="fixClear">
        <div class="star fixClear"></div>
        <div class="price">¥ <fmt:formatNumber
                type="number" value="${merchant.merchantInfo.perSale/100}"
                pattern="0.00"
                maxFractionDigits="2"/>/人
        </div>
        <c:if test="${status == 1}">
            <div class="distance"></div>
        </c:if>
    </div>
</section>
<section class="shopInfo">
    <div class="fixClear">
        <div><img src="${resourceUrl}/frontRes/shop/index/img/addressIcon.png" alt=""></div>
        <div onclick="openLocation()">${merchant.location}</div>
        <div onclick="phone()"><img src="${resourceUrl}/frontRes/shop/detail/img/toCall.png" alt="">
        </div>
    </div>
    <c:if test="${shopNum > 0}">
        <div class="fixClear findShop">
            <div style="margin-top: 7px;"><img
                    src="${resourceUrl}/frontRes/shop/detail/img/shopIcon.png" alt=""></div>
            <div>其他${shopNum}家门店</div>
            <div class="arrowUp"></div>
        </div>
    </c:if>
</section>
<c:if test="${policy.importScoreCScale != null && policy.importScoreCScale > 0}">
    <section class="shopInfo shopOffer">
            <%--<div class="fixClear">--%>
            <%--<div style="margin-top: 7px;"><img--%>
            <%--src="${resourceUrl}/frontRes/shop/detail/img/sellIcon.png" alt=""></div>--%>
            <%--<div>本店51期间全场八八折，更有海量优惠活动哦！</div>--%>
            <%--<div></div>--%>
            <%--</div>--%>
        <div class="fixClear VIP">
            <div style="margin-top: 7px;"><img
                    src="${resourceUrl}/frontRes/shop/detail/img/awardIcon.png" alt=""></div>
            <div>消费奖励<fmt:formatNumber
                    type="number" value="${policy.importScoreCScale}"
                    pattern="0.0"
                    maxFractionDigits="1"/>%金币
            </div>
            <div></div>
        </div>
    </section>
</c:if>
<section class="shopInfo shopOther" onclick="buyScore()">
    <div class="fixClear">
        <div style="margin-top: 7px;"><img
                src="${resourceUrl}/frontRes/shop/detail/img/cardIcon.png" alt=""></div>
        <div>在线储值</div>
        <div class="arrowUp"></div>
    </div>
</section>
<section class="buyButton">
    <p onclick="goPay()">在线买单</p>
</section>
<section class="VIPBuyButton fixClear">
    <div>您有¥<fmt:formatNumber
            type="number" value="${score/100}"
            pattern="0.00"
            maxFractionDigits="2"/>鼓励金，买单可用
    </div>
    <div onclick="goPay()">优惠买单</div>
</section>

<section class="layer2">
    <div class="layer2Title">请选择您消费的门店</div>
    <div class="shopList">
        <c:forEach items="${list}" var="m">
            <c:if test="${m.id != merchant.id}">
                <p onclick="otherShop(${m.id})">${m.name}</p>
            </c:if>
        </c:forEach>
    </div>
    <div class="closeLayer">
        <img src="${resourceUrl}/frontRes/shop/detail/img/close.png" alt="">
    </div>
</section>
</body>
<script src="${resourceUrl}/js/jquery.min.js"></script>
<script src="${resourceUrl}/frontRes/js/swiper.min.js"></script>
<script src="${resourceUrl}/frontRes/js/layer.js"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script>
    wx.config({
                  debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                  appId: '${wxConfig['appId']}', // 必填，公众号的唯一标识
                  timestamp: ${wxConfig['timestamp']}, // 必填，生成签名的时间戳
                  nonceStr: '${wxConfig['noncestr']}', // 必填，生成签名的随机串
                  signature: '${wxConfig['signature']}',// 必填，签名，见附录1
                  jsApiList: [
                      'openLocation'
                  ] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
              });
    wx.ready(function () {
    });
    wx.error(function (res) {
    });

    function openLocation() {
        wx.openLocation({
                            latitude: ${merchant.lat}, // 纬度，浮点数，范围为90 ~ -90
                            longitude: ${merchant.lng}, // 经度，浮点数，范围为180 ~ -180。
                            name: '${merchant.location}', // 位置名
                            address: '${merchant.location}', // 地址详情说明
                            scale: 20, // 地图缩放级别,整形值,范围从1~28。默认为最大
                            infoUrl: '' // 在查看位置界面底部显示的超链接,可点击跳转
                        });
    }

</script>
<script>

    /** 进入商家详情事件统计*/
    $.get("/front/visit/shop/${merchant.id}");
    /**
     * swiper
     * banner滑动图插件
     */
    var swiper = new Swiper('.swiper-container', {
        pagination: '.swiper-pagination',
        paginationClickable: true,
        spaceBetween: 30,
        centeredSlides: true,
        autoplay: 2500,
        autoplayDisableOnInteraction: false
    });

    /**
     * layer
     * layer弹窗插件
     */
    $(".findShop").click(function () {
        layer.open({
                       type: 1,
                       title: false,
                       closeBtn: 0,
                       area: ['80%', '300px'], //宽高
                       content: $(".layer2")
                   });
    });
    $(".closeLayer").click(function () {
        layer.closeAll();
    });

    /**
     * isOpenCard
     * 检测是否开卡 0:未开；1：已开
     */
    var isOpenCard = eval('${merchant.receiptAuth}');
    if (isOpenCard == 0) {
        $(".VIP").hide();
        $(".buyButton").show();
        $(".VIPBuyButton").hide();
    } else {
        $(".VIP").show();
        $(".buyButton").hide();
        $(".VIPBuyButton").show();
    }

    /** 距离 */
    var distance = '${distance}';
    $(".distance").html(distance > 1000 ? ((distance / 1000).toFixed(1) + "km") : distance
                                                                                  + "m");
    /**
     * star
     * score
     * 实现星级评定
     */
    var star = ${merchant.merchantInfo.star};
    var distance = '${distance}';
    $("#distance").html(distance > 1000 ? ((distance / 1000).toFixed(1) + "km") : distance
                                                                                  + "m");
    if (star > 5) {
        star = 5;
    } else if (star < 0) {
        star = 0;
    }
    drawStar(star, "${resourceUrl}/frontRes/shop/detail/img/Star.png");
    drawStar(5 - star, "${resourceUrl}/frontRes/shop/detail/img/Star_.png");
    function drawStar(num, url) {
        for (var i = 0; i < num; i++) {
            $(".star").append(
                $("<div></div>").append(
                    $("<img />").attr("src", url)
                )
            )
        }
    }
    /**线下支付*/
    function goPay() {
        window.location.href = 'http://www.lepluslife.com/lepay/merchant/${merchant.merchantSid}';
    }
    /**跳转到其他门店*/
    function otherShop(shopId) {
        window.location.href = "/front/shop/weixin/m?status=0&id=" + shopId;
    }
    /**跳转到其他门店*/
    function phone() {
        window.location.href = "tel://${merchant.phoneNumber}";
    }
    /**跳转到其他门店*/
    function buyScore() {
        alert('即将上线，敬请期待！');
    }
</script>
</html>