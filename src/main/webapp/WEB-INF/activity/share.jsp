<%--
  Created by IntelliJ IDEA.
  User: zhangwen
  Date: 2016/9/7
  Time: 20:17
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/commen.jsp" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <!--强制以webkit内核来渲染-->
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <!--按设备宽度缩放，并且用户不允许手动缩放-->
    <meta name="format-detection" content="telephone=no">
    <!--不显示拨号链接-->
    <title></title>
    <link rel="stylesheet" href="${resourceUrl}/frontRes/activity/share/css/sharePage.css"/>
</head>
<body>
<div id="page">
    <c:if test="${status == 1}">
        <div class="floor floor12"></div>
    </c:if>
    <c:if test="${status == 0}">
        <div class="floor floor1"></div>
        <div class="floor floor2"></div>
        <div class="floor floor3">
            <input class="tel" id="phoneNumber" type="text" placeholder="请输入手机号"/>

            <div class="registered" onclick="register()"></div>
        </div>
    </c:if>
    <div class="floor floor4"></div>
    <div class="floor floor5"></div>
    <div class="floor floor6"></div>
    <div class="floor floor7"></div>
    <div class="floor floor8"></div>
    <div class="floor floor9"></div>
    <div class="floor floor10"></div>
    <div class="floor floor11"></div>
</div>
<script src="${resourceUrl}/js/jquery-1.11.3.min.js"></script>
<script>
    function register() {
        var phoneNumber = $("#phoneNumber").val();
        if (!/^1\d{10}$/g.test(phoneNumber)) {
            alert("请输入1开头的11位数字！");
            return false;
        }
        $.ajax({
                   type: "get",
                   url: "/weixin/share/submit?token=${token}&phoneNumber=" + phoneNumber,
                   success: function (data) {
                       if (data.status == 200) {
                           location.href =
                           "${resourceUrl}/frontRes/activity/share/registrationSuccess.html";
                       } else {
                           alert(data.msg);
                       }
                   }
               });
    }
</script>

</body>
</html>
