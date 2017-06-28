<%--
  Created by IntelliJ IDEA.
  User: root
  Date: 2017/6/23
  Time: 16:08
  订单详情页
--%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
    <title>订单详情</title>
    <link rel="stylesheet" type="text/css" href="${commonResource}/css/reset.css">
    <link rel="stylesheet" type="text/css" href="${commonResource}/css/main.css">
    <link rel="stylesheet" href="${resource}/groupon/order_detail/css/orderInfo.css">
</head>
<body>
<section class="head">
    <div class="canInfo fixClear">
        <div><img src="${order.product.displayPicture}" alt=""></div>
        <div>
            <p>${order.product.name}</p>
            <p>${order.product.description}</p>
            <p><span>¥<fmt:formatNumber
                    type="number" value="${order.totalPrice/order.buyNum/100}" pattern="0.00"
                    maxFractionDigits="2"/></span>门市价<span>¥<fmt:formatNumber
                    type="number" value="${order.product.originPrice/100}" pattern="0.00"
                    maxFractionDigits="2"/></span></p>
        </div>
        <div>×${order.buyNum}</div>
    </div>
    <div class="canName fixClear">
        <div>有效日期至<fmt:formatDate value="${order.expiredDate}" pattern="yyyy-MM-dd"/></div>
        <c:if test="${order.orderState == 0}">
            <div>待使用</div>
        </c:if>
        <c:if test="${order.orderState == 1}">
            <div>已使用</div>
        </c:if>
        <c:if test="${order.orderState == 2}">
            <div>退款</div>
        </c:if>
    </div>
    <div class="codeList">
        <c:forEach items="${order.codeList}" var="code" varStatus="sid">
            <div class="fixClear">
                <div>券码${sid.index + 1}</div>
                <div>${fn:substring(code.code, 0,4)}&nbsp;&nbsp;${fn:substring(code.code, 4,8)}&nbsp;&nbsp;${fn:substring(code.code, 8,12)}</div>
                <c:if test="${code.state == 0}">
                    <div>待使用</div>
                </c:if>
                <c:if test="${code.state == 1}">
                    <div>已使用</div>
                </c:if>
                <c:if test="${code.state == 2}">
                    <div>退款中</div>
                </c:if>
                <c:if test="${code.state == 3}">
                    <div>已退款</div>
                </c:if>
                <c:if test="${code.state == 4}">
                    <div>已过期</div>
                </c:if>
                <c:if test="${code.state == -1}">
                    <div>未付款</div>
                </c:if>
            </div>
        </c:forEach>
    </div>
    <section class="code">
        <div class="erCode" id="qrcode">
        </div>
        <p>请出示此二维码核销</p>
        <div class="codeButton">
            <button class="setTkButton">申请退款</button>
        </div>
    </section>
</section>
<section class="canDate">
    <div class="fixClear">
        <div>订单编号</div>
        <div>${order.orderSid}</div>
    </div>
    <div class="fixClear">
        <div>数量</div>
        <div>${order.buyNum}</div>
    </div>
    <div class="fixClear">
        <div>订单金额</div>
        <div>¥<fmt:formatNumber
                type="number" value="${order.totalPrice/100}" pattern="0.00"
                maxFractionDigits="2"/></div>
    </div>
    <div class="fixClear">
        <div>实付金额</div>
        <div>¥<fmt:formatNumber
                type="number" value="${order.truePay/100}" pattern="0.00"
                maxFractionDigits="2"/>＋<fmt:formatNumber
                type="number" value="${order.scorea/100}" pattern="0.00"
                maxFractionDigits="2"/>鼓励金
        </div>
    </div>
    <div class="fixClear">
        <div>付款时间</div>
        <div><fmt:formatDate value="${order.expiredDate}" pattern="yyyy-MM-dd HH:mm:ss"/></div>
    </div>
    <c:if test="${order.orderType == 1}">
        <div class="fixClear">
            <div>乐加奖励</div>
            <div><fmt:formatNumber
                    type="number" value="${order.rebateScorea/100}" pattern="0.00"
                    maxFractionDigits="2"/>元鼓励金＋<fmt:formatNumber
                    type="number" value="${order.rebateScorec/100}" pattern="0.00"
                    maxFractionDigits="2"/>金币
            </div>
        </div>
    </c:if>
</section>
<section class="layer">
    <div class="setTk">
        <div>
            <img src="${resource}/groupon/order_detail/img/tk.png" alt="">
        </div>
        <p>确认要申请退款吗？</p>
        <c:if test="${order.orderType == 1}">
            <p>退款时，乐＋将收回<span>¥<fmt:formatNumber
                    type="number" value="${order.rebateScorea/100}" pattern="0.00"
                    maxFractionDigits="2"/>鼓励金+<fmt:formatNumber
                    type="number" value="${order.rebateScorec/100}" pattern="0.00"
                    maxFractionDigits="2"/>金币</span></p>
            <p>（在您购买当笔团购后，乐加奖励的呦）</p>
        </c:if>
        <div class="layerButton">
            <div class="layerClose close_cancel">取消</div>
            <div class="TkSuccessButton">确认退款</div>
        </div>
    </div>
    <div class="TkSuccess">
        <div>
            <img src="${resource}/groupon/order_detail/img/tkSuccess.png" alt="">
        </div>
        <p>申请退款成功</p>
        <p>您的资金将在1-3个工作日到账</p>
        <div class="paySuccessButton">
            <div class="layerClose close_success">关闭</div>
        </div>
    </div>
</section>
</body>
<script src="${commonResource}/js/jquery.min.js"></script>
<script src="${commonResource}/js/layer.js"></script>
<script src="${resourceUrl}/js/qrcode.js"></script>
<script>
    var content = '${order.qrCodeUrl}';
    // 创建二维码
    var qrcode = new QRCode(document.getElementById("qrcode"), {
        width: 150,//设置宽高
        height: 150
    });
    qrcode.makeCode(content);
</script>
<script>
    function refundSuccess() {
        layer.closeAll();
        layer.open({
                       type: 1,
                       title: 0,
                       closeBtn: 0,
                       offset: '20%',
                       area: ['80%'], //宽高
                       content: $(".TkSuccess")
                   });
    }
    $(".setTkButton").click(function () {
        layer.open({
                       type: 1,
                       title: 0,
                       closeBtn: 0,
                       offset: '20%',
                       area: ['80%'], //宽高
                       content: $(".setTk")
                   });
    });
    $(".close_cancel").click(function () {
        layer.closeAll();
    });
    $(".close_success").click(function () {
        window.location.href = '/groupon//weixin/orderList';
    });
    $(".TkSuccessButton").click(function () {
        //确认退款
        $.post('/groupon/sign/refund', {
            orderId: '${order.orderId}',
            source: 'WEB'
        }, function (res) {
            if (res.status == 200) {//调用微信支付js-api接口
                refundSuccess();
            } else {
                alert(res['msg']);
                $('.setTkButton').attr('onclick', '');
                $('.TkSuccessButton').attr('onclick', '');
            }
        });
    });
</script>
</html>
