<%--
  Created by IntelliJ IDEA.
  User: wcg
  Date: 16/3/18
  Time: 下午9:01
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/WEB-INF/commen.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>

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
    <link rel="stylesheet" type="text/css" href="${resourceUrl}/css/tradeDetail.css"/>
    <script src="${resourceUrl}/js/jquery-1.11.3.min.js"></script>
</head>

<body>
<!--头部-->
<%--<header class="mui-bar mui-bar-nav" style="background: #fff;">--%>
    <%--<a style="color: #D62D2B;" class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>--%>

    <%--<h1 class="mui-title" style="color: #D62D2B;font-weight: bold;">明细记录</h1>--%>
    <%--<a style="color: #D62D2B;" class="mui-action-back mui-icon mui-icon-more mui-pull-right"></a>--%>
<%--</header>--%>
<!--底部菜单-->
<%--<nav class="mui-bar mui-bar-tab">--%>
    <%--<a class="mui-tab-item mui-active" id="tradePersonl">--%>
        <%--<span class="mui-icon mui-icon-contact"></span>--%>
        <%--<span class="mui-tab-label">账户</span>--%>
    <%--</a>--%>
    <%--<a class="mui-tab-item" id="tradeIndex">--%>
        <%--<span class="mui-icon mui-icon-home"></span>--%>
        <%--<span class="mui-tab-label">商城</span>--%>
    <%--</a>--%>
    <%--<a class="mui-tab-item" id="tradeLocation">--%>
        <%--<span class="mui-icon mui-icon-location"></span>--%>
        <%--<span class="mui-tab-label">周边</span>--%>
    <%--</a>--%>
<%--</nav>--%>
<!--内容部分-->
<%--<div class="mui-content" >--%>
    <%--<nav class="mui-bar-tab" style="background: #fff;border-bottom: solid 1px #CBCACA;">--%>
        <%--<a class="mui-tab-item mui-active" href="#tabbar-with-detail1"--%>
           <%--style="border-right:solid 1px #CBCACA ;">--%>
            <%--<span class="mui-tab-label focusCol">我的订单</span>--%>
        <%--</a>--%>
        <%--<a class="mui-tab-item" href="#tabbar-with-detail2">--%>
            <%--<span class="mui-tab-label initCol">明细记录</span>--%>
        <%--</a>--%>
    <%--</nav>--%>

    <!--我的订单-->
    <%--<div id="tabbar-with-detail1" class="mui-control-content mui-active" style="padding-top: 0vw;">--%>
        <%--<c:forEach items="${orders}" var="order">--%>
            <%--<c:if test="${order.state==0}">--%>
                <%--<div class="list">--%>
                    <%--<p class="mui-table list-ttl trade-font-black">--%>
                        <%--<span class="logo"></span>--%>
                        <%--<span>乐+商城</span>--%>
                        <%--<span class="mui-pull-right state">待付款</span>--%>
                    <%--</p>--%>
                    <%--<c:forEach items="${order.orderDetails}" var="orderDetail">--%>
                        <%--<div class="list-top">--%>
                            <%--<div class="top-left">--%>
                                <%--<span style="background: url(${orderDetail.product.thumb}) no-repeat;background-size: 100%;"></span>--%>
                            <%--</div>--%>
                            <%--<div class="top-right">--%>
                                <%--<p class="right-ttl">--%>
                                    <%--<span class="trade-font-black mui-ellipsis">${orderDetail.product.name}</span>--%>
                                <%--</p>--%>

                                <%--<p class="right-pra">--%>
                                    <%--规格：<font>${orderDetail.productSpec.specDetail}</font></p>--%>

                                <%--<p class="right-pra">数量：<font>${orderDetail.productNumber}</font>件--%>
                                <%--</p>--%>
                            <%--</div>--%>
                        <%--</div>--%>
                    <%--</c:forEach>--%>
                    <%--<p class="trade-font-black"><span--%>
                            <%--class="list-ttl">合计：￥<font>${order.totalPrice/100}</font></span>--%>
                    <%--</p>--%>

                    <%--<p class="mui-text-right">--%>
                        <%--<button type="button" class="mui-btn mui-btn-outlined"--%>
                                <%--onclick="cancleOrder(${order.id})">取消订单--%>
                        <%--</button>--%>
                        <%--<button type="button" class="mui-btn mui-btn-danger mui-btn-outlined"--%>
                                <%--onclick="goOrderPayPage(${order.id})">立即支付--%>
                        <%--</button>--%>
                    <%--</p>--%>
                <%--</div>--%>
            <%--</c:if>--%>
            <%--<c:if test="${order.state==1}">--%>
                <%--<div class="list">--%>
                    <%--<p class="mui-table list-ttl trade-font-black">--%>
                        <%--<span class="logo"></span>--%>
                        <%--<span>乐+商城</span>--%>
                        <%--<span class="mui-pull-right state">待发货</span>--%>
                    <%--</p>--%>
                    <%--<c:forEach items="${order.orderDetails}" var="orderDetail">--%>
                        <%--<div class="list-top">--%>
                            <%--<div class="top-left">--%>
                                <%--<span style="background: url(${orderDetail.product.thumb}) no-repeat;background-size: 100%;"></span>--%>
                            <%--</div>--%>
                            <%--<div class="top-right">--%>
                                <%--<p class="right-ttl">--%>
                                    <%--<span class="trade-font-black mui-ellipsis">${orderDetail.product.name}</span>--%>
                                <%--</p>--%>

                                <%--<p class="right-pra">--%>
                                    <%--规格：<font>${orderDetail.productSpec.specDetail}</font></p>--%>

                                <%--<p class="right-pra">数量：<font>${orderDetail.productNumber}</font>件--%>
                                <%--</p>--%>
                            <%--</div>--%>
                        <%--</div>--%>
                    <%--</c:forEach>--%>
                    <%--<p class="trade-font-black"><span--%>
                            <%--class="list-ttl">合计：￥<font>${order.truePrice}</font>+<font>${order.trueScore}</font>积分</span>--%>
                    <%--</p>--%>

                    <%--<p class="mui-text-right">--%>
                    <%--</p>--%>
                <%--</div>--%>
            <%--</c:if>--%>
            <%--<c:if test="${order.state==2}">--%>
                <%--<div class="list">--%>
                    <%--<p class="mui-table list-ttl trade-font-black">--%>
                        <%--<span class="logo"></span>--%>
                        <%--<span>乐+商城</span>--%>
                        <%--<span class="mui-pull-right state">已发货</span>--%>
                    <%--</p>--%>
                    <%--<c:forEach items="${order.orderDetails}" var="orderDetail">--%>
                        <%--<div class="list-top">--%>
                            <%--<div class="top-left">--%>
                                <%--<span style="background: url(${orderDetail.product.thumb}) no-repeat;background-size: 100%;"></span>--%>
                            <%--</div>--%>
                            <%--<div class="top-right">--%>
                                <%--<p class="right-ttl">--%>
                                    <%--<span class="trade-font-black mui-ellipsis">${orderDetail.product.name}</span>--%>
                                <%--</p>--%>

                                <%--<p class="right-pra">--%>
                                    <%--规格：<font>${orderDetail.productSpec.specDetail}</font></p>--%>

                                <%--<p class="right-pra">数量：<font>${orderDetail.productNumber}</font>件--%>
                                <%--</p>--%>
                            <%--</div>--%>
                        <%--</div>--%>
                    <%--</c:forEach>--%>
                    <%--<p class="trade-font-black"><span--%>
                            <%--class="list-ttl">合计：￥<font>${order.truePrice}</font>+<font>${order.trueScore}</font>积分</span>--%>
                    <%--</p>--%>

                    <%--<p class="mui-text-right">--%>
                        <%--<button type="button" class="mui-btn mui-btn-outlined"--%>
                                <%--onclick="confirmOrder(${order.id})">确认收货--%>
                        <%--</button>--%>
                    <%--</p>--%>
                <%--</div>--%>
            <%--</c:if>--%>
            <%--<c:if test="${order.state==3}">--%>
                <%--<div class="list">--%>
                    <%--<p class="mui-table list-ttl trade-font-black">--%>
                        <%--<span class="logo"></span>--%>
                        <%--<span>乐+商城</span>--%>
                        <%--<span class="mui-pull-right state">已完成</span>--%>
                    <%--</p>--%>
                    <%--<c:forEach items="${order.orderDetails}" var="orderDetail">--%>
                        <%--<div class="list-top">--%>
                            <%--<div class="top-left">--%>
                                <%--<span style="background: url(${orderDetail.product.thumb}) no-repeat;background-size: 100%;"></span>--%>
                            <%--</div>--%>
                            <%--<div class="top-right">--%>
                                <%--<p class="right-ttl">--%>
                                    <%--<span class="trade-font-black mui-ellipsis">${orderDetail.product.name}</span>--%>
                                <%--</p>--%>

                                <%--<p class="right-pra">--%>
                                    <%--规格：<font>${orderDetail.productSpec.specDetail}</font></p>--%>

                                <%--<p class="right-pra">数量：<font>${orderDetail.productNumber}</font>件--%>
                                <%--</p>--%>
                            <%--</div>--%>
                        <%--</div>--%>
                    <%--</c:forEach>--%>
                    <%--<p class="trade-font-black"><span--%>
                            <%--class="list-ttl">合计：￥<font>${order.truePrice}</font>+<font>${order.trueScore}</font>积分</span>--%>
                    <%--</p>--%>
                <%--</div>--%>
            <%--</c:if>--%>
        <%--</c:forEach>--%>
    <%--</div>--%>
    <!--明细记录-->
    <div id="tabbar-with-detail2" class="mui-control-content mui-active" style="padding-top: 0vw;" >
        <c:forEach items="${scoreADetails}" var="scoreADetail">
            <c:if test="${scoreADetail.number>0}">
                <div class="list">
                    <div class="list-top">
                        <div class="top-left">
                            <span class="hongbaoadd"></span>
                        </div>
                        <div class="top-right">
                            <p class="right-ttl">
                                <span class="trade-font-red">+<font>${scoreADetail.number/100}</font>红包</span>
                                <span class="trade-font-red">${scoreADetail.operate}</span>
                            </p>

                            <p class="right-pra"><font><fmt:formatDate
                                    value="${scoreADetail.dateCreated}" type="both"/></font></p>
                        </div>
                    </div>
                </div>
            </c:if>
            <c:if test="${scoreADetail.number<0}">
                <div class="list">
                    <div class="list-top">
                        <div class="top-left">
                            <span class="hongbaocut"></span>
                        </div>
                        <div class="top-right">
                            <p class="right-ttl">
                                <span class="trade-font-black"><font>${scoreADetail.number/100}</font>红包</span>
                                <span class="trade-font-black">${scoreADetail.operate}</span>
                            </p>

                            <p class="right-pra"><font><fmt:formatDate
                                    value="${scoreADetail.dateCreated}" type="both"/></font></p>
                        </div>
                    </div>
                </div>
            </c:if>
        </c:forEach>
        <c:forEach items="${scoreBDetails}" var="scoreBDetail">
            <c:if test="${scoreBDetail.number>0}">
                <div class="list">
                    <div class="list-top">
                        <div class="top-left">
                            <span class="jifenadd"></span>
                        </div>
                        <div class="top-right">
                            <p class="right-ttl">
                                <span class="trade-font-red">+<font>${scoreBDetail.number}</font>积分</span>
                                <span class="trade-font-red">${scoreBDetail.operate}</span>
                            </p>

                            <p class="right-pra"><font><fmt:formatDate
                                    value="${scoreBDetail.dateCreated}" type="both"/></font></p>
                        </div>
                    </div>
                </div>
            </c:if>
            <c:if test="${scoreBDetail.number<0}">
                <div class="list">
                    <div class="list-top">
                        <div class="top-left">
                            <span class="jifencut"></span>
                        </div>
                        <div class="top-right">
                            <p class="right-ttl">
                                <span class="trade-font-black"><font>${scoreBDetail.number}</font>积分</span>
                                <span class="trade-font-black">${scoreBDetail.operate}</span>
                            </p>

                            <p class="right-pra"><font><fmt:formatDate
                                    value="${scoreBDetail.dateCreated}" type="both"/></font></p>
                        </div>
                    </div>
                </div>
            </c:if>
        </c:forEach>
    </div>
</div>

</body>
<script type="text/javascript" src="${resourceUrl}/js/mui.min.js"></script>

<script type="text/javascript">
    document.title = "明细记录";

    var spans = $('.mui-content .mui-bar-tab .mui-tab-item .mui-tab-label');
    click(spans);
    //点击事件
    function click(s) {
        s.each(function (i) {
            s.eq(i).bind('tap', function () {
                clear(s);
                $(this).removeClass('initCol').addClass('focusCol');
            })
        });
    }
    //清除
    function clear(s) {
        s.each(function (i) {
            s.eq(i).removeClass('focusCol').addClass('initCol');
        })
    }
    //	    解决加载mui.min.js后无法实现跳转的bug
    $('#tradePersonl').bind('touchstart', function () {
        window.location.href = '${wxRootUrl}/weixin/user';
    })
    $('#tradeIndex').bind('touchstart', function () {
        window.location.href = '${wxRootUrl}/weixin/shop';
    })
    $('#tradeLocation').bind('touchstart', function () {
        window.location.href = '${wxRootUrl}/weixin/city';
    })

    function cancleOrder(orderId) {

        $.post("/weixin/order/orderCancle", {"orderId": orderId}, function (result) {
            if (result.status != 200) {
                alert("取消订单失败");
            }
            location.href = "${wxRootUrl}/weixin/orderDetail";
        });
    }
    function goOrderPayPage(orderId) {
        location.href = "${wxRootUrl}/weixin/order/" + orderId;
    }

    function confirmOrder(id) {
        $.post("/weixin/order/orderConfirm", {"orderId": id}, function (result) {
            if (result.status != 200) {
                alert("确认失败");
            }
            location.href = "${wxRootUrl}/weixin/orderDetail";
        });
    }
</script>

</html>
