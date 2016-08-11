<%--
  Created by IntelliJ IDEA.
  User: wcg
  Date: 16/4/18
  Time: 下午2:18
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/WEB-INF/commen.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="viewport" id="viewport" content="width=device-width, initial-scale=1">
    <meta name="format-detection" content="telephone=no">
    <meta name="format-detection" content="telephone=yes"/>
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="black"/>
    <title>关注领红包</title>
    <link rel="stylesheet" href="${resourceUrl}/frontRes/css/reset.css">
    <link rel="stylesheet" href="${resourceUrl}/frontRes/activity/forever/css/active3.css">
</head>
<body>
<section class="header">
    <img id="t10" src="${resourceUrl}/frontRes/activity/forever/img/header${singleMoney}.png"
         alt="">
</section>
<section class="main hongbao">
    <div>
        <c:if test="${status == 200}">
            <img id="h10"
                 src="${resourceUrl}/frontRes/activity/forever/img/hongbao${singleMoney}.png"
                 alt="">
        </c:if>
        <c:if test="${status != 200}">
            <img id="he" src="${resourceUrl}/frontRes/activity/forever/img/empty.png" alt="">
        </c:if>
    </div>
</section>
<section class="main">
    <div class="shop">
        <img src="${resourceUrl}/frontRes/activity/forever/img/shop.png" alt="">
    </div>
    <div class="xu">
        <div></div>
    </div>
    <div class="more" onclick="goMerchant()">
        查看更多
    </div>
</section>
<section class="main">
    <div>
        <img src="${resourceUrl}/frontRes/activity/forever/img/pai.png" alt="">
    </div>
</section>
<section class="main activityIntroduction">
    <div>
        <img src="${resourceUrl}/frontRes/activity/forever/img/activityIntroduction.png" alt="">
    </div>
</section>
<section class="k"></section>
</body>
<script src="${resourceUrl}/js/jquery-1.11.3.min.js"></script>
<script>
    $(".hongbao").css("height",$(window).width() / 690 * 603 + "px");
    function goMerchant() {
        window.location.href = "http://www.lepluslife.com/merchant/index";
    }
</script>
</html>
