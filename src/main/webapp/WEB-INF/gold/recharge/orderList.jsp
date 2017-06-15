<%--
  Created by IntelliJ IDEA.
  User: zhangwen
  Date: 2016/11/4
  Time: 12:59
  话费充值记录
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/commen.jsp" %>
<html>
<head>
    <title></title>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <!--强制以webkit内核来渲染-->
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <!--按设备宽度缩放，并且用户不允许手动缩放-->
    <meta name="format-detection" content="telephone=no">
    <!--不显示拨号链接-->
    <title>积分充值话费</title>
    <link rel="stylesheet" href="${resourceUrl}/frontRes/activity/phone/css/common.css">
    <link rel="stylesheet" href="${resourceUrl}/frontRes/activity/phone/css/rechargeRecord.css">
</head>
<style>
    .main .top ul li:first-child {
        border: 0;
    }
    .main .top ul li {
        width: 100%;
    }
</style>
<body>
<div class="main">
    <div class="top">
        <p>您已累计节省</p>
        <ul>
            <li class="">
                <span class="num"><span class="icon"></span><fmt:formatNumber type="number"
                                                                              value="${totalScore/100}"
                                                                              pattern="0.00"
                                                                              maxFractionDigits="2"/></span>元话费
            </li>
            <%--<li class="left">--%>
            <%--<span class="num"><span class="icon"></span>${totalScore}</span>积分--%>
            <%--</li>--%>
        </ul>
    </div>
    <ul class="bottom">
        <c:forEach items="${orderList}" var="order">
            <li class="list list-ing">
                <c:if test="${order.type == 2}">
                    <p>充值话费：￥${order.worth} (￥<fmt:formatNumber type="number"
                                                                value="${order.truePrice/100}"
                                                                pattern="0.00"
                                                                maxFractionDigits="2"/>+<fmt:formatNumber
                            type="number"
                            value="${order.trueScoreB/100}"
                            pattern="0.00"
                            maxFractionDigits="2"/>金币)</p>
                </c:if>
                <c:if test="${order.type != 2}">
                    <p>充值话费：￥${order.worth} (￥<fmt:formatNumber type="number"
                                                                value="${order.truePrice/100}"
                                                                pattern="0.00"
                                                                maxFractionDigits="2"/>+${order.trueScoreB}积分)</p>
                </c:if>

                <p>手机号：${order.phone}</p>

                <p><fmt:formatDate value="${order.payDate}"
                                   pattern="yyyy-MM-dd HH:mm:ss"/></p>
                <span class="state state-ing">
                    <c:if test="${order.state == 1}">
                        充值中
                    </c:if>
                    <c:if test="${order.state == 2}">
                        已充值
                    </c:if>
                    <c:if test="${order.state == 3}">
                        充值中
                    </c:if>
                </span>
            </li>
        </c:forEach>
    </ul>
    <div class="btn" onclick="goPhoneIndex()">继续充值</div>
</div>
<script>
    function goPhoneIndex() {
//        window.location.href = "/front/phone/weixin/index";
        window.location.href = "/front/order/weixin/recharge";
    }
</script>
</body>
</html>
