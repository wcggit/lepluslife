<%--
  Created by IntelliJ IDEA.
  User: zhangwen
  Date: 16/05/09
  Time: 下午9:01
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
    <link rel="stylesheet" type="text/css" href="${resourceUrl}/css/logisticsDetail.css"/>
    <script src="${resourceUrl}/js/jquery-2.0.3.min.js"></script>
</head>

<body>
<!--头部-->
<div class="header-pic">
    <p><font>${expressCompany}</font>快递单号：<font>${expressNumber}</font></p>
</div>

<!--内容部分-->
<div class="mui-content">
    <ul class="mui-table-view list trade-font-grey list-detail">
        <c:forEach items="${expressList}" var="express" varStatus="status">
            <c:if test="${status.count == 1}">
                <li class="mui-table-view-cell focus-class">
                    <span class="cicle-icon"></span>

                    <p>${express.time}</p>

                    <p>${express.status}</p>
                </li>
            </c:if>
            <c:if test="${status.count != 1}">
                <li class="mui-table-view-cell init-class">
                    <span class="cicle-icon"></span>

                    <p>${express.time}</p>

                    <p>${express.status}</p>
                </li>
            </c:if>

        </c:forEach>
    </ul>
</div>
</body>
<script type="text/javascript" src="${resourceUrl}/js/mui.min.js"></script>
</html>