<%--
  Created by IntelliJ IDEA.
  User: wcg
  Date: 16/3/18
  Time: 下午7:05
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
    <link type="text/css" rel="stylesheet" href="${resourceUrl}/css/swiper-3.3.1.min.css">
    <!--App自定义的css-->
    <link rel="stylesheet" type="text/css" href="${resourceUrl}/css/productDetail.css"/>
    <link rel="stylesheet" href="${resourceUrl}/frontRes/css/tanChuang.css">
    <script type="text/javascript" src="${resourceUrl}/js/swiper-3.3.1.min.js"></script>
    <script src="${resourceUrl}/js/jquery-2.0.3.min.js"></script>


</head>

<body>
<nav class="mui-bar mui-bar-tab">
			<span class="mui-pull-left">
                <span class="mui-icon iconfont" onclick="goCartPage()">&#xe607;<span
                        class="mui-badge cartNum" id="cart-number"></span></span>
			</span>
    <span class="mui-tab-label" id="cart">加入购物车</span>
    <span class="mui-tab-label" id="buy">立即购买</span>
</nav>
<script>

</script>
<!--下拉刷新容器-->
<div id="pullrefresh" class="mui-content mui-scroll-wrapper">
    <div class="mui-scroll">
        <!--内容部分-->
        <div class="mui-content">
            <div class="swiper-container swiper-container-horizontal" id="swiper-container2">
                <!--列表-->
                <div class="swiper-wrapper">
                    <c:forEach items="${product.scrollPictures}" var="scrollPicture">
                        <div class="swiper-slide blue-slide swiper-slide-active"
                             style="background: url('${scrollPicture.picture}') no-repeat;
                                     background-size: 100%;">
                        </div>
                    </c:forEach>
                </div>
                <div class="swiper-pagination swiper-pagination-fraction"
                     id="swiper-pagination2"></div>
            </div>

            <!--详情-->
            <div class="page_ttl">
                <p class="ttl_name">${product.name}</p>

                <p class="ttl_practice">${product.description}</p>
                <c:if test="${product.minPrice != product.price}">
                    <p class="ttl_price">￥<font id="min_price"><fmt:formatNumber type="number"
                                                                                 value="${product.minPrice/100}"
                                                                                 pattern="0.00"
                                                                                 maxFractionDigits="2"/></font>
                        ~
                        ￥<font
                                class="total_price"><fmt:formatNumber type="number"
                                                                      value="${product.price/100}"
                                                                      pattern="0.00"
                                                                      maxFractionDigits="2"/></font>
                    </p>
                </c:if>
                <c:if test="${product.minPrice == product.price}">
                    <p class="ttl_price">￥<font class="total_price"><fmt:formatNumber type="number"
                                                                                      value="${product.price/100}"
                                                                                      pattern="0.00"
                                                                                      maxFractionDigits="2"/></font>
                    </p>
                </c:if>
                <p class="ttl_main">
                    <c:if test="${product.postage == 0}">
                        <span>包邮</span>
                    </c:if>
                    <c:if test="${product.postage != 0}">
                        <span>满<font><fmt:formatNumber type="number"
                                                       value="${product.freePrice/100}"
                                                       pattern="0.00"
                                                       maxFractionDigits="2"/></font>包邮</span>
                    </c:if>
                </p>
            </div>
            <div class="page_more_btn">+规格数量选择</div>

            <!--更多详情-->
            <div class="page_more">
                <div class="more_main">
                    <div class="main_top">
                        <div class="top_left">
                            <span><img id="specPicture" src="${product.thumb}"
                                       alt=""></span>
                        </div>
                        <div class="top_right">
                            <p class="right_name">${product.name}</p>

                            <p class="right_price"><font id="moneyNum1">￥<font
                                    class="moneyNum1">${product.minPrice/100}</font> ~
                            </font>￥<font class="moneyNum2">${product.price/100}</font>
                                <input type="hidden" value="${product.productSpecs[0].price/100}"
                                       id="price-hidden"/>
                                <input type="hidden"
                                       value="${product.productSpecs[0].price-product.productSpecs[0].minPrice}"
                                       id="score-hidden"/>
                            </p>

                            <p class="right_ttl" id="availablePoint">请选择规格数量</p>
                        </div>
                    </div>
                    <ul class="main_chose">
                        <li>
                            <p class="chose_left">款式：</p>

                            <p class="chose_right">
                                <c:forEach items="${product.productSpecs}" var="productSpec"
                                           varStatus="index">

                                        <span class="initClass">${productSpec.specDetail}
                                        <input
                                                type="hidden" class="id-hidden"
                                                value="${productSpec.id}"/>
                                        <input
                                                type="hidden" class="repository-hidden"
                                                value="${productSpec.repository}"/>
                                         <input
                                                 type="hidden" class="picture-hidden"
                                                 value="${productSpec.picture}"/>
                                         <input
                                                 type="hidden" class="price-hidden"
                                                 value="${productSpec.price}"/>
                                        <input
                                                type="hidden" class="minPrice-hidden"
                                                value="${productSpec.minPrice}"/></span>

                                </c:forEach>
                            </p>
                        </li>
                        <li>
                            <p class="chose_left">数量：</p>

                            <p class="chose_right right_all">
                                <button class="btnCut1"></button>
                                <input type="number" value="1" class="num1"/>
                                <button class="btnAdd1"></button>
                            </p>
                            <p class="chose_kucun">库存仅剩：<font
                                    class="maxNum">${product.productSpecs[0].repository}</font></p>
                        </li>
                    </ul>
                    <span class="main_close"></span>
                </div>
            </div>
        </div>
        <ul class="mui-table-view mui-table-view-chevron detail-hid"></ul>
    </div>
    <form action="/weixin/order/confirm" method="post" id="form">
        <input type="hidden" id="productId" name="productId" value="${product.id}">
        <input type="hidden" id="productNum" name="productNum">
        <input type="hidden" id="totalPrice" name="totalPrice">
        <input type="hidden" id="totalScore" name="totalScore">
        <input type="hidden" id="productSpec" name="productSpec">
    </form>
</div>

<!--提示-->
<div class="xzgg">
    <div>
        <p>请选择规格</p>

        <p>知道了</p>
    </div>

</div>
<%--弹窗--%>
<section class="shade-layer" style="display: none">
    <p id="warningInput"></p>

    <div>
        <div class="layerClose">取消</div>
        <div class="yes"></div>
    </div>
</section>

</body>
<script type="text/javascript" src="${resourceUrl}/js/mui.min.js"></script>
<script type="text/javascript" src="${resourceUrl}/js/dropload.min.js"></script>
<script type="text/javascript" src="${resourceUrl}/js/prodect_detail.js"></script>
<script src="${resourceUrl}/frontRes/js/layer.js"></script>
<script>

    var tanState = 0, warnType = 1;
    $(function () {
        $(".total_price").text(toDecimal($(".total_price").text()));
        $("#min_price").text(toDecimal($("#min_price").text()));
        $(".moneyNum1").text(toDecimal($(".moneyNum1").text()));
        $(".moneyNum2").text(toDecimal($(".moneyNum2").text()));
    });

    $.ajax({
               type: "get",
               url: "/weixin/cart/cartNumber",
               contentType: "application/json",
               success: function (data) {
                   if (data.msg != 0) {
                       $("#cart-number").attr("style", "display:block")
                       $("#cart-number").text(data.msg);
                   } else {
                       $("#cart-number").attr("style", "display:none")
                   }
               }
           });
    //下拉刷新
    mui.init({
                 pullRefresh: {
                     container: '#pullrefresh',
                     up: {
                         contentrefresh: '正在加载...',
                         callback: pullupRefresh
                     }
                 }
             });

    /**
     * 上拉加载具体业务实现
     */
    var count = 0;
    var productDetails = eval('${productdetails}');
    var imgLength = productDetails.length;
    var shangshu = imgLength / 3;
    var yushu = imgLength % 3;
    function pullupRefresh() {
        setTimeout(function () {

            mui('#pullrefresh').pullRefresh().endPullupToRefresh((++count > shangshu)); //参数为true代表没有更多数据了。
            var table = document.body.querySelector('.mui-table-view');
            var cells = document.body.querySelectorAll('.detailImg');
            table.className = "mui-table-view mui-table-view-chevron detail-show";
            if (count <= shangshu) {
                for (var i = cells.length, len = i + 3; i < len; i++) {
                    var li = document.createElement('li');
                    li.className = 'detailImg';
                    li.innerHTML = '<img src="' + productDetails[i].picture + '" alt="" />';
                    table.appendChild(li);
                }
            } else {
                for (var i = cells.length, len = i + yushu; i < len; i++) {
                    var li = document.createElement('li');
                    li.className = 'detailImg';
                    li.innerHTML = '<img src="' + productDetails[i].picture + '" alt="" />';
                    table.appendChild(li);
                }
            }

        }, 1500);
    }
    if (mui.os.plus) {
        mui.plusReady(function () {
            setTimeout(function () {
                mui('#pullrefresh').pullRefresh().pullupLoading();
                $('.mui-scroll').css('transform', 'translate3d(0px, 0px, 0px)');
            }, 0);
        });
    } else {
        mui.ready(function () {
            mui('#pullrefresh').pullRefresh().pullupLoading();
            $('.mui-scroll').css('transform', 'translate3d(0px, 0px, 0px)');
        });
    }

    function buyNow() {
        $("#productNum").val($(".num1").val());
        var productNum = $("#productNum").val();
        if (productNum == 0) {
            alert("选择商品后才可以购买");
            return;
        }
        if ($(".maxNum").text() <= 0) {
            alert("库存不足,无法购买");
            return;
        }
        if ($(".num1").val() > 50) {
            alert("最多购买50件,批量购买请联系管理员");
            $(".num1").val(50);
            return;
        }
        var focusClass = $(".focusClass");
        if (focusClass.find(".id-hidden").val() == null) {
            xuanzheguige();
            return;
        }

        $("#buy").attr("onclick", "");
        var totalPrice = $("#price-hidden").val() * $(".num1").val();
        $("#totalPrice").val(parseInt(totalPrice * 100));
        var totalScore = $("#score-hidden").val() * $(".num1").val();
        $("#totalScore").val(totalScore);
        var productSpec = $(".focusClass").find(".id-hidden").val();
        $("#productSpec").val(productSpec);
        // $("#form").submit();
        $.ajax({
                   type: "post",
                   url: "/front/order/weixin/createCommonOrder",
                   data: {
                       productId: ${product.id},
                       specId: productSpec,
                       buyNumber: $("#productNum").val()
                   },
                   success: function (data) {
                       if (data.status == 200) {
                           location.href = "/front/order/weixin/confirmOrder?orderId=" + data.data.id;
                       } else if (data.status == 5004) {
                           warnType = 1;
                           showTanChuang();
                       } else if (data.status == 5003) {
                           warnType = 2;
                           showTanChuang();
                       } else if (data.status == 5001) {
                           warnType = 3;
                           showTanChuang();
                       }
                   }
               });
    }

    $("#buy").attr("onclick", "buyNow()");

    function showTanChuang() {
        if (warnType == 1) {
            $("#buy").attr("onclick", "buyNow()");
            $("#warningInput").html('您有该商品的未支付订单！请到订单列表处理');
            $(".yes").html('查看');
        } else if (warnType == 2) {
            $("#warningInput").html('抱歉,该规格已无库存,请选择其他规格');
            $(".yes").html('知道了');
        } else if (warnType == 3) {
            $("#warningInput").html('未支付订单过多,请支付后再下单');
            $(".yes").html('去处理');
        }
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
        if (warnType == 2) {
            location.reload(true);
        }
    });
    $(".yes").click(function (e) {
        if (warnType == 1) {
            location.href = "/front/order/weixin/orderList";
        } else if (warnType == 2) {
            location.reload(true);
        } else if (warnType == 3) {
            location.href = "/front/order/weixin/orderList";
        }
    });

    $("#cart").bind("tap", function () {
        if ($(".num1").val() <= 0) {
            alert("库存不足");
            return;
        }
        if ($(".num1").val() > 50) {
            alert("最多购买50件,批量购买请联系管理员");
            $(".num1").val(50);
            return;
        }
        var orderDetail = {};
        var productSpec = {};
        var product = {};
        var focusClass = $(".focusClass");
        if (focusClass.find(".id-hidden").val() == null) {

            xuanzheguige();
            return;
        }
        productSpec.id = $(".focusClass").find(".id-hidden").val();
        product.id =${product.id};
        orderDetail.productSpec = productSpec;
        orderDetail.product = product;
        orderDetail.productNumber = $(".num1").val();
        $.ajax({
                   type: "post",
                   url: "/weixin/cart",
                   contentType: "application/json",
                   data: JSON.stringify(orderDetail),
                   success: function (data) {
                       $("#cart-number").attr("style", "display:block")
                       $("#cart-number").text(data.msg);
                       if (data.data != null && data.data != "") {
                           alert(data.data);
                       }
                   }
               });
    });

    function goCartPage() {
        location.href = "/weixin/cart"
    }

    function xuanzheguige() {
        if (tanState == 1) {
            tanchaung();
            return;
        }
        //先出现
        tanState = 1;
        $('.mui-content .page_more').eq(0).fadeIn();
        $('.mui-table-view').eq(0).css('display', 'none');
        $('.mui-pull').eq(0).css('display', 'none');
        //再设置高度
        var elementTop = $('.mui-bar-tab').offset().top,
                elementTop2 = elementTop - eval($('.more_main').height());
        $('.more_main').css('bottom', '0px');
        $('.mui-scroll').css('transform', 'translate3d(0px, 0px, 0px)');
    }

    function tanchaung() {
        //  提示框
        $('.xzgg').css({'display': 'block'});
        $('.xzgg div').animate({'top': '80vw'});
        $('.xzgg div p:last-child').on('click', function () {
            $('.xzgg').css({'display': 'none'});
        })
    }

</script>
</html>
