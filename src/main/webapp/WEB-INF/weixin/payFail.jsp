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
  <meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
  <meta name="apple-mobile-web-app-capable" content="yes">
  <meta name="apple-mobile-web-app-status-bar-style" content="black">
  <!--标准mui.css-->
  <link rel="stylesheet" href="${resourceUrl}/css/mui.min.css">
  <!--App自定义的css-->
  <!--<link rel="stylesheet" type="text/css" href="css/trade_detail.css"/>-->
  <style>
    .mui-content{
      position: relative;
    }
    .mui-content .failed-top{
      width: 48vw;
      height: 34.27vw;
      background: url(${resourceUrl}/images/tradeDetail/payfailed.png) no-repeat;
      background-size: 100%;
      position: absolute;
      top: 30vw;
      left: 0;
      right: 0;
      margin: auto;
    }
    .mui-content .failed-btn{
      width: 45vw;
      height: 12vw;
      line-height: 12vw;
      position: absolute;
      top: 100vw;
      left: 0;
      right: 0;
      margin: auto;
      color: #FFFFFF;
      background: #D62C2C;
      text-align: center;
    }

  </style>
</head>

<body style="background: #fff;">
<!--头部-->
<header class="mui-bar mui-bar-nav" style="background: #fff;">
  <a style="color: #D62D2B;" class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
  <h1 class="mui-title" style="color: #D62D2B;font-weight: bold;">付款失败</h1>
  <a style="color: #D62D2B;" class="mui-action-back mui-icon mui-icon-more mui-pull-right"></a>
</header>
<div class="mui-content" style="background: #fff;">
  <div class="failed-top"></div>
  <div class="failed-btn" onclick="goPage('shop')">再去逛逛</div>
</div>
</body>
<script type="application/javascript">
  function goPage(page) {
    location.href = "${wxRootUrl}/weixin/" + page;
  }

</script>

</html>
