<%--
  Created by IntelliJ IDEA.
  User: zhangwen
  Date: 2016/10/9
  Time: 20:17
  新版app分享页面
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
    <meta name='apple-itunes-app' content='app-id=1155893772'>
    <!--不显示拨号链接-->
    <title>邀请好友</title>
    <link rel="stylesheet" href="${resourceUrl}/frontRes/css/reset.css">
    <link rel="stylesheet" href="${resourceUrl}/frontRes/activity/share/css/invite.css"/>
</head>
<body>
<section class="title-bg">
    <c:if test="${status == 0}">
    <img class="title" src="${resourceUrl}/frontRes/activity/share/img/title.png" alt="">

    <div class="t2">
        <div class="phone">

            <div>
                <div><img src="${resourceUrl}/frontRes/activity/share/img/p.png" alt=""></div>
                <div><input type="number" id="phoneNumber" placeholder="请输入手机号"/></div>
            </div>
        </div>
        <div><img src="${resourceUrl}/frontRes/activity/share/img/tishi.png" alt=""></div>
        <div id="submit" onclick="register()">
            <img src="${resourceUrl}/frontRes/activity/share/img/button.png" alt="">
        </div>
    </div>
</section>
<section class="success">
    <img src="${resourceUrl}/frontRes/activity/share/img/s1.png" alt="">
</section>
<section class="download">
    </c:if>
    <c:if test="${status == 1}">
    <img class="title" src="${resourceUrl}/frontRes/activity/share/img/title2.png" alt="">
</section>
<section class="success">
    <img src="${resourceUrl}/frontRes/activity/share/img/s1.png" alt="">
</section>
<section class="download" style="margin-top: -40%">
    </c:if>
    <div>
        <div onclick="downloadAPP()"><img src="${resourceUrl}/frontRes/activity/share/img/APP.png"
                                          alt=""></div>
    </div>
    <div>
        <div><img src="${resourceUrl}/frontRes/qrcode/12.jpg" alt=""></div>
    </div>
</section>
<section class="success">
    <img src="${resourceUrl}/frontRes/activity/share/img/s2.png" alt="">
</section>
<section class="hb">
    <img src="${resourceUrl}/frontRes/activity/share/img/b1.png" alt="">
    <img src="${resourceUrl}/frontRes/activity/share/img/b2.png" alt="">
</section>
<script src="${resourceUrl}/js/zepto.min.js"></script>
<script>
    $(".title-bg").css("height", $(window).width() / 750 * 1206 + "px");
    $(".phone").css("height", $(window).width() / 750 * 83 + "px");
    $(".success").hide();

    function register() {
        $('#submit').attr('onclick', '');
        var phoneNumber = $("#phoneNumber").val();
        if (!/^1\d{10}$/g.test(phoneNumber)) {
            alert("请输入1开头的11位数字！");
            $('#submit').attr('onclick', 'register()');
            return false;
        }
        $.ajax({
                   type: "get",
                   url: "/weixin/share/submit?token=${token}&phoneNumber=" + phoneNumber,
                   success: function (data) {
                       if (data.status == 200) {
                           $(".title").attr("src",
                                            "${resourceUrl}/frontRes/activity/share/img/success.png");
                           $(".hb").hide();
                           $(".t2").hide();
                           $(".download").css("margin-top", "0");
                           $(".success").css("margin-top", "-40%");
                           $(".success").show();
                       } else {
                           alert(data.msg);
                           $('#submit').attr('onclick', 'register()');
                       }
                   }
               });
    }

    function downloadAPP() {
        window.location.href = 'http://a.app.qq.com/o/simple.jsp?pkgname=com.jfk.ypw.lelife';
    }
</script>

</body>
</html>
