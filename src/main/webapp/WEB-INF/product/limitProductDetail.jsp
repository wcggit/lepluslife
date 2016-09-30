<%--
  Created by IntelliJ IDEA.
  User: zhangwen
  Date: 2016/9/19
  Time: 23:05
  Content:爆品详情页
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--<%@include file="/WEB-INF/commen.jsp" %>--%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="format-detection" content="telephone=no">
    <meta name="format-detection" content="telephone=yes"/>
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="black"/>
    <meta name="viewport"
          content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
    <title></title>
    <c:set var="resourceUrl" value="http://www.lepluslife.com/resource"></c:set>
    <c:set var="wxRootUrl" value="http://www.lepluslife.com"></c:set>
    <link rel="stylesheet" href="${resourceUrl}/frontRes/css/reset.css">
    <link rel="stylesheet" href="${resourceUrl}/frontRes/css/swiper.min.css">
    <link rel="stylesheet" href="${resourceUrl}/frontRes/product/hotDetail/css/hot.css">
    <link rel="stylesheet" href="${resourceUrl}/frontRes/css/tanChuang.css">
</head>
<style>
    .swiper-pagination-bullet {
        width:6px !important;
        height:6px !important;
    }
</style>
<body>
<section class="banner">
    <div class="swiper-container swiper-banner">
        <div class="swiper-wrapper">
            <c:forEach items="${scrollList}" var="scroll">
                <div class="swiper-slide">
                    <img src="${scroll.picture}">
                </div>
            </c:forEach>
        </div>
        <div class="swiper-pagination"></div>
    </div>
</section>
<section class="title">
    <p>${product.name}</p>

    <p><span id="minPrice"></span>+<span id="minScore"></span></p>

    <p class="type"></p>
</section>
<section class="norms">
    <div>
        <div>规格</div>
        <c:if test="${product.buyLimit != 0}">
            <div>限购${product.buyLimit}份</div>
        </c:if>
    </div>
    <div class="norms-list">
        <%--规格列表--%>
    </div>
</section>


<section class="num">
    <div>数量</div>
    <div class="num-num">
        <div id="btnCut1" style="border: 1px solid #e1e1e1;border-right: 1px solid #999"><img class="w-jian" style="width: 30%;" src="${resourceUrl}/frontRes/product/hotIndex/img/4.png" alt=""></div>
        <div>
            <input id="num1" type="number" value="1"/>
        </div>
        <div id="btnAdd1"><img class="w-jia" style="width: 30%" src="${resourceUrl}/frontRes/product/hotIndex/img/2.png" alt=""></div>
    </div>

</section>
<section class="imgText">
    <p>↓下拉加载图文详情↓</p>

    <div id="productDetail">
        <%-- < img src="" alt=""> 订单详情--%>
    </div>
</section>
<section class="blank"></section>
<section class="footer cantPay">
    <p id="buySubmit">立即购买</p>
</section>

<%--弹窗--%>
<section class="shade-layer" style="display: none">
    <p id="warningInput"></p>

    <div>
        <div class="layerClose">取消</div>
        <div class="yes"></div>
    </div>
</section>
</body>
<script src="${resourceUrl}/js/jquery-2.0.3.min.js"></script>
<script src="${resourceUrl}/frontRes/js/swiper.min.js"></script>
<script src="${resourceUrl}/frontRes/js/layer.js"></script>
<%--------以下是数量切换的逻辑结构--------%>
<script>
    var u = navigator.userAgent;
    if (u.indexOf('Android') > -1 || u.indexOf('Linux') > -1) {//安卓手机
        $(".num-num input").css("margin-top","-0.5px");
    } else if (u.indexOf('iPhone') > -1) {//苹果手机
    } else if (u.indexOf('Windows Phone') > -1) {//winphone手机
    }
</script>
<script>
    var num1 = $('#num1'), btnCut1 = $('#btnCut1'), btnAdd1 = $('#btnAdd1'), minPrice = $('#minPrice'), minScore = $('#minScore');
    var maxNum = eval('${product.buyLimit}');
    if (maxNum == 0) {
        maxNum = 10000;
    }
    //数量切换
    //可买数量的最大值和最小值判断
    function judgeFun1() {
        if (num1.val() >= eval(maxNum)) {
            num1.val(maxNum);
            $(".w-jia").attr("src","${resourceUrl}/frontRes/product/hotIndex/img/3.png");
            $("#btnAdd1").attr("style","border: 1px solid #e1e1e1;border-left: 1px solid #999");
            $("#btnCut1").attr("style","border: 1px solid #999999;");
        } else if (num1.val() <= 0) {
            num1.val(1);
            $(".w-jian").attr("src","${resourceUrl}/frontRes/product/hotIndex/img/4.png");
            $("#btnCut1").attr("style","border: 1px solid #e1e1e1;border-right: 1px solid #999");
            $("#btnAdd1").attr("style","border: 1px solid #999999;");
        }else {
            $(".w-jian").attr("src","${resourceUrl}/frontRes/product/hotIndex/img/1.png");
            $(".w-jia").attr("src","${resourceUrl}/frontRes/product/hotIndex/img/2.png");
            $("#btnAdd1").attr("style","border: 1px solid #999999;");
            $("#btnCut1").attr("style","border: 1px solid #999999;");
        }
    }
    //点击事件
    function allClick() {
        var pricePre = $(".check").prev().prev().val();
        var scorePre = $(".check").prev().val();
        minPrice.text("￥" + toDecimal(eval(pricePre) * eval(num1.val())));
        minScore.text(eval(scorePre) * eval(num1.val()) + "积分");
    }

    //数量点击
    btnCut1.bind('touchstart', function () {
        if (num1.val() != 0) {
            num1.val(eval(num1.val()) - 1);
            judgeFun1();
            allClick();
        }
    });
    btnAdd1.bind('touchstart', function () {
        num1.val(eval(num1.val()) + 1);
        judgeFun1();
        allClick();
    });
    //输入框改变
    num1.bind('keyup', function () {
        judgeFun1();
        allClick();
    });
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
<%--------轮播加载及点击生成订单及弹窗--------%>
<script>
    var specList = ${specList}, detailList = ${detailList}, canBuy = '${canBuy}', repository = 0, checkedSpec = 0, warnType = 1;
    var swiper = new Swiper('.swiper-banner', {
        pagination: '.swiper-pagination',
        paginationClickable: true,
        spaceBetween: 30,
        centeredSlides: true,
        autoplay: 3500,
        autoplayDisableOnInteraction: false
    });
    function createOrder() {
        $(".footer").attr("onclick", "");
        var productId = '${product.id}', specId = $(".check").prev().prev().prev().val(), buyNumber = eval(num1.val());
        if (maxNum != 10000 && buyNumber > maxNum) {
            alert("最多购买数量为:" + maxNum);
            $(".footer").attr('onclick', 'createOrder()');
            return
        }

        $.ajax({
                   type: "post",
                   url: "/front/order/weixin/createHotOrder",
                   data: {productId: productId, specId: specId, buyNumber: buyNumber},
                   success: function (data) {
                       if (data.status == 200) {
                           location.href = "/front/order/weixin/confirmOrder/" + data.data.id;
                           //alert("订单ID为：" + data.data.id);
                       } else if (data.status == 5002) {
                           warnType = 1;
                           showTanChuang();
                       } else if (data.status == 5003) {
                           warnType = 2;
                           $(".footer").attr('onclick', 'createOrder()');
                           showTanChuang();
                       }
                   }
               });

//        console.log(productId + "========" + specId + "===========" + buyNumber);
    }

    function showTanChuang() {
        if (warnType == 1) {
            $("#warningInput").html('您有该商品的未支付订单！请到订单列表处理');
            $(".yes").html('查看');
        } else if (warnType == 2) {
            $("#warningInput").html('抱歉,该规格已无库存,请选择其他规格');
            $(".yes").html('知道了');
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
        }
    });


</script>
<%--进入页面就加载的数据--%>
<script>
    $(".type").html($(".check").html());
    $("#minPrice").html("￥" + $(".check").prev().prev().val());
    $("#minScore").html($(".check").prev().val() + "积分");
    for (var i = 0; i < specList.length; i++) {
//        console.log('------------');
        if (specList[i].repository > 0) {
            repository += specList[i].repository;
            if (checkedSpec == 0) {
                $(".norms-list").append(
                        $("<input>").attr("type", "hidden").attr("value", specList[i].id)
                ).append(
                        $("<input>").attr("type", "hidden").attr("value",
                                                                 specList[i].minPrice / 100)
                ).append(
                        $("<input>").attr("type", "hidden").attr("value", specList[i].minScore)
                ).append(
                        $("<div></div>").attr("class", "check").html(specList[i].specDetail)
                );
                $(".type").html($(".check").html());
                $("#minPrice").html("￥" + toDecimal($(".check").prev().prev().val()));
                $("#minScore").html($(".check").prev().val() + "积分");
                checkedSpec = 1;
            } else {
                $(".norms-list").append(
                        $("<input>").attr("type", "hidden").attr("value", specList[i].id)
                ).append(
                        $("<input>").attr("type", "hidden").attr("value",
                                                                 specList[i].minPrice / 100)
                ).append(
                        $("<input>").attr("type", "hidden").attr("value", specList[i].minScore)
                ).append(
                        $("<div></div>").html(specList[i].specDetail)
                );
            }
        } else {
            $(".norms-list").append(
                    $("<input>").attr("type", "hidden").attr("value", specList[i].id)
            ).append(
                    $("<input>").attr("type", "hidden").attr("value", specList[i].minPrice / 100)
            ).append(
                    $("<input>").attr("type", "hidden").attr("value", specList[i].minScore)
            ).append(
                    $("<div></div>").attr("class", "sellout").html(specList[i].specDetail)
            );
        }
    }
    if (repository == 0) {
        $("#buySubmit").html('已售罄');
        $("#minPrice").html("￥" + toDecimal(${product.minPrice/100}));
        $("#minScore").html(${product.minScore} +"积分");
        btnCut1.unbind('touchstart');
        btnAdd1.unbind('touchstart');
        num1.unbind('keyup');
    } else if (canBuy == 0) {
        $("#buySubmit").html('您已兑换过，无法继续兑换');
        $("#minPrice").html("￥" + toDecimal(${product.minPrice/100}));
        $("#minScore").html(${product.minScore} +"积分");
    } else {
        $(".footer").attr('class', 'footer');
        $(".footer").attr('onclick', 'createOrder()');
    }

    $(".blank").css("height", $(".footer").height() + 26 + "px");
    $(".norms-list > div").click(function () {
        if (!$(this).hasClass("sellout")) {
            $(".norms-list > div").removeClass("check");
            $(this).addClass("check");
            $(".type").html($(".check").html());
            $("#minPrice").html("￥" + toDecimal($(".check").prev().prev().val()));
            $("#minScore").html($(".check").prev().val() + "积分");
        }
    });

    var nowLoadImg = 0;
    var loadLength = detailList.length;
    if ($(document).height() == $(this).height()) {
        loadImg(nowLoadImg);
    }
    $(window).scroll(function () {
        var scrollTop = $(this).scrollTop();
        var scrollHeight = $(document).height();
        var windowHeight = $(this).height();
        if (scrollTop + windowHeight >= scrollHeight) {
            if (nowLoadImg < loadLength) {
                loadImg(nowLoadImg);
                loadImg(nowLoadImg);
            }
        }
    });
    function loadImg(i) {
        if (i < loadLength) {
            $("#productDetail").append(
                    $("<div></div>").append(
                            $("<img>").attr("src", detailList[i].picture)
                    )
            );
            nowLoadImg++;
        } else {
            $("#productDetail").append(
                    $("<p></p >").html("已加载所有图文")
            );
            return false;
        }
    }
</script>
</html>
