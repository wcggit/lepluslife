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
    <!--App自定义的css-->
    <link rel="stylesheet" type="text/css" href="${resourceUrl}/css/newPersonal.css"/>
</head>
<script type="application/javascript">
    document.title = "我的钱包";
    function goPage(page) {
        location.href = "${wxRootUrl}/weixin/" + page;
    }

</script>
<script src="${resourceUrl}/js/jquery-2.0.3.min.js"></script>

<body>

<div class="content">
    <div class="personal">
        <div class="person">
            <div class="pic">
                <img src="${user.headImageUrl}" alt="">
            </div>
            <p>${user.nickname}</p>
        </div>
    </div>
    <div class="redpacket">
        <p>￥${scoreA.score/100}</p>
    </div>
    <div class="point">
        <p>￥${scoreB.score}</p>
    </div>
    <div class="bill" onclick="goPage('scoreDetail')">
        <img src="${resourceUrl}/images/newPersonal/bill.png" alt="">

        <p>积分明细</p>
    </div>
    <c:if test="${user.leJiaUser.phoneNumber == null || user.leJiaUser.phoneNumber == ''}">
        <div class="phonenum">
            <img src="${resourceUrl}/images/newPersonal/phone.png" alt="">

            <p>绑定手机号</p>
            <a href="${wxRootUrl}/weixin/hongbao">绑定就送￥100元大礼包</a>
            <span>&gt;</span>
        </div>
    </c:if>
    <c:if test="${user.leJiaUser.phoneNumber != null && user.leJiaUser.phoneNumber != ''}">
        <div class="phonenum">
            <img src="${resourceUrl}/images/newPersonal/phone.png" alt="">

            <p>${user.leJiaUser.phoneNumber}</p>
        </div>
    </c:if>

    <div class="que">
        <a href="${resourceUrl}/scoreExplain.html">
            积分和红包怎么用？
        </a>
    </div>
</div>

</body>

</html>
