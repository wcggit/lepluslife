<%--
  Created by IntelliJ IDEA.
  User: zhangwen
  Date: 16/05/09
  Time: 下午9:01
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/commen.jsp" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <title></title>
    <meta name="viewport"
          content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <!--标准mui.css-->
    <link rel="stylesheet" href="${resourceUrl}/css/mui.min.css">
    <!--App自定义的css-->
    <link rel="stylesheet" type="text/css" href="${resourceUrl}/css/orderInfo.css"/>
    <script src="${resourceUrl}/js/jquery-2.0.3.min.js"></script>
</head>

<!--头部-->

<body>
<c:if test="${order.state == 0}">
    <!--待付款-->
    <div class="order-top order-top-obligation">
        <span>待付款</span>
        <span><font><fmt:formatDate
                value="${order.createDate}" pattern="HH"/></font>分钟后自动取消</span>
    </div>
</c:if>
<c:if test="${order.state == 3}">
    <!--订单已完成-->
    <div class="order-top order-top-finished">
        <span>订单已完成</span>
    </div>
</c:if>
<c:if test="${order.state == 1}">
    <!--未发货-->
    <div class="order-top order-top-notShipped">
        <span>买家已付款</span>
        <span>等待卖家发货</span>
    </div>
</c:if>
<c:if test="${order.state == 2}">
    <!--已发货-->
    <div class="order-top order-top-shipped">
        <span>卖家已发货</span>
        <span><font>8</font>天<font>2</font>小时后自动确认收货</span>
    </div>
</c:if>

<!--内容部分-->
<div class="mui-content" style="padding-bottom: 60px">
    <!--个人信息-->
    <ul class="mui-table-view mui-table-view-chevron">

        <c:if test="${order.state == 3}">
            <li class="mui-table-view-cell mui-media">
                <a class="mui-navigate-right">
                    <img class="mui-media-object mui-pull-left"
                         src="${resourceUrl}/images/tradeDetail/sign.png">

                    <div class="mui-media-body">
                        <span><font>${address.name}</font> 已签收</span>

                        <p><font>${address.location}</font> <font><fmt:formatDate
                                value="${order.confirmDate}" pattern="yyyy.MM.dd HH:mm"/></font></p>
                    </div>
                </a>
            </li>
        </c:if>

        <li class="mui-table-view-cell mui-media">
            <a href="javascript:;">
                <img class="mui-media-object mui-pull-left" src="${wxUser.headImageUrl}">

                <div class="mui-media-body">
                    <span><font
                            style="margin-right: 20px">${address.name}</font><font>${address.phoneNumber}</font></span>

                    <p class="mui-ellipsis">${address.province}${address.city}${address.county}${address.location}</p>
                </div>
            </a>
        </li>
    </ul>
    <!--订单列表-->
    <div class="list">
        <p class="mui-table list-ttl trade-font-black">
            <span class="logo mui-pull-left"></span>
            <span>乐+商城</span>
            <c:if test="${order.state == 0}">
                <span class="mui-pull-right state">待付款</span>
            </c:if>
            <c:if test="${order.state == 3}">
                <span class="mui-pull-right state">已完成</span>
            </c:if>
            <c:if test="${order.state == 1}">
                <span class="mui-pull-right state">已付款</span>
            </c:if>
            <c:if test="${order.state == 2}">
                <span class="mui-pull-right state">已发货</span>
            </c:if>
        </p>

        <div class="product-list">
            <c:forEach items="${order.orderDetails}" var="detail">

                <div class="list-top">
                    <div class="top-left">
                        <span style="background: url(${detail.productSpec.picture}) no-repeat;background-size: 100%;"></span>
                    </div>
                    <div class="top-right">
                        <p class="right-ttl">
                            <span class="trade-font-black mui-ellipsis">${detail.product.name}</span>
                        </p>

                        <p class="right-pra">规格：<font>${detail.productSpec.specDetail}</font></p>

                        <p class="right-pra">数量：<font> ${detail.productNumber}</font>件<span
                                class="mui-pull-right trade-font-black">￥<font><fmt:formatNumber
                                type="number"
                                value="${detail.productSpec.price/100}"
                                maxFractionDigits="2"/></font></span>
                        </p>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
    <!--价格明细-->


    <ul class="mui-table-view list list-detail">
        <li class="mui-table-view-cell">
            <span class="trade-font-grey">订单金额：</span>
            <span class="mui-pull-right">￥<fmt:formatNumber type="number"
                                                            value="${(order.totalPrice-order.freightPrice)/100}"
                                                            maxFractionDigits="2"/></span>
        </li>
        <li class="mui-table-view-cell">
            <span class="trade-font-grey">运费：</span>
            <span class="mui-pull-right">￥<fmt:formatNumber type="number"
                                                            value="${order.freightPrice/100}"
                                                            maxFractionDigits="2"/></span>
        </li>
        <li class="mui-table-view-cell trade-font-red"><span
                class="mui-pull-right">合计：<font>￥<fmt:formatNumber type="number"
                                                                   value="${order.totalPrice/100}"
                                                                   maxFractionDigits="2"/></font></span>
        </li>
        <!--积分支付-->
        <li class="mui-table-view-cell" style="border-top: solid 1px #E8E8E8">
            <span class="trade-font-grey">积分支付：</span>
            <span class="mui-pull-right">-￥<fmt:formatNumber type="number"
                                                             value="${order.trueScore/100}"
                                                             maxFractionDigits="2"/></span>
        </li>
        <li class="mui-table-view-cell trade-font-red"><span
                class="mui-pull-right">实付金额：<font>￥<fmt:formatNumber type="number"
                                                                     value="${order.truePrice/100}"
                                                                     maxFractionDigits="2"/></font></span>
        </li>
    </ul>
    <!--消费返利-->
    <ul class="mui-table-view list list-detail">
        <li class="mui-table-view-cell">
            <span class="trade-font-grey">消费返利：</span>
            <span class="mui-pull-right">获得乐+生活 ￥<font>
                <c:if test="${payBackScoreA == null}">
                    0
                </c:if>
                <c:if test="${payBackScoreA != null}">
                    <fmt:formatNumber type="number"
                                      value="${payBackScoreA/100}"
                                      maxFractionDigits="2"/>
                </c:if>
            </font>红包</span>
        </li>
    </ul>
    <!--订单明细-->
    <ul class="mui-table-view list trade-font-grey list-detail">
        <li class="mui-table-view-cell">
            <span>订单编号：<font>${order.orderSid}</font></span>
        </li>
        <li class="mui-table-view-cell">
            <span>下单时间：<font><fmt:formatDate value="${order.createDate}"
                                             pattern="yyyy.MM.dd HH:mm:ss"/> </font></span>
        </li>
        <li class="mui-table-view-cell mui-text-center" style="border-top: solid 1px #E8E8E8">
            <span>
                <button type="button" class="mui-btn mui-btn-outlined" style="border: none">
                    <i class="iconfont1">&#xe622;</i>
                    <a style="color: #333" href="tel:18710089228">拨打客服电话</a>
                </button>
            </span>
        </li>
    </ul>
    <!--底部菜单-->
    <nav class="mui-bar mui-bar-tab">
        <c:if test="${order.state == 0}">
            <span class="mui-pull-left"></span>
            <span class="mui-tab-label" onclick="orderCancle()">取消订单</span>
            <span class="mui-tab-label" onclick="goPayPage()">重新支付</span>
        </c:if>
        <c:if test="${order.state == 3}">
            <span style="width: 100vw;height: 100%;text-align: center;color: #D62C2C;background-color: #efeff4;"
                  onclick="goDeliveryPage()">查看物流</span>
        </c:if>
        <c:if test="${order.state == 1}">
        </c:if>
        <c:if test="${order.state == 2}">
            <span class="mui-pull-left"></span>
            <span class="mui-tab-label" onclick="goDeliveryPage()">查看物流</span>
            <span class="mui-tab-label" onclick="orderConfirm()">确认收货</span>
        </c:if>
    </nav>
</div>
</body>
<script type="text/javascript" src="${resourceUrl}/js/mui.min.js"></script>
<script>
    function goDeliveryPage() {//查看物流信息
        location.href = "/order/showExpress/" + ${order.id};
    }
    function orderCancle() { //取消订单
        $.ajax({
                   type: "post",
                   url: "/weixin/order/orderCancle",
                   data: {orderId:${order.id}},
                   success: function (data) {
                       //   location.reload(true);
                       location.href = "/weixin/orderDetail";
                   }
               });
    }
    function goPayPage() { //支付
        location.href = '/weixin/order/' +${order.id};
    }
    function orderConfirm() {  //确认收货
        $.ajax({
                   type: "post",
                   url: "/weixin/order/orderConfirm",
                   data: {orderId:${order.id}},
                   success: function (data) {
                       location.reload(true);
                   }
               });
    }
</script>
</html>