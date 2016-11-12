<%--
  Created by IntelliJ IDEA.
  User: zhangwen
  Date: 2016/9/21
  Time: 23:05
  Content:秒杀中心
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--<%@include file="/WEB-INF/commen.jsp" %>--%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
    <meta name="format-detection" content="telephone=no">
    <meta name="format-detection" content="telephone=yes"/>
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="black"/>
    <title>秒杀</title>
    <c:set var="resourceUrl" value="http://www.lepluslife.com/resource"></c:set>
    <c:set var="wxRootUrl" value="http://www.lepluslife.com"></c:set>
    <link rel="stylesheet" href="${resourceUrl}/frontRes/css/reset.css">

    <link rel="stylesheet" href="${resourceUrl}/frontRes/product/hotIndex/css/main2.css">
</head>
<body class="body">
<section class="top">
    <img src="${resourceUrl}/frontRes/product/hotIndex/img/line.png" alt="">

    <p>主打爆款</p>
</section>
<c:if test="${product != null}">
    <section class="hotGoods" onclick="goHotDetail(${product.id})">
        <div>
            <img src="${product.thumb}" alt="">
        </div>
        <div class="only">
            <div>
                <img src="${resourceUrl}/frontRes/product/hotIndex/img/w-zdbk.png" alt=""/>
            </div>
            <div>
                <div>${product.name}</div>
                <div style="margin-top: 3px;"><span style="color:#f54339;font-size: 20px;"><fmt:formatNumber
                        type="number" value="${product.minPrice/100}" pattern="0.00"
                        maxFractionDigits="2"/>元</span> +
                    <span style="color:#fb991a;font-size:20px;margin-left: -2px;">${product.minScore}积分</span>
                        <%--<span class="line-down">市场价<fmt:formatNumber type="number" value="${product.price/100}"--%>
                        <%--pattern="0.00" maxFractionDigits="2"/>元</span>--%>
                </div>
            </div>
            <p class="lll">
                <img width="14" height="14" style="padding-top: 1px" src="${resourceUrl}/frontRes/activity/subPage/img/lightning.png" alt=""/>
                <span style="margin-top: -1px;">仅剩${product.repository}份</span>
            </p>
        </div>
    </section>
</c:if>

<section class="top">
    <img src="${resourceUrl}/frontRes/product/hotIndex/img/line.png" alt="">

    <p>全部秒杀</p>
</section>
<section class="list" id="hotContent">

</section>
<section class="footer">
    <div onclick="goMenu(1)">
        <div>
            <img src="${resourceUrl}/frontRes/product/hotIndex/img/zhenpin1.png" alt="">
        </div>
        <p>臻品</p>
    </div>
    <div>
        <div>
            <img src="${resourceUrl}/frontRes/product/hotIndex/img/miaosha2.png" alt="">
        </div>
        <p>秒杀</p>
    </div>
    <div onclick="goMenu(3)">
        <div>
            <img src="${resourceUrl}/frontRes/product/hotIndex/img/gouwuche1.png" alt="">
        </div>
        <p>购物车</p>
    </div>
    <div onclick="goMenu(4)">
        <div>
            <img src="${resourceUrl}/frontRes/product/hotIndex/img/dingdan1.png" alt="">
        </div>
        <p>订单</p>
    </div>
</section>
</body>
<script src="${resourceUrl}/js/jquery-2.0.3.min.js"></script>
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
    var currPage = 2, currLength = 0, pic = '${resourceUrl}/frontRes/activity/subPage/img/lightning.png';
    $(".only").css("margin-top", -$(".only").height() - 10 + "px");
    $(".only-small").css("margin-top", -$(".only-small").height() - 12 + "px");
    $(".list").css("margin-bottom", $(".footer").height() + 13 + "px");
    function ajaxHotProductList(page) {
        $.ajax({
                   type: "get",
                   url: "/front/product/hotList?page=" + page,
                   //url: "/shop/productList?typeId=0&page=" + page,
                   success: function (data) {
                       var list = data.data, i = 0, mainContent = $("#hotContent"), content = "";
                       currLength = 0;
                       if (list != null) {
                           currLength = list.length;
                           for (i; i < list.length; i++) {
                               content +=
                               '<div  onclick="goHotDetail(' + list[i].id
                               + ')"><div><img class="w-imgSize" src="' + list[i].picture
                               + '" alt=""></div><div class="information"><p>' + list[i].name
                               + '</p><div>' + toDecimal(list[i].minPrice / 100)
                               + '<span style="font-size: 12px;">元</span>+<span style="color: #fb991a;">'
                               + list[i].minScore
                               + '<span style="font-size: 12px;">积分</span></span><span style ="font-size: 11px;color: #AEAEAE;margin-left: 6%;text-decoration:line-through;">市场价'
                               + toDecimal(list[i].price / 100)
                               + '元</span></div><div><div><img src = "' + pic + '"></div><div>还剩'
                               + list[i].repository + '份</div><div>马上抢</div></div></div></div>';
                           }
                           mainContent.html(mainContent.html() + content);
                           $(".secondsKill img").css("height", 105 + "px");
                           $(".only-small").css("margin-top",
                                                -$(".only-small").height() - 12 + "px");
                       }
                   }
               });
    }

    function goHotDetail(id) { //go爆品详情页
        location.href = "/front/product/weixin/limitDetail?productId=" + id;
    }
    function goMenu(curIndex) { //go其他菜单页
        if (curIndex == 1) {
            location.href = "/front/product/weixin/productIndex";
        } else if (curIndex == 3) {
            location.href = "/weixin/cart";
        } else if (curIndex == 4) {
            location.href = "/front/order/weixin/orderList";
        }
    }
    window.onload = ajaxHotProductList(1);
</script>
<script>

    $(window).scroll(function () {
        var scrollTop = $(this).scrollTop();
        var scrollHeight = $(document).height();
        var windowHeight = $(this).height();
        if (scrollTop + windowHeight >= scrollHeight) {
            if (currLength >= 10) {
                console.log("this is going next page:" + currPage);
                //请求数据
                ajaxHotProductList(currPage);
                currPage++;
            }
        }
    });

</script>
</html>
