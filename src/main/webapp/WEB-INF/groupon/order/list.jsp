<%--
  Created by IntelliJ IDEA.
  User: root
  Date: 2017/6/23
  Time: 17:13
  团购列表
--%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/commen.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no"/>
    <meta name="format-detection" content="telephone=no">
    <meta name="format-detection" content="telephone=yes"/>
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="black"/>
    <title>我的团购</title>
    <link rel="stylesheet" type="text/css" href="${commonResource}/css/reset.css">
    <link rel="stylesheet" type="text/css" href="${commonResource}/css/main.css">
    <link rel="stylesheet" href="${resource}/groupon/order_list/css/myGroup.css">
</head>
<body>
<header>
    <div class="active">全部</div>
    <div>待使用</div>
    <div>已使用</div>
    <div>退款</div>
</header>
<section class="tab_all appendHtml" id="type9"></section>
<section class="tab_dsy appendHtml" id="type0"></section>
<section class="tab_ysy appendHtml" id="type1"></section>
<section class="tab_tk appendHtml" id="type2"></section>
</body>
<script src="${commonResource}/js/jquery.min.js"></script>
<script>
    /**强制保留两位小数*/
    function toDecimal(x) {
        var f = parseFloat(x);
        if (isNaN(f)) {
            return false;
        }
        f = Math.round(x * 100) / 100;
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
    /**格式化日期*/
    Date.prototype.Format = function (fmt) { //author: meizz
        var o = {
            "M+": this.getMonth() + 1, //月份
            "d+": this.getDate(), //日
            "h+": this.getHours(), //小时
            "m+": this.getMinutes(), //分
            "s+": this.getSeconds(), //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds() //毫秒
        };
        if (/(y+)/.test(fmt)) fmt =
            fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt)) fmt =
                fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(
                    ("" + o[k]).length)));
        return fmt;
    }
</script>
<script>
    var orderState = 9, currPage = 1, pageSize = 10;
    var logoUrl = '${resource}/groupon/order_list/img/le_logo.png';
    function orderList() {
        if (pageSize == 10) {
            $.post('/groupon/sign/orderList', {
                currPage: currPage,
                orderState: orderState,
                source: 'WEB'
            }, function (res) {
                var list = res.data;
                if (list != null && list.length > 0) {
                    pageSize = list.length;
                    var $type = $('#' + 'type' + orderState);
                    var content = '';
                    var currType = '<div class="dsy">待使用</div>';
                    for (var i = 0; i < list.length; i++) {
                        if (list[i].orderState === 0) {
                            currType = '<div class="dsy">待使用</div>';
                        } else if (list[i].orderState === 1) {
                            currType = '<div class="ysy">已使用</div>';
                        } else {
                            currType = '<div class="ytk">退款</div>';
                        }
                        content +=
                            '<div class="code"><div class="codeTitle fixClear"><div><img src="'
                            + logoUrl
                            + '" alt=""></div><div>乐＋生活团购券</div>' + currType
                            + '</div><div class="codeMain fixClear" onclick="window.location.href=\'/groupon//weixin/orderDetail?orderId='
                            + list[i].orderId + '\'"><div><img src="'
                            + list[i].picture
                            + '" alt=""></div><div><p>' + list[i].name
                            + '</p><p>¥' + toDecimal(list[i].truePay / 100)
                            + '+' + toDecimal(list[i].scorea / 100)
                            + '鼓励金</p><p>' + new Date(list[i].expiredDate).Format("yyyy-MM-dd")
                            + '到期</p></div><div>× ' + list[i].buyNum + '</div></div></div>';
                    }
                    $type.html($type.html() + content);
                } else {
                    pageSize = 0;
                }
            });
        }
    }
    orderList();
</script>
<script>


    $("header > div").click(function () {
        pageSize = 10;
        currPage = 1;
        var thisName = $(this).html();
        $("header > div").removeClass("active");
        $(this).addClass("active");
        $(".appendHtml").html('');
        switch (thisName) {
            case '全部':
                orderState = 9;
                $(".tab_all").show();
                $(".tab_dsy").hide();
                $(".tab_ysy").hide();
                $(".tab_tk").hide();
                break;
            case '待使用':
                orderState = 0;
                $(".tab_all").hide();
                $(".tab_dsy").show();
                $(".tab_ysy").hide();
                $(".tab_tk").hide();
                break;
            case '已使用':
                orderState = 1;
                $(".tab_all").hide();
                $(".tab_dsy").hide();
                $(".tab_ysy").show();
                $(".tab_tk").hide();
                break;
            case '退款':
                orderState = 2;
                $(".tab_all").hide();
                $(".tab_dsy").hide();
                $(".tab_ysy").hide();
                $(".tab_tk").show();
                break;
            default:
                break;
        }
        orderList();
    });

    $(window).scroll(function () {
        var scrollTop = $(this).scrollTop();
        var scrollHeight = $(document).height();
        var windowHeight = $(this).height();
        if (scrollTop + windowHeight >= scrollHeight) {
            if (pageSize >= 10) {
                console.log("this is in" + currPage);
                currPage++;
                //请求数据
                orderList();
            }
        }
    });
</script>
</html>
