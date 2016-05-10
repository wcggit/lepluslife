<%--
  Created by IntelliJ IDEA.
  User: wcg
  Date: 16/4/15
  Time: 下午3:39
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/WEB-INF/commen.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    <link rel="stylesheet" href="css/mui.min.css">
    <!--App自定义的css-->
    <!--<link rel="stylesheet" type="text/css" href="css/location.css"/>-->
</head>

<body>
<!--底部菜单-->
<!--内容部分-->
<div class="mui-content" style="background-color: #fff;overflow: hidden">
    ${topic.content.content}
</div>
</body>
</html>
