<%--
  Created by IntelliJ IDEA.
  User: root
  Date: 2017/6/22
  Time: 17:20
  团购单品可使用商家列表页
--%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/commen.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no"/>
    <meta name="format-detection" content="telephone=no">
    <meta name="format-detection" content="telephone=yes"/>
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="black"/>
    <title>可用门店</title>
    <link rel="stylesheet" type="text/css" href="${commonResource}/css/reset.css">
    <link rel="stylesheet" type="text/css" href="${commonResource}/css/main.css">
    <link rel="stylesheet" type="text/css" href="${commonResource}/css/swiper.min.css">
    <link rel="stylesheet" type="text/css"
          href="${resource}/groupon/product_merchant/css/canStore.css">
</head>
<body>
<!--header-->
<header class="fixClear">
    <div>${name}</div>
    <div>${list.size()}家店可用</div>
</header>
<section class="appendStore">
    <c:if test="${status == 1}">
        <c:forEach items="${list}" var="m">
            <div class="fixClear"
                 onclick="window.location.href='/front/shop/weixin/m?id=${m.merchantId}&status=1&distance=${m.distance}'">
                <div class="mainImg">
                    <img src="${m.picture}" alt="">
                </div>
                <div class="main">
                    <p>${m.name}</p>
                    <p>${m.location}</p>
                    <div class="fixClear">
                        <div><img src="${resource}/groupon/product_detail/img/address.png" alt="">
                        </div>
                        <c:if test="${m.distance > 1000}">
                            <div>距您<fmt:formatNumber type="number" value="${m.distance/1000}"
                                                     pattern="0.0"
                                                     maxFractionDigits="1"/>km
                            </div>
                        </c:if>
                        <c:if test="${m.distance < 1000}">
                            <div>距您${m.distance}m</div>
                        </c:if>
                    </div>
                </div>
            </div>
        </c:forEach>
    </c:if>
    <c:if test="${status != 1}">
        <c:forEach items="${list}" var="m">
            <div class="fixClear"
                 onclick="window.location.href='/front/shop/weixin/m?id=${m.id}&status=0'">
                <div class="mainImg">
                    <img src="${m.picture}" alt="">
                </div>
                <div class="main">
                    <p>${m.name}</p>
                    <p>${m.location}</p>
                    <div class="fixClear">
                        <div><img src="${resource}/groupon/product_detail/img/address.png" alt="">
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
    </c:if>

</section>
</body>
</html>
