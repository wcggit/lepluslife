<%--
  Created by IntelliJ IDEA.
  User: zhangwen
  Date: 2016/9/23
  Time: 09:20
  Content:订单列表
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="viewport" id="viewport" content="width=device-width, initial-scale=1">
    <meta name="format-detection" content="telephone=no">
    <meta name="format-detection" content="telephone=yes"/>
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="black"/>
    <title>我的订单</title>
    <c:set var="resourceUrl" value="http://www.lepluslife.com/resource"></c:set>
    <c:set var="wxRootUrl" value="http://www.lepluslife.com"></c:set>
    <link rel="stylesheet" href="${resourceUrl}/frontRes/css/reset.css">
    <link rel="stylesheet" href="${resourceUrl}/frontRes/order/orderList/css/orderlist.css">
</head>
<body>
<section class="tab">
    <div class="active">全部</div>
    <div>待付款</div>
    <div>待发货</div>
    <div>待收货</div>
    <div>已完成</div>
</section>
<section class="blank"></section>
<section class="orderList allOrder"></section>
<section class="orderList notPayment"></section>
<section class="orderList notDelivery"></section>
<section class="orderList notGet"></section>
<section class="orderList completed"></section>

<%--取消订单弹窗--%>
<section class="shade-layer" style="display: none">
    <p>确认取消该订单？</p>

    <div>
        <div class="layerClose">取消</div>
        <div class="yes">确定</div>
    </div>
</section>
<section class="blank"></section>
<section class="footer">
    <div onclick="goMenu(1)">
        <div>
            <img src="${resourceUrl}/frontRes/product/hotIndex/img/zhenpin1.png" alt="">
        </div>
        <p>臻品</p>
    </div>
    <div onclick="goMenu(2)">
        <div>
            <img src="${resourceUrl}/frontRes/product/hotIndex/img/miaosha1.png" alt="">
        </div>
        <p>秒杀</p>
    </div>
    <div onclick="goMenu(3)">
        <div>
            <img src="${resourceUrl}/frontRes/product/hotIndex/img/gouwuche1.png" alt="">
        </div>
        <p>购物车</p>
    </div>
    <div>
        <div>
            <img src="${resourceUrl}/frontRes/product/hotIndex/img/dingdan2.png" alt="">
        </div>
        <p>订单</p>
    </div>
</section>
</body>
<script src="${resourceUrl}/js/jquery-2.0.3.min.js"></script>
<script src="${resourceUrl}/frontRes/js/layer.js"></script>
<script>
    function goMenu(curIndex) { //go其他菜单页
        if (curIndex == 1) {
            location.href = "/front/product/weixin/productIndex";
        } else if (curIndex == 2) {
            location.href = "/front/product/weixin/hotIndex";
        } else if (curIndex == 3) {
            location.href = "/weixin/cart";
        }
    }
</script>
<!--style for html-->
<script>
    $(".only").css("margin-top", -$(".only").height() - 25 + "px");
    $(".only-small").css("margin-top", -$(".only-small").height() - 12 + "px");
    $(".secondsKill").css("margin-bottom", $(".footer").height() + 13 + "px");
    $(".blank").css("height", $(".tab").height() + "px");
    $(".orderList > div:last-child").css("margin-bottom", $(".footer").height() + 10 + "px");
    var currPageList = [1, 1, 1, 1, 0, 1], hasAjaxList = [1, 0, 0, 0, 0];
    var orderHasProCount = 0, orderState = 5, orderStateClassName = "allOrder", orderCancleId = 0;
    function pushIn(orderState, stateClassName) {
        $.ajax({
                   type: "post",
                   url: "/front/order/weixin/orderList",
                   data: {state: orderState, currPage: currPageList[orderState]},
                   success: function (data) {
                       var orders = data.data, contentHtml = '';
                       if (orders != '' && orders != null) {
                           for (var i = 0; i < orders.length; i++) {
                               var divHtml = '';
                               if (orders[i].state == 0) {//待支付
                                   divHtml =
                                   orderToList(divHtml, orders[i], "待付款", orders[i].totalPrice,
                                               orders[i].totalScore);
                                   divHtml +=
                                   '<div class="button"><button class="cancelOrder" onclick="orderCancle('
                                   + orders[i].id
                                   + ',event)">取消订单</button><button onclick="goPayPage('
                                   + orders[i].id + ',event)">去付款</button></div></div>';
                                   contentHtml += divHtml;
                               } else if (orders[i].state == 1) { //待发货
                                   divHtml =
                                   orderToList(divHtml, orders[i], "待发货", orders[i].truePrice,
                                               orders[i].totalScore);
                                   divHtml += '</div>';
                                   contentHtml += divHtml;
                               } else if (orders[i].state == 2) { //待收货
                                   divHtml =
                                   orderToList(divHtml, orders[i], "待收货", orders[i].truePrice,
                                               orders[i].totalScore);
                                   divHtml +=
                                   '<div class="button"><button class="logistics" onclick="goDeliveryPage('
                                   + orders[i].id
                                   + ',event)">查看物流</button><button onclick="orderConfirm('
                                   + orders[i].id + ',event)">确认收货</button></div></div>';
                                   contentHtml += divHtml;
                               } else if (orders[i].state == 3) {
                                   divHtml =
                                   orderToList(divHtml, orders[i], "已完成", orders[i].truePrice,
                                               orders[i].totalScore);
                                   divHtml += '</div>';
                                   contentHtml += divHtml;
                               }
                               orderHasProCount = 0;
                           }
                           $("." + stateClassName).html($("." + stateClassName).html()
                                                        + contentHtml);
                           $("." + stateClassName).show();
                       } else {
                           empty(stateClassName);
                           $("." + stateClassName).show();
                       }
                       $(".dImg").css("height",$(".dImg").width() + "px");
                   }
               });
    }
    function orderToList(divStr, data, stateInput, totalPrice, totalScore) {
        divStr += '<div onclick="showOrderInfo(' + data.id + ')"><div><div>订单编号：' + data.orderSid
                  + '</div><div>' + stateInput + '</div></div>';
        divStr = productSplice(divStr, data);
        divStr +=
        '<div class="all"><div>共 <span style="margin-right: 3px;">' + orderHasProCount
        + '</span>件商品</div><div>合计：<span style="font-size: 15px;color: #333;">￥'
        + toDecimal(totalPrice / 100)
        + '</span> + <span style="font-size: 14px;color: #fb991a;">'
        + totalScore
        + '积分</span><spanstyle="font-size: 13px;color: #666;">';
        if (data.transmitWay == 1) {
            divStr += '（线下自提）</span></div></div>';
        } else if (data.freightPrice == 0) {
            divStr += '（包邮）</span></div></div>';
        } else {
            divStr += '（运费￥' + toDecimal(data.freightPrice / 100) + '）</span></div></div>';
        }
        return divStr;
    }
    function productSplice(divStr, data) {
        divStr += '<div class="information">';
        var orderDetails = data.orderDetails;
        for (var i = 0; i < orderDetails.length; i++) {
            orderHasProCount += orderDetails[i].productNumber;
            divStr +=
            '<div><div class="dImg"><img src="' + (orderDetails[i].product.type == 1
                    ? orderDetails[i].productSpec.picture : orderDetails[i].product.picture)
            + '" alt=""></div><div class="guige"><div><div>'
            + orderDetails[i].product.name
            + '</div><div>×'
            + orderDetails[i].productNumber
            + '</div></div><p>'
            + orderDetails[i].productSpec.specDetail
            + '</p><p>￥'
            + toDecimal(orderDetails[i].productSpec.minPrice
                        / 100) + '+<span>'
            + (orderDetails[i].productSpec.minScore == null ? 0
                    : orderDetails[i].productSpec.minScore) + '积分</span></p></div></div>';
        }
        divStr += '</div>';
        return divStr;
    }
    function showOrderInfo(id) {
        if (id != null) {
            location.href = "/order/showOrderInfo/" + id;
        }
    }
    function orderCancle(id, e) {
        var ev = e || window.event;
        if (ev.stopPropagation) {
            ev.stopPropagation();
        }
        else if (window.event) {
            window.event.cancelBubble = true;//兼容IE
        }
        orderCancleId = id;
        showTanChuang();
    }
    function goPayPage(id, e) {
        var ev = e || window.event;
        if (ev.stopPropagation) {
            ev.stopPropagation();
        }
        else if (window.event) {
            window.event.cancelBubble = true;//兼容IE
        }
        location.href = '/front/order/weixin/confirmOrder/' + id;
    }
    function orderConfirm(id, e) {
        var ev = e || window.event;
        if (ev.stopPropagation) {
            ev.stopPropagation();
        }
        else if (window.event) {
            window.event.cancelBubble = true;//兼容IE
        }
        $.ajax({
                   type: "post",
                   url: "/weixin/order/orderConfirm",
                   data: {orderId: id},
                   success: function (data) {
                       location.reload(true);
                   }
               });
    }
    function goDeliveryPage(id, e) {//查看物流信息
        var ev = e || window.event;
        if (ev.stopPropagation) {
            ev.stopPropagation();
        }
        else if (window.event) {
            window.event.cancelBubble = true;//兼容IE
        }
        if (id != null) {
            location.href = "/order/showExpress/" + id;
        }
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
    pushIn(5, orderStateClassName);
    $(window).scroll(function () {
        var scrollTop = $(this).scrollTop();
        var scrollHeight = $(document).height();
        var windowHeight = $(this).height();
        if (scrollTop + windowHeight > scrollHeight) {
            console.log("this is to next page");
            //请求数据
            currPageList[orderState]++;
            pushIn(orderState, orderStateClassName);
        }
    });
</script>
<script>
    $(".allOrder").show();
    $(".tab > div").click(function (e) {
        $(".tab > div").removeClass("active");
        $(this).addClass("active");
        $(".orderList").html('');
        var val = $(this).html();
        switch (val) {
            case "全部" :
                orderState = 5;
                orderStateClassName = "allOrder";
                currPageList[5] = 1;
                pushIn(orderState, orderStateClassName);
                break;
            case "待付款" :
                orderState = 0;
                orderStateClassName = "notPayment";
                currPageList[0] = 1;
                pushIn(orderState, orderStateClassName);
                break;
            case "待发货" :
                orderState = 1;
                orderStateClassName = "notDelivery";
                currPageList[1] = 1;
                pushIn(orderState, orderStateClassName);
                break;
            case "待收货" :
                orderState = 2;
                orderStateClassName = "notGet";
                currPageList[2] = 1;
                pushIn(orderState, orderStateClassName);
                break;
            case "已完成" :
                orderState = 3;
                orderStateClassName = "completed";
                currPageList[3] = 1;
                pushIn(orderState, orderStateClassName);
                break;
        }
    });
    function empty(id) {
        $("." + id).append(
                $("<div></div>").attr("style",
                                      "width:23%;background-color: transparent;text-align: center;margin:0 auto;margin-top:33%;margin-bottom: 30px;").append(
                        $("<img>").attr("style", "width:100%;").attr("src",
                                                                     "${resourceUrl}/frontRes/product/hotIndex/img/empty.png")
                )
        ).append(
                $("<div></div>").attr("style",
                                      "background-color: transparent;text-align: center;color: #b2b2b2;font-size: 16px;").html("目前还没有订单呢！")
        )
    }
</script>
<!--弹窗-->
<script>
    function showTanChuang() {
        $(".shade-layer").show();
        layer.open({
                       type: 1,
                       area: ['78%', ''], //宽高
                       content: $(".shade-layer"),
                       title: false,
                       closeBtn: 0,
                       scrollbar: false
                   });
    }
    $(".layerClose").click(function (e) {
        layer.closeAll();
        $(".shade-layer").hide();
    });
    $(".yes").click(function (e) { //取消订单
        $.ajax({
                   type: "post",
                   url: "/weixin/order/orderCancle",
                   data: {orderId: orderCancleId},
                   success: function (data) {
                       location.reload(true);
                   }
               });
    });
</script>
</html>
