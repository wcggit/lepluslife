<%--
  Created by IntelliJ IDEA.
  User: zhangwen
  Date: 2017/4/18
  Time: 17:05
  用户个人中心
--%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/commen.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="viewport" id="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">
    <meta name="format-detection" content="telephone=no">
    <meta name="format-detection" content="telephone=yes"/>
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="black"/>
    <title>个人中心</title>
    <link rel="stylesheet" href="${resourceUrl}/frontRes/css/reset.css">
    <link rel="stylesheet" href="${resourceUrl}/frontRes/user/index/css/index.css">
</head>
<body>
<section class="myHeader">
    <div class="headerImg">
        <img src="${user.headImageUrl}" alt="">
    </div>
    <div class="erCode fixClear u-e" onclick="showQrcode()">
        <div><img src="${resourceUrl}/frontRes/user/index/img/erCode.png" alt=""></div>
        <div><img src="${resourceUrl}/frontRes/user/index/img/to.png" alt=""></div>
    </div>
    <p class="userName">${user.nickname}</p>

    <div class="dataShow">
        <div>
            <p>金币余额</p>

            <p onclick="goPage('score/scoreDetail?type=2')"><fmt:formatNumber
                    type="number" value="${scoreC.score/100}"
                    pattern="0.00"
                    maxFractionDigits="2"/></p>
        </div>
        <div>
            <p>鼓励金余额</p>

            <p onclick="goPage('score/scoreDetail?type=0')"><fmt:formatNumber
                    type="number" value="${scoreA.score/100}"
                    pattern="0.00"
                    maxFractionDigits="2"/></p>
        </div>
    </div>
</section>
<section class="shopButton">
    <div class="fixClear" onclick="goldIndex()">
        <div><img src="${resourceUrl}/frontRes/user/index/img/zhen.png" alt=""></div>
        <div>臻品商城</div>
    </div>
    <div class="fixClear" onclick="shopIndex()">
        <div><img src="${resourceUrl}/frontRes/user/index/img/li.png" alt=""></div>
        <div>周边好店</div>
    </div>
</section>
<section class="Button">
    <button class="addCard" onclick="bindCard()">我的银行卡</button>
</section>
<section class="w-tc">
    <div>
        <div>
            <img src="${resourceUrl}/frontRes/user/index/img/logo_.png" alt=""/>
        </div>
        <div class="closeLayer">
            <img src="${resourceUrl}/frontRes/user/center/img/close-l.png" alt=""/>
        </div>
    </div>
    <div>
        <div id="qrcode"></div>
    </div>
    <div>
        <img width="60px" height="60px" class="yj" src="${user.headImageUrl}" alt=""/>

        <p style="font-size: 15px; color:#666;margin: 10px 0 5px 0">${user.nickname}</p>

        <p style="font-size: 13px;color:#999;">扫一扫二维码，识别会员身份</p>
    </div>
</section>
</body>
<script src="${resourceUrl}/js/jquery-2.0.3.min.js"></script>
<script src="${resourceUrl}/js/qrcode.js"></script>
<script src="${resourceUrl}/frontRes/user/center/js/layer.js"></script>
<script>
    $(".myHeader").css("height", $(window).width() / 750 * 510 + "px");

    /**跳转到臻品商城*/
    function goldIndex() {
        window.location.href = "/front/gold/weixin";
    }
    /**跳转到周边好店*/
    function shopIndex() {
        window.location.href = "/front/shop/weixin";
    }
    /**我的银行卡*/
    function bindCard() {
        window.location.href = "/front/user/weixin/cardList";
    }
    /**鼓励金或金币详情*/
    function goPage(page) {
        location.href = "/" + page;
    }
    /**展示二维码*/
    $(".u-e").click(function () {
        $(".w-tc").show();
        layer.open({
                       type: 1,
                       title: false,
                       closeBtn: 0,
                       area: ['83%', '430px'], //宽高
                       content: $(".w-tc")
                   });
    });
    $(".closeLayer").click(function () {
        layer.closeAll();
        $(".w-tc").hide();
    });
    var qrcode = null, size = 150, content = 'leplususer:' + '${user.leJiaUser.userSid}';
    function showQrcode() {
        if (qrcode != null) {
            qrcode.clear();
        }
        content = content.replace(/(^\s*)|(\s*$)/g, "");
        // 创建二维码
        qrcode = new QRCode(document.getElementById("qrcode"), {
            width: size,//设置宽高
            height: size
        });
        qrcode.makeCode(content);
    }
</script>
</html>
