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
                    <h3 class="left-ttl">${products[0].name}</h3>
                    <div class="littleLable">
                        <span class="halfLength">不限场次</span>
                        <span class="halfLength">不限时间</span>
                        <span class="halfLength">不限2D3D</span>
                        <span class="halfLength">不限影片</span>
                    </div>
                    <h3 class="oldPrice">原价：<span>${products[0].price/100.0}</span>元</h3>
                    <h3 class="newPrice">需付：<span>
                        <c:if test="${products[0].price<=scoreC.score}">
                    </span>0元+<span>${products[0].price/100.0}</span>金币</h3>
                    </c:if>
                    <c:if test="${products[0].price>scoreC.score}">
                        </span>${(products[0].price-scoreC.score)/100.0}元+<span>${scoreC.score/100.0}</span>金币</h3>
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
                    <h3 class="left-ttl">${products[1].name}</h3>
                    <div class="littleLable">
                        <span class="halfLength">不限场次</span>
                        <span class="halfLength">不限影片</span>
                        <span class="completeLength">只限周一至周四，上午场</span>
                    </div>
                    <h3 class="oldPrice">原价：<span>${products[1].price/100.0}</span>元</h3>
                    <h3 class="newPrice">需付：<span>
                        <c:if test="${products[1].price<=scoreC.score}">
                    </span>0元+<span>${products[1].price/100.0}</span>金币</h3>
                    </c:if>
                    <c:if test="${products[1].price>scoreC.score}">
                        </span>${(products[1].price-scoreC.score)/100.0}元+<span>${scoreC.score/100.0}</span>金币</h3>
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
                <div class="logo"><img src="" alt=""></div>
                <div class="desc">
                    <p class="ttl">观影特权（套餐1）</p>
                    <p class="allNum">订单金额：<span>30</span>元</p>
                    <p class="jinbiNum">订单金额：<span>12</span>金币</p>
                    <p class="trueNum">实际支付：<span>18</span>元</p>
                </div>
            </div>
            <div class="btn-confirm">确认支付</div>
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
    function loadDetail() {
        location.href="/front/movie/weixin/privilege";
    }
    //    loadTopBanner();
    //    loadBottomBanner();
    //    function loadTopBanner() {
    //        $.ajax({
    //            type: "get",
    //            url: "/front/movie/topBanner",
    //            success: function (result) {
    //                var data = result.data;
    //                $("#top1").attr("src", data[0].picture);
    //                $("#top2").attr("src", data[1].picture);
    //                $("#top3").attr("src", data[2].picture);
    //            }
    //        });
    //    }


    /* $.ajax({
        type: "get",
        url: "/front/movie/shangmi/searchVaild?phoneNumber=18910264249",
        success: function (result) {
            console.log(JSON.stringify(result));
        }
    });*/


//    $.ajax({
//        type: "get",
//        url: "/front/movie/shangmi/searchVaild?lejiaUserSid=0184391249659",
//        success: function (result) {
//            console.log(JSON.stringify(result));
//        }
//    });

//            $.ajax({
//                type: "get",
//                url: "/front/movie/shangmi/searchChecked?terminalNo=1893409",
//                success: function (result) {
//                    console.log(JSON.stringify(result));
//                }
//            });

    //        $.ajax({
    //            type: "post",
    //            url: "/front/movie/shangmi/doCheckMovie",
    //            data: {
    //                orderSid:"5ssfkjkj" ,
    //                phoneNumber:"18910264249" ,
    //                terminalNo:"1893409"
    //            },
    //            success: function (data) {
    //                console.log(JSON.stringify(data));
    //            }
    //        });


    //    function loadBottomBanner() {
    //        $.ajax({
    //            type: "get",
    //            url: "/front/movie/hotMovieBanner",
    //            success: function (result) {
    //                var data = result.data;
    //                $("#btom1").attr("src",data[0].picture);
    //                $("#btom2").attr("src",data[1].picture);
    //                $("#btom3").attr("src",data[2].picture);
    //                $("#btom4").attr("src",data[3].picture);
    //                $("#btom5").attr("src",data[4].picture);
    //                $("#btom6").attr("src",data[5].picture);
    ////                console.log(JSON.stringify(data));
    //            }
    //        });
    //    }
</script>
</body>
</html>