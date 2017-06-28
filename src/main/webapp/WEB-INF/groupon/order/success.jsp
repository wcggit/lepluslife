<%--
  Created by IntelliJ IDEA.
  User: root
  Date: 2017/6/23
  Time: 15:05
  支付成功
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
    <title>支付完成</title>
    <link rel="stylesheet" type="text/css" href="${commonResource}/css/reset.css">
    <link rel="stylesheet" type="text/css" href="${commonResource}/css/main.css">
    <link rel="stylesheet" href="${resource}/groupon/order_success/css/paySuccess.css">
</head>
<body>
<section>
    <div class="payImg">
        <img src="${resource}/groupon/order_success/img/paySuccess.png" alt="">
        <div class="payText">获得<fmt:formatNumber
                type="number" value="${order.rebateScorea/100}" pattern="0.00"
                maxFractionDigits="2"/>鼓励金＋<fmt:formatNumber
                type="number" value="${order.rebateScorec/100}" pattern="0.00"
                maxFractionDigits="2"/>金币
        </div>
    </div>
    <div class="canName fixClear">
        <div>${order.product.name}</div>
        <div onclick="window.location.href='/groupon/weixin?id=${order.product.id}'"><span class="arrowUp"></span></div>
    </div>
    <div class="codeList">
        <c:forEach items="${order.codeList}" var="code" varStatus="sid">
            <div class="fixClear">
                <div>券码${sid.index + 1}</div>
                <div>${fn:substring(code.code, 0,4)}&nbsp;&nbsp;${fn:substring(code.code, 4,8)}&nbsp;&nbsp;${fn:substring(code.code, 8,12)}</div>
            </div>
        </c:forEach>
    </div>
</section>
<section class="code">
    <div class="erCode" id="qrcode">
        <%--<img src="img/lobster.png" alt="">--%>
    </div>
    <p>请出示此二维码核销</p>
    <div class="codeButton">
        <%-- todo: 链接地址修改--%>
        <button onclick="window.location.href='/groupon/weixin/orderList'">我的团购</button>
        <button onclick="window.location.href='/front/shop/weixin'">返回首页</button>
    </div>
</section>
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
</body>
</html>
