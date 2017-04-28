<%--
  Created by IntelliJ IDEA.
  User: zhangwen
  Date: 2016/9/20
  Time: 20:15
  Content:商品中心
--%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/commen.jsp" %>
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
    <title>臻品</title>
    <c:set var="resourceUrl" value="http://www.lepluslife.com/resource"></c:set>
    <c:set var="wxRootUrl" value="http://www.lepluslife.com"></c:set>
    <link rel="stylesheet" href="${resource}/product/index/css/reset.css">
    <link rel="stylesheet" href="${resource}/product/index/css/bastMain2.css">
    <link rel="stylesheet" href="${commonResource}/css/swiper.min.css">
    <link rel="stylesheet" href="${resourceUrl}/css/paySuccess.css">
    <link rel="stylesheet" href="${resourceUrl}/css/payFailed.css">
</head>

<body>
<c:if test="${orderId!=null}">
    <div id="mask-failed">
        <div class="mask-top"></div>
        <p class="mask-ttl">您本次付款失败了<br>请在15分钟内完成付款</p>

        <div class="mask-btn">
            <div><a style="color: #fff;width: 100%;height: 100%;display: block"
                    href="/front/order/weixin/confirmOrder?orderId=${orderId}">重新付款</a></div>
            <div onclick="hideModel('mask-failed')">继续逛逛</div>
        </div>
    </div>
</c:if>
<c:if test="${truePrice!=null}">
    <div id="mask-success">
        <div class="mask-top">
            <p>￥<font><fmt:formatNumber type="number"
                                        value="${payBackScore/100}"
                                        pattern="0.00"
                                        maxFractionDigits="2"/></font></p>

            <p>您在乐+商城的消费获得<font><fmt:formatNumber type="number"
                                                  value="${payBackScore/100}"
                                                  pattern="0.00"
                                                  maxFractionDigits="2"/></font>元鼓励金</p>

            <p>累计鼓励金：￥<font><fmt:formatNumber type="number"
                                              value="${totalScore/100}"
                                              pattern="0.00"
                                              maxFractionDigits="2"/></font></p>
        </div>
        <div class="mask-btn">
            <div><a style="color: #fff;width: 100%;height: 100%;display: block"
                    href="/front/order/weixin/orderList">查看订单</a></div>
            <div onclick="hideModel('mask-success')">继续逛逛</div>
        </div>
    </div>
</c:if>
<section>
    <div id="tabs" class="tabs">
        <div class="swiper-wrapper">
            <div class="swiper-slide active" id="productType_0">推荐</div>
            <c:forEach items="${typeList}" var="type">
                <div class="swiper-slide" id="productType_${type.id}">${type.type}</div>
            </c:forEach>
        </div>
    </div>
    <div class="blank"></div>
    <div id="tabs-container" class="swiper-container">
        <div class="swiper-wrapper">
            <div class="swiper-slide">
                <div class="content-slide">
                    <div class="news-list p0">
                        <section class="banner">
                            <div class="swiper-container swiper-banner">
                                <div class="swiper-wrapper" id="banner">

                                </div>
                                <!-- Add Pagination -->
                                <div class="swiper-pagination"></div>
                            </div>
                        </section>
                        <section class="myJf">
                            <div>我的金币：<span><fmt:formatNumber type="number"
                                                              value="${scoreC.score/100}"
                                                              pattern="0.00"
                                                              maxFractionDigits="2"/>金币</span></div>
                            <div class="helpForJf">
                                <div>如何赚金币</div>
                                <div>
                                    <img src="${resourceUrl}/frontRes/product/hotIndex/img/qusetion.png"
                                         alt="">
                                </div>
                            </div>
                        </section>
                        <%--<section class="top">--%>
                        <%--<img src="${resourceUrl}/frontRes/product/hotIndex/img/line.png" alt="">--%>

                        <%--<p>主打爆款</p>--%>

                        <%--<div style="float: right;margin-top: -6%;font-size: 14px;color: #999999;margin-right: 3%"--%>
                        <%--onclick="goMenu(2)">--%>
                        <%--<img style="width: 10%;position: absolute;right: 0;left:87%;margin-top: 0.8%;"--%>
                        <%--src="${resourceUrl}/frontRes/product/hotIndex/img/index_all.png"--%>
                        <%--alt=""/>--%>
                        <%--</div>--%>
                        <%--</section>--%>
                        <%--<c:if test="${product != null}">--%>
                        <%--<section class="hotGoods" style="margin-top: -2px;"--%>
                        <%--onclick="goHotDetail(${product.id})">--%>
                        <%--<div>--%>
                        <%--<img src="${product.thumb}" alt="">--%>
                        <%--</div>--%>

                        <%--<div></div>--%>
                        <%--<div>${product.name}</div>--%>
                        <%--<div><span--%>
                        <%--style="color:#333;font-size: 15px;margin-right: -2px"><fmt:formatNumber--%>
                        <%--type="number"--%>
                        <%--value="${product.minPrice/100}"--%>
                        <%--pattern="0.00"--%>
                        <%--maxFractionDigits="2"/>元</span> + <span--%>
                        <%--style="margin-left: -2px;font-size: 14px;color: #fb991a;"><fmt:formatNumber--%>
                        <%--type="number"--%>
                        <%--value="${product.minScore/100}"--%>
                        <%--pattern="0.00"--%>
                        <%--maxFractionDigits="2"/>金币</span>--%>
                        <%--</div>--%>
                        <%--<div>--%>
                        <%--<span class="line-down"--%>
                        <%--style="margin-top: -5px;font-size: 12px;color: #999;">市场价<fmt:formatNumber--%>
                        <%--type="number"--%>
                        <%--value="${product.price/100}"--%>
                        <%--pattern="0.00"--%>
                        <%--maxFractionDigits="2"/>元</span>--%>
                        <%--</div>--%>
                        <%--<div class="w-buyNow">--%>
                        <%--<img src="${resourceUrl}/frontRes/product/hotIndex/img/buyNow.png"--%>
                        <%--alt="">--%>

                        <%--<p>仅剩${product.repository}份</p>--%>
                        <%--</div>--%>
                        <%--</section>--%>
                        <%--</c:if>--%>

                        <section class="top" style="margin-bottom: 10px;">
                            <img src="${resourceUrl}/frontRes/product/hotIndex/img/line.png" alt="">

                            <p>臻品推荐</p>
                        </section>
                        <section class="secondsKill" id="typeContent0">
                        </section>
                    </div>
                </div>
            </div>
            <div class="swiper-slide">
                <div class="content-slide">
                    <div class="news-list p1">
                        <section class="secondsKill" style="margin-top: 10px" id="typeContent1">
                        </section>
                    </div>
                </div>
            </div>
            <div class="swiper-slide">
                <div class="content-slide">
                    <div class="news-list p1">
                        <section class="secondsKill" style="margin-top: 10px" id="typeContent2">
                        </section>
                    </div>
                </div>
            </div>
            <div class="swiper-slide">
                <div class="content-slide">
                    <div class="news-list p2">
                        <section class="secondsKill" style="margin-top: 10px" id="typeContent3">
                        </section>
                    </div>
                </div>
            </div>
            <div class="swiper-slide">
                <div class="content-slide">
                    <div class="news-list p3">
                        <section class="secondsKill" style="margin-top: 10px" id="typeContent4">
                        </section>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
<section class="shade-layer" style="display: none">
    <div class="logo">
        <img src="${resourceUrl}/frontRes/product/hotIndex/img/logo.png" alt="">
    </div>
    <div>
        <div>
            <p>1.合作商家消费赚金币</p>

            <p>您在乐加合作商家内消费，并且扫描乐加微信收款牌付款后，即可得到金币和鼓励金奖励，花的越多，得的越多。</p>

            <p>2.签到赚金币</p>

            <p>参加乐加公众号的签到活动，即可领取金币和鼓励金奖励，连续签到还有更多奖励！</p>

            <p>3.评论抽奖赢金币</p>

            <p>在乐加生活推送的图文消息下留言，即有机会获得百元金币鼓励金大礼!</p>

            <p>4.您每天最多获得20金币</p>
        </div>
    </div>
    <div class="money-bottom">
        <img src="${resourceUrl}/frontRes/product/hotIndex/img/money-bottom.png" alt="">
    </div>
    <div class="layerClose">
        <img src="${resourceUrl}/frontRes/product/hotIndex/img/close.png" alt="">
    </div>
</section>
<section class="footer">
    <div>
        <div>
            <img src="${resource}/gold/img/zhenpin2.png" alt="">
        </div>
        <p>臻品</p>
    </div>
    <div onclick="goMenu(2)">
        <div>
            <img src="${resource}/gold/img/miaosha1.png" alt="">
        </div>
        <p>秒杀</p>
    </div>
    <div onclick="goMenu(3)">
        <div>
            <img src="${resource}/gold/img/gouwuche1.png" alt="">
        </div>
        <p>购物车</p>
    </div>
    <div onclick="goMenu(4)">
        <div>
            <img src="${resource}/gold/img/dingdan1.png" alt="">
        </div>
        <p>订单</p>
    </div>
</section>
</body>
<script src="${resourceUrl}/js/jquery-2.0.3.min.js"></script>
<script src="${resourceUrl}/frontRes/js/swiper.min.js"></script>
<script src="${resourceUrl}/frontRes/js/layer.js"></script>
<!--style for html-->
<script>
    var productType = 0, typeCurrPage = [2, 1, 1, 1, 1], typeCurrLength = [0, 0, 0, 0, 0];
    var hasFirst = [0, 0, 0, 0, 0], typeContent = ['', '', '', '', ''], firstTypeLength = [0, 0, 0,
                                                                                           0, 0];
    $(".only").css("margin-top", -$(".only").height() - 25 + "px");
    $(".only-small").css("margin-top", -$(".only-small").height() - 12 + "px");
    $(".secondsKill").css("margin-bottom", $(".footer").height() + 13 + "px");
    $(".blank").css("height", $(".tabs").height() + "px");
    window.onload = function () {
        //加载臻品轮播图
        $.ajax({
                   type: "get",
                   url: "/app/banner/home/2",
                   success: function (data) {
                       var list = data.data, i = 0, banner = $("#banner"), content = "";

                       for (i; i < list.length; i++) {
                           var image = '<div class="swiper-slide">' +
                                       '<input type="hidden" value="' + list[i].merchantId + '"/>' +
                                       '<input type="hidden" value="' + list[i].productId + '"/>' +
                                       '<input type="hidden" value="' + list[i].url + '"/>' +
                                       '<input type="hidden" value="' + list[i].afterType + '"/>' +
                                       '<img onclick="clickPic(this)" src="' + list[i].picture
                                       + '" alt=""></div>';
                           content += image;
                       }
                       banner.html(content);
                       bannerSwiper();
                   }
               });

        //分页获取首页臻品列表
        ajaxProductList("typeContent0", 0, 1);

        var mySwiper1 = new Swiper('#tabs', {
            freeMode: true,
            slidesPerView: 'auto'
        });

        var tabsSwiper = new Swiper('#tabs-container', {
            speed: 500,
            onSlideChangeStart: function () {
                $(".tabs .active").removeClass('active');
                $(".tabs > div > div").eq(tabsSwiper.activeIndex).addClass('active');
                var swiperPage = tabsSwiper.activeIndex;
                var h;
                ($(".p" + swiperPage).height() < $(window).height()) ? h =
                    $(window).height()
                    - $(".tabs").height() : h =
                    $(".p"
                      + swiperPage).height();
                $(".pHeight").css("height", h + "px");
                $('html,body').animate({scrollTop: '0px'}, 100);
                productType = $(".tabs .active").attr('id').split('_')[1];
                var typeContent = "typeContent" + productType;
                //将其他分类隐藏
                $(".secondsKill").html("");
                //请求数据
                typeCurrPage[productType] = 1;
                typeCurrLength[productType] = 0;
                ajaxProductList(typeContent, productType, 1);
                typeCurrPage[productType]++;
            }
        });
        $(".tabs > div > div").on('touchstart mousedown', function (e) {
            e.preventDefault();
            $(".tabs .active").removeClass('active');
            $(this).addClass('active');
            tabsSwiper.slideTo($(this).index());
//            $('html,body').animate({scrollTop: '0px'}, 800);

        });
        $(".tabs > div > div").click(function (e) {
            e.preventDefault();
//            $('html,body').animate({scrollTop: '0px'}, 800);

        });
    };
</script>

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
    function bannerSwiper() {
        var swiper = new Swiper('.swiper-banner', {
            pagination: '.swiper-pagination',
            paginationClickable: true,
            spaceBetween: 30,
            centeredSlides: true,
            autoplay: 3500,
            autoplayDisableOnInteraction: false
        });
    }

    function goHotDetail(id) { //go爆品详情页
        location.href = "/front/product/weixin/limitDetail?productId=" + id;
    }
    function goProductDetail(productId) { //go爆品详情页
        location.href = "/weixin/product/" + productId;
    }
    function goMenu(curIndex) { //go其他菜单页
        if (curIndex == 2) {
            location.href = "/front/gold/weixin";
        } else if (curIndex == 3) {
            location.href = "/weixin/cart";
        } else if (curIndex == 4) {
            location.href = "/front/order/weixin/orderList";
        }
    }

    function hideModel(idName) {
        $("#" + idName).css('display', 'none');
    }

    function ajaxProductList(contentId, typeId, page) {
        var mainContent = $("#" + contentId);
        if (page == 1 && hasFirst[typeId] == 1) {
            mainContent.html(typeContent[typeId]);
            typeCurrLength[typeId] = firstTypeLength[typeId];
        } else {
            $.ajax({
                       type: "get",
                       url: "/shop/productList?typeId=" + typeId + "&page=" + page,
                       success: function (data) {
                           var list = data.data, content = '';
                           typeCurrLength[typeId] = 0;
                           if (list != null) {
                               typeCurrLength[typeId] = list.length;
                               for (var i = 0; i < list.length; i++) {
                                   var currP = '<div onclick="goProductDetail(' + list[i].id
                                               + ')"><div><img height="125px" src="'
                                               + list[i].picture
                                               + '" alt=""></div><div>' + list[i].name
                                               + '</div><div><span style="font-size: 16px;color:#333;margin-right: -3px;">'
                                               + toDecimal(list[i].minPrice / 100)
                                               + '元</span> + <span style="margin-left: -3px">'
                                               + toDecimal(list[i].minScore / 100)
                                               + '金币</span></div><div class="line-down" style="padding-left: 7%;color: #bebebe;">市场价'
                                               + toDecimal(list[i].price / 100)
                                               + '元</div> <div> <div>' + list[i].saleNumber
                                               + '份已售</div><div>' + (list[i].postage == 0
                                           ? '包邮' : '') + '</div></div></div>';
                                   content += currP;
                               }
                               mainContent.html(mainContent.html() + content);
                           }
                           if (page == 1) {
                               hasFirst[typeId] = 1;
                               firstTypeLength[typeId] = list != null ? list.length : 0;
                               typeContent[typeId] = content;
                           }
                       }
                   });
        }
    }
    function clickPic(o) {
        var afterType = $(o).prev().val();
        if (afterType == 1) {
            var url = $(o).prev().prev().val();
            window.location.href = "http://" + url;
        } else if (afterType == 2) {
            var productId = $(o).prev().prev().prev().val();
            location.href = "/weixin/product/" + productId;
        } else if (afterType == 3) {
            var merchantId = $(o).prev().prev().prev().prev().val();
            location.href = "merchant/info/" + merchantId + "?status=0";
        }
    }
</script>
<!--layer-->
<script>
    $(".helpForJf").click(function (e) {
        $(".shade-layer").show();
        layer.open({
                       type: 1,
                       area: ['85%', '395px'], //宽高
                       content: $(".shade-layer"),
                       title: false,
                       closeBtn: 0,
                       scrollbar: false
//        shadeClose:true
                   });
    });
    $(".layerClose").click(function (e) {
        layer.closeAll(); //疯狂模式，关闭所有层
        $(".shade-layer").hide();
    });

    $(window).scroll(function () {
        var scrollTop = $(this).scrollTop();
        var scrollHeight = $(document).height();
        var windowHeight = $(this).height();
        if (scrollTop + windowHeight >= scrollHeight) {
            if (typeCurrLength[productType] >= 10) {
                console.log("this is in" + productType);
                var typeContent = "typeContent" + productType;
                //请求数据
                ajaxProductList(typeContent, productType, typeCurrPage[productType]);
                typeCurrPage[productType]++;
            }
        }
    });

</script>

</html>
