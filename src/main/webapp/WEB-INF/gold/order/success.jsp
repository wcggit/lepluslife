<%--
  Created by IntelliJ IDEA.
  User: zhangwen
  Date: 2017/3/12
  Time: 15:50
  金币订单支付成功页面
--%>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@include file="/WEB-INF/commen.jsp" %>
<html>
<head>
    <meta charset="utf-8">
    <title></title>
    <meta name="viewport"
          content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <!--App自定义的css-->
    <link rel="stylesheet" href="${resourceUrl}/frontRes/gold/success/css/paySuccess.css">
</head>
<body>
<div class="main">
    <div class="top">
        <div class="item">
            <img src="${resourceUrl}/frontRes/gold/success/img/icon-package@2x.png" alt=""/>
        </div>
        <div class="item">
            <h1 class="ttl">支付成功</h1>

            <div class="desc">您的包裹正在整装待发</div>
            <div class="price">实付款：￥<fmt:formatNumber type="number"
                                                      value="${order.truePrice/100}"
                                                      pattern="0.00"
                                                      maxFractionDigits="2"/></div>
        </div>
    </div>
    <div class="center">
        <c:if test="${order.transmitWay == 1}">
            <div class="person">线下自提</div>
        </c:if>
        <c:if test="${order.transmitWay != 1}">
            <div class="person">收货人：<span class="name">${order.address.name}</span>  <span
                    class="tel">${order.address.phoneNumber}</span>
            </div>
            <div class="address">${order.address.city}${order.address.county}${order.address.location}</div>
        </c:if>

    </div>
</div>
<div class="getMoney">
    <img src="${resourceUrl}/frontRes/gold/success/img/bg-Red-envelopes@2x.png" alt=""/>

    <div class="desc">
        <div class="item">
            <span>恭喜您</span>
            <span>获得鼓励金<a class="money"><fmt:formatNumber type="number"
                                                          value="${order.payBackA/100}"
                                                          pattern="0.00"
                                                          maxFractionDigits="2"/><span>元</span></a></span>
            <span>已存入您的账户中</span>
        </div>
        <div class="item">
            <span class="btn-look" onclick="goUserCenter()"></span>
        </div>
    </div>
</div>
<div class="btn clear-fix">
    <div class="btn-left left" onclick="showOrderInfo()">查看订单</div>
    <div class="btn-right right" onclick="goldIndex()">返回金币商城</div>
</div>
</body>
<script>
    function showOrderInfo() {
        location.href = "/order/showOrderInfo/${order.id}";
    }
    function goldIndex() {
        location.href = "/front/gold/weixin";
    }
    function goUserCenter() {
        location.href = "/weixin/user";
    }
</script>
</html>
