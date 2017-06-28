<%--
  Created by IntelliJ IDEA.
  User: root
  Date: 2017/6/22
  Time: 17:15
  单品的图文详情.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <title>图文详情</title>
    <link rel="stylesheet" type="text/css" href="${commonResource}/css/reset.css">
    <link rel="stylesheet" type="text/css" href="${commonResource}/css/main.css">
</head>
<body>
<c:forEach items="${list}" var="e">
    <img src="${e.picture}" alt="">
</c:forEach>
</body>
</html>
