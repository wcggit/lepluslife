<%--
  Created by IntelliJ IDEA.
  User: wcg
  Date: 16/3/18
  Time: 下午9:01
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/commen.jsp" %>
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
    <%--<link rel="stylesheet" type="text/css" href="${resourceUrl}/css/orderInfo.css"/>--%>
    <link rel="stylesheet" type="text/css" href="${resourceUrl}/css/orderDetail.css"/>
    <script src="${resourceUrl}/js/jquery-2.0.3.min.js"></script>
</head>

<body>
<!--底部菜单-->
<%@include file="/WEB-INF/weixin/common/shopfoot.jsp" %>
<!--内容部分-->
<div class="mui-content">
    <nav class="mui-bar-tab"
         style="background: #fff;border-bottom: solid 1px #CBCACA;margin-bottom: 10px">
        <a class="mui-tab-item mui-active" href="#tabbar-with-detail1">
            <span class="mui-tab-label focusCol">全部</span>
        </a>
        <a class="mui-tab-item" href="#tabbar-with-detail2">
            <span class="mui-tab-label initCol">待付款</span>
        </a>
        <a class="mui-tab-item" href="#tabbar-with-detail3">
            <span class="mui-tab-label initCol">待发货</span>
        </a>
        <a class="mui-tab-item" href="#tabbar-with-detail4">
            <span class="mui-tab-label initCol">已发货</span>
        </a>
        <a class="mui-tab-item" href="#tabbar-with-detail5">
            <span class="mui-tab-label initCol">已完成</span>
        </a>
    </nav>

    <!--我的订单-->
    <div id="tabbar-with-detail1" class="mui-control-content mui-active">
        <div class="empty" style="display: none">
            <div class="empty-icon"></div>
            <p class="empty-ttl">暂无相关订单！</p>
        </div>
    </div>
    <div id="tabbar-with-detail2" class="mui-control-content">
        <div class="empty" style="display: none">
            <div class="empty-icon" ></div>
            <p class="empty-ttl">暂无相关订单！</p>
        </div>
    </div>
    <div id="tabbar-with-detail3" class="mui-control-content">
        <div class="empty" style="display: none">
            <div class="empty-icon" ></div>
            <p class="empty-ttl">暂无相关订单！</p>
        </div>
    </div>
    <div id="tabbar-with-detail4" class="mui-control-content">
        <div class="empty" style="display: none">
            <div class="empty-icon"></div>
            <p class="empty-ttl">暂无相关订单！</p>
        </div>
    </div>
    <div id="tabbar-with-detail5" class="mui-control-content">
        <div class="empty" style="display: none">
            <div class="empty-icon"></div>
            <p class="empty-ttl">暂无相关订单！</p>
        </div>
    </div>
</div>
</body>
<script type="text/javascript" src="${resourceUrl}/js/mui.min.js"></script>

<script type="text/javascript" charset="utf-8">
    document.title="订单";
    var div1 = document.body.querySelector('#tabbar-with-detail2');
    var div2 = document.body.querySelector('#tabbar-with-detail3');
    var div3 = document.body.querySelector('#tabbar-with-detail4');
    var div4 = document.body.querySelector('#tabbar-with-detail5');
    var spans = $('.mui-content .mui-bar-tab .mui-tab-item .mui-tab-label');
    var flag = false;
    var flag1 = false;
    var flag2 = false;
    var flag3 = false;
    var flag4 = false;
    //click(spans);
    //点击事件
    $('.mui-content .mui-bar-tab .mui-tab-item').each(function (i) {
        $('.mui-content .mui-bar-tab .mui-tab-item').eq(i).bind('tap', function () {
            var index = $('.mui-content .mui-bar-tab .mui-tab-item').index(this);
            $("#tabbar-with-detail1").css({'display': 'block'});
            $("#tabbar-with-detail2").find(".empty").css({'display': 'none'});
            $("#tabbar-with-detail3").find(".empty").css({'display': 'none'});
            $("#tabbar-with-detail4").find(".empty").css({'display': 'none'});
            $("#tabbar-with-detail5").find(".empty").css({'display': 'none'});
            if (index == 0) {
                if (!flag) {
                    $("#tabbar-with-detail1").css({'display': 'block'});
                    $("#tabbar-with-detail2").css({'display': 'none'});
                    $("#tabbar-with-detail3").css({'display': 'none'});
                    $("#tabbar-with-detail4").css({'display': 'none'});
                    $("#tabbar-with-detail5").css({'display': 'none'});
                } else {
                    $("#tabbar-with-detail1").css({'display': 'block'});
                    $("#tabbar-with-detail2").find(".empty").css({'display': 'none'});
                    $("#tabbar-with-detail3").find(".empty").css({'display': 'none'});
                    $("#tabbar-with-detail4").find(".empty").css({'display': 'none'});
                    $("#tabbar-with-detail5").find(".empty").css({'display': 'none'});
                    $("#tabbar-with-detail2").css({'display': 'block'});
                    $("#tabbar-with-detail3").css({'display': 'block'});
                    $("#tabbar-with-detail4").css({'display': 'block'});
                    $("#tabbar-with-detail5").css({'display': 'block'});
                }
            }
            if (index == 1) {
                if (flag1) {
                    $("#tabbar-with-detail1").css({'display': 'none'});
                    $("#tabbar-with-detail2").css({'display': 'block'});
                    $("#tabbar-with-detail3").css({'display': 'none'});
                    $("#tabbar-with-detail4").css({'display': 'none'});
                    $("#tabbar-with-detail5").css({'display': 'none'});
                } else {
                    $("#tabbar-with-detail1").css({'display': 'none'});
                    $("#tabbar-with-detail2").css({'display': 'block'});
                    $("#tabbar-with-detail2").find(".empty").css({'display': 'block'});
                    $("#tabbar-with-detail3").css({'display': 'none'});
                    $("#tabbar-with-detail4").css({'display': 'none'});
                    $("#tabbar-with-detail5").css({'display': 'none'});
                }
            }
            if (index == 2) {
                if (flag2) {
                    $("#tabbar-with-detail1").css({'display': 'none'});
                    $("#tabbar-with-detail2").css({'display': 'none'});
                    $("#tabbar-with-detail3").css({'display': 'block'});
                    $("#tabbar-with-detail4").css({'display': 'none'});
                    $("#tabbar-with-detail5").css({'display': 'none'});
                } else {
                    $("#tabbar-with-detail1").css({'display': 'none'});
                    $("#tabbar-with-detail3").css({'display': 'block'});
                    $("#tabbar-with-detail3").find(".empty").css('display','block');
                    $("#tabbar-with-detail2").css({'display': 'none'});
                    $("#tabbar-with-detail4").css({'display': 'none'});
                    $("#tabbar-with-detail5").css({'display': 'none'});
                }
            }
            if (index == 3) {
                if (flag3) {
                    $("#tabbar-with-detail1").css({'display': 'none'});
                    $("#tabbar-with-detail4").css({'display': 'block'});
                    $("#tabbar-with-detail2").css({'display': 'none'});
                    $("#tabbar-with-detail3").css({'display': 'none'});
                    $("#tabbar-with-detail5").css({'display': 'none'});
                } else {
                    $("#tabbar-with-detail1").css({'display': 'none'});
                    $("#tabbar-with-detail4").css({'display': 'block'});
                    $("#tabbar-with-detail4").find(".empty").css('display', 'block');
                    $("#tabbar-with-detail3").css({'display': 'none'});
                    $("#tabbar-with-detail2").css({'display': 'none'});
                    $("#tabbar-with-detail5").css({'display': 'none'});
                }
            }
            if (index == 4) {
                if (flag4) {
                    $("#tabbar-with-detail1").css({'display': 'none'});
                    $("#tabbar-with-detail5").css({'display': 'block'});
                    $("#tabbar-with-detail3").css({'display': 'none'});
                    $("#tabbar-with-detail4").css({'display': 'none'});
                    $("#tabbar-with-detail2").css({'display': 'none'});
                } else {
                    $("#tabbar-with-detail1").css({'display': 'none'});
                    $("#tabbar-with-detail5").css({'display': 'block'});
                    $("#tabbar-with-detail5").find(".empty").css({'display': 'block'});
                    $("#tabbar-with-detail3").css({'display': 'none'});
                    $("#tabbar-with-detail4").css({'display': 'none'});
                    $("#tabbar-with-detail2").css({'display': 'none'});
                }
            }
            clear($('.mui-content .mui-bar-tab .mui-tab-item'));
            $(this).find('.mui-tab-label').removeClass('initCol').addClass('focusCol');
        })
    });
    //清除
    function clear(s) {
        s.each(function (i) {
            s.eq(i).find('.mui-tab-label').removeClass('focusCol').addClass('initCol');
        })
    }

    //    ajax
    $.ajax({
               type: "get",
               url: "/weixin/order/orderList",
               success: function (data) {
                   if (data.data != '' && data.data != null) {
                       flag = true;
                       $("#tabbar-with-detail1").css({'display': 'none'});

                       var orders = data.data;
                       for (var i = 0; i < orders.length; i++) {
                           //在全部里面放一次
                           if (orders[i].state == 0) {
                               flag1 = true;
                               var divStr = '<div class="list" onclick="showOrderInfo('+orders[i].id+')"><input type="hidden" value="'
                                            + orders[i].id + '"/>';
                               divStr +=
                               '<p class="mui-table list-ttl trade-font-black"><span class="logo mui-pull-left"></span><span>'
                               + '乐+商城' + '</span><span class="mui-pull-right state">待付款</span>';
                               divStr = productSplice(divStr, orders[i]);
                               divStr +=
                               '<p class="trade-font-black total-money"><span class="list-ttl">合计：￥<font>'
                               + orders[i].totalPrice/100 + '</font></span></p>';
                               divStr +=
                               '<p class="mui-text-right"><button type="button" class="mui-btn mui-btn-outlined" onclick="orderCancle('+orders[i].id+',event)">取消订单</button><button type="button" class="mui-btn mui-btn-danger mui-btn-outlined" onclick="goPayPage('+orders[i].id+',event)">立即支付</button></p></div>';
                               div1.innerHTML += divStr;
                           } else if (orders[i].state == 1) {
                               flag2 = true;
                               //在未发货里面放一次
                               var divStr = '<div class="list" onclick="showOrderInfo('+orders[i].id+')"><input type="hidden" value="'
                                            + orders[i].id
                                            + '" ><p class="mui-table list-ttl trade-font-black"><span class="logo mui-pull-left"></span><span>乐+商城</span><span class="mui-pull-right state">待发货</span></p>';
                               divStr = productSplice(divStr, orders[i]);
                               if(orders[i].trueScore!=null&&orders[i].trueScore!=""){
                               divStr +=
                               '<p class="trade-font-black total-money"><span class="list-ttl">合计：￥<font>'
                               + orders[i].truePrice/100 + '</font></font>积分抵扣：￥<font>'
                               + orders[i].trueScore + '</font></span></p></div>';
                               }else{
                                   divStr +=
                                   '<p class="trade-font-black total-money"><span class="list-ttl">合计：￥<font>'
                                   + orders[i].truePrice/100 + '</font></font>积分抵扣：￥<font>0</font></span></p></div>'
                               }
                               div2.innerHTML += divStr;
                           } else if (orders[i].state == 2) {
                               //在已发货里面放一次
                               flag3 = true;
                               var divStr = '<div class="list" onclick="showOrderInfo('+orders[i].id+')"><input type="hidden" value="'
                                            + orders[i].id
                                            + '" /><p class="mui-table list-ttl trade-font-black"><span class="logo mui-pull-left"></span><span>乐+商城</span><span class="mui-pull-right state">已发货</span></p>';
                               divStr = productSplice(divStr, orders[i]);
                               if(orders[i].trueScore!=null&&orders[i].trueScore!=""){
                                   divStr +=
                                   '<p class="trade-font-black total-money"><span class="list-ttl">合计：￥<font>'
                                   + orders[i].truePrice/100 + '</font></font>积分抵扣：￥<font>'
                                   + orders[i].trueScore + '</font></span></p>';
                               }else{
                                   divStr +=
                                   '<p class="trade-font-black total-money"><span class="list-ttl">合计：￥<font>'
                                   + orders[i].truePrice/100 + '</font></font>积分抵扣：￥<font>0</font></span></p>'
                               }
                               divStr +=
                               '<p class="mui-text-right"><button type="button" class="mui-btn mui-btn-outlined" onclick="goDeliveryPage('+orders[i].id+',event)">查看物流</button><button type="button" class="mui-btn mui-btn-danger mui-btn-outlined" onclick="orderConfirm('+orders[i].id+',event)">确认收货</button></p></div>';
                               div3.innerHTML += divStr;

                           } else if (orders[i].state == 3) {
                               flag4 = true;
                               //在已完成里面放一次
                               var divStr = '<div class="list" onclick="showOrderInfo('+orders[i].id+')"><input type="hidden" value="'
                                            + orders[i].id
                                            + '" /><p class="mui-table list-ttl trade-font-black"><span class="logo mui-pull-left"></span><span>乐+商城</span><span class="mui-pull-right state">已完成</span></p>';
                               divStr = productSplice(divStr, orders[i]);
                               if(orders[i].trueScore!=null&&orders[i].trueScore!=""){
                                   divStr +=
                                   '<p class="trade-font-black total-money"><span class="list-ttl">合计：￥<font>'
                                   + orders[i].truePrice/100 + '</font></font>积分抵扣：￥<font>'
                                   + orders[i].trueScore + '</font></span></p></div>';
                               }else{
                                   divStr +=
                                   '<p class="trade-font-black total-money"><span class="list-ttl">合计：￥<font>'
                                   + orders[i].truePrice/100 + '</font></font>积分抵扣：￥<font>0</font></span></p></div>'
                               }
                               div4.innerHTML += divStr;
                           }
                           $("#tabbar-with-detail2").css('display','block');
                           $("#tabbar-with-detail3").css({'display': 'block'});
                           $("#tabbar-with-detail4").css({'display': 'block'});
                           $("#tabbar-with-detail5").css({'display': 'block'});
                       }
                       ;
                       //判断是否为空,空的话让空页面显示
                   } else {
                       $("#tabbar-with-detail1").css({'display': 'block'});
                       $("#tabbar-with-detail1").find(".empty").css({'display': 'block'});
                       //console.log('数据为空！')
                   }
               }
           });

    function productSplice(divStr, data) {
        divStr += '<div class="product-list">';
        var orderDetails = data.orderDetails;
        for (var i = 0; i < orderDetails.length; i++) {
            divStr += ' <div class="list-top"><div class="top-left">';
            divStr +=
            '<span style="background: url(' + orderDetails[i].productSpec.picture
            + ') no-repeat;background-size: 100%;"></span></div>';
            divStr +=
            '<div class="top-right"><p class="right-ttl"><span class="trade-font-black mui-ellipsis">'
            + orderDetails[i].product.name + '</span></p>';
            divStr +=
            ' <p class="right-pra">规格：<font>' + orderDetails[i].productSpec.specDetail
            + '</font></p>';
            divStr +=
            '<p class="right-pra">数量：<font>' + orderDetails[i].productNumber
            + '</font>件<span class="mui-pull-right trade-font-red">￥<font>'+orderDetails[i].productSpec.price/100+'</font></span> </div>';
        }
        divStr += '</div>';
        return divStr;
    }

    function orderCancle(id,e){
        var ev = e || window.event;
        if(ev.stopPropagation){
            ev.stopPropagation();
        }
        else if(window.event) {
            window.event.cancelBubble = true;//兼容IE
        }
        $.ajax({
                   type: "post",
                   url: "/weixin/order/orderCancle",
                    data:{orderId:id},
                   success: function (data) {
                       location.reload(true);
                   }});
    }

    function goPayPage(id,e){
        var ev = e || window.event;
        if(ev.stopPropagation){
            ev.stopPropagation();
        }
        else if(window.event) {
            window.event.cancelBubble = true;//兼容IE
        }
        location.href='/weixin/order/'+id;
    }

    function goDeliveryPage(id,e){//查看物流信息
        var ev = e || window.event;
        if(ev.stopPropagation){
            ev.stopPropagation();
        }
        else if(window.event) {
            window.event.cancelBubble = true;//兼容IE
        }
        if(id != null){
            location.href = "/order/showExpress/" + id;
        }
    }

    function showOrderInfo(id){
        if(id != null){
            location.href = "/order/showOrderInfo/" + id;
        }
    }

    function orderConfirm(id,e){
        var ev = e || window.event;
        if(ev.stopPropagation){
            ev.stopPropagation();
        }
        else if(window.event) {
            window.event.cancelBubble = true;//兼容IE
        }
        $.ajax({
                   type: "post",
                   url: "/weixin/order/orderConfirm",
                   data:{orderId:id},
                   success: function (data) {
                    location.reload(true);
                   }});
    }
</script>

</html>