<%--
  Created by IntelliJ IDEA.
  User: zhangwen
  Date: 2016/11/4
  Time: 12:59
  话费充值页面
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <title>积分充值话费</title>
    <link rel="stylesheet" href="${resourceUrl}/frontRes/activity/phone/css/common.css">
    <link rel="stylesheet" href="${resourceUrl}/frontRes/activity/phone/css/homePage.css">
    <link rel="stylesheet" href="${resourceUrl}/frontRes/activity/phone/css/soldOut.css">
    <script src="${resourceUrl}/js/zepto.js"></script>
    <script>
        var total = eval('${total}'), limit = eval('${limit}'), update = eval('${update}'), buyCheapCount = 0, canBuyCheap = 1;
        var ruleList = eval('${ruleList}'), orderList = eval('${orderList}'), worthList = [], buyTimes = 0;
        var date = new Date();
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        date.setMilliseconds(0);
        var currDay = date.getTime(); //今日零点零分
        var currWeek = currDay - (date.getDay() - 1) * 86400000;//本周一零点零分
        date.setDate(1);
        var currMonth = date.getTime();  //本月一号零点零分
        var time_end = currDay + 86400000;  //明日零点零分

        //计算已购限购数量，添加每一种产品的已购数量
        for (var i = 0; i < orderList.length; i++) {//判断在更新后购买特惠商品是否达到上限
            if (orderList[i].cheap == 1 && orderList[i].payDate >= update) {
                buyCheapCount += 1;
                if (buyCheapCount >= limit) {
                    canBuyCheap = 0;
                    break;
                }
            }
        }

        for (var j = 0; j < ruleList.length; j++) {
            if (worthList.indexOf(ruleList[j].worth) != 0) {
                worthList.push(ruleList[j].worth);
            }
            var hasBuyTotalCount = 0, hasBuyTypeCount = 0; //累计购买和分类购买
            if (ruleList[j].cheap == 0) { //不是特惠，累计购买和时段购买是否达到上限
                for (var m = 0; m < orderList.length; m++) {
                    if(orderList[m].phoneRule != null){
                        if (ruleList[j].id == orderList[m].phoneRule.id && orderList[m].cheap == 0) {
                            hasBuyTotalCount += 1; //累计购买该产品数量
                            if (ruleList[j].limitType != 0) {
                                //分日期购买限制
                                if (ruleList[j].limitType == 1) {
                                    // console.log(orderList[m].payDate+'==='+ currDay)
                                    if (orderList[m].payDate >= currDay) {
                                        hasBuyTypeCount += 1;
                                    }
                                } else if (ruleList[j].limitType == 2) {
                                    if (orderList[m].payDate >= currWeek) {
                                        hasBuyTypeCount += 1;
                                    }
                                } else if (ruleList[j].limitType == 3) {
                                    if (orderList[m].payDate >= currMonth) {
                                        hasBuyTypeCount += 1;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            ruleList[j].hasBuyTotal = hasBuyTotalCount;
            ruleList[j].hasBuyType = hasBuyTypeCount;
        }
        worthList.sort();

        for (var i = 0; i < ruleList.length; i++) {
            console.log(ruleList[i]);
        }


    </script>
</head>
<body>

<div class="main">
    <div class="top">
        <ul class="description">
            <li class="left" onclick="goOrderList()"><span></span>查看充值记录</li>
            <li class="right">如何赚积分<span></span></li>
        </ul>
        <p class="jf-num">${scoreB.score}</p>

        <p class="jf-desc">我的积分</p>

        <p class="progress">
            <span style="width: ${used*100/total}%"></span>
        </p>

        <p class="progress-desc">乐加话费池${used}/${total}</p>
    </div>
    <c:if test="${soldOut == 0}">
        <div class="bottom">
            <p class="telNum ttl">充值手机号</p>
            <input class="telNum" type="text" id="phone" placeholder="请输入您的手机号" value="${phone}">

            <p class="ttl">请选择充值金额</p>

            <p class="moneyNum">

            </p>

            <p class="payNum ttl">请选择付款金额</p>

            <div class="payNum" id="payNum">

            </div>
            <p class="hotline">乐加客服电话 <a href="tel:400-0412-800">400-0412-800</a></p>
        </div>

    </c:if>
    <c:if test="${soldOut == 1}">
        <div class="bottom">
            <div class="btn-soldOut">话费已售罄</div>
            <div class="recharge-desc">
                <p><span id="times_hour"></span>小时<span id="times_minute"></span>分钟<span
                        id="times_second"></span>秒后刷新话费池</p>

                <p>乐加生活还为您准备了丰厚的商品，别忘了抢购哦！</p>
            </div>
            <p class="bottom-ttl">为你推荐</p>
            <ul id="hotContent">

            </ul>
            <div class="look-more" onclick="goProductIndex()">查看更多 ></div>
        </div>
    </c:if>

    <!--确认充值和话费限购提示框-->
    <div class="prompt0 prompt01" style="display: none">
        <div>
            <p id="warnText">确定要使用100积分<br>购买100元话费吗<span>？</span></p>
            <ul>
                <li class="left">取消</li>
                <li class="right">确认</li>
            </ul>
        </div>
    </div>

    <!--运营商维护提示框-->
    <div class="prompt2 prompt234" style="display: none">
        <div>
            <p class="ttl">积分还能购买其他优惠商品哦！</p>

            <p class="btn">马上逛逛</p>
            <span class="btn-close"></span>
        </div>
    </div>
    <!--没有充值记录提示框-->
    <div class="prompt3 prompt234" style="display: none">
        <div>
            <p class="btn">知道了</p>
        </div>
    </div>
    <!--话费已售罄提示框-->
    <div class="prompt4 prompt234" style="display: none">
        <div>
            <p class="ttl">积分还能购买其他优惠商品哦！</p>

            <p class="btn">马上逛逛</p>
            <span class="btn-close"></span>
        </div>
    </div>

</div>
<c:if test="${soldOut == 0}">
    <p class="btn-recharge btn-no">立刻充值</p>
</c:if>
<script src="http://res.wx.qq.com/open/js/jweixin-1.1.0.js"></script>
<script>
    var soldOut = '${soldOut}';
    if (soldOut == 0) {
        //填充话费面额列表
        var worthElement = $('.moneyNum'), worthHtml = '', selectWorth = 0, selectRuleId = 0;
        for (var q = 0; q < worthList.length; q++) {
            worthHtml +=
            '<span class="initClass" onclick="oneWorthList(' + worthList[q] + ')">' + worthList[q]
            + '元</span>';
        }
        worthElement.html(worthHtml);

        var telState = false, selectState = false;

        var mainObject = {
            //radioState
            radioState: function () {
                var radioList = $('.main .bottom .payNum label');
                radioList.each(function (i) {
                    if (radioList.eq(i).find('input').prop('disabled')) {
                        radioList.eq(i).find('span.icon-radio').removeAttr('class').attr('class',
                                                                                         'icon-not icon-radio');
                        radioList.eq(i).removeClass('focusClass').addClass('initClass');
                        radioList.eq(i).on('touchstart', function () {
                            radioList.eq(i).find('p.prompt').css('display', 'block');
                            setTimeout(function () {
                                radioList.eq(i).find('p.prompt').css('display', 'none');
                            }, 3000)
                        })
                    } else {
                        if (radioList.eq(i).find('input').prop('checked')) {
                            radioList.eq(i).find('span.icon-radio').removeAttr('class').attr('class',
                                                                                             'icon-yes icon-radio');
                            radioList.eq(i).removeClass('initClass').addClass('focusClass');
                        } else if (!radioList.eq(i).find('input').prop('checked')) {
                            radioList.eq(i).find('span.icon-radio').removeAttr('class').attr('class',
                                                                                             'icon-no icon-radio');
                            radioList.eq(i).removeClass('focusClass').addClass('initClass');
                        }
                    }
                })
            },
            //btnState
            btnState: function () {
                if (telState == true && selectState == true) {
                    $('.btn-recharge').removeClass('btn-no').addClass('btn-yes');
                    $('.btn-recharge').attr('onclick', 'btnFun()');
                } else {
                    $('.btn-recharge').removeClass('btn-yes').addClass('btn-no');
                    $('.btn-recharge').attr('onclick', '');
                }
            }
        };
        if (((/^1[3|4|5|6|7|8]\d{9}$/.test($('#phone').val())))) {
            $('.telNum').removeClass('focusClass');
            telState = true;
            mainObject.btnState();
        }
        //        初始化radioState
        mainObject.radioState();
        //        telNum
        $('.telNum').on('input propertychange', function () {
            if (!/1[0-9]{10}/.test($(this).val())) {
                $(this).addClass('focusClass');
                telState = false;
                mainObject.btnState();
            } else {
                $(this).removeClass('focusClass');
                $(this).val($(this).val().replace(/(1[0-9]{10}).*/, '$1'));
                telState = true;
                mainObject.btnState();

            }
        });
        //        moneyNumList
        function moneyNumListFun() {
            var moneyNumList = $('.main .bottom .moneyNum span');
            moneyNumList.each(function (i) {
                moneyNumList.eq(i).on('touchstart', function () {
                    moneyNumList.removeClass('focusClass').addClass('initClass');
                    $(this).removeClass('initClass').addClass('focusClass');
                })
            });
        }

        moneyNumListFun();

        //        radioList
        function radioFun() {
            var radioList = $('.main .bottom .payNum label');
            radioList.each(function (i) {
                radioList.eq(i).on('click', function () {
                    if (radioList.eq(i).find('input').prop('disabled')) {
                        selectState = false;
                    } else {
                        selectState = true;
                        mainObject.radioState();
                        mainObject.btnState();
                    }
                })
            });
        }

        radioFun();

        function submit(subPhone, subRuleId) {
            $.post("/weixin/pay/phonePay", {phone: subPhone, ruleId: subRuleId},
                   function (res) {
                       if (res.status == 200) { //创建订单成功，吊起微信支付
                           orderId = res.data.orderId;
                           weixinPay(res.data);
                           return
                       } else {
                           if (res.status == 1006) {//该活动已过期
                               alert(res.msg);
                           } else if (res.status == 1001) {//话费池已满
                               $('.prompt4').css('display', 'block');
                           } else if (res.status == 1002) {//话费库存不足
                               alert(res.msg);
                           } else if (res.status == 1003) {//特惠活动参与次数已达上限
                               alert(res.msg);
                           } else if (res.status == 1004) {//累计购买超限
                               alert(res.msg);
                           } else if (res.status == 1005) {//分类购买超限
                               alert(res.msg);
                           } else if (res.status == 500) {//系统异常
                               alert(res.msg);
                           } else if (res.status == 2000) {//全积分支付成功
                               orderId = res.data;
                               // 支付成功后的回调函数
                               window.location.href = '/weixin/pay/phoneSuccess/' + orderId;
                           }
                           $('.btn-recharge').attr('onclick', 'btnFun()');

                       }
                   })
        }

        //        在此函数内书写“立刻充值”的按钮绑定的事件
        var orderId = '';

        function btnFun() {
            $('.btn-recharge').attr('onclick', '');
            var subState = '${subState}';
            if (subState != 1) {
                window.location.href = '/resource/frontRes/activity/phone/unregistered.html';
            }
            var phone = '${phone}';
            if (phone == null || phone == '' || phone == 'null') {
                window.location.href = '/resource/frontRes/activity/phone/registered.html';
            }

            var radioList = $('.main .bottom .payNum label');
            radioList.each(function (i) {
                if (radioList.eq(i).find('input').prop('checked')) {
                    selectRuleId = radioList.eq(i).find('input').val();
                }
            });

            var rechargePhone = $('#phone').val();
            if ((!(/^1[3|4|5|6|7|8]\d{9}$/.test(rechargePhone)))) {
                alert("请输入正确的手机号");
                $('#phone').focus();
                return false
            }
            //检查是否可以下单
            $.get("/front/phone/check", {phone: rechargePhone, worth: selectWorth}, function (res) {
                if (res.status == 200) {
                    //如果是全积分支付或特惠产品，弹出提示窗
                    var warnText = '', warn = 0;
                    for (var j = 0; j < ruleList.length; j++) {
                        if (ruleList[j].id == selectRuleId) {
                            if (ruleList[j].cheap == 1) {
                                warn = 1;
                                warnText += '特惠话费只能购买' + limit + '次<br>';
                                if (ruleList[j].payType == 3) {
                                    warnText +=
                                    '使用' + ruleList[j].score + '积分购买' + ruleList[j].worth + '元话费？';
                                }
                            } else if (ruleList[j].payType == 3) {
                                warn = 1;
                                warnText +=
                                '确定要使用' + ruleList[j].score + '积分<br>购买' + ruleList[j].worth
                                + '元话费吗<span>？</span>';

                            } else {
                                warnText += '本次充值后您将无法购买其他特惠话费哦！';
                            }
                            break;
                        }
                    }
                    if (warn == 1) {
                        $('#warnText').html(warnText);
                        $('.prompt0').css('display', 'block');
                        return
                    }
                    submit(rechargePhone, selectRuleId);
                } else {
                    $('.btn-recharge').attr('onclick', 'btnFun()');
                    $('.prompt2').css('display', 'block');
                }
            })
        }

        //根据选中的面值加载对应的规则列表
        var content = '';

        function oneWorthList(currWorth) {
            content = '';
            selectWorth = currWorth;
            for (var j = 0; j < ruleList.length; j++) {
                var currRule = ruleList[j];
                if (currWorth == currRule.worth) {
                    if (currRule.cheap == 1) { //特惠
                        if (currRule.repository == 0) { //已售罄
                            content +=
                            '<label><input name="payNum" type="radio" value="'
                            + currRule.id + '" disabled/><span class="left icon-tehui">'
                            + toDecimal(currRule.price / 100)
                            + '元 + ' + currRule.score
                            + '积分</span><span class="right">(已售罄)</span><span class="icon-radio"></span><p class="prompt">该产品已售罄</p></label>';
                        } else if (canBuyCheap == 0) { //购买特惠产品已达上限
                            content +=
                            '<label><input name="payNum" type="radio" value="'
                            + currRule.id + '" disabled/><span class="left icon-tehui">'
                            + toDecimal(currRule.price / 100)
                            + '元 + ' + currRule.score
                            + '积分</span><span class="right">(限购' + limit +
                            '次)</span><span class="icon-radio"></span><p class="prompt">购买特惠产品已达上限</p></label>';
                        } else {
                            content +=
                            '<label><input name="payNum" type="radio" value="'
                            + currRule.id + '"/><span class="left icon-tehui">'
                            + toDecimal(currRule.price / 100)
                            + '元 + ' + currRule.score
                            + '积分</span><span class="right">(限购' + limit +
                            '次)</span><span class="icon-radio"></span></label>';
                        }
                    } else { //不是特惠
                        if (currRule.totalLimit != 0) { //有累计购买限制
                            if (currRule.hasBuyTotal >= currRule.totalLimit) {//超限
                                content +=
                                '<label><input name="payNum" type="radio" value="'
                                + currRule.id + '" disabled/><span class="left">'
                                + toDecimal(currRule.price / 100)
                                + '元 + ' + currRule.score
                                + '积分</span><span class="right">(限购' + currRule.totalLimit
                                + '次，已购' + currRule.hasBuyTotal
                                + '次)</span><span class="icon-radio"></span><p class="prompt">该产品累计限购'
                                + currRule.totalLimit + '次</p></label>';
                            } else {//不超限
                                checkLimitType(currRule);
                            }
                        } else {//无累计购买限制
                            checkLimitType(currRule);
                        }
                    }
                }
            }
            $('#payNum').html(content);
            mainObject.radioState();
            radioFun();
        }

        function checkLimitType(currRule) {
            if (currRule.limitType != 0) { //有分类购买限制
                var currType = '';
                if (currRule.limitType == 1) {
                    currType = '每日';
                } else if (currRule.limitType == 2) {
                    currType = '每周';
                } else if (currRule.limitType == 3) {
                    currType = '每月';
                }
                if (currRule.hasBuyType >= currRule.buyLimit) {//分类购买超限
                    content +=
                    '<label><input name="payNum" type="radio" value="' + currRule.id
                    + '" disabled/><span class="left">'
                    + toDecimal(currRule.price / 100)
                    + '元 + ' + currRule.score
                    + '积分</span><span class="right">(' + currType + '限购' + currRule.buyLimit
                    + '次，已购' + currRule.hasBuyType
                    + '次)</span><span class="icon-radio"></span><p class="prompt">该产品' + currType
                    + '限购'
                    + currRule.buyLimit + '次</p></label>';
                } else { //分类购买未超限
                    content +=
                    '<label><input name="payNum" type="radio" value="' + currRule.id
                    + '"/><span class="left">'
                    + toDecimal(currRule.price / 100)
                    + '元 + ' + currRule.score
                    + '积分</span><span class="right">(' + currType + '限购' + currRule.buyLimit
                    + '次</span><span class="icon-radio"></span></label>';
                }
            } else { //无任何限制
                content +=
                '<label><input name="payNum" type="radio" value="' + currRule.id
                + '"/><span class="left">'
                + toDecimal(currRule.price / 100)
                + '元 + ' + currRule.score
                + '积分</span><span class="icon-radio"></span></label>';
            }
        }
    } else {
        //话费池已满的倒计时
        function count_down() {
            var time_now = new Date(); // 获取当前时间
            time_now = time_now.getTime();
            var time_distance = time_end - time_now; // 时间差：活动结束时间减去当前时间
            var int_hour, int_minute, int_second;
            if (time_distance >= 0) {

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
                $("#times_hour").html(int_hour);
                $("#times_minute").html(int_minute);
                $("#times_second").html(int_second);
                setTimeout(count_down, 1000);
            }
        }

        count_down();
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
    }

</script>

<script>
    function goProductIndex() {
        window.location.href = "/front/product/weixin/productIndex";
    }
    function goHotDetail(id) { //go爆品详情页
        location.href = "/front/product/weixin/limitDetail?productId=" + id;
    }

    function goOrderList() {
        //已购买次数
        if (orderList != null && orderList != '') {
            buyTimes = orderList.length;
        }
        if (buyTimes > 0) {
            window.location.href = "/front/phone/weixin/orderList";
        } else {
            $('.prompt3').css('display', 'block');
        }
    }
    //    确认充值和话费限购提示框
    $(".prompt0 .left").on('touchstart', function () {
        $('.prompt0').css('display', 'none');
        $('.btn-recharge').attr('onclick', 'btnFun()');
    });
    $(".prompt0 .right").on('touchstart', function () {
        var rechargePhone = $('#phone').val();
        $('.prompt0').css('display', 'none');
        submit(rechargePhone, selectRuleId);
    });
    //    运营商维护提示框
    $(".prompt2 .btn").on('touchstart', function () {
//        $('.prompt2').css('display', 'none');
        goProductIndex();
    });
    $(".prompt2 .btn-close").on('touchstart', function () {
        $('.prompt2').css('display', 'none');
    });
    //    没有充值记录提示框
    $(".prompt3 .btn").on('touchstart', function () {
        $('.prompt3').css('display', 'none');
    });
    //    话费已售罄提示框
    $(".prompt4 .btn").on('touchstart', function () {
        goProductIndex();
    });
    $(".prompt4 .btn-close").on('touchstart', function () {
        $('.prompt2').css('display', 'none');
    });

    function goProductIndex() {
        window.location.href = "/front/product/weixin/productIndex";
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
<script>
    function weixinPay(res) {
        wx.chooseWXPay({
                           timestamp: res['timeStamp'], // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
                           nonceStr: res['nonceStr'], // 支付签名随机串，不长于 32 位
                           package: res['package'], // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
                           signType: res['signType'], // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
                           paySign: res['sign'], // 支付签名
                           success: function (res) {
                               // 支付成功后的回调函数
                               window.location.href = '/weixin/pay/phoneSuccess/' + orderId;
                           },
                           cancel: function (res) {

                               $('.btn-recharge').attr('onclick', 'btnFun()');
                           },
                           fail: function (res) {

                               $('.btn-recharge').attr('onclick', 'btnFun()');
                           }
                       });
    }

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

</body>
</html>
