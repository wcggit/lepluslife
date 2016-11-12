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
    <link rel="stylesheet" href="${resourceUrl}/frontRes/activity/share2/css/register.css"/>
</head>
<body>
<section class="body">
    <div class="titleImg">
        <img src="${resourceUrl}/frontRes/activity/share2/img/titleImg.png" alt="">
    </div>
    <div class="mainImg">
        <img src="${resourceUrl}/frontRes/activity/share2/img/mainImg1.png" alt="">
    </div>
    <div class="phoneSet">
        <div>
            <div class="input-left"></div>
            <div class="input-main">
                <input type="number" name="phoneNumber" placeholder="手机号码"/>
            </div>
            <div class="input-right"></div>
        </div>
        <div>
            <div class="input-left"></div>
            <div class="checkNumber input-main">
                <input type="number" name="validateCode" placeholder="验证码"/>
            </div>
            <div class="input-right"></div>
            <div class="input-left checkBgColor" style="margin-left: 10px;"></div>
            <div class="checkBottom input-main checkBgColor">
                <p onclick="getVerify()" id="sendCode">验证</p>
            </div>
            <div class="input-right checkBgColor"></div>
        </div>
        <div id="clickRegister">
            <div class="input-left registerBgColor"></div>
            <div class="input-main registerBottom registerBgColor" onclick="register()" id="submit">
                立即注册
            </div>
            <div class="input-right registerBgColor"></div>
        </div>
    </div>
    <div class="downloadApp">
        <div class="download">
            <img src="${resourceUrl}/frontRes/activity/share2/img/downloadButton.png" alt="">
        </div>
        <div>
            <img src="${resourceUrl}/frontRes/activity/share2/img/er.png" alt="">
        </div>
    </div>
</section>
<section class="mask">
    <img src="${resourceUrl}/frontRes/activity/share2/img/mask.png" alt="">
</section>
</body>
<script src="${resourceUrl}/js/zepto.min.js"></script>
<script>
    $('html,body').addClass('ovfHiden');
    $(".body").css("height", $(window).height() + "px");
    $(".input-main").css("width", $(window).width() * 0.9 - 88 + "px");
    $(".checkNumber").css("width",
                          $(window).width() * 0.9 - 88 * 2 - 10 - $(".checkBottom").width() + "px");
    $(".mask > img").css("height", $(window).height() + "px");

    var ua = navigator.userAgent.toLowerCase();
    if (/android/.test(ua)) {
        $(".download").click(function () {
            $(".mask").show();
        });
        $(".mask").click(function () {
            $(".mask").hide();
        });
    }
</script>
<script>

    function getVerify() {
        var phoneNumber = $("input[name='phoneNumber']").val();
        if (!/^(13[0-9]|14[0-9]|15[0-9]|18[0-9])\d{8}$/i.test(phoneNumber)) {
            alert('请输入正确的手机号！');
            return true
        }
        $("#sendCode").addClass("disClick");
        f_timeout();
        $.post("/user/sendCode", {
            phoneNumber: phoneNumber,
            type: 3
        }, function (res) {
            if (res.status != 200) {
                alert(res.msg);
                window.clearInterval(timer);
                $("#sendCode").removeClass("disClick");
                $('#sendCode').addClass("reSend").html('<span id="timeb2"></span>重获验证码');
                $('#sendCode').attr({disabled: false});
                $('#sendCode').attr('onclick', 'getVerify()');
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
        var self = '${self}';
        if (self == 1) {
            alert("您只能由别人分享的页面注册,快去告诉小伙伴!");
            return
        }
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
                           type: "get",
                           url: "/weixin/share/submit?token=${token}&phoneNumber=" + phoneNumber,
                           success: function (data) {
                               if (data.status == 200) {
                                   window.location.href =
                                   'http://www.lepluslife.com/resource/frontRes/activity/share2/register.html';
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

    function downloadAPP() {
        window.location.href = 'http://a.app.qq.com/o/simple.jsp?pkgname=com.jfk.ypw.lelife';
    }
</script>


</html>
