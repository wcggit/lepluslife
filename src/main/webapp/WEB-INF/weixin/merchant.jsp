<%--
  Created by IntelliJ IDEA.
  User: wcg
  Date: 16/4/8
  Time: 下午4:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/WEB-INF/commen.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<script type="application/javascript">
    function goPage(page) {
        location.href = "${wxRootUrl}/weixin/" + page;
    }

</script>
<head>
    <meta charset="utf-8">
    <title></title>
    <meta name="viewport"
          content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <!--标准mui.css-->
    <link rel="stylesheet" href="${resourceUrl}/css/mui.min.css">
    <!--App自定义的css-->
    <link rel="stylesheet" type="text/css" href="${resourceUrl}/css/location.css"/>
</head>

<body>
<!--头部-->
<header class="mui-bar mui-bar-nav">
    <%--<a style="color: #D62D2B;" class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left" onclick="history.go(-1)"></a>--%>

    <h1 class="mui-title" style="color: #D62D2B;font-weight: bold;">鞍山</h1>
</header>
<!--底部菜单-->
<%--<nav class="mui-bar mui-bar-tab">--%>
<%--<a class="mui-tab-item" href="personal.html" id="tradePersonl">--%>
<%--<span class="mui-icon mui-icon-contact"></span>--%>
<%--<span class="mui-tab-label">账户</span>--%>
<%--</a>--%>
<%--<a class="mui-tab-item" href="index.html" id="tradeIndex">--%>
<%--<span class="mui-icon mui-icon-home"></span>--%>
<%--<span class="mui-tab-label">商城</span>--%>
<%--</a>--%>
<%--<a class="mui-tab-item mui-active" href="location.html" id="tradeLocation">--%>
<%--<span class="mui-icon mui-icon-location"></span>--%>
<%--<span class="mui-tab-label">周边</span>--%>
<%--</a>--%>
<%--</nav>--%>
<!--下拉刷新容器-->
<div id="pullrefresh" class="mui-content mui-scroll-wrapper">
    <div class="mui-scroll">
        <ul class="mui-table-view mui-table-view-chevron detail-hid"
            style="background: #EFEFF4;"></ul>
    </div>
</div>
</body>
<script src="${resourceUrl}/js/jquery-1.11.3.min.js"></script>

<script type="text/javascript" src="${resourceUrl}/js/mui.min.js"></script>
<script>
    //下拉刷新
    mui.init({
                 pullRefresh: {
                     container: '#pullrefresh',
                     up: {
                         contentrefresh: '正在加载...',
                         callback: pullupRefresh
                     }
                 }
             });

    //count判断是第几次加载
    var count = 1, imgLength=10, shangshu, yushu, url = '${wxRootUrl}/merchant/list';

    /**
     * 上拉加载具体业务实现
     */
    function pullupRefresh() {
        setTimeout(function () {
            mui('#pullrefresh').pullRefresh().endPullupToRefresh((imgLength < 10)); //参数为true代表没有更多数据了。
            var table = document.body.querySelector('.mui-table-view');
            var cells = document.body.querySelectorAll('.detailImg');
            table.className = "mui-table-view mui-table-view-chevron detail-show";

            mui.ajax(url + '?page=' + count, {
                dataType: 'json',//服务器返回json格式数据
                type: 'get',//HTTP请求类型
                timeout: 10000,//超时时间设置为10秒；
                success: function (data) {
                    imgLength=data.length;
                    for (var i = 0; i < data.length; i++) {
                        var li = document.createElement('li');
                        var liHtmlStr = '<div class="list"><div class="list-top"><div class="top-left">';
                        liHtmlStr +=
                        '<img src="' + data[i].picture + '" /><span class="product-list"><font>'
                        + data[i].discount + '</font>折</span>';
                        liHtmlStr += '</div><div class="top-right"><p class="right-ttl">';
                        liHtmlStr += '<span class="trade-font-black">' + data[i].name + '</span>';
                        liHtmlStr +=
                        '</p><p class="right-pra  mui-ellipsis"><span class="mui-icon mui-icon-location"></span>';
                        liHtmlStr +=
                        data[i].location
                        + '</p><hr /><p class="right-pra"><span class="right-return">返</span>';
                        liHtmlStr +=
                        '<span>消费额的' + data[i].rebate + '%将返到您的乐+账户</span></p></div></div></div>';
                        li.innerHTML = liHtmlStr;
                        table.appendChild(li);
                    }
                    count++;
                },
                error: function (xhr, type, errorThrown) {
                    //异常处理；
                    console.log(type);
                }
            });

        }, 0);
    }
    if (mui.os.plus) {
        mui.plusReady(function () {
            setTimeout(function () {
                mui('#pullrefresh').pullRefresh().pullupLoading();
            }, 1000);

        });
    } else {
        mui.ready(function () {
            mui('#pullrefresh').pullRefresh().pullupLoading();
        });
    }
</script>
</html>
