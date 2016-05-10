<%--
  Created by IntelliJ IDEA.
  User: wcg
  Date: 16/3/29
  Time: 下午2:42
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <!--<link rel="stylesheet" type="text/css" href="css/trade_detail.css"/>-->
    <style>
        .mui-content {
            position: relative;
        }

        .mui-content .success-top {
            width: 66.27vw;
            height: 29.73vw;
            background: url(${resourceUrl}/images/tradeDetail/paysuccess.png) no-repeat;
            background-size: 100%;
            position: absolute;
            top: 30vw;
            left: 0;
            right: 0;
            margin: auto;

        }

        .mui-content .success-top span {
            font-size: 4vw;
            color: #D62C2C;
            font-weight: bold;
            position: absolute;
            bottom: -1vw;
            right: -4vw;
        }

        .mui-content .success-ttl {
            width: 100vw;
            font-size: 4.5vw;
            line-height: 10vw;
            border-top: solid 1px #E1E0E0;
            border-bottom: solid 1px #E1E0E0;
            color: #ccc;
            font-weight: bold;
            position: absolute;
            top: 85vw;
            text-align: center;
            box-shadow: #333 1px 0px 4px -2px;
        }

        .mui-content .success-btn {
            width: 100vw;
            height: 12vw;
            position: absolute;
            top: 115vw;
        }

        .mui-content .success-btn span {
            display: inline-block;
            width: 45vw;
            height: 12vw;
            border: solid 1px #D62C2C;
            text-align: center;
            line-height: 12vw;
            margin: 0 2.5vw;
        }

        .mui-content .success-btn span:first-child {
            color: #FFFFFF;
            background: #D62C2C;

        }

        .mui-content .success-btn span:last-child {
            color: #D62C2C;
        }

    </style>
</head>

<body style="background: #fff;">
<!--头部-->
<header class="mui-bar mui-bar-nav" style="background: #fff;">
    <a style="color: #D62D2B;" class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>

    <h1 class="mui-title" style="color: #D62D2B;font-weight: bold;">付款成功</h1>
    <a style="color: #D62D2B;" class="mui-action-back mui-icon mui-icon-more mui-pull-right"></a>
</header>
<div class="mui-content" style="background: #fff;">
    <div class="success-top">
        <span>获得<font>${payBackScore/100}</font>元乐+红包</span>
    </div>
    <p class="success-ttl">乐+生活会尽快为您安排发货的！</p>

    <div class="success-btn">
        <span onclick="goPage('orderDetail')">查看订单</span><span onclick="goPage('shop')">再去逛逛</span>
    </div>
</div>
</body>
<script src="${resourceUrl}/js/mui.min.js"></script>

<script type="application/javascript">
    function goPage(page) {
        location.href = "${wxRootUrl}/weixin/" + page;
    }

</script>


</html>