<%--
  Created by IntelliJ IDEA.
  User: zhangwen
  Date: 2017/6/22
  Time: 14:21
  团购单品主页
--%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/commen.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no"/>
    <meta name="format-detection" content="telephone=no">
    <meta name="format-detection" content="telephone=yes"/>
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="black"/>
    <title>${p.name}</title>
    <link rel="stylesheet" type="text/css" href="${commonResource}/css/reset.css">
    <link rel="stylesheet" type="text/css" href="${commonResource}/css/main.css">
    <link rel="stylesheet" type="text/css" href="${commonResource}/css/swiper.min.css">
    <link rel="stylesheet" type="text/css" href="${resource}/groupon/product_detail/css/index.css">
</head>
<body>
<!--banner-->
<section class="banner">
    <div class="swiper-container">
        <div class="swiper-wrapper">
            <c:forEach items="${scrollPicList}" var="scroll">
                <div class="swiper-slide">
                    <img src="${scroll.picture}" alt="">
                </div>
            </c:forEach>
        </div>
        <!-- Add Pagination -->
        <div class="swiper-pagination"></div>
    </div>
</section>

<!--团购券名+简介+特权-->
<section class="name_description">
    <p>${p.name}</p>
    <p>${p.description}</p>
    <div class="privilege fixClear">
        <div class="fixClear">
            <div><img src="${resource}/groupon/product_detail/img/Pri.png" alt=""></div>
            <c:if test="${p.reservation == 0}">
                <div>免预约</div>
            </c:if>
            <c:if test="${p.reservation != 0}">
                <div>提前${p.reservation}天预约</div>
            </c:if>
        </div>
        <div class="fixClear">
            <div><img src="${resource}/groupon/product_detail/img/Pri.png" alt=""></div>
            <c:if test="${p.refundType == 0}">
                <div>随时退款</div>
            </c:if>
            <c:if test="${p.refundType != 0}">
                <div>不可退款</div>
            </c:if>
        </div>
        <div class="fixClear">
            <div>已售${p.sellVolume}</div>
            <div><img src="${resource}/groupon/product_detail/img/noPri.png" alt=""></div>
        </div>
    </div>
</section>

<!--下单成功后返还-->
<c:if test="${state == 1}">
    <section class="payBack fixClear">
        <div>
            <img src="${resource}/groupon/product_detail/img/payBack.png" alt="">
        </div>
        <div>下单成功后，乐加奖励您<fmt:formatNumber
                type="number" value="${p.rebateScorea/100}" pattern="0.00"
                maxFractionDigits="2"/>元鼓励金＋<fmt:formatNumber
                type="number" value="${p.rebateScorec/100}" pattern="0.00"
                maxFractionDigits="2"/>金币
        </div>
    </section>
</c:if>

<!--可用门店-->
<section class="store">
    <div class="storeList fixClear">
        <div>可用门店（<span id="totalMerchant"></span>家）</div>
        <div><span class="arrowUp"></span></div>
    </div>
    <div class="oneStore fixClear">
        <div class="text">
            <p id="merchantName"></p>
            <p id="merchantLocation"></p>
            <p id="distanceOr" style="display: none"><span>离你最近</span><span
                    id="distance"></span></p>
        </div>
        <div id="call">
            <div>
                <img src="${resource}/groupon/product_detail/img/phone.png" alt="">
            </div>
        </div>
    </div>
    <div class="toStore fixClear">
        <div><img src="${resource}/groupon/product_detail/img/address.png" alt=""></div>
        <div>开启导航前往商家</div>
        <div><span class="arrowUp"></span></div>
    </div>
</section>

<!--详细明细-->
<section class="info">
    <div class="infoText fixClear">
        <div>详情明细</div>
        <div onclick="productPicList()"><span>查看图文详情</span><span class="arrowUp"></span></div>
    </div>
    <div class="xzImg">
        <img src="${p.explainPicture}" alt="">
    </div>
</section>

<!--购买须知-->
<section class="howToBuy">
    <div class="buyText fixClear">
        <div>购买须知</div>
    </div>
    <div class="xzImg">
        <img src="${p.instruction}" alt="">
    </div>
</section>

<footer class="fixClear">
    <div>
        <span>￥</span>
        <c:if test="${state == 1}">
            <span><fmt:formatNumber type="number" value="${p.ljPrice/100}" pattern="0.00"
                                    maxFractionDigits="2"/></span>
        </c:if>
        <c:if test="${state != 1}">
            <span><fmt:formatNumber type="number" value="${p.normalPrice/100}" pattern="0.00"
                                    maxFractionDigits="2"/></span>
        </c:if>
        <span>门市价</span>
        <span>¥<fmt:formatNumber type="number" value="${p.originPrice/100}" pattern="0.00"
                                 maxFractionDigits="2"/></span>
    </div>
    <div id="create" onclick="createOrder()">立即抢购</div>
</footer>
</body>
<script src="${commonResource}/js/jquery.min.js"></script>
<script src="${commonResource}/js/swiper.min.js"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
<script>
    var gps = {};
    gps.id = '${id}';
    //    banner auto
    var swiper = new Swiper('.swiper-container', {
        pagination: '.swiper-pagination',
        paginationClickable: true,
        spaceBetween: 0,
        centeredSlides: true,
        autoplay: 2500,
        autoplayDisableOnInteraction: false
    });
</script>
<script>
    /**打电话*/
    function phone(phone) {
        window.location.href = "tel://" + phone;
    }
    /**可使用商家列表页*/
    function merchantList(lat, lon, status) {
        window.location.href =
            "/groupon/merchantList?id=${id}&mId=${p.merchantUser.id}&status=" + status + "&lat="
            + lat
            + "&lon=" + lon;
    }
    /**图文详情*/
    function productPicList() {
        window.location.href = "/groupon/picList?id=${id}";
    }
    /**导航*/
    function openLocation(lat, lon, location) {
        wx.openLocation({
                            latitude: lat, // 纬度，浮点数，范围为90 ~ -90
                            longitude: lon, // 经度，浮点数，范围为180 ~ -180。
                            name: location, // 位置名
                            address: location, // 地址详情说明
                            scale: 20, // 地图缩放级别,整形值,范围从1~28。默认为最大
                            infoUrl: '' // 在查看位置界面底部显示的超链接,可点击跳转
                        });
    }
    /*****异步加载商家信息*/
    function ajaxMerchantList() {
        $.ajax({
                   type: "post",
                   url: "/groupon/ajaxMerchant",
                   data: gps,
                   success: function (data) {
                       var list = data.data;
                       if (list != null) {
                           $('#totalMerchant').html(list.length);
                           var first = list[0];
                           $('#merchantName').html(first.name);
                           $('#merchantLocation').html(first.location);
                           $('#call').attr('onclick', 'phone("' + first.phone + '")');
                           $('.toStore').attr('onclick',
                                              'openLocation(' + first.lat + ',' + first.lon + ',"'
                                              + first.location + '")');
                           if (list.length > 1) {
                               $('.storeList').attr('onclick',
                                                    'merchantList(' + first.lat + ',' + first.lon
                                                    + ','
                                                    + gps.status + ')');
                           }
                           if (gps.status === 1) {
                               $('#distanceOr').css('display', 'block');
                               var distance = first.distance;
                               if (distance < 1000) {
                                   $('#distance').html(' ' + distance + 'm');
                               } else {
                                   $('#distance').html(' ' + (distance / 1000).toFixed(1) + 'km');
                               }
                           }
                       }
                   }
               });
    }
</script>

<script>
    /**微信获取用户位置信息*/
    wx.config({
                  debug: false,
                  appId: '${wxConfig['appId']}',
                  timestamp: ${wxConfig['timestamp']},
                  nonceStr: '${wxConfig['noncestr']}',
                  signature: '${wxConfig['signature']}',
                  jsApiList: [
                      'getLocation', 'openLocation'
                  ]
              });
    wx.ready(function () {
        //获取地理位置
        wx.getLocation({
                           type: 'wgs84', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
                           success: function (res) {
                               var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
                               var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
                               gps.status = 1;
                               gps.lat = latitude;
                               gps.lon = longitude;
                               ajaxMerchantList();
                           },
                           fail: function (res) {
                               gps.status = 0;
                               $("#loading").hide();
                               ajaxMerchantList();
                           },
                           cancel: function (res) {
                               gps.status = 0;
                               $("#loading").hide();
                               ajaxMerchantList();
                           }
                       });
    });
    wx.error(function (res) {
        gps.status = 0;
        ajaxMerchantList();
    });
</script>
<script>
    /** 下单 */
    function createOrder() {
        $('#create').attr("onclick", "");
        $.ajax({
                   type: "post",
                   url: "/groupon/sign/create",
                   data: {payOrigin: 0, id: ${p.id}, source: 'WEB'},
                   success: function (data) {
                       if (data.status === 200) {
                           location.href =
                               "/front/order/weixin/groupon?orderId=" + data.data.orderId;
                       } else {
                           alert(data.msg);
                           $('#create').attr("onclick", "createOrder()");
                       }
                   }
               });
    }
</script>
</html>
