<%--
  Created by IntelliJ IDEA.
  User: zhangwen
  Date: 2017/3/9
  Time: 15:46
  金币商品详情页
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/commen.jsp" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no">
    <title>商品详情</title>
    <link rel="stylesheet" type="text/css" href="${resourceUrl}/frontRes/gold/index/css/reset.css">
    <link rel="stylesheet" type="text/css"
          href="${resourceUrl}/frontRes/gold/detail/css/goodDetail.css">
    <link rel="stylesheet" href="${resourceUrl}/frontRes/css/swiper.min.css">
</head>
<body>
<div class="main">
    <div class="top">
        <div class="swiper-container">
            <div class="swiper-wrapper">
                <c:forEach items="${scrollList}" var="scroll">
                    <div class="swiper-slide">
                        <img src="${scroll.picture}">
                    </div>
                </c:forEach>
            </div>
            <!-- Add Pagination -->
            <div class="swiper-pagination"></div>
        </div>
    </div>
    <div class="shadow border-1px">
        <span class="number"><fmt:formatNumber
                type="number" value="${product.minScore/100}" pattern="0.00"
                maxFractionDigits="2"/></span>金币<span class="rmb">￥<fmt:formatNumber
            type="number" value="${product.price/100}" pattern="0.00"
            maxFractionDigits="2"/></span>
    </div>
    <div class="center">
        <p class="ttl">${product.name}</p>
        <p class="desc">${product.description}</p>
        <div class="clearfix">
            <span class="left">快递：<fmt:formatNumber
                    type="number" value="${product.postage/100}" pattern="0.00"
                    maxFractionDigits="2"/></span>
            <span class="right">已售出：${product.customSale + product.saleNumber}件</span>
        </div>
    </div>
    <div class="bottom">
        <h1 class="ttl">规格</h1>
        <ul class="guige">
            <c:forEach items="${specList}" var="spec" begin="0" end="0">
                <li class="item active"><input type="hidden"
                                               value="${spec.id}"><input type="hidden"
                                                                         value="${spec.minScore}"><input
                        type="hidden"
                        value="${spec.repository}"><input
                        type="hidden"
                        value="${spec.price}">${spec.specDetail}</li>
            </c:forEach>
            <c:forEach items="${specList}" var="spec" begin="1">
                <li class="item"><input type="hidden"
                                        value="${spec.id}"><input type="hidden"
                                                                  value="${spec.minScore}"><input
                        type="hidden"
                        value="${spec.repository}"><input
                        type="hidden"
                        value="${spec.price}">${spec.specDetail}</li>
            </c:forEach>
        </ul>
        <h1 class="ttl">数量</h1>

        <div class="num">
            <span class="decrease">-</span>
            <input class="buy-num" id="buyNumber" type="text" value="1" disabled>
            <span class="add">+</span>
            <span class="kucun">库存：<span class="kucun-num"><c:forEach items="${specList}" var="spec"
                                                                      begin="0"
                                                                      end="0">${spec.repository}</c:forEach></span></span>
        </div>
    </div>
    <div class="detail">
    </div>
    <div class="btn-dh" onclick="buy()">立即兑换</div>
</div>
</body>
<script src="${resourceUrl}/js/zepto.min.js"></script>
<script src="${resourceUrl}/frontRes/js/swiper.min.js"></script>
<script>
    var repository = eval($('.kucun-num').html()), buySpecId = eval($($('.active').find('input')[0]).val());

    //强制保留两位小数
    function toDecimal(x) {
        var f = parseFloat(x);
        if (isNaN(f)) {
            return false;
        }
        var f = Math.round(x * 100) / 100;
        var s = f.toString();
        var rs = s.indexOf('.');
        if (rs < 0) {
            rs = s.length;
            s += '.';
        }
        while (s.length <= rs + 2) {
            s += '0';
        }
        return s;
    }

    //swiper
    var swiper = new Swiper('.swiper-container', {
        pagination: '.swiper-pagination',
        paginationClickable: true
    });
    //    规格选择
    var items = $('.guige .item');
    items.on('touchstart', function () {
        items.removeClass('active');
        $(this).addClass('active');
        var specId = $($(this).find('input')[0]).val();
        var minScore = $($(this).find('input')[1]).val();
        repository = $($(this).find('input')[2]).val();
        var price = $($(this).find('input')[3]).val();
        console.log("input specId==" + specId);
        console.log("input minScore==" + minScore);
        console.log("input repository==" + repository);
        console.log("input price==" + price);
        buySpecId = specId;
        $('.kucun-num').html(repository);
        $('.number').html(toDecimal(minScore / 100));
        $('.rmb').html('￥' + toDecimal(price / 100));
        var buynum = $('.buy-num');
        var num = eval(buynum.val());
        num = checkNum(num);
        buynum.val(num);
    });

    $('.add').on('touchstart', function () {
        var buynum = $('.buy-num');
        var num = eval(buynum.val()) + 1;
        num = checkNum(num);
        buynum.val(num);
    });
    $('.decrease').on('touchstart', function () {
        var buynum = $('.buy-num');
        var num = eval(buynum.val()) - 1;
        num = checkNum(num);
        buynum.val(num);
    });

    function checkNum(number) {
        if (number > repository) {
            number = repository;
        } else if (number < 0) {
            number = 0;
        }
        return number;
    }

    function buy() {
        var productId = '${product.id}';
        if (buySpecId == null || buySpecId == 0) {
            alert('请选择规格');
            return
        }
        var buyNum = eval($('#buyNumber').val());
        if (buyNum == null || buyNum <= 0) {
            alert('请选择购买数量');
            return
        }
        $(".btn-dh").attr("onclick", "");

        $.ajax({
                   type: "post",
                   url: "/front/order/weixin/createGoldOrder",
                   data: {productId: productId, specId: buySpecId, buyNumber: buyNum},
                   success: function (data) {
                       if (data.status == 200) {
                           location.href =
                           "/front/order/weixin/confirmOrder?orderId=" + data.data.id;
                       } else {
                           alert(data.msg);
                           $(".btn-dh").attr('onclick', 'buy()');
                       }
                   }
               });
    }

</script>
<script>
    var detailList = ${detailList};

    /********************************加载图文详情***************************/
    var nowLoadImg = 0;
    var loadLength = detailList.length;
    if ($(document).height() == $(this).height()) {
        loadImg(nowLoadImg);
    }
    $(window).scroll(function () {
        var scrollTop = $(this).scrollTop();
        var scrollHeight = $(document).height();
        var windowHeight = $(this).height();
        if (scrollTop + windowHeight >= scrollHeight) {
            if (nowLoadImg < loadLength) {
                loadImg(nowLoadImg);
                loadImg(nowLoadImg);
            }
        }
    });
    function loadImg(i) {
        if (i < loadLength) {
            $(".detail").append($("<img>").attr("src", detailList[i].picture));
            nowLoadImg++;
        } else {
            $(".detail").append(
                    $("<p></p >").html("已加载所有图文")
            );
            return false;
        }
    }
</script>
</html>
