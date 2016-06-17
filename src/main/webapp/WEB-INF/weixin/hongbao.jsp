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
</head>
<body>

<div class="content">
    <div class="topbg"></div>
    <div class="middleform">

        <%--<input type="tel" placeholder="请输入手机号" name="phoneNumber" class="phonenum"/>--%>
        <%--<input type="text" placeholder="请输入验证码" class="yanzhengma" name="verifyCode">--%>
        <%--<button class="yzmbtn" onclick="getVerify()" id="sendCode">获取验证码</button>--%>
        <%--<button class="atonce" onclick="openHongbao()">马上领取</button>--%>

        <input type="tel" placeholder="请输入姓名" name="realName" class="phonenum"/>
        <input type="tel" style="margin:10px auto;" placeholder="请输入手机号" name="phoneNumber" class="phonenum"/>
        <button class="atonce" onclick="openHongbao()">马上领取</button>
    </div>
</div>

</body>
<script src="${resourceUrl}/js/jquery-1.11.3.min.js"></script>
<script type="text/javascript">
    //发送验证码
    function getVerify() {

        var phoneNumber = $("input[name='phoneNumber']").val();

        // if (!phoneNumber.match(/1\d{10}/g)) {
        if (!/^(13[0-9]|14[0-9]|15[0-9]|18[0-9])\d{8}$/i.test(phoneNumber)) {
            alert('请输入正确的手机号！');
            return true;
        }

        $.post("${wxRootUrl}/user/sendCode", {
            phoneNumber: phoneNumber,
            type: 3
        }, function (res) {
            if (res.status == 200) {
                f_timeout();
                return;
            } else {
                alert(res.msg);
            }
        });
    }

    function f_timeout() {

        $('#sendCode').html('<span id="timeb2">60</span>秒后重新获取');
        $('#sendCode').attr({disabled: 'true'});
        timer = self.setInterval(addsec, 1000);
    }

    function addsec() {

        var t = $('#timeb2').html();
        if (t > 0) {
            $('#timeb2').html(t - 1);
        } else {
            window.clearInterval(timer);
            $('#sendCode').html('<span id="timeb2"></span>重获验证码');
            $('#sendCode').attr({disabled: false});
        }
    }

    <%--function openHongbao() {--%>
    <%--var phoneNumber = $("input[name='phoneNumber']").val(),--%>
    <%--validateCode = $("input[name='verifyCode']").val();--%>

    <%--if (!phoneNumber.match(/1\d{10}/g)) {--%>
    <%--alert("请输入正确的手机号");--%>
    <%--return false;--%>
    <%--}--%>
    <%--if (!validateCode || validateCode.length < 1) {--%>
    <%--alert("请输入验证码");--%>
    <%--return false;--%>
    <%--}--%>

    <%--$.ajax({--%>
    <%--type: "post",--%>
    <%--url: "${wxRootUrl}/user/validate",--%>
    <%--data: {--%>
    <%--phoneNumber: phoneNumber,--%>
    <%--code: validateCode--%>
    <%--},--%>
    <%--success: function (data) {--%>
    <%--if (data.status == 200) {--%>
    <%--location.href =--%>
    <%--"${wxRootUrl}/weixin/hongbao/open?phoneNumber=" + phoneNumber;--%>
    <%--} else if (data.status == 202) {--%>
    <%--alert("验证码不正确!");--%>
    <%--return false;--%>
    <%--} else {--%>
    <%--alert("注册失败!");--%>
    <%--}--%>
    <%--},--%>
    <%--error: function () {--%>
    <%--alert("注册失败！");--%>
    <%--}--%>
    <%--})--%>
    <%--}--%>
    function openHongbao() {
        var phoneNumber = $("input[name='phoneNumber']").val(), realName = $("input[name='realName']").val();

        if (realName == null || realName == '') {
            alert("请输入姓名");
            return false;
        }

        if (!phoneNumber.match(/1\d{10}/g)) {
            alert("请输入正确的手机号");
            return false;
        }

        location.href =
        "${wxRootUrl}/weixin/hongbao/open?phoneNumber=" + phoneNumber + "&realName=" + realName;
    }
</script>
</html>
