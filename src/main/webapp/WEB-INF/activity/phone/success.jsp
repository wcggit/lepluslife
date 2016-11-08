<%--
  Created by IntelliJ IDEA.
  User: zhangwen
  Date: 2016/11/7
  Time: 12:59
  话费充值成功页面
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
    <link rel="stylesheet" href="${resourceUrl}/frontRes/activity/phone/css/rechargeSuccess.css">
</head>
<body>
<div class="main">
    <div class="top">
        <div class="recharge-success">
            <div class="success-img"></div>
            <div class="success-text">充值成功</div>
            <div class="recharge-num">${order.worth}元</div>
        </div>
        <div class="reward">
            <p>奖励您<br><fmt:formatNumber type="number"
                                        value="${order.payBackScore/100}"
                                        pattern="0.00"
                                        maxFractionDigits="2"/>元乐加红包！</p>

            <p>
                红包可在同城乐店消费抵现<br>
                <a href="/merchant/index"> 查看可用乐店 > </a>
            </p>

            <p class="btn">
                <span onclick="goOrderList()">查看充值记录</span><span
                    onclick="goPhoneIndex()">继续充值</span>
            </p>
        </div>
    </div>
    <div class="bottom">
        <p class="bottom-ttl">为你推荐</p>
        <ul id="hotContent">

        </ul>
        <div class="look-more" onclick="goProductIndex()">查看更多 ></div>
    </div>
</div>
<script src="${resourceUrl}/js/zepto.min.js"></script>
<script>
    $.ajax({
               type: "get",
               url: "/front/product/hotList?page=1",
               success: function (data) {
                   var list = data.data, mainContent = $("#hotContent"), content = '';
                   if (list != null) {
                       var length = list.length > 5 ? 5 : list.length;
                       for (var i = 0; i < length; i++) {
                           var o = list[i];
                           var currP = '<li class="list" onclick="goHotDetail(' + o.id
                                       + ')"><div class="left"><img src="' + o.picture
                                       + '" alt=""></div><div class="left"><p>' + o.name
                                       + '</p><p><span>' + toDecimal(o.minPrice / 100)
                                       + '</span>元＋<span>' + o.minScore
                                       + '</span>积分</p><p><span class="final-num"><i></i>仅剩'
                                       + o.repository
                                       + '份</span><span class="btn-buy">立即抢购</span></p></div></li>';
                           content += currP;
                       }
                       mainContent.html(content);
                   }
               }
           });
    function goProductIndex() {
        window.location.href = "/front/product/weixin/productIndex";
    }
    function goOrderList() {
        window.location.href = "/front/phone/weixin/orderList";
    }
    function goPhoneIndex() {
        window.location.href = "/front/phone/weixin/index";
    }
    function goHotDetail(id) { //go爆品详情页
        location.href = "/front/product/weixin/limitDetail?productId=" + id;
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
</script>
</body>
</html>
