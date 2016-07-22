<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/WEB-INF/commen.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    <title></title>
    <link rel="stylesheet" href="${resourceUrl}/frontRes/merchant/css/reset.css">
    <link rel="stylesheet" href="${resourceUrl}/frontRes/merchant/css/w-3.css">
</head>
<body>
<!--轮播图-->
<div id="jssor_1">
    <!-- Loading Screen -->
    <div data-u="slides" class="lb-main">
        <c:if test="${hasScroll == 0}">
            <div data-p="144.50">
                <img data-u="image" src="${resourceUrl}/frontRes/merchant/img/infoLogo.jpg"/>
            </div>
        </c:if>
        <c:if test="${hasScroll != 0}">
            <c:forEach items="${scrolls}" var="scroll">
                <div data-p="144.50">
                    <img data-u="image" src="${scroll.picture}"/>
                </div>
            </c:forEach>
        </c:if>
    </div>
    <!-- Thumbnail Navigator -->
    <div data-u="thumbnavigator" class="jssort01"
         style="position:absolute;left:0px;bottom:0px;width:800px;height:100px;"
         data-autocenter="1">
        <!-- Thumbnail Item Skin Begin -->
        <div data-u="slides" style="cursor: default;">
            <div data-u="prototype" class="p">
                <div class="w">
                    <div data-u="thumbnailtemplate" class="t"></div>
                </div>
                <div class="c"></div>
            </div>
        </div>
        <!-- Thumbnail Item Skin End -->
    </div>
    <!-- Arrow Navigator -->
    <span data-u="arrowleft" class="jssora05l"
          style="top:158px;left:8px;width:40px;height:40px;"></span>
    <span data-u="arrowright" class="jssora05r"
          style="top:158px;right:8px;width:40px;height:40px;"></span>

</div>

<section class="main">
    <p>${merchant.name}</p>

    <div class="star"></div>
    <div class="information">
        <div>
            <div><img
                    src="${resourceUrl}/frontRes/merchant/img/infotype${merchant.merchantType.id}.png"
                    alt=""></div>
            <span>${merchant.merchantType.name}</span>
        </div>
        <div>
            <img src="${resourceUrl}/frontRes/merchant/img/fengexian.png" alt=""/>
        </div>
        <div>
            <div><img src="${resourceUrl}/frontRes/merchant/img/where.png" alt=""></div>
            <span>${merchant.city.name}</span>
        </div>
    </div>
</section>
<section class="tab">
    <c:if test="${status == 1}">
        <div>
            <div id="distance"></div>
        </div>
    </c:if>
    <c:if test="${merchant.merchantInfo.wifi == 1}">
        <div>
            <div>有WiFi</div>
        </div>
    </c:if>
    <c:if test="${merchant.merchantInfo.card == 1}">
        <div>
            <div>可刷卡</div>
        </div>
    </c:if>
    <c:if test="${merchant.merchantInfo.park == 1}">
        <div>
            <div>有停车位</div>
        </div>
    </c:if>
</section>
<section class="action" onclick="openLocation()">
    <div>
        <img src="${resourceUrl}/frontRes/merchant/img/where-to.png" alt="">
    </div>
    <div>
        ${merchant.location}
    </div>
    <div>
        <img src="${resourceUrl}/frontRes/merchant/img/to.png" alt="">
    </div>
</section>
<a class="aStyle" href="tel:${merchant.phoneNumber}">
    <section class="action">
        <div>
            <img src="${resourceUrl}/frontRes/merchant/img/phone.png" alt="">
        </div>
        <div>
            ${merchant.phoneNumber}
        </div>
        <div>
            <img src="${resourceUrl}/frontRes/merchant/img/to.png" alt="">
        </div>
    </section>
</a>
<footer>
    <div>红包余额：<span style="font-weight: bold;">￥${scoreA.score /100}</span></div>
    <div>红包可在本店抵现，消费再送红包</div>
</footer>

</body>
<script src="${resourceUrl}/js/jquery-1.11.3.min.js"></script>
<script src="${resourceUrl}/frontRes/merchant/js/jssor.slider.mini.js"></script>
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
<!--轮播图-->
<script>
    jQuery(document).ready(function ($) {

        var jssor_1_SlideshowTransitions = [
            {
                $Duration: 1200,
                x: 0.3,
                $During: {$Left: [0.3, 0.7]},
                $Easing: {$Left: $Jease$.$InCubic, $Opacity: $Jease$.$Linear},
                $Opacity: 2
            },
            {
                $Duration: 1200,
                x: -0.3,
                $SlideOut: true,
                $Easing: {$Left: $Jease$.$InCubic, $Opacity: $Jease$.$Linear},
                $Opacity: 2
            },
            {
                $Duration: 1200,
                x: -0.3,
                $During: {$Left: [0.3, 0.7]},
                $Easing: {$Left: $Jease$.$InCubic, $Opacity: $Jease$.$Linear},
                $Opacity: 2
            },
            {
                $Duration: 1200,
                x: 0.3,
                $SlideOut: true,
                $Easing: {$Left: $Jease$.$InCubic, $Opacity: $Jease$.$Linear},
                $Opacity: 2
            },
            {
                $Duration: 1200,
                y: 0.3,
                $During: {$Top: [0.3, 0.7]},
                $Easing: {$Top: $Jease$.$InCubic, $Opacity: $Jease$.$Linear},
                $Opacity: 2
            },
            {
                $Duration: 1200,
                y: -0.3,
                $SlideOut: true,
                $Easing: {$Top: $Jease$.$InCubic, $Opacity: $Jease$.$Linear},
                $Opacity: 2
            },
            {
                $Duration: 1200,
                y: -0.3,
                $During: {$Top: [0.3, 0.7]},
                $Easing: {$Top: $Jease$.$InCubic, $Opacity: $Jease$.$Linear},
                $Opacity: 2
            },
            {
                $Duration: 1200,
                y: 0.3,
                $SlideOut: true,
                $Easing: {$Top: $Jease$.$InCubic, $Opacity: $Jease$.$Linear},
                $Opacity: 2
            },
            {
                $Duration: 1200,
                x: 0.3,
                $Cols: 2,
                $During: {$Left: [0.3, 0.7]},
                $ChessMode: {$Column: 3},
                $Easing: {$Left: $Jease$.$InCubic, $Opacity: $Jease$.$Linear},
                $Opacity: 2
            },
            {
                $Duration: 1200,
                x: 0.3,
                $Cols: 2,
                $SlideOut: true,
                $ChessMode: {$Column: 3},
                $Easing: {$Left: $Jease$.$InCubic, $Opacity: $Jease$.$Linear},
                $Opacity: 2
            },
            {
                $Duration: 1200,
                y: 0.3,
                $Rows: 2,
                $During: {$Top: [0.3, 0.7]},
                $ChessMode: {$Row: 12},
                $Easing: {$Top: $Jease$.$InCubic, $Opacity: $Jease$.$Linear},
                $Opacity: 2
            },
            {
                $Duration: 1200,
                y: 0.3,
                $Rows: 2,
                $SlideOut: true,
                $ChessMode: {$Row: 12},
                $Easing: {$Top: $Jease$.$InCubic, $Opacity: $Jease$.$Linear},
                $Opacity: 2
            },
            {
                $Duration: 1200,
                y: 0.3,
                $Cols: 2,
                $During: {$Top: [0.3, 0.7]},
                $ChessMode: {$Column: 12},
                $Easing: {$Top: $Jease$.$InCubic, $Opacity: $Jease$.$Linear},
                $Opacity: 2
            },
            {
                $Duration: 1200,
                y: -0.3,
                $Cols: 2,
                $SlideOut: true,
                $ChessMode: {$Column: 12},
                $Easing: {$Top: $Jease$.$InCubic, $Opacity: $Jease$.$Linear},
                $Opacity: 2
            },
            {
                $Duration: 1200,
                x: 0.3,
                $Rows: 2,
                $During: {$Left: [0.3, 0.7]},
                $ChessMode: {$Row: 3},
                $Easing: {$Left: $Jease$.$InCubic, $Opacity: $Jease$.$Linear},
                $Opacity: 2
            },
            {
                $Duration: 1200,
                x: -0.3,
                $Rows: 2,
                $SlideOut: true,
                $ChessMode: {$Row: 3},
                $Easing: {$Left: $Jease$.$InCubic, $Opacity: $Jease$.$Linear},
                $Opacity: 2
            },
            {
                $Duration: 1200,
                x: 0.3,
                y: 0.3,
                $Cols: 2,
                $Rows: 2,
                $During: {$Left: [0.3, 0.7], $Top: [0.3, 0.7]},
                $ChessMode: {$Column: 3, $Row: 12},
                $Easing: {
                    $Left: $Jease$.$InCubic,
                    $Top: $Jease$.$InCubic,
                    $Opacity: $Jease$.$Linear
                },
                $Opacity: 2
            },
            {
                $Duration: 1200,
                x: 0.3,
                y: 0.3,
                $Cols: 2,
                $Rows: 2,
                $During: {$Left: [0.3, 0.7], $Top: [0.3, 0.7]},
                $SlideOut: true,
                $ChessMode: {$Column: 3, $Row: 12},
                $Easing: {
                    $Left: $Jease$.$InCubic,
                    $Top: $Jease$.$InCubic,
                    $Opacity: $Jease$.$Linear
                },
                $Opacity: 2
            },
            {
                $Duration: 1200,
                $Delay: 20,
                $Clip: 3,
                $Assembly: 260,
                $Easing: {$Clip: $Jease$.$InCubic, $Opacity: $Jease$.$Linear},
                $Opacity: 2
            },
            {
                $Duration: 1200,
                $Delay: 20,
                $Clip: 3,
                $SlideOut: true,
                $Assembly: 260,
                $Easing: {$Clip: $Jease$.$OutCubic, $Opacity: $Jease$.$Linear},
                $Opacity: 2
            },
            {
                $Duration: 1200,
                $Delay: 20,
                $Clip: 12,
                $Assembly: 260,
                $Easing: {$Clip: $Jease$.$InCubic, $Opacity: $Jease$.$Linear},
                $Opacity: 2
            },
            {
                $Duration: 1200,
                $Delay: 20,
                $Clip: 12,
                $SlideOut: true,
                $Assembly: 260,
                $Easing: {$Clip: $Jease$.$OutCubic, $Opacity: $Jease$.$Linear},
                $Opacity: 2
            }
        ];

        var jssor_1_options = {
            $AutoPlay: true,
            $SlideshowOptions: {
                $Class: $JssorSlideshowRunner$,
                $Transitions: jssor_1_SlideshowTransitions,
                $TransitionsOrder: 1
            },
            $ArrowNavigatorOptions: {
                $Class: $JssorArrowNavigator$
            },
            $ThumbnailNavigatorOptions: {
                $Class: $JssorThumbnailNavigator$,
                $Cols: 10,
                $SpacingX: 8,
                $SpacingY: 8,
                $Align: 360
            }
        };

        var jssor_1_slider = new $JssorSlider$("jssor_1", jssor_1_options);

        //responsive code begin
        //you can remove responsive code if you don't want the slider scales while window resizing
        function ScaleSlider() {
            var refSize = jssor_1_slider.$Elmt.parentNode.clientWidth;
            if (refSize) {
                refSize = Math.min(refSize, 800);
                jssor_1_slider.$ScaleWidth(refSize);
            }
            else {
                window.setTimeout(ScaleSlider, 30);
            }
        }

        ScaleSlider();
        $(window).bind("load", ScaleSlider);
        $(window).bind("resize", ScaleSlider);
        $(window).bind("orientationchange", ScaleSlider);
        //responsive code end
    });
</script>
<!--star-->
<script>
    var star = ${merchant.merchantInfo.star};
    var distance = '${distance}';
    $("#distance").html(distance > 1000 ? ((distance / 1000).toFixed(1) + "km") : distance
                                                                                  + "m");
    if (star > 5) {
        star = 5;
    } else if (star < 0) {
        star = 0;
    }
    drawStar(star, "${resourceUrl}/frontRes/merchant/img/star.png");
    drawStar(5 - star, "${resourceUrl}/frontRes/merchant/img/n-star.png");
    function drawStar(num, url) {
        for (var i = 0; i < num; i++) {
            $(".star").append(
                    $("<div></div>").append(
                            $("<img />").attr("src", url)
                    )
            )
        }
    }
</script>
<script>
    var length = $(".tab").children().length;
    var width = $(".tab > div");
    $(".tab > div > div").css("width", $(window).width() * 0.19 + "px");
    if (length == 2) {
        $(".tab > div:first-child > div").css("margin-left", "50%");
        $(".tab > div:last-child > div").css("margin-left", "6%");
    }
    $(width).css("width", 100 / length + "vw");
</script>

</html>