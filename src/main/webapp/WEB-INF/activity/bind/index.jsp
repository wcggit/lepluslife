<%--
  Created by IntelliJ IDEA.
  User: zhangwen
  Date: 2017/3/8
  Time: 20:17
  用户扫商户注册码注册
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/commen.jsp" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="format-detection" content="telephone=no">
    <meta name='apple-itunes-app' content='app-id=1155893772'>
    <title>会员注册</title>
    <link rel="stylesheet" type="text/css" href="${resourceUrl}/frontRes/gold/index/css/reset.css">
    <link rel="stylesheet" type="text/css"
          href="${resourceUrl}/frontRes/activity/bind/css/register.css">
</head>
<body>
<div class="main">
    <div class="name">${merchantName}</div>
    <div class="out-div">
        <div class="logo"></div>
        <div class="desc">乐+生活会员俱乐部</div>
        <div class="from-wrapper">
            <input class="tel border-1px_bottom" name="phoneNumber" type="text"
                   placeholder="请输入您的手机号码">
        </div>
        <div class="from-wrapper">
            <input class="code" type="text" name="validateCode" placeholder="请输入验证码"><span
                class="btn-getCode" onclick="getVerify()" id="sendCode">获取验证码</span>
        </div>
        <div class="from-wrapper">
            <div class="btn-register" onclick="register()" id="submit">立即注册</div>
        </div>
    </div>
</div>
</body>
<script src="${resourceUrl}/js/zepto.min.js"></script>

<script>
    var merchantSid = '${merchantSid}', subState = eval('${subState}');
    function getVerify() {
        var phoneNumber = $("input[name='phoneNumber']").val();
        if ((!(/^1[3|4|5|6|7|8]\d{9}$/.test(phoneNumber)))) {
            alert("请输入正确的手机号");
            return false
        }
        $("#sendCode").addClass("disClick");
        f_timeout();
        $.post("/code/sign/send", {
            phoneNumber: phoneNumber,
            type: 3,
            source: 'WEB',
            pageSid: '${pageSid}'
        }, function (res) {
            if (res.status != 200) {
                alert(res.msg);
            }
        });
    }
    function f_timeout() {
        $('#sendCode').attr('onclick', '');
        $('#sendCode').addClass("reSend").html('<span id="timeb2">60</span>秒后重发');
        $('#sendCode').attr({disabled: 'true'});
        timer = self.setInterval(addsec, 1000)
    }
    function addsec() {
        var t = $('#timeb2').html();
        if (t > 0) {
            $('#timeb2').html(t - 1)
        } else {
            window.clearInterval(timer);
            $("#sendCode").removeClass("disClick");
            $('#sendCode').html('<span id="timeb2"></span>重获验证码');
            $('#sendCode').attr({disabled: false});
            $('#sendCode').attr('onclick', 'getVerify()');
        }
    }

    function register() {
        $('#submit').attr('onclick', '');
        var phoneNumber = $("input[name='phoneNumber']").val();
        if (!/^1\d{10}$/g.test(phoneNumber)) {
            alert("请输入1开头的11位数字！");
            $('#submit').attr('onclick', 'register()');
            return false;
        }
        var code = $("input[name='validateCode']").val();
        if (code == null || code == '') {
            alert("请输入验证码");
            $('#submit').attr('onclick', 'register()');
            return false
        }
        $.post("/user/validate", {phoneNumber: phoneNumber, code: code}, function (res) {
            if (res.status == 200) {
                $.ajax({
                           type: "post",
                           url: "/front/bind/weixin/submit",
                           data: {phoneNumber: phoneNumber, code: code, merchantSid: merchantSid},
                           success: function (data) {
                               if (data.status == 200) {
                                   if (subState == 1) {
                                       window.location.href =
                                           'http://www.lepluslife.com/resource/frontRes/activity/bind/registerSuccess.html';
                                   } else {
                                       window.location.href =
                                           'http://www.lepluslife.com/resource/frontRes/activity/bind/registerSuccessToSub.html';
                                   }

                               } else {
                                   alert(data.msg);
                                   $('#submit').attr('onclick', 'register()');
                               }
                           }
                       });
            } else {
                $('#submit').attr('onclick', 'register()');
                alert(res.msg)
            }
        });
    }
</script>


</html>
