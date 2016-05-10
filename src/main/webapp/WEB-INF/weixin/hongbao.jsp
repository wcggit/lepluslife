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
    <title></title>
    <link rel="stylesheet" type="text/css" href="${resourceUrl}/css/hongbao_close.css"/>
    <script type="text/javascript">
        function openHongbao() {
            location.href = "${wxRootUrl}/weixin/hongbao/open";
        }
        function goUser() {
            location.href = "${wxRootUrl}/weixin/user";
        }
    </script>
</head>
<body>
<div id="page">
    <div class="page_bg"></div>

    <div class="page_content">
        <div class="content_topPic"></div>
        <p class="content_ttl">乐+生活</p>

        <p class="content_from">给你发了一个利是</p>

        <p class="content_practice">恭喜发财，大吉大利</p>

        <div class="content_open" onclick="openHongbao()"></div>
    </div>

</div>
</body>
</html>
