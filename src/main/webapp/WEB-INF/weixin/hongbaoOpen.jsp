<%--
  Created by IntelliJ IDEA.
  User: wcg
  Date: 16/3/24
  Time: 上午10:05
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/commen.jsp" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <!--强制以webkit内核来渲染-->
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <!--按设备宽度缩放，并且用户不允许手动缩放-->
    <meta name="format-detection" content="telephone=no">
    <!--不显示拨号链接-->
    <title>page1</title>
    <link rel="stylesheet" type="text/css" href="${resourceUrl}/css/hongbao_open.css"/>
</head>
<body>

<div class="content">
    <div class="topbg"></div>
    <div class="middlediv">
        <div class="redpacket">
            <div class="lefticon"></div>
            <div class="redright">
                <p class="num"><span>￥</span>10</p>
                <p class="word">红包</p>
            </div>
        </div>
        <%--<div class="goldbox">--%>
            <%--<div class="lefticon"></div>--%>
            <%--<div class="goldright">--%>
                <%--<p class="num"><span>￥</span>100</p>--%>
                <%--<p class="word">乐+积分</p>--%>
            <%--</div>--%>
        <%--</div>--%>
        <%--<div class="atonce">--%>
            <%--<a href="${wxRootUrl}/weixin/shop">立即消费</a>--%>
        <%--</div>--%>
    </div>
</div>
</body>
</html>
