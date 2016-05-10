<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@include file="/WEB-INF/commen.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <!--强制以webkit内核来渲染-->
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <!--按设备宽度缩放，并且用户不允许手动缩放-->

    <script type="text/javascript">
        window.onload = function () {
        setTimeout(fnScore, 1000);
        }
        function fnScore() {
        location.href = "${redirectUrl}";
        }

    </script>
    <meta name="format-detection" content="telephone=no">
    <!--不显示拨号链接-->
    <title></title>
    <style>
        * {
            margin: 0;
            padding: 0;
            border: none;
        }

        html, body {
            height: 100%;
            overflow: hidden;

            background-color: #EDEDED;
        }

        /*page bg*/
        #page {
            position: absolute;
            width: 100%;
            height: 100%;
            top: 0;
        }

        /*背景*/
        #page > .page_bg {
            position: absolute;
            z-index: -1;
            width: 100%;
            height: 100%;
            background-color: #fff;
        }

        .main {
            position: absolute;
            background-color:;
            background-image: url("${resourceUrl}/images/img.jpg");
            background-repeat: no-repeat;
            background-size: 100%;
            padding: 0;
            width: 100%;
            height: 100%;
            margin: 0;
            background-color: #fff;
        }
    </style>
</head>
<body>
<div id="page">
    <div class="page_bg"></div>
    <div class="main"></div>
</div>
</body>
</html>