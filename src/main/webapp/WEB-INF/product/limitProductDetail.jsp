<%--
  Created by IntelliJ IDEA.
  User: zhangwen
  Date: 2016/9/19
  Time: 23:05
  Content:爆品详情页
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/WEB-INF/commen.jsp" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="viewport" id="viewport" content="width=device-width, initial-scale=1">
    <meta name="format-detection" content="telephone=no">
    <meta name="format-detection" content="telephone=yes"/>
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="black"/>
    <title></title>
    <link rel="stylesheet" href="${resourceUrl}/frontRes/css/reset.css">
    <link rel="stylesheet" href="${resourceUrl}/frontRes/css/swiper.min.css">
    <link rel="stylesheet" href="${resourceUrl}/frontRes/product/hotDetail/css/hot.css">
</head>
<body>
<section class="banner">
    <div class="swiper-container swiper-banner">
        <div class="swiper-wrapper">
            <c:forEach items="${scrollList}" var="scroll">
                <div class="swiper-slide">
                    <img src="${scroll.picture}">
                </div>
            </c:forEach>
            <%--<div class="swiper-slide">--%>
            <%--<img src="img/banner-pic.png" alt="">--%>
            <%--</div>--%>
            <%--<div class="swiper-slide">--%>
            <%--<img src="img/banner-pic.png" alt="">--%>
            <%--</div>--%>
        </div>
        <!-- Add Pagination -->
        <div class="swiper-pagination"></div>
    </div>
</section>
<section class="title">
    <p>${product.name}</p>

    <p><span id="minPrice"></span>+<span id="minScore"></span></p>

    <p class="type"></p>
</section>
<section class="norms">
    <div>
        <div>规格</div>
        <div>限购${product.buyLimit}份</div>
    </div>
    <div class="norms-list">
        <%--<div class="check">耗子药味</div>--%>
    </div>
</section>
<section class="footer cantPay">
    <!--<p>立即购买<span>（仅剩8份）</span></p>-->
    <!--<p>积分不足</p>-->
    <!--<p>您已兑换过一份，无法继续兑换</p>-->
    <p id="buySubmit">立即购买</p>
</section>
</body>
<script src="${resourceUrl}/js/jquery-2.0.3.min.js"></script>
<script src="${resourceUrl}/frontRes/js/swiper.min.js"></script>
<script>
    var swiper = new Swiper('.swiper-banner', {
        pagination: '.swiper-pagination',
        paginationClickable: true,
        spaceBetween: 30,
        centeredSlides: true,
        autoplay: 3500,
        autoplayDisableOnInteraction: false
    });
</script>
<script>
    $(".type").html($(".check").html());
    $("#minPrice").html("￥" + $(".check").prev().prev().val());
    $("#minScore").html($(".check").prev().val() + "积分");
    var specList = ${specList}, repository = 0, checkedSpec = 0;
    for (var i = 0; i < specList.length; i++) {
        console.log(specList[i].repository);
//        console.log('------------');
        if (specList[i].repository > 0) {
            repository += specList[i].repository;
            if (checkedSpec == 0) {
                $(".norms-list").append(
                        $("<input>").attr("type", "hidden").attr("value", specList[i].id)
                ).append(
                        $("<input>").attr("type", "hidden").attr("value",
                                                                 specList[i].minPrice / 100)
                ).append(
                        $("<input>").attr("type", "hidden").attr("value", specList[i].minScore)
                ).append(
                        $("<div></div>").attr("class", "check").html(specList[i].specDetail)
                );
                $(".type").html($(".check").html());
                $("#minPrice").html("￥" + $(".check").prev().prev().val());
                $("#minScore").html($(".check").prev().val() + "积分");
                checkedSpec = 1;
            } else {
                $(".norms-list").append(
                        $("<input>").attr("type", "hidden").attr("value", specList[i].id)
                ).append(
                        $("<input>").attr("type", "hidden").attr("value",
                                                                 specList[i].minPrice / 100)
                ).append(
                        $("<input>").attr("type", "hidden").attr("value", specList[i].minScore)
                ).append(
                        $("<div></div>").html(specList[i].specDetail)
                );
            }
        } else {
            $(".norms-list").append(
                    $("<input>").attr("type", "hidden").attr("value", specList[i].id)
            ).append(
                    $("<input>").attr("type", "hidden").attr("value", specList[i].minPrice / 100)
            ).append(
                    $("<input>").attr("type", "hidden").attr("value", specList[i].minScore)
            ).append(
                    $("<div></div>").attr("class", "sellout").html(specList[i].specDetail)
            );
        }
    }
    if (repository == 0) {
        $("#buySubmit").html('已售罄');
        $("#minPrice").html("￥" + ${product.minPrice/100});
        $("#minScore").html(${product.minScore} + "积分");
    }

    var height = $(".norms-list").height();
    var minHeight = $(window).height() - $(".banner").height() - $(".title").height()
                    - $(".norms > div:first-child").height() - 24;
    if (height < minHeight) {
        $(".norms-list").css("height", minHeight + "px");
    }
    $(".norms-list > div").click(function () {
        if (!$(this).hasClass("sellout")) {
            $(".norms-list > div").removeClass("check");
            $(this).addClass("check");
            $(".type").html($(".check").html());
            $("#minPrice").html("￥" + $(".check").prev().prev().val());
            $("#minScore").html($(".check").prev().val() + "积分");
        }
    });

    //    $(".norms-list").append(
    //            $("<div></div>").html("葡萄味")
    //    ).append(
    //            $("<div></div>").html("蟑螂药味")
    //    ).append(
    //            $("<div></div>").attr("class", "sellout").html("海鲜味")
    //    );
</script>
</html>
