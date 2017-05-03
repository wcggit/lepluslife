<%--
  Created by IntelliJ IDEA.
  User: xf
  Date: 2017/4/27
  Time: 14:37
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"><!--强制以webkit内核来渲染-->
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>乐+电影</title>
    <%--<link rel="stylesheet" href="${commonResource}/css/reset.css">--%>
    <%--<link rel="stylesheet" href="${commonResource}/css/swiper.min.css">--%>
    <%--<link rel="stylesheet" href="${resource}/movie/css/home.css">--%>
    <%--<script src="${commonResource}/js/zepto.min.js"></script>--%>
    <%--<script src="${commonResource}/js/swiper.min.js"></script>--%>

    <link rel="stylesheet" href="http://image.lepluslife.com/common/css/reset.css">
    <link rel="stylesheet" href="http://image.lepluslife.com/common/css/swiper.min.css">
    <link rel="stylesheet" href="http://image.lepluslife.com/frontRes/movie/css/home.css">
    <script src="http://image.lepluslife.com/common/js/zepto.min.js"></script>
    <script src="http://image.lepluslife.com/common/js/swiper.min.js"></script>
</head>
<body>
<div class="main">
    <!-- Swiper -->
    <div class="swiper-container banner">
        <div class="swiper-wrapper">
            <div class="swiper-slide">
                <img id="top1" src="${topBanner[0].picture}" alt="">
            </div>
            <div class="swiper-slide">
                <img id="top2" src="${topBanner[1].picture}" alt="">
            </div>
            <div class="swiper-slide">
                <img id="top3" src="${topBanner[2].picture}" alt="">
            </div>
        </div>
        <!-- Add Pagination -->
        <div class="swiper-pagination"></div>
    </div>
    <div class="btn-tobeused">
        <div class="btn-inner clearfix">
            <span class="left">待使用特权：<span>${vaildCount}</span></span>
            <span class="right" onclick="loadDetail()">查看详情 >></span>
        </div>
    </div>
    <c:if test="${products[0]!=null}">
        <div class="view-privilege view-privilege1">
            <div class="desc">
                <div class="desc-left">
                    <h3 class="left-ttl" product-id="${products[0].id}">${products[0].name}</h3>
                    <div class="littleLable">
                        <span class="halfLength">不限场次</span>
                        <span class="halfLength">不限时间</span>
                        <span class="halfLength">不限2D3D</span>
                        <span class="halfLength">不限影片</span>
                    </div>
                    <h3 class="oldPrice">原价：<span>${products[0].price/100.0}</span>元</h3>
                    <h3 class="newPrice">需付：<span>
                        <c:if test="${products[0].price<=scoreC.score}">0
                    </span>元+<span>${products[0].price/100.0}</span>金币</h3>
                    </c:if>
                    <c:if test="${products[0].price>scoreC.score}">${(products[0].price-scoreC.score)/100.0}
                        </span>元+<span>${scoreC.score/100.0}</span>金币</h3>
                    </c:if>
                </div>
                <div class="desc-right">
                    <div class="right-logo">
                        <img src="${products[0].picture}" alt="${products[0].name}">
                    </div>
                    <div class="btn-buy">立即抢购</div>
                </div>
            </div>
            <div class="address">
                    <%--<h3>支持以下影院：</h3>--%>
                <p>${products[0].introduce}</p>
            </div>
        </div>
    </c:if>

    <c:if test="${products[1]!=null}">
        <div class="view-privilege view-privilege2">
            <div class="desc">
                <div class="desc-left">
                    <h3 class="left-ttl" product-id="${products[1].id}">${products[1].name}</h3>
                    <div class="littleLable">
                        <span class="halfLength">不限场次</span>
                        <span class="halfLength">不限影片</span>
                        <span class="completeLength">只限周一至周四，上午场</span>
                    </div>
                    <h3 class="oldPrice">原价：<span>${products[1].price/100.0}</span>元</h3>
                    <h3 class="newPrice">需付：<span>
                        <c:if test="${products[1].price<=scoreC.score}">0
                    </span>元+<span>${products[1].price/100.0}</span>金币</h3>
                    </c:if>
                    <c:if test="${products[1].price>scoreC.score}">${(products[1].price-scoreC.score)/100.0}
                        </span>元+<span>${scoreC.score/100.0}</span>金币</h3>
                    </c:if>
                </div>
                <div class="desc-right">
                    <div class="right-logo">
                        <img src="${products[1].picture}" alt="${products[1].name}">
                    </div>
                    <div class="btn-buy">立即抢购</div>
                </div>
            </div>
            <div class="address">
                <h3>支持以下影院：</h3>
                <p>${products[1].introduce}</p>
            </div>
        </div>
    </c:if>

    <div class="swiper-container movieShow">
        <h3 class="ttl">热门电影</h3>
        <div class="swiper-wrapper">
            <%--<div class="swiper-slide" style="margin-right: 0"><img src="${hotMovieBanner[0].picture}" alt=""></div>--%>
            <div class="swiper-slide"><img src="${hotMovieBanner[0].picture}" alt=""></div>
            <div class="swiper-slide"><img src="${hotMovieBanner[1].picture}" alt=""></div>
            <div class="swiper-slide"><img src="${hotMovieBanner[2].picture}" alt=""></div>
            <div class="swiper-slide"><img src="${hotMovieBanner[3].picture}" alt=""></div>
            <div class="swiper-slide"><img src="${hotMovieBanner[4].picture}" alt=""></div>
            <div class="swiper-slide"><img src="${hotMovieBanner[5].picture}" alt=""></div>
        </div>
    </div>
    <div class="shadow">
        <div class="window">
            <div class="top">
                <div class="logo"><img src="" alt=""><input type="hidden" type="hidden" id="productId"/></div>
                <div class="desc">
                    <p class="ttl">观影特权（套餐1）</p>
                    <p class="allNum">订单金额：<span>30</span>元</p>
                    <p class="jinbiNum">订单金额：<span>12</span>金币</p>
                    <p class="trueNum">实际支付：<span>18</span>元</p>
                </div>
            </div>
            <div class="btn-confirm" onclick="payConfirm()">确认支付</div>
            <span class="close"></span>
        </div>
    </div>
</div>
<script>
    var banner = new Swiper('.banner', {
        pagination: '.swiper-pagination',
        paginationClickable: true
    });
    var movieShow = new Swiper('.movieShow', {
        slidesPerView: "auto",
        spaceBetween: 12,
        paginationClickable: true
    });
    $('.btn-buy').on("touchstart", function (e) {
        var imgUrl = $(this).prev('.right-logo').find('img').attr('src');
        var ttl = $(this).parents('.desc').find('.desc-left .left-ttl').text();
        var allNum = $(this).parents('.desc').find('.desc-left .oldPrice span').text();
        var jinbiNum = $(this).parents('.desc').find('.desc-left .newPrice span:last-child').text();
        var trueNum = $(this).parents('.desc').find('.desc-left .newPrice span:first-child').text();
        var productId = $(this).parents('.desc').find('.desc-left .left-ttl').attr("product-id");
        $("#productId").val(productId);
        $('.window .logo img').attr('src', imgUrl);
        $('.window .desc .ttl').text(ttl);
        $('.window .desc .allNum span').text(allNum);
        $('.window .desc .jinbiNum').find('span').text(jinbiNum);
        $('.window .desc .trueNum').find('span').text(trueNum);
        $('.shadow').css("display", "block");
        setTimeout(function () {
            $('.window').css("bottom", "0");
        }, 100);

        $('.shadow').on('touchstart', function () {
            $('.window').css("bottom", "-70vw");
            setTimeout(function () {
                $('.shadow').css("display", "none");
            }, 500);
        });
        e.stopPropagation();//阻止事件向上冒泡
    });
    $('.close').on("touchstart", function () {
        $('.window').css("bottom", "-70vw");
        setTimeout(function () {
            $('.shadow').css("display", "none");
        }, 500);
    });
    $('.window').on("touchstart", function (e) {
        e.stopPropagation();//阻止事件向上冒泡
    });
</script>
<script src="http://www.lepluslife.com/resource/backRes/js/jquery.min.js"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
<script type="text/javascript"
        src="http://webapi.amap.com/maps?v=1.3&key=48f94cad8f49fc73c9ba59b281bb1e84"></script>
<script>
    wx.config({
        debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
        appId: '${wxConfig['appId']}', // 必填，公众号的唯一标识
        timestamp: ${wxConfig['timestamp']}, // 必填，生成签名的时间戳
        nonceStr: '${wxConfig['noncestr']}', // 必填，生成签名的随机串
        signature: '${wxConfig['signature']}',// 必填，签名，见附录1
        jsApiList: [
            'chooseWXPay'
        ] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
    });
    wx.ready(function () {
        // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
//       隐藏菜单
        wx.hideOptionMenu();

    });
    wx.error(function (res) {
        // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。

    });

</script>
<script>
    var orderSid = null;
    //  点击查看详情 跳转到电影特权页面
    function loadDetail() {
        location.href="/front/movie/weixin/privilege";
    }
    //   点击确认支付 进入支付流程
    function payConfirm() {
        var productId = $("#productId").val();
        $.post('/front/movie/weixin/moviePay', {
            productId: productId
        }, function (response) {
            //  纯金币支付
            if(response.status==2000) {
                orderSid = response.data;
                window.location.href = '/front/movie/pay/successPage?orderSid='+orderSid;
            // 微信支付
            }else if(response.status==200) {
                var res = response.data;
                orderSid = res.orderSid;
                weixinPay(res);
                return;
            }
        });
    }


    function weixinPay(res) {
        $('.waiting').css('display', 'none');
        WeixinJSBridge.invoke(
            'getBrandWCPayRequest', {
                "appId":     res.appId,     //公众号名称，由商户传入
                "timeStamp": res.timeStamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
                "nonceStr":  res.nonceStr, // 支付签名随机串，不长于 32 位
                "package":   res.package, // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
                "signType":  res.signType, // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
                "paySign":   res.sign // 支付签名
            },
            function (reslut) {
                if (reslut.err_msg == "get_brand_wcpay_request:ok") {
                    // 支付成功后的回调函数
                    window.location.href = '/front/movie/pay/successPage?orderSid='+orderSid;
//                  window.location.href = '/weixin/pay/paySuccess?orderId='+orderId;
                } else {
                    // 取消支付或支付失败，回到乐加电影页面
                    window.location.href = '/front/order/weixin/movie/';
                }
            }
        );
        if (typeof WeixinJSBridge == "undefined") {
            if (document.addEventListener) {
                document.addEventListener('WeixinJSBridgeReady', weixinPay, false);
            } else if (document.attachEvent) {
                document.attachEvent('WeixinJSBridgeReady', weixinPay);
                document.attachEvent('onWeixinJSBridgeReady', weixinPay);
            }
        } else {
            weixinPay();
        }
    }
</script>
</body>
</html>