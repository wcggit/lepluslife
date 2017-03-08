<%--
  Created by IntelliJ IDEA.
  User: zhangwen
  Date: 2017/3/3
  Time: 17:48
  金币商城首页
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
    <title>金币商城</title>
    <link rel="stylesheet" type="text/css" href="${resourceUrl}/frontRes/gold/index/css/reset.css">
    <link rel="stylesheet" type="text/css" href="${resourceUrl}/frontRes/gold/index/css/home.css">
</head>
<body>
<div class="main">
    <div class="wdjb">
        <p class="clearfix">
            <span class="left">我的金币</span>
            <span class="right icon-problem">如何赚金币</span>
        </p>

        <h1><fmt:formatNumber
                type="number" value="${score/100}" pattern="0.00"
                maxFractionDigits="2"/></h1>
    </div>
    <!--话费充值-->
    <div class="hfcz kind" onclick="goRecharge()">
        <h3 class="clearfix kind-title">
            <span class="left">话费充值</span>
            <span class="right">即时到账</span>
        </h3>

        <div class="banner-img">
            <img src="${resourceUrl}/frontRes/gold/index/img/001.png" alt="">
        </div>
        <div class="products">
            <div class="product-item border-right">
                <h1 class="ttl"><span>100</span>元话费</h1>

                <p class="true-price">原价：100元</p>
                <c:if test="${score >= 10000}">
                    <p class="now-price">需付：0元+100金币</p>
                </c:if>
                <c:if test="${score < 10000}">
                    <p class="now-price">需付：<fmt:formatNumber
                            type="number" value="${(10000-score)/100}" pattern="0.00"
                            maxFractionDigits="2"/>元+<fmt:formatNumber
                            type="number" value="${score/100}" pattern="0.00"
                            maxFractionDigits="2"/>金币</p>
                </c:if>

            </div>
            <div class="product-item border-right">
                <h1 class="ttl"><span>50</span>元话费</h1>

                <p class="true-price">原价：50</p>
                <c:if test="${score >= 5000}">
                    <p class="now-price">需付：0元+50金币</p>
                </c:if>
                <c:if test="${score < 5000}">
                    <p class="now-price">需付：<fmt:formatNumber
                            type="number" value="${(5000-score)/100}" pattern="0.00"
                            maxFractionDigits="2"/>元+<fmt:formatNumber
                            type="number" value="${score/100}" pattern="0.00"
                            maxFractionDigits="2"/>金币</p>
                </c:if>
            </div>
            <div class="product-item">
                <h1 class="ttl"><span>20</span>元话费</h1>

                <p class="true-price">原价：20元</p>

                <c:if test="${score >= 2000}">
                    <p class="now-price">需付：0元+20金币</p>
                </c:if>
                <c:if test="${score < 2000}">
                    <p class="now-price">需付：<fmt:formatNumber
                            type="number" value="${(2000-score)/100}" pattern="0.00"
                            maxFractionDigits="2"/>元+<fmt:formatNumber
                            type="number" value="${score/100}" pattern="0.00"
                            maxFractionDigits="2"/>金币</p>
                </c:if>
            </div>
        </div>
    </div>
    <%--<!--视频会员-->--%>
    <%--<div class="sphy kind">--%>
    <%--<h3 class="clearfix kind-title">--%>
    <%--<span class="left">视频会员</span>--%>
    <%--<span class="right">专享特权</span>--%>
    <%--</h3>--%>

    <%--<div class="banner-img">--%>
    <%--<img src="images/home/007.png" alt="">--%>
    <%--</div>--%>
    <%--<div class="products">--%>
    <%--<div class="product-item border-right">--%>
    <%--<div class="good-img">--%>
    <%--<img src="images/home/good1.png" alt="">--%>
    <%--</div>--%>
    <%--<h1 class="ttl">优酷</h1>--%>

    <%--<p class="true-price">原价：25元/月</p>--%>

    <%--<p class="now-price">需付：3元+23金币</p>--%>
    <%--</div>--%>
    <%--<div class="product-item border-right">--%>
    <%--<div class="good-img">--%>
    <%--<img src="images/home/good1.png" alt="">--%>
    <%--</div>--%>
    <%--<h1 class="ttl">爱奇艺</h1>--%>

    <%--<p class="true-price">原价：15元/月</p>--%>

    <%--<p class="now-price">需付：0元+15金币</p>--%>
    <%--</div>--%>
    <%--<div class="product-item">--%>
    <%--<div class="good-img">--%>
    <%--<img src="images/home/good1.png" alt="">--%>
    <%--</div>--%>
    <%--<h1 class="ttl">腾讯视频</h1>--%>

    <%--<p class="true-price">原价：20元/月</p>--%>

    <%--<p class="now-price">需付：0元+20金币</p>--%>
    <%--</div>--%>
    <%--</div>--%>
    <%--</div>--%>
    <%--<!--加油卡-->--%>
    <%--<div class="jyk kind">--%>
    <%--<h3 class="clearfix kind-title border-1px_bottom">--%>
    <%--<span class="left">加油卡</span>--%>
    <%--<span class="right">爆款热销</span>--%>
    <%--</h3>--%>

    <%--<div class="banner-img">--%>
    <%--<img src="images/home/004.png" alt="">--%>
    <%--</div>--%>
    <%--<div class="products">--%>
    <%--<div class="product-item border-right">--%>
    <%--<div class="good-img">--%>
    <%--<img src="images/home/good1.png" alt="">--%>
    <%--</div>--%>
    <%--<h1 class="ttl">Iphone7</h1>--%>

    <%--<p class="true-price">原价：6,998元</p>--%>

    <%--<p class="now-price">需付：6,975元+23金币</p>--%>
    <%--</div>--%>
    <%--<div class="product-item">--%>
    <%--<div class="good-img">--%>
    <%--<img src="images/home/good1.png" alt="">--%>
    <%--</div>--%>
    <%--<h1 class="ttl">Iphone7</h1>--%>

    <%--<p class="true-price">原价：6,998元</p>--%>

    <%--<p class="now-price">需付：6,975元+23金币</p>--%>
    <%--</div>--%>
    <%--</div>--%>
    <%--</div>--%>
    <%--<!--北京公交卡-->--%>
    <%--<div class="bjgjk kind">--%>
    <%--<h3 class="clearfix kind-title border-1px_bottom">--%>
    <%--<span class="left">北京公交卡</span>--%>
    <%--<span class="right">仅限北京</span>--%>
    <%--</h3>--%>

    <%--<div class="banner-img">--%>
    <%--<img src="images/home/003.png" alt="">--%>

    <%--<div class="product-desc">--%>
    <%--<h1 class="ttl">北京公交卡 | 地铁卡</h1>--%>

    <%--<p class="desc">押金：20元 余额：80元</p>--%>

    <%--<p class="true-price">原价：100元</p>--%>

    <%--<p class="now-price">需付：77元+23金币</p>--%>
    <%--</div>--%>
    <%--</div>--%>
    <%--<div class="products">--%>
    <%--<div class="product-item border-right">--%>
    <%--<div class="good-img">--%>
    <%--<img src="images/home/good1.png" alt="">--%>
    <%--</div>--%>
    <%--<h1 class="ttl">Iphone7</h1>--%>

    <%--<p class="true-price">原价：6,998元</p>--%>

    <%--<p class="now-price">需付：6,975元+23金币</p>--%>
    <%--</div>--%>
    <%--<div class="product-item">--%>
    <%--<div class="good-img">--%>
    <%--<img src="images/home/good1.png" alt="">--%>
    <%--</div>--%>
    <%--<h1 class="ttl">Iphone7</h1>--%>

    <%--<p class="true-price">原价：6,998元</p>--%>

    <%--<p class="now-price">需付：6,975元+23金币</p>--%>
    <%--</div>--%>
    <%--</div>--%>
    <%--</div>--%>
    <%--<!--数码产品-->--%>
    <%--<div class="smcp kind">--%>
    <%--<h3 class="clearfix kind-title border-1px_bottom">--%>
    <%--<span class="left">数码产品</span>--%>
    <%--<span class="right">正品行货</span>--%>
    <%--</h3>--%>

    <%--<div class="banner-img">--%>
    <%--<img src="images/home/002.png" alt="">--%>

    <%--<div class="product-desc">--%>
    <%--<h1 class="ttl">MacBook Pro</h1>--%>

    <%--<p class="desc">一身才华，一触，即发</p>--%>

    <%--<p class="true-price">原价：11,488元</p>--%>

    <%--<p class="now-price">需付：11,465元+23金币</p>--%>
    <%--</div>--%>
    <%--</div>--%>
    <%--<div class="products">--%>
    <%--<div class="product-item border-right border-1px_bottom">--%>
    <%--<div class="good-img">--%>
    <%--<img src="images/home/good1.png" alt="">--%>
    <%--</div>--%>
    <%--<h1 class="ttl">Iphone7</h1>--%>

    <%--<p class="true-price">原价：6,998元</p>--%>

    <%--<p class="now-price">需付：6,975元+23金币</p>--%>
    <%--</div>--%>
    <%--<div class="product-item border-1px_bottom">--%>
    <%--<div class="good-img">--%>
    <%--<img src="images/home/good1.png" alt="">--%>
    <%--</div>--%>
    <%--<h1 class="ttl">Iphone7</h1>--%>

    <%--<p class="true-price">原价：6,998元</p>--%>

    <%--<p class="now-price">需付：6,975元+23金币</p>--%>
    <%--</div>--%>
    <%--</div>--%>
    <%--<div class="products">--%>
    <%--<div class="product-item border-right">--%>
    <%--<div class="good-img">--%>
    <%--<img src="images/home/good1.png" alt="">--%>
    <%--</div>--%>
    <%--<h1 class="ttl">Iphone7</h1>--%>

    <%--<p class="true-price">原价：6,998元</p>--%>

    <%--<p class="now-price">需付：6,975元+23金币</p>--%>
    <%--</div>--%>
    <%--<div class="product-item">--%>
    <%--<div class="good-img">--%>
    <%--<img src="images/home/good1.png" alt="">--%>
    <%--</div>--%>
    <%--<h1 class="ttl">Iphone7</h1>--%>

    <%--<p class="true-price">原价：6,998元</p>--%>

    <%--<p class="now-price">需付：6,975元+23金币</p>--%>
    <%--</div>--%>
    <%--</div>--%>
    <%--</div>--%>
    <!--说走就走的旅行-->
    <div class="szjz kind" onclick="goTravel()">
        <h3 class="clearfix kind-title border-1px_bottom">
            <span class="left">说走就走的旅行</span>
            <span class="right">猜你想去</span>
        </h3>

        <div class="banner-img">
            <img src="${resourceUrl}/frontRes/gold/index/img/005.png" alt="">
        </div>
        <div class="products">
            <div class="product-item">
                <h1 class="ttl">北京-柬埔寨6日游 全程五星艾美酒店 近距离探秘古老的</h1>

                <div class="prompt">
                    <span class="prompt-item">24小时客服服务</span>
                    <span class="prompt-item">赠送航空延误险</span>
                    <span class="prompt-item">落地签随时走</span>
                </div>
                <p class="true-price">原价：4,599元 <span>起/人</span></p>
                <c:if test="${score >= 459900}">
                    <p class="now-price">需付：0元+4599金币 <span>起/人</span></p>
                </c:if>
                <c:if test="${score < 459900}">
                    <p class="now-price">需付：<fmt:formatNumber
                            type="number" value="${(459900-score)/100}" pattern="0.00"
                            maxFractionDigits="2"/>元+<fmt:formatNumber
                            type="number" value="${score/100}" pattern="0.00"
                            maxFractionDigits="2"/>金币 <span>起/人</span></p>
                </c:if>


            </div>
        </div>
    </div>
    <%--<!--美妆个护-->--%>
    <%--<div class="mzgh kind">--%>
    <%--<h3 class="clearfix kind-title border-1px_bottom">--%>
    <%--<span class="left">美妆个护</span>--%>
    <%--<span class="right">爆款热销</span>--%>
    <%--</h3>--%>

    <%--<div class="banner-img">--%>
    <%--<img src="images/home/006.png" alt="">--%>
    <%--</div>--%>
    <%--<div class="products">--%>
    <%--<div class="product-item border-right">--%>
    <%--<div class="good-img">--%>
    <%--<img src="images/home/good1.png" alt="">--%>
    <%--</div>--%>
    <%--<h1 class="ttl">Iphone7</h1>--%>

    <%--<p class="true-price">原价：6,998元</p>--%>

    <%--<p class="now-price">需付：6,975元+23金币</p>--%>
    <%--</div>--%>
    <%--<div class="product-item">--%>
    <%--<div class="good-img">--%>
    <%--<img src="images/home/good1.png" alt="">--%>
    <%--</div>--%>
    <%--<h1 class="ttl">Iphone7</h1>--%>

    <%--<p class="true-price">原价：6,998元</p>--%>

    <%--<p class="now-price">需付：6,975元+23金币</p>--%>
    <%--</div>--%>
    <%--</div>--%>
    <%--</div>--%>
    <%--<!--神奇百货-->--%>
    <%--<div class="sqbh kind">--%>
    <%--<h3 class="clearfix kind-title border-1px_bottom">--%>
    <%--<span class="left">神奇百货</span>--%>
    <%--<span class="right">爆款热销</span>--%>
    <%--</h3>--%>

    <%--<div class="products">--%>
    <%--<div class="product-item border-right border-1px_bottom">--%>
    <%--<div class="good-img">--%>
    <%--<img src="images/home/good1.png" alt="">--%>
    <%--</div>--%>
    <%--<h1 class="ttl">Iphone7</h1>--%>

    <%--<p class="true-price">原价：6,998元</p>--%>

    <%--<p class="now-price">需付：6,975元+23金币</p>--%>
    <%--</div>--%>
    <%--<div class="product-item border-1px_bottom">--%>
    <%--<div class="good-img">--%>
    <%--<img src="images/home/good1.png" alt="">--%>
    <%--</div>--%>
    <%--<h1 class="ttl">Iphone7</h1>--%>

    <%--<p class="true-price">原价：6,998元</p>--%>

    <%--<p class="now-price">需付：6,975元+23金币</p>--%>
    <%--</div>--%>
    <%--</div>--%>
    <%--<div class="products">--%>
    <%--<div class="product-item border-right">--%>
    <%--<div class="good-img">--%>
    <%--<img src="images/home/good1.png" alt="">--%>
    <%--</div>--%>
    <%--<h1 class="ttl">Iphone7</h1>--%>

    <%--<p class="true-price">原价：6,998元</p>--%>

    <%--<p class="now-price">需付：6,975元+23金币</p>--%>
    <%--</div>--%>
    <%--<div class="product-item">--%>
    <%--<div class="good-img">--%>
    <%--<img src="images/home/good1.png" alt="">--%>
    <%--</div>--%>
    <%--<h1 class="ttl">Iphone7</h1>--%>

    <%--<p class="true-price">原价：6,998元</p>--%>

    <%--<p class="now-price">需付：6,975元+23金币</p>--%>
    <%--</div>--%>
    <%--</div>--%>
    <%--</div>--%>
</div>
</body>
<script src="${resourceUrl}/js/zepto.min.js"></script>
<script>
    function goRecharge() {
        window.location.href = '/front/order/weixin/recharge';
    }
    function goTravel() {
        window.location.href = 'http://u.eqxiu.com/s/4puvrvwJ';
    }
</script>
</html>
