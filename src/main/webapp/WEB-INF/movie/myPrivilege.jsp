<%--
  Created by IntelliJ IDEA.
  User: xf
  Date: 2017/5/2
  Time: 16:07
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
    <title>我的特权</title>
    <%--<link rel="stylesheet" href="${commonResource}/css/reset.css">--%>
    <%--<script src="${commonResource}/js/zepto.min.js"></script>--%>
    <%--<link rel="stylesheet" href="${resource}/movie/css/myPrivilege.css">--%>
    <link rel="stylesheet" href="http://image.lepluslife.com/common/css/reset.css">
    <link rel="stylesheet" href="http://image.lepluslife.com/frontRes/movie/css/myPrivilege.css">
    <script src="http://image.lepluslife.com/common/js/zepto.min.js"></script>
</head>
<body>
<ul class="tab">
    <li class="tab-nouse tab-true active">待使用</li>
    <li class="tab-line"></li>
    <li class="tab-used tab-true">已使用</li>
</ul>
<div class="content">
    <div class="content-inner content-nouse active">
        <div class="top">
            <c:forEach var="vaildOrder" items="${vaildMovies}">
                <div class="list">
                    <div class="logo">
                        <img src="${vaildOrder.sMovieProduct.picture}" alt="">
                    </div>
                    <div class="desc">
                        <h3 class="ttl">${vaildOrder.sMovieProduct.name}</h3>
                        <div class="littleLable">
                            <span>不限场次</span><span>不限时间</span>
                            <span>不限2D3D</span><span>不限影片</span>
                        </div>
                        <h3 class="payTime">购买时间：${vaildOrder.dateCompleted}</h3>
                    </div>
                </div>
            </c:forEach>
        </div>
        <div class="bottom">
            <h3 class="ttl">观影特权使用说明 : </h3>
            <ul>
                <li>step1<span>到电影院售票处直接说：我是乐+会员</span></li>
                <li>step2<span>出示乐+会员二维码或者手机号</span></li>
                <li>step3<span>选电影、选座位直接入场观看</span></li>
            </ul>
        </div>
    </div>
    <div class="content-inner content-used">
        <c:forEach var="usedOrder" items="${usedMovies}">
            <div class="list">
                <div class="logo">
                    <img src="${usedOrder.sMovieProduct.picture}" alt="">
                </div>
                <div class="desc">
                    <h3 class="ttl">${usedOrder.sMovieProduct.name}</h3>
                    <h3 class="address">观影地址：${usedOrder.sMovieProduct.introduce}</h3>
                    <h3 class="payTime">购买时间：${usedOrder.dateCompleted}</h3>
                    <div class="logo-used"></div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
<script>
    $('.tab .tab-true').on('touchstart', function () {

        var index = ($(this).index() == 0) ? 0 : 1;
        $('.tab .tab-true').removeClass('active');
        $('.tab .tab-true').eq(index).addClass('active');
        $('.content-inner').removeClass('active');
        $('.content-inner').eq(index).addClass('active');
    })
</script>
</body>
</html>