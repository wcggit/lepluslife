<%--
  Created by IntelliJ IDEA.
  User: zhangwen
  Date: 2017/6/23
  Time: 10:11
  订单确认页
--%>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    <title>订单确认</title>
    <link rel="stylesheet" type="text/css" href="${commonResource}/css/reset.css">
    <link rel="stylesheet" type="text/css" href="${commonResource}/css/main.css">
    <link rel="stylesheet" type="text/css" href="${commonResource}/css/waiting.css">
    <link rel="stylesheet" href="${resource}/groupon/order_confirm/css/honeySwitch.css">
    <link rel="stylesheet" href="${resource}/groupon/order_confirm/css/orderCheck.css">
</head>
<body>
<section class="orderCan">
    <div class="showCan fixClear">
        <div>
            <img src="${order.grouponProduct.displayPicture}" alt="">
        </div>
        <div>
            <p>${order.grouponProduct.name}</p>
            <p>￥<fmt:formatNumber
                    type="number" value="${order.grouponProduct.originPrice/100}" pattern="0.00"
                    maxFractionDigits="2"/></p>
        </div>
        <div>×<span id="showNum">1</span></div>
    </div>
    <div class="infoList fixClear">
        <div>数量</div>
        <div class="add-down fixClear">
            <div class="down">-</div>
            <div class="num">1</div>
            <div class="add">+</div>
        </div>
    </div>
    <div class="infoList fixClear">
        <div>订单金额</div>
        <div>￥<span id="totalPrice"><fmt:formatNumber
                type="number" value="${order.totalPrice/100}" pattern="0.00"
                maxFractionDigits="2"/></span></div>
    </div>
    <c:if test="${state == 1}">
        <div class="infoList fixClear">
            <div class="glj">使用鼓励金</div>
            <div class="setglj">
                <input type="number" id="scorea" onkeyup="javascript:CheckInputIntFloat(this);">
                <span>余额：<span id="canUseScore"><fmt:formatNumber
                        type="number" value="${canUseScore/100}" pattern="0.00"
                        maxFractionDigits="2"/></span>元</span>
            </div>
            <div class="switch-on"></div>
        </div>
    </c:if>
    <div class="pay">
        <div>实际支付</div>
        <div>￥<span id="truePay"><fmt:formatNumber
                type="number" value="${order.totalPrice/100}" pattern="0.00"
                maxFractionDigits="2"/></span></div>
    </div>
</section>
<section class="payButton">
    <button id="btn-wxPay" onclick="payByWx()">确认支付</button>
    <c:if test="${state == 1}">
        <p>消费后再得¥<span id="rebateScorea"><fmt:formatNumber
                type="number" value="${order.rebateScorea/100}" pattern="0.00"
                maxFractionDigits="2"/></span>鼓励金和<span id="rebateScorec"><fmt:formatNumber
                type="number" value="${order.rebateScorec/100}" pattern="0.00"
                maxFractionDigits="2"/></span>金币</p>
    </c:if>
</section>

<div class="waiting">
    <div class="img">
        <img src="${resourceUrl}/frontRes/img/waiting.gif" alt="">
    </div>
</div>
</body>
<script src="${commonResource}/js/jquery.min.js"></script>
<script src="${resource}/groupon/order_confirm/js/honeySwitch.js"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
<script>
    var canUseScore = eval('${canUseScore}'), state = eval('${state}'), isUseScore = 1;
    var payBackA = eval('${order.rebateScorea}'), payBackC = eval('${order.rebateScorec}');
    var singlePrice = eval('${order.totalPrice}'), currBuyNum = 1,
        currTruePay = eval('${order.totalPrice / 100}');
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
    /**购买数量修改联动*/
    var $truePay = $('#truePay');
    function editBuyNum(currNum, status) {
        currBuyNum = currNum;
        //订单总金额和数量
        $('#totalPrice').html(toDecimal(singlePrice * currNum / 100));
        $('#showNum').html(currNum);
        //返金币和鼓励金
        $('#rebateScorea').html(toDecimal(payBackA * currNum / 100));
        $('#rebateScorec').html(toDecimal(payBackC * currNum / 100));
        //订单实付
        if (status === 1) {
            $truePay.html(toDecimal((eval($truePay.html()) * 100 + singlePrice) / 100));
        } else {
            $truePay.html(toDecimal((eval($truePay.html()) * 100 - singlePrice) / 100));
        }
        currTruePay = eval($truePay.html());
    }
</script>
<script>
    $(".glj").next().next().click(function () {
        $('#scorea').val('');
        $truePay.html(toDecimal(singlePrice * currBuyNum / 100));
        var thisClass = $(this).attr("class");
        switch (thisClass) {
            case "switch-on":
                isUseScore = 0;
                $(".setglj").hide();
                break;
            case "switch-off":
                isUseScore = 1;
                $(".setglj").show();
                break;
            default:
                break;
        }
    });
    $(".down").click(function () {
        var num = parseInt($(".num").html());
        if (num > 1) {
            $(".num").html(num - 1);
            editBuyNum(num - 1, 0);
        }
    });
    $(".add").click(function () {
        var num = parseInt($(".num").html());
        if (num < 100) {
            $(".num").html(num + 1);
            editBuyNum(num + 1, 1);
        }
    });
</script>
<script>
    $scorea = $('#scorea');
    /**最大值控制*/
    function maxControl(val) {
        console.log(parseInt(val * 100) + "===" + canUseScore);
        if (parseInt(val * 100) > canUseScore) {
            $scorea.val(toDecimal(canUseScore / 100));
        }

        if (eval($scorea.val()) > eval($('#totalPrice').html())) {
            $scorea.val($('#totalPrice').html());
        }
    }
    /**使用鼓励金输入框改变*/
    function CheckInputIntFloat(obj) {
        //先把非数字的都替换掉，除了数字和.
        obj.value = obj.value.replace(/[^\d.]/g, "");
        //必须保证第一个为数字而不是.
        obj.value = obj.value.replace(/^\./g, "");
        //保证只有出现一个.而没有多个.
        obj.value = obj.value.replace(/\.{2,}/g, ".");
        //保证.只出现一次，而不能出现两次以上
        obj.value = obj.value.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
        //保证 数字整数位不大于8位
        if (100000000 <= parseFloat(obj.value))
            obj.value = "";

        maxControl(parseFloat(obj.value));
        var score = $scorea.val();
        if (score === null || score === '') {
            score = 0;
        }
        $truePay.html(toDecimal(currTruePay - eval(score)));
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
<script>
    function payByWx() {
        $('.waiting').css('display', 'block');
        $('#btn-wxPay').attr('onclick', '');
        var trueScore = 0;
        if (state === 1) {
            if ($scorea.val() != null && $scorea.val() != '') {
                trueScore = toDecimal($scorea.val());
            }
            if (trueScore < 0) {
                alert("鼓励金不能小于0");
                return;
            }
        }

        $.post('/groupon/sign/submit', {
            orderId: '${order.id}',
            useScore: trueScore,
            source: 'WEB',
            buyNum: currBuyNum
        }, function (res) {
            if (res.status == 2000) {
                window.location.href = '/groupon/paySuccess/${order.id}';
            } else if (res.status == 200) {//调用微信支付js-api接口
                weixinPay(res.data);
            } else {
                $('.waiting').css('display', 'none');
                alert(res['msg']);
                $('#btn-wxPay').attr('onclick', 'payByWx()');
            }
        });
    }

    function weixinPay(res) {
        $('.waiting').css('display', 'none');
        WeixinJSBridge.invoke(
            'getBrandWCPayRequest', {
                "appId": res['appId'] + "",     //公众号名称，由商户传入
                "timeStamp": res['timeStamp'] + "", // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
                "nonceStr": res['nonceStr'] + "", // 支付签名随机串，不长于 32 位
                "package": res['package'] + "", // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
                "signType": res['signType'] + "", // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
                "paySign": res['sign'] + "" // 支付签名
            },
            function (reslut) {
                if (reslut.err_msg == "get_brand_wcpay_request:ok") {
                    window.location.href = '/groupon/paySuccess/${order.id}';
                } else {
                    $('#btn-wxPay').attr('onclick', 'payByWx()');
                }
            }
        );
        if (typeof WeixinJSBridge == "undefined") {
            if (document.addEventListener) {
                document.addEventListener('WeixinJSBridgeReady', weixinPay, false);
            } else if (document.attachEvent) {
                document.attachEvent('WeixinJSBridgeReady', weixinPay);
                document.attachEvent('onWeixinJSBridgeReady', weixinPay);
            }
        } else {
            weixinPay();
        }
    }
</script>
</html>
