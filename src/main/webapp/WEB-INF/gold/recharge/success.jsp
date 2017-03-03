<%--
  Created by IntelliJ IDEA.
  User: zhangwen
  Date: 2017/3/3
  Time: 12:59
  金币话费充值成功页面
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/commen.jsp" %>
<html>
<head>
    <title></title>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <!--强制以webkit内核来渲染-->
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <!--按设备宽度缩放，并且用户不允许手动缩放-->
    <meta name="format-detection" content="telephone=no">
    <!--不显示拨号链接-->
    <title>充值成功</title>
    <link rel="stylesheet" href="${resourceUrl}/frontRes/activity/phone/css/common.css">
    <link rel="stylesheet" href="${resourceUrl}/frontRes/app/phone/css/PaymentSuccessful.css">
</head>
<body>
<section>
    <div class="PaySuccess-top">
        <div class="PaySuccess-logoImg">
            <img src="${resourceUrl}/frontRes/app/phone/img/PaySuccess-logo.png" alt="">
        </div>
        <p class="PaySuccess-word">话费${order.worth}元，支付成功！</p>

        <p class="PaySuccess-postscript">话费预计1小时内到账，如有疑问请联系客服400-0412-800</p>
    </div>
    <div class="PaySuccess-middle">
        <div>
            <img src="${resourceUrl}/frontRes/app/phone/img/le+.png" alt="">

            <div class="PaySuccess-leHBWord">
                <p>感谢选择乐+生活充值，奖励<span style="color: #D62C2C;"><fmt:formatNumber type="number"
                                                                                value="${order.payBackScore/100}"
                                                                                pattern="0.00"
                                                                                maxFractionDigits="2"/>元</span>鼓励金！
                </p>

                <p>鼓励金已发放到您的账户中，可在钱包页面查看</p>
            </div>
        </div>
    </div>
</section>
<script src="${resourceUrl}/js/zepto.min.js"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
<script>
    var shareTitle = '金币可以充话费啦',
            shareLink = 'http://www.lepluslife.com/front/order/weixin/recharge',
            shareImgUrl = '${resourceUrl}/loggo.png';
    wx.config({
                  debug: false,
                  appId: '${wxConfig['appId']}',
                  timestamp: ${wxConfig['timestamp']},
                  nonceStr: '${wxConfig['noncestr']}',
                  signature: '${wxConfig['signature']}',
                  jsApiList: [
                      'onMenuShareTimeline', 'onMenuShareAppMessage'
                  ]
              });
    wx.ready(function () {
        // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，
        // config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放
        // 在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
        wx.onMenuShareTimeline({
                                   title: shareTitle, // 分享标题
                                   link: shareLink, // 分享链接
                                   imgUrl: shareImgUrl, // 分享图标
                                   success: function () {
                                       // 用户确认分享后执行的回调函数
                                   },
                                   cancel: function () {
                                       // 用户取消分享后执行的回调函数
                                   }
                               });
        wx.onMenuShareAppMessage({
                                     title: shareTitle, // 分享标题
                                     desc: '金币可以充话费啦！还不快来', // 分享描述
                                     link: shareLink, // 分享链接
                                     imgUrl: shareImgUrl, // 分享图标
                                     type: '', // 分享类型,music、video或link，不填默认为link
                                     dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
                                     success: function () {
                                         // 用户确认分享后执行的回调函数
                                     },
                                     cancel: function () {
                                         // 用户取消分享后执行的回调函数
                                     }
                                 });
    });

</script>
</body>
</html>
