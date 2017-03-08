<%--
  Created by IntelliJ IDEA.
  User: zhangwen
  Date: 16/09/27
  Time: 下午9:01
  content:订单详情
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/commen.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>订单详情</title>
    <meta name="viewport"
          content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <!--标准mui.css-->
    <link rel="stylesheet" href="${resourceUrl}/css/mui.min.css">
    <!--App自定义的css-->
    <link rel="stylesheet" href="${resourceUrl}/frontRes/css/reset.css">
    <link rel="stylesheet" href="${resourceUrl}/frontRes/order/orderDetail/css/orderdetails.css">
    <script src="${resourceUrl}/js/zepto.min.js"></script>
</head>
<style>
    .w-text {
        margin-top: 5px;
        margin-bottom: -5px;
        padding-left: 3%
    }

    .w-text > div > div {
        float: left;
    }

    .w-text > div > div:first-child {
        color: #e94643;
        font-size: 13px;
        padding: 2px 5px 0 0;
    }

    .w-text > div > div:last-child {
        color: #666;
        font-size: 13px;
    }

    .w-text > div:after {
        content: '\20';
        display: block;
        clear: both;
    }

    .special2 > div:first-child {
        width: 45% !important;
        margin-left: 5% !important;
    }

    .special2 > div:last-child {
        width: 50% !important;
        text-align: center !important;
    }

    .special2 button {
        margin: 0 !important;
    }
</style>
<!--头部-->

<body>
<section class="orderType">
    <div><img id="stateImg" src="" alt=""></div>
    <div id="stateInput" class="orderTypeMain"></div>
</section>
<section class="orderLine">
    <div>
        <div><img class="orderLine0"
                  src="${resourceUrl}/frontRes/order/orderDetail/img/typeoff.png" alt=""></div>
        <p>待付款</p>
    </div>
    <div><img src="${resourceUrl}/frontRes/order/orderDetail/img/line1.png" alt=""></div>
    <div>
        <div><img class="orderLine1" src="${resourceUrl}/frontRes/order/orderDetail/img/typeoff.png"
                  alt=""></div>
        <p>待发货</p>
    </div>
    <div><img src="${resourceUrl}/frontRes/order/orderDetail/img/line1.png" alt=""></div>
    <div>
        <div><img class="orderLine2" src="${resourceUrl}/frontRes/order/orderDetail/img/typeoff.png"
                  alt=""></div>
        <p>待收货</p>
    </div>
    <div><img src="${resourceUrl}/frontRes/order/orderDetail/img/line1.png" alt=""></div>
    <div>
        <div><img class="orderLine3" src="${resourceUrl}/frontRes/order/orderDetail/img/typeoff.png"
                  alt=""></div>
        <p>已完成</p>
    </div>
</section>
<c:if test="${order.address != null}">
    <section class="address">
        <div>
            <div>
                <img src="${resourceUrl}/frontRes/order/orderDetail/img/address.png" alt="">
            </div>
        </div>
        <div>
            <c:if test="${order.transmitWay == 1}">
                <p style="padding: 13px 0 10px 0;">线下自提</p>
            </c:if>
            <c:if test="${order.transmitWay != 1}">
                <p>${order.address.name}<span
                        style="font-size: 15px">${order.address.phoneNumber}</span></p>

                <p>${order.address.province}${order.address.city}${order.address.county}${order.address.location}</p>
            </c:if>
        </div>
        <div></div>
    </section>
</c:if>
<c:if test="${order.address == null && order.transmitWay == 1}">
    <section class="address">
        <div>
            <div>
                <img src="${resourceUrl}/frontRes/order/orderDetail/img/address.png" alt="">
            </div>
        </div>
        <div>
            <p style="padding: 13px 0 10px 0;">线下自提</p>
        </div>
        <div></div>
    </section>
</c:if>

<section class="shop-type">
    <div><img src="${resourceUrl}/frontRes/order/orderDetail/img/shopicon.png" alt=""></div>
    <div>乐加商城</div>
    <div id="stateText"></div>
</section>
<section class="goodsList">
    <c:forEach items="${order.orderDetails}" var="orderDetail">
        <div>
            <div>
                <img src="${orderDetail.product.type == 1 ? orderDetail.productSpec.picture : orderDetail.product.picture}"
                     alt="">
            </div>
            <div>
                <div>
                    <div>${orderDetail.product.name}</div>
                    <div><span
                            style="font-size: 16px;color: #666;">×</span>${orderDetail.productNumber}
                    </div>
                </div>
                <p>${orderDetail.productSpec.specDetail}</p>

                <p>￥<fmt:formatNumber type="number"
                                      value="${orderDetail.productSpec.minPrice/100}"
                                      pattern="0.00"
                                      maxFractionDigits="2"/>+<span><c:if
                        test="${orderDetail.product.type == 1}">
                    <fmt:formatNumber type="number"
                                      value="${((orderDetail.productSpec.price-orderDetail.productSpec.minPrice) - (orderDetail.productSpec.price-orderDetail.productSpec.minPrice)%100)/100}"
                                      pattern="0"
                                      maxFractionDigits="0"/>积分

                </c:if>
                     <c:if test="${orderDetail.product.type == 2}">
                         ${orderDetail.productSpec.minScore}积分
                     </c:if></span></p>
            </div>
        </div>
    </c:forEach>
</section>
<section class="cost">
    <div>
        <div>订单金额</div>
        <div>￥<fmt:formatNumber type="number"
                                value="${(order.totalPrice - order.freightPrice)/100}"
                                pattern="0.00"
                                maxFractionDigits="2"/>+<span>${order.totalScore}积分</span></div>
    </div>
    <div>
        <div>运费</div>
        <div>
            <c:if test="${order.freightPrice == 0}">
                包邮
            </c:if>
            <c:if test="${order.freightPrice != 0}">
                <c:if test="${order.transmitWay == 1}">
                    线下自提
                </c:if>
                <c:if test="${order.transmitWay != 1}">
                    ￥<fmt:formatNumber type="number" value="${order.freightPrice/100}"
                                       pattern="0.00"
                                       maxFractionDigits="2"/>
                </c:if>
            </c:if>
        </div>
    </div>
    <div>
        <div>总价</div>
        <c:if test="${order.transmitWay == 1 || order.freightPrice == 0}">
            <div>￥<fmt:formatNumber type="number"
                                    value="${(order.totalPrice - order.freightPrice)/100}"
                                    pattern="0.00"
                                    maxFractionDigits="2"/>+<span>${order.totalScore}积分</span></div>
        </c:if>
        <c:if test="${order.transmitWay != 1 && order.freightPrice != 0}">
            <div>￥<fmt:formatNumber type="number" value="${order.totalPrice/100}" pattern="0.00"
                                    maxFractionDigits="2"/>+<span>${order.totalScore}积分</span></div>
        </c:if>
    </div>
</section>
<c:if test="${order.state != 0 && order.state != 4}">
    <section class="cost cost-jf">
        <div>
            <div>使用积分</div>
            <div>-${order.trueScore}积分</div>
        </div>
        <div>
            <div>实付金额</div>
            <div style="color: #e94643;">￥<fmt:formatNumber type="number"
                                                            value="${order.truePrice/100}"
                                                            pattern="0.00"
                                                            maxFractionDigits="2"/></div>
        </div>
    </section>
</c:if>

<section class="cost jiang">
    <div>
        <div id="payState"></div>
        <div id="payText"></div>
    </div>
</section>
<section class="orderInfo">
    <p>订单编号：${order.orderSid}</p>

    <p>下单时间：<fmt:formatDate value="${order.createDate}"
                            pattern="yyyy-MM-dd HH:mm:ss"/></p>

    <p id="payDate" style="display: none;">付款时间：<c:if
            test="${order.payDate != null}"><fmt:formatDate value="${order.payDate}"
                                                            pattern="yyyy-MM-dd HH:mm:ss"/></c:if></p>

    <p id="deliveryDate" style="display: none">发货时间：<c:if
            test="${order.deliveryDate != null}"><fmt:formatDate value="${order.deliveryDate}"
                                                                 pattern="yyyy-MM-dd HH:mm:ss"/></c:if></p>

    <p id="confirmDate" style="display: none;">确认时间：<c:if
            test="${order.confirmDate != null}"><fmt:formatDate value="${order.confirmDate}"
                                                                pattern="yyyy-MM-dd HH:mm:ss"/></c:if></p>
</section>
<section class="blank"></section>
<section id="waitFaHuo" class="footer special" style="display: none;">
    <div>
        <div><img src="${resourceUrl}/frontRes/order/orderDetail/img/phone.png" alt=""></div>
        <div>联系乐加客服</div>
        <a href="tel:4000412800"
           style="width: 100%; position: fixed;left: 0;bottom: 0;height: 50px;"></a>
    </div>
</section>
<section id="waitPay" class="footer" style="display: none;">
    <div>
        <div><img src="${resourceUrl}/frontRes/order/orderDetail/img/phone.png" alt=""></div>
        <div>联系乐加客服</div>
        <a href="tel:4000412800"
           style="width: 40%; position: fixed;left: 0;bottom: 0;height: 50px;"></a>
    </div>
    <div class="footerButton">
        <button onclick="orderCancle()">取消订单</button>
        <button class="buttonActive" onclick="goPayPage()">重新支付</button>
    </div>
</section>
<section class="blank"></section>
<section id="waitConfirm" class="footer" style="display: none;">
    <div>
        <div><img src="${resourceUrl}/frontRes/order/orderDetail/img/phone.png" alt=""></div>
        <div>联系乐加客服</div>
        <a href="tel:4000412800"
           style="width: 40%; position: fixed;left: 0;bottom: 0;height: 50px;"></a>
    </div>
    <div class="footerButton">
        <button onclick="goDeliveryPage()">查看物流</button>
        <button class="buttonActive" onclick="orderConfirm()">确认收货</button>
    </div>
</section>
<section id="finished" class="footer special2" style="display: none;"><%-- special2--%>
    <div>
        <div><img src="${resourceUrl}/frontRes/order/orderDetail/img/phone.png" alt=""></div>
        <div>联系乐加客服</div>
        <a href="tel:4000412800"
           style="width: 50%; position: fixed;left: 0;bottom: 0;height: 50px;"></a>
    </div>
    <div class="footerButton">
        <button onclick="goDeliveryPage()">查看物流</button>
    </div>
</section>
</body>
<script>
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
<script>
    var orderState = eval('${order.state}'), stateImg = '', orderLine = 'orderLine', typeActive = 'typeActive';
    if (orderState == 0) { //待付款
        stateImg = 'dfk';
        typeActive += orderState;
        orderLine += orderState;
        $('#stateText').html('待付款');
        $("#stateInput").html('<p>待付款</p><p><span id="times_minute"></span>:<span id="times_second"></span>后订单自动取消</p>');
        if (eval('${order.totalPrice}') == 0) {
            $('.jiang').css('display', 'none');
        } else {
            $('#payState').html('支付有奖');
            $('#payText').html('支付后获得￥'
                               + toDecimal(Math.ceil(eval('${order.totalPrice * backA/100}')) / 100)
                               + '乐加红包');
        }
        $('#waitPay').css('display', 'block');
    } else if (orderState == 1) { //已付款,待发货
        stateImg = 'dfh';
        typeActive += orderState;
        orderLine += orderState;
        $('#stateText').html('待发货');
        $("#stateInput").html('<p style="margin-top: 5%;">已付款，等待发货</p>');
        if (eval('${order.truePrice}') == 0) {
            $('.jiang').css('display', 'none');
        } else {
            $('#payState').html('订单奖励');
            $('#payText').html('已获得￥' + toDecimal(eval('${order.payBackA/100}')) + '乐加红包');
        }
        $('#payDate').css('display', 'block');
        $('#waitFaHuo').css('display', 'block');
    } else if (orderState == 2) { //已发货,待收货
        stateImg = 'yfh';
        typeActive += orderState;
        orderLine += orderState;
        $('#stateText').html('已发货');
        $("#stateInput").html('<p>已发货</p><p><span id="times_day"></span>天<span id="times_hour"></span>时<span id="times_minute2"></span>分<span id="times_second2"></span>秒后自动确认</p>');
        if (eval('${order.truePrice}') == 0) {
            $('.jiang').css('display', 'none');
        } else {
            $('#payState').html('订单奖励');
            $('#payText').html('已获得￥' + toDecimal(eval('${order.payBackA/100}')) + '乐加红包');
        }
        $('#payDate').css('display', 'block');
        $('#deliveryDate').css('display', 'block');
        $('#waitConfirm').css('display', 'block');
    } else if (orderState == 3) { //已完成
        stateImg = 'ywc';
        typeActive += orderState;
        orderLine += orderState;
        $('#stateText').html('已完成');
        $("#stateInput").html('<p style="margin-top: 5%;">订单已完成</p>');
        if (eval('${order.truePrice}') == 0) {
            $('.jiang').css('display', 'none');
        } else {
            $('#payState').html('订单奖励');
            $('#payText').html('已获得￥' + toDecimal(eval('${order.payBackA/100}')) + '乐加红包');
        }
        $('#payDate').css('display', 'block');
        $('#deliveryDate').css('display', 'block');
        $('#confirmDate').css('display', 'block');
        $('#finished').css('display', 'block');
    }
    $("#stateImg").attr('src',
                        '${resourceUrl}/frontRes/order/orderDetail/img/' + stateImg + '.png');
    var orderLineNode = $("." + orderLine);
    orderLineNode.attr('src', '${resourceUrl}/frontRes/order/orderDetail/img/typeactive.png');
    orderLineNode.parent().next().attr('class', 'typeActive');
</script>
<script>
    var time_end;
    $(function () {
        var state = "${order.state}";
        if (state == 0) {
            time_end = ${order.createDate.getTime()}+900000; // 设定结束时间
            count_down0();
        } else if (state == 2) {
            time_end = ${order.createDate.getTime()}+864000000; // 设定结束时间
            count_down2();
        }
    });

    //定义倒计时函数
    function count_down0() {
        var time_now = new Date(); // 获取当前时间
        time_now = time_now.getTime();
        var time_distance = time_end - time_now; // 时间差：活动结束时间减去当前时间
        var int_day, int_hour, int_minute, int_second;
        if (time_distance >= 0) {
            // 相减的差数换算成天数
            int_day = Math.floor(time_distance / 86400000);
            time_distance -= int_day * 86400000;

            // 相减的差数换算成小时
            int_hour = Math.floor(time_distance / 3600000);
            time_distance -= int_hour * 3600000;

            // 相减的差数换算成分钟
            int_minute = Math.floor(time_distance / 60000);
            time_distance -= int_minute * 60000;

            // 相减的差数换算成秒数
            int_second = Math.floor(time_distance / 1000);

            // 判断分钟小于10时，前面加0进行占位
            if (int_minute < 10)
                int_minute = "0" + int_minute;

            // 判断秒数小于10时，前面加0进行占位
            if (int_second < 10)
                int_second = "0" + int_second;

            // 显示倒计时效果
            $("#times_minute").html(int_minute);
            $("#times_second").html(int_second);
            if (eval(int_minute) == 0 && eval(int_second) == 0) {
                location.href = '/front/order/weixin/orderList';
            }
            setTimeout(count_down0, 1000);
        }
    }
    function count_down2() {
        var time_now = new Date(); // 获取当前时间
        time_now = time_now.getTime();
        var time_distance = time_end - time_now; // 时间差：活动结束时间减去当前时间
        var int_day, int_hour, int_minute, int_second;
        if (time_distance >= 0) {
            // 相减的差数换算成天数
            int_day = Math.floor(time_distance / 86400000);
            time_distance -= int_day * 86400000;

            // 相减的差数换算成小时
            int_hour = Math.floor(time_distance / 3600000);
            time_distance -= int_hour * 3600000;

            // 相减的差数换算成分钟
            int_minute = Math.floor(time_distance / 60000);
            time_distance -= int_minute * 60000;

            // 相减的差数换算成秒数
            int_second = Math.floor(time_distance / 1000);

            // 判断小时小于10时，前面加0进行占位
            if (int_hour < 10)
                int_hour = "0" + int_hour;

            // 判断分钟小于10时，前面加0进行占位
            if (int_minute < 10)
                int_minute = "0" + int_minute;

            // 判断秒数小于10时，前面加0进行占位
            if (int_second < 10)
                int_second = "0" + int_second;

            // 显示倒计时效果
            $("#times_day").html(int_day);
            $("#times_hour").html(int_hour);
            $("#times_minute2").html(int_minute);
            $("#times_second2").html(int_second);
            setTimeout(count_down2, 1000);
        }
    }
</script>
<script>
    $(".blank").css("height", $(".footer").height() + 34 + "px");
    $(".goodsList > div > div:first-child").css("height",
                                                $(".goodsList > div > div:first-child").width()
                                                + "px");
    function goDeliveryPage() {//查看物流信息
        location.href = "/order/showExpress/" + ${order.id};
    }
    function orderCancle() { //取消订单
        $.ajax({
                   type: "post",
                   url: "/order/orderCancle",
                   data: {orderId:${order.id}},
                   success: function (data) {
                       location.href = "/front/order/weixin/orderList";
                   }
               });
    }
    function goPayPage() { //支付
        location.href = '/front/order/weixin/confirmOrder?orderId=' +${order.id};
    }
    function orderConfirm() {  //确认收货
        $.ajax({
                   type: "post",
                   url: "/order/orderConfirm",
                   data: {orderId:${order.id}},
                   success: function (data) {
                       location.href = '/front/order/weixin/orderList';
                   }
               });
    }
</script>
</html>