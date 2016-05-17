<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/WEB-INF/commen.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    <link rel="stylesheet" type="text/css" href="${resourceUrl}/css/confirmOrder.css"/>
    <script src="${resourceUrl}/js/jquery-1.11.3.min.js"></script>
    <script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
</head>

<body>
<!--头部-->
<header class="mui-bar mui-bar-nav" style="background: #fff;">
    <a style="color: #D62D2B;" class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"
       onclick="history.go(-1)"></a>

    <h1 class="mui-title" style="color: #D62D2B;font-weight: bold;">订单确定</h1>
</header>
<!--底部菜单-->
<nav class="mui-bar mui-bar-tab">
    <a class="mui-tab-item mui-active">
        <span class="mui-tab-label">实际支付：<font
                id="turePay">${order.totalPrice/100}</font>元</span>
    </a>
    <a class="mui-tab-item mui-active">
        <span class="mui-tab-label" id="btn-wxpay">去付款</span>
    </a>
</nav>
<div class="mui-content">
    <ul class="mui-table-view mui-table-view-striped mui-table-view-condensed">
        <li class="mui-table-view-cell">
            <div class="mui-table">
                <div class="mui-table-cell mui-col-xs-12">
                    <c:if test="${address!=null}">
                        <h5>
                            <font class="order-name" color="#333">${address.name}</font>
                            <font class="order-tel" color="#333">${address.phoneNumber}</font>
                            <button class="mui-pull-right order-edit" onclick="goAddressEdit()">编辑
                            </button>
                        </h5>
                        <p class="mui-h6 mui-ellipsis">${address.province}${address.city}${address.county}${address.location}</p>
                    </c:if>
                    <c:if test="${address==null}">
                        <h5>
                            <font class="order-name" color="#333">姓名</font>
                            <font class="order-tel" color="#333">电话</font>
                            <button class="mui-pull-right order-edit" onclick="goAddressEdit()">编辑
                            </button>
                        </h5>
                        <p class="mui-h6 mui-ellipsis">地址</p>
                    </c:if>

                </div>
            </div>
        </li>
        <li class="mui-table-view-cell">
            <c:forEach items="${order.orderDetails}" var="orderDetail">
                <c:if test="${orderDetail.state==1}">
                    <div class="mui-table">
                        <div class="mui-table-cell mui-col-xs-3">
		                    <span class="order-img">
		                    	<img src="${orderDetail.productSpec.picture}" alt="">
		                    </span>
                        </div>
                        <div class="mui-table-cell mui-col-xs-10 mui-text-left mui-pull-right">
                            <h4 class="mui-ellipsis">${orderDetail.product.name}</h4>
                            <h5>规格：<font>${orderDetail.productSpec.specDetail}</font></h5>
                            <h5>数量：<font>${orderDetail.productNumber}</font>件</h5>
                        </div>
                    </div>
                </c:if>
            </c:forEach>
            <c:forEach items="${order.orderDetails}" var="orderDetail">
                <c:if test="${orderDetail.state==0}">
                    <div class="mui-table">
                        <div class="mui-table-cell mui-col-xs-3">
		                    <span class="order-img">
		                    	<img src="${orderDetail.productSpec.picture}" alt="">
		                    </span>
                        </div>
                        <div class="mui-table-cell mui-col-xs-10 mui-text-left mui-pull-right">
                            <h4 class="mui-ellipsis">${orderDetail.product.name}</h4>
                            <h5>规格：<font>${orderDetail.productSpec.specDetail}</font></h5>
                            <h5><font color="red"><s>库存不足</s></font></h5>
                        </div>
                    </div>
                </c:if>
            </c:forEach>
        </li>
    </ul>
    <ul class="mui-table-view mui-table-view-striped mui-table-view-condensed">
        <li class="mui-table-view-cell">
            <div class="mui-table">
                <div class="mui-table-cell mui-col-xs-3">
                    <h5>总价</h5>
                </div>
                <div class="mui-table-cell mui-col-xs-5 mui-text-left">
                    <h5>${order.totalPrice/100}元</h5>
                </div>
            </div>
        </li>
        <li class="mui-table-view-cell">
            <div class="mui-table">
                <div class="mui-table-cell mui-col-xs-3">
                    <h5>邮费</h5>
                </div>
                <div class="mui-table-cell mui-col-xs-5 mui-text-left">
                    <h5><font>${order.freightPrice/100}</font>元</h5>
                </div>
                <c:if test="${order.freightPrice == 0}">
                <div class="mui-table-cell mui-col-xs-3 mui-text-right">
                    <h5>包邮</h5>
                </div>
                </c:if>
            </div>
        </li>
        <li class="mui-table-view-cell">
            <div class="mui-table">
                <div class="mui-table-cell mui-col-xs-3">
                    <h5>支付方式</h5>
                </div>
                <div class="mui-table-cell mui-col-xs-5 mui-text-left">
                    <span class="order-img2"></span><span style="color: #8F8F94;">微信支付</span>
                </div>
            </div>
        </li>
        <li class="mui-table-view-cell">
            <div class="mui-table">
                <div class="mui-table-cell mui-col-xs-3">
                    <h5>积分抵扣</h5>
                </div>
                <div class="mui-table-cell mui-col-xs-5 mui-text-left">
                    <h5><input type="text" class="btn-entry"/>积分=<font class="entry-change">0</font>元
                    </h5>
                </div>
            </div>
            <div class="mui-table">
                <div class="mui-table-cell mui-col-xs-3">
                    <h5 style="color: #D62C2C;font-size: 12px;">*积分余额：<font>${scoreB}</font></h5>
                    <input type="hidden" id="score-hidden" value="${scoreB}">
                </div>
                <div class="mui-table-cell mui-col-xs-5 mui-text-left">
                    <span style="color: #D62C2C;font-size: 12px;">该订单最多可使用<font
                            class="max-jf">${order.totalScore}</font>积分</span>
                </div>
            </div>
        </li>
    </ul>
</div>
</body>
<script type="text/javascript">
    //		手机号中间四位变*
    if (${address!=null}) {
        var value = document.getElementsByClassName('order-tel')[0].innerHTML;
        value = value.substr(0, 3) + '****' + value.substr(7);
        document.getElementsByClassName('order-tel')[0].innerHTML = value;
    }

    var entry_change = document.getElementsByClassName('entry-change')[0];
    var btn_entry = document.getElementsByClassName('btn-entry')[0];
    var max_jf = document.getElementsByClassName('max-jf')[0];
    btn_entry.oninput = function () {
        var score = this.value;
        if (score > eval(max_jf.innerHTML)) {
            alert('您最多可以使用' + eval(max_jf.innerHTML) + '积分');
            this.value = eval(max_jf.innerHTML);
            return;
        }
        score = this.value;
        var scoreB = $("#score-hidden").val();
        if (score > eval(scoreB)) {
            alert('您的积分不足');
            this.value = $("#score-hidden").val();
        }
        entry_change.innerHTML = btn_entry.value;

        $("#turePay").html(numSub(${order.totalPrice/100}, btn_entry.value));
    }
    function numSub(num1, num2) {
        var baseNum, baseNum1, baseNum2;
        var precision;// 精度
        try {
            baseNum1 = num1.toString().split(".")[1].length;
        } catch (e) {
            baseNum1 = 0;
        }
        try {
            baseNum2 = num2.toString().split(".")[1].length;
        } catch (e) {
            baseNum2 = 0;
        }
        baseNum = Math.pow(10, Math.max(baseNum1, baseNum2));
        precision = (baseNum1 >= baseNum2) ? baseNum1 : baseNum2;
        return ((num1 * baseNum - num2 * baseNum) / baseNum).toFixed(precision);
    }
    ;

    function goAddressEdit() {
        var truePrice = $("#turePay").html();
        if (truePrice <= 0) {
            alert("请选择商品");
            location.href = "/weixin/shop";
            return;
        }
        location.href = "${wxRootUrl}/weixin/order/addressEdit/${order.id}";
        return;

    }


</script>
<script>
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
<script type="text/javascript">
    $('#btn-wxpay').on('click', function () {
        if (${address!=null}) {
            var truePrice = $("#turePay").html();
            if (truePrice <= 0) {
                alert("选择商品后才能付款~");
                location.href = "/weixin/shop";
                return;
            }
            var trueScore = document.getElementsByClassName('btn-entry')[0].value;
            if ($(this).hasClass('btn-disabled')) {
                return false;
            }
            $(this).addClass('btn-disabled');
//            首先提交请求，生成预支付订单
            $.post('${wxRootUrl}/weixin/pay/weixinpay', {
                orderId: '${order.id}',
                truePrice: truePrice,
                trueScore: trueScore
            }, function (res) {
                $(this).removeClass('btn-disabled');
//            调用微信支付js-api接口
                if (res['err_msg'] != null && res['err_msg'] != "") {
                    alert(res['err_msg']);
                    return;
                } else {
                    weixinPay(res);
                    return;
                }
            });
        } else {
            location.href = "${wxRootUrl}/weixin/order/addressEdit/${order.id}";
        }
    });

    function weixinPay(res) {
        wx.chooseWXPay({
                           timestamp: res['timeStamp'], // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
                           nonceStr: res['nonceStr'], // 支付签名随机串，不长于 32 位
                           package: res['package'], // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
                           signType: res['signType'], // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
                           paySign: res['sign'], // 支付签名
                           success: function (res) {
                               // 支付成功后的回调函数
                               var total = $("#turePay").html() * 100;
                               window.location.href =
                               '${wxRootUrl}/weixin/pay/paySuccess/' + total;
                           },
                           cancel: function (res) {
                               window.location.href =
                               '${wxRootUrl}/weixin/pay/payFail/${order.id}';
                           },
                           fail: function (res) {
                               window.location.href =
                               '${wxRootUrl}/weixin/pay/payFail/${order.id}';
                           }
                       });
    }


</script>
</html>