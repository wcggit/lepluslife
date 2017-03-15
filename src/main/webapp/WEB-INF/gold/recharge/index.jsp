<%--
  Created by IntelliJ IDEA.
  User: zhangwen
  Date: 2017/2/22
  Time: 17:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    <title>金币充值话费</title>
    <link rel="stylesheet" href="${resourceUrl}/frontRes/activity/phone/css/common.css">
    <link rel="stylesheet" href="${resourceUrl}/frontRes/activity/phone/css/homePage.css">
    <link rel="stylesheet" href="${resourceUrl}/frontRes/activity/phone/css/soldOut.css">
    <link rel="stylesheet" href="${resourceUrl}/frontRes/app/phone/css/Recharge2.css">
    <script src="${resourceUrl}/js/jquery.min.js"></script>

</head>
<style>
    .mask-close {
        z-index: 999999;
    }
</style>
<body>
<div class="main">
    <div class="top">
        <ul class="description">
            <li class="left" onclick="goOrderList()"><span></span>查看充值记录</li>
            <li class="right">如何赚金币<span></span></li>
        </ul>
        <p class="jf-num"><fmt:formatNumber
                type="number" value="${canUseScore/100}" pattern="0.00"
                maxFractionDigits="2"/></p>

        <p class="jf-desc">我的金币</p>

    </div>
    <div class="bottom">
        <p class="telNum ttl">充值手机号</p>
        <input class="telNum" type="text" id="newTel" placeholder="请输入您的手机号" value="${phone}">
        <span id="notes" style="display: none"></span>

        <p class="ttl">请选择充值金额</p>

        <p class="moneyNum">
        </p>

        <%--<p class="hotline">乐加客服电话 <a href="tel:400-0412-800">400-0412-800</a></p>--%>
    </div>
    <p class="btn-recharge btn-yes" onclick="btnFun()">提交订单</p>
</div>
<section class="mask">
    <div class="information">
        <div class="mask-top">
            <div class="mask-close"><a></a></div>
            <p>付款详情</p>
        </div>
        <div class="mask-main">
            <div>订单信息</div>
            <div><span id="submitPhone"></span> <span id="submitArea"></span> 充值<span
                    id="submitWorth"></span>元
            </div>
        </div>
        <div class="mask-main">
            <div>订单价格</div>
            <div><span id="submitScore"></span>金币+<span id="submitPrice"></span>元</div>
        </div>
        <div class="mask-main mask-payPrice">
            <div>需付金额</div>
            <div><span id="submitNeedPay"></span>元</div>
        </div>
        <div class="mask-main mask-integralOver">
            <div>金币余额 ：<span id="submitUserScore"></span></div>
            <div id="submitExtra" style="display: none;">金币不足，差额将以人民币支付</div>
        </div>
        <div class="mask-button">
            <div class="confirmPayment" onclick="buy()">确认支付<span id="realNeedPay"></span>元</div>
        </div>
    </div>
    <div class="layer"></div>
</section>
<script src="http://res.wx.qq.com/open/js/jweixin-1.1.0.js"></script>
<script>
    $(".mask").css("height", $(window).height() + 100 + "px");
    $(".layer > div").css("height", $(window).width() / 375 * 300 + "px");
    function layerClose() {
        $(".mask").fadeOut();
        $(".layer").empty();
    }
    //    遮罩层设置
    $(".mask").on('touchstart', function () {
        $(".mask").on('touchmove', function (event) {
            event.preventDefault();
        }, false);
    });
    $(".mask-close").click(function () {
        $(".mask").fadeOut();
    });
    var userPhone = '${phone}', submitArea = '', selectScore = 0, selectPrice = 0;
    //填充手机号
    if (userPhone != null && userPhone != '' && userPhone.length == 11) {
        $('#newTel').val(userPhone);
        getPhoneInfo(userPhone);
    }
    //绑定手机号码输入框，当内容改变时执行以下代码
    $('#newTel').bind('input propertychange', function () {
        $(document).keyup(function (event) {
            var telephone = $('#newTel').val();
            console.log(telephone);
            if (telephone.length == 11) {
                getPhoneInfo(telephone);
            } else {
                $("#notes").empty();
            }

        });
    });
    //异步请求获取手机归属地
    function getPhoneInfo(currPhone) {
        $.ajax({
                   type: "get",
                   url: "https://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=" + currPhone,
                   async: false,
                   dataType: 'jsonp',
                   timeout: 1000,
                   contentType: "application/json;utf-8",
                   data: null,
                   success: function (data) {
                       if (data.carrier != null) {
                           submitArea = data.carrier;
                           $("#notes").html("（" + data.carrier + "）");
                       }
                   }
               });
    }
    //强制保留两位小数
    function toDecimal(x) {
        var f = parseFloat(x);
        if (isNaN(f)) {
            return false;
        }
        var f = Math.round(x * 100) / 100;
        var s = f.toString();
        var rs = s.indexOf('.');
        if (rs < 0) {
            rs = s.length;
            s += '.';
        }
        while (s.length <= rs + 2) {
            s += '0';
        }
        return s;
    }

    function goOrderList() {
        window.location.href = "/front/phone/weixin/orderList";
    }

</script>
<script>
    //填充话费面额列表
    var worthElement = $('.moneyNum'), worthHtml = '', userScore = eval('${canUseScore}');
    $.get("/app/banner/common/52",
          function (res) {
              var worthList = res.data.split('_');
              for (var q = 0; q < worthList.length; q++) {
                  worthHtml +=
                  '<span class="initClass">' + worthList[q] + '元</span>';
              }
              worthElement.html(worthHtml);
              //moneyNumList
              var moneyNumList = $('.main .bottom .moneyNum span');
              moneyNumList.each(function (i) {
                  moneyNumList.eq(i).on('touchstart', function () {
                      moneyNumList.removeClass('focusClass').addClass('initClass');
                      $(this).removeClass('initClass').addClass('focusClass');
                  })
              });
          });
    //        在此函数内书写“立刻充值”的按钮绑定的事件
    var orderId = '';
    function btnFun() {
        var currPhone = $('#newTel').val();
        if (!/1[0-9]{10}/.test(currPhone)) {
            alert("请输入正确的手机号码");
            return
        }

        console.log('手机号码===' + currPhone + '金币余额===' + userScore);
        var focusWorth = $('.focusClass').html();
        if (focusWorth == null) {
            alert('请选择充值面额');
            return
        }
        var selectWorth = focusWorth.substr(0, focusWorth.length - 1);
        $('#submitPhone').html(currPhone);
        $('#submitArea').html(submitArea);
        $('#submitWorth').html(selectWorth);
        if (userScore > selectWorth * 100) {
            selectScore = selectWorth * 100;
            selectPrice = 0;
            $('#submitExtra').css('display', 'none');
        } else {
            selectScore = userScore;
            selectPrice = selectWorth * 100 - selectScore;
            $('#submitExtra').css('display', 'block');
        }
        $('#submitScore').html(toDecimal(selectScore / 100));
        var needPay = 0;
        $('#submitPrice').html(toDecimal(selectPrice / 100));
        console.log('用户积分=' + userScore);
        $('#submitUserScore').html(toDecimal(userScore / 100));
        var p = toDecimal(selectPrice / 100);
        $('#submitNeedPay').html(p);
        $('#realNeedPay').html(p);

        $(".mask").fadeIn(200);
        $(".mask > div").addClass("maskAnimation");
        $(".information").fadeIn();
    }
    function buy() {
        var focusWorth = $('.focusClass').html();
        var selectWorth = focusWorth.substr(0, focusWorth.length - 1);
        console.log('手机号码===' + $('#newTel').val() + '面值===' + selectWorth + '金币余额===' + userScore);
        $.post("/front/phone/create", {phone: $('#newTel').val(), worth: selectWorth},
               function (res) {
                   if (res.status == 2000) {//全积分支付成功
                       orderId = res.data;
                       // 支付成功后的回调函数
                       window.location.href = '/weixin/pay/phoneSuccess/' + orderId;
                   } else if (res.status == 200) { //创建订单成功，吊起微信支付
                       orderId = res.data.orderId;
                       weixinPay(res.data);
                   } else {
                       alert(res.msg);
                   }
               })
    }
</script>
<script>
    function weixinPay(res) {
        wx.chooseWXPay({
                           timestamp: res['timeStamp'], // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
                           nonceStr: res['nonceStr'], // 支付签名随机串，不长于 32 位
                           package: res['package'], // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
                           signType: res['signType'], // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
                           paySign: res['sign'], // 支付签名
                           success: function (res) {
                               // 支付成功后的回调函数
                               window.location.href = '/weixin/pay/phoneSuccess/' + orderId;
                           },
                           cancel: function (res) {
                               alert("pay cancel");
                               $('.btn-recharge').attr('onclick', 'btnFun()');
                           },
                           fail: function (res) {
                               alert("pay fail");
                               $('.btn-recharge').attr('onclick', 'btnFun()');
                           }
                       });
    }

    wx.config({
                  debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                  appId: '${wxConfig['appId']}', // 必填，公众号的唯一标识
                  timestamp: ${wxConfig['timestamp']}, // 必填，生成签名的时间戳
                  nonceStr: '${wxConfig['noncestr']}', // 必填，生成签名的随机串
                  signature: '${wxConfig['signature']}',// 必填，签名，见附录1
                  jsApiList: [
                      'chooseWXPay'
                  ] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
              });
    wx.ready(function () {
        // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
//       隐藏菜单
        wx.hideOptionMenu();

    });
    wx.error(function (res) {
        // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。

    });

</script>
</body>
</html>
