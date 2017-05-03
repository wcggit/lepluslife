<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/5/3
  Time: 9:04
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"><!--强制以webkit内核来渲染-->
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>乐+电影</title>
    <link rel="stylesheet" href="http://image.lepluslife.com/common/css/reset.css">
    <link rel="stylesheet" href="http://image.lepluslife.com/frontRes/movie/css/paySuccess.css">
    <script src="http://image.lepluslife.com/common/js/zepto.min.js"></script>

    <%--<link rel="stylesheet" href="${commonResource}/css/reset.css">--%>
    <%--<link rel="stylesheet" href="${resource}/movie/css/paySuccess.css">--%>
    <%--<script src="${commonResource}/js/zepto.min.js"></script>--%>
</head>
<body>
<div class="main">
    <div class="top">
        <div class="success">
            <div class="logo"></div>
            <h3 class="ttl">付款成功</h3>
        </div>
        <ul class="list">
            <li class="list-detail clearfix">
                <span class="left">金额</span>
                <span class="right">￥${order.totalPrice/100.0}</span>
            </li>
            <li class="list-detail clearfix">
                <span class="left">支付方式</span>
                <span class="right">￥${order.truePrice/100.0} + ${order.trueScore/100.0}金币</span>
            </li>
            <li class="list-detail clearfix">
                <span class="left">交易时间</span>
                <span class="right"><fmt:formatDate value="${order.dateCompleted}" pattern="yyyy/MM/dd  HH:ss"/></span>
            </li>
            <li class="list-detail clearfix">
                <span class="left">获得鼓励金</span>
                <span class="right">￥${order.payBackA/100.0}</span>
            </li>
            <li class="list-detail clearfix">
                <span class="left">订单编号</span>
                <span class="right">${order.orderSid}</span>
            </li>
        </ul>
    </div>
    <div class="bottom">
        <h3 class="ttl">观影特权</h3>
        <ul>
            <li>step1<span>到电影院售票处直接说：我是乐+会员</span></li>
            <li>step2<span>出示乐+会员二维码或者手机号</span></li>
            <li>step3<span>选电影、选座位直接入场观看</span></li>
        </ul>
    </div>

    <div class="shadow">
        <div class="logo">
            <h3 class="ttl">恭喜您获得乐+鼓励金</h3>
            <h3 class="price">￥0.35</h3>
        </div>

        <p class="desc">
            进入公众号底部菜单中的【周边好店】，查看附近可用鼓励金的商家，消费后还可能得到金币和鼓励金哟~
        </p>
        <div class="btn">知道了</div>
    </div>
</div>
<script>
    $('.btn').on("touchstart", function () {
        $('.shadow').css("display", "none");
    });
</script>
</body>
</html>