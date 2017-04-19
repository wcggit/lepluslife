<%--
  Created by IntelliJ IDEA.
  User: zhangwen
  Date: 2017/4/18
  Time: 18:01
  银行卡列表
--%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/commen.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="viewport" id="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">
    <meta name="format-detection" content="telephone=no">
    <meta name="format-detection" content="telephone=yes"/>
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="black"/>
    <title>银行卡</title>
    <link rel="stylesheet" href="${resourceUrl}/frontRes/css/reset.css">
    <link rel="stylesheet" href="${resourceUrl}/frontRes/user/cardList/css/cardList.css">
</head>
<body>
<section class="cardList">
    <c:if test="${list != null}">
        <c:forEach items="${list}" var="vard">
            <div>
                <p>${vard.bankName}</p>

                <p>${vard.cardType}</p>

                <p>${vard.number}</p>
            </div>
        </c:forEach>
    </c:if>

</section>
<section class="Button">
    <button class="addCard" onclick="bindCard()">+ 添加银行卡</button>
</section>
<section class="tips">
    <p>Tips：</p>

    <p>
        本功能仅需您提供银行卡的卡号，用做POS机消费的身份识别。使用绑定的银行卡在持有乐加POS机的门店消费时，即可获得金币和鼓励金奖励。绑定的银行卡暂时无法注销，如需解绑请联系乐加客服。</p>
</section>
</body>
<script src="${resourceUrl}/js/zepto.min.js"></script>
<script>
    $(".cardList > div").css("height", $(window).width() / 710 * 220 + "px");

    /**绑定银行卡*/
    /**我的银行卡*/
    function bindCard() {
        window.location.href = "/front/user/weixin/addCard";
    }
</script>
</html>
