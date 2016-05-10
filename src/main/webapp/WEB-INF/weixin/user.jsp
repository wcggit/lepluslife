<%--
  Created by IntelliJ IDEA.
  User: wcg
  Date: 16/3/18
  Time: 下午8:58
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/WEB-INF/commen.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>

<title></title>
<head>
    <meta charset="utf-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <!--标准mui.css-->
    <link rel="stylesheet" href="${resourceUrl}/css/mui.min.css">
    <!--App自定义的css-->
    <link rel="stylesheet" type="text/css" href="${resourceUrl}/css/personal.css"/>
</head>
<script type="application/javascript">
    document.title = "我的钱包";
    function goPage(page) {
        location.href = "${wxRootUrl}/weixin/" + page;
}

</script>
<script src="${resourceUrl}/js/jquery-2.0.3.min.js"></script>

<body>

<div id="tabbar-with-person" class="mui-control-content mui-active" style="padding-bottom: 75px;">
    <div class="page_top"
         style="background: url('${resourceUrl}/images/personal/bg.png') no-repeat;background-size: 100%;">
        <div class="top_face"
             style="background: url('${user.headImageUrl}') no-repeat;background-size: 100%;"></div>
        <p class="top_ttl">ID：<font>${user.nickname}</font></p>
    </div>
    <div class="page_code"><img src="${user.leJiaUser.oneBarCodeUrl}" alt=""/></div>
    <ul class="page_bottom">
        <li class="bottom_jf">
            <p>${scoreB.score}</p>

            <p>乐+积分</p>
        </li>
        <li style="height: 12vw;overflow: hidden;margin: 4vw 0 0;">
            <img src="${resourceUrl}/images/personal/line.png" alt=""/>
        </li>
        <li class="bottom_money">
            <p>${scoreA.score/100}</p>

            <p>红包余额</p>
        </li>
    </ul>
    <a class="page_btn" onclick="goPage('scoreDetail')">交易明细</a>
</div>
</body>

</html>
