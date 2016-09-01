<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/commen.jsp" %>
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
    <link rel="stylesheet" type="text/css" href="${resourceUrl}/css/index.css"/>
    <link rel="stylesheet" href="${resourceUrl}/css/paySuccess.css">
    <link rel="stylesheet" href="${resourceUrl}/css/payFailed.css">
    <script src="${resourceUrl}/js/jquery-2.0.3.min.js"></script>
</head>
<nav class="mui-bar  mui-bar-nav" style="background: #D62C2C;border-bottom: solid 1px #CBCACA;"
     id="type-page">
    <c:forEach items="${productTypes}" var="productType" varStatus="index">
        <c:if test="${index.count==1}">
            <a class="mui-tab-item">
                <span class="mui-tab-label focusCol">${productType.type}</span>
            </a>
        </c:if>
        <c:if test="${index.count!=1}">
            <a class="mui-tab-item">
                <span class="mui-tab-label initCol">${productType.type}</span>
            </a>
        </c:if>

    </c:forEach>
</nav>
<body>
<!--头部-->

<!--底部菜单-->
<!--下拉刷新容器-->
<div id="pullrefresh" class="mui-content mui-scroll-wrapper" style="background: #EFEFF4;">
    <div class="mui-scroll" id="scroll">
        <div id="ul-page">
            <ul class="mui-table-view mui-table-view-chevron detail-hid"></ul>
        </div>
    </div>
</div>

</body>
<%@include file="/WEB-INF/weixin/common/shopfoot.jsp" %>

<c:if test="${orderId!=null}">
    <div id="mask-failed">
        <div class="mask-top"></div>
        <p class="mask-ttl">您本次付款失败了<br>请在15分钟内完成付款</p>

        <div class="mask-btn">
            <div><a style="color: #fff;width: 100%;height: 100%;display: block"
                    href="/weixin/order/${orderId}">重新付款</a></div>
            <div>继续逛逛</div>
        </div>
    </div>
</c:if>
<c:if test="${truePrice!=null}">
    <div id="mask-success">
        <div class="mask-top">
            <p>￥<font>${payBackScore/100}</font></p>

            <p>您在乐+商城的消费获得<font>${payBackScore/100}</font>元红包</p>

            <p>累计红包：￥<font>${totalScore/100}</font></p>
        </div>
        <div class="mask-btn">
            <div><a style="color: #fff;width: 100%;height: 100%;display: block"
                    href="/weixin/orderDetail">查看订单</a></div>
            <div>继续逛逛</div>
        </div>
    </div>
</c:if>

<script type="text/javascript" src="${resourceUrl}/js/mui.min.js"></script>

<script type="text/javascript">


    var count = 1, imgLength = 10, url = '${wxRootUrl}/shop/product', indexVal = 1;
    var index = 0;

    //点击事件
    var spans = $('.mui-bar-nav .mui-tab-item');
    click(spans);
    function click(s) {
        s.each(function (i) {
            s.eq(i).bind('tap', function () {
                //重置下拉刷新
                mui('#pullrefresh').pullRefresh().refresh(true);
                var table = document.body.querySelector('.mui-table-view');
                table.innerHTML = "";
                var index = $(this).index() + 1;
                indexVal = index;
                count = 1;
                clear(s);
                imgLength = 10;
                index = 0;
                //initPage();
                $(this).find('span').removeClass('initCol').addClass('focusCol');
                mui('#pullrefresh').pullRefresh().pullupLoading();
                $('.mui-scroll').css({'transform': 'translate3d(0px, 0px, 0px)'});
            })
        });
    }

    $(function () {
        var page = window.localStorage.getItem('pages');

        var type = window.localStorage.getItem('types');
        if (page != null && page != "") {
            document.getElementById("ul-page").innerHTML = page;
            document.getElementById("type-page").innerHTML = "";
            document.getElementById("type-page").innerHTML = type;
            count = window.localStorage.getItem('count');
            indexVal = window.localStorage.getItem('indexVal');
            var spans = $('.mui-bar-nav .mui-tab-item');
            click(spans);
            initPage();

        }
    })

    //清除
    function clear(s) {
        s.each(function (i) {
            s.eq(i).find('span').removeClass('focusCol').addClass('initCol');
        })
    }
    document.title = "分类";

    mui.init({
         pullRefresh: {
             container: '#pullrefresh',
             up: {
                 contentrefresh: '正在加载...',
                 callback: pullupRefresh
             }
         }
     });

    function initPage() {
        mui.init({
             pullRefresh: {
                 container: '#pullrefresh',
                 up: {
                     contentrefresh: '正在加载...',
                     callback: pullupRefresh
                 }
             }
         });
    }
    //下拉刷新
    function pullupRefresh() {
        setTimeout(function () {
            mui('#pullrefresh').pullRefresh().endPullupToRefresh((imgLength < 10)); //参数为true代表没有更多数据了。
            var table = document.body.querySelector('.mui-table-view');
            var cells = document.body.querySelectorAll('.detailImg');
            table.className = "mui-table-view mui-table-view-chevron detail-show";
            mui.ajax(url + '?page=' + count + '&productType=' + indexVal, {
                dataType: 'json',//服务器返回json格式数据
                //async: true,
                type: 'get',//HTTP请求类型
                timeout: 10000,//超时时间设置为10秒；
                success: function (data) {
                    imgLength = data.length;
                    //服务器返回响应，根据响应结果，分析是否登录成功；
                    for (var i = 0; i < data.length; i++) {
                        var li = document.createElement('li');
                        li.className = 'detailImg';
                        var liHtmlStr = '<input type="hidden" class="hidden-index"  value="'
                                        + index
                                        + '" /><input type="hidden" class="hidden-id"  value="'
                                        + data[i].id
                                        + '" /><div class="list" ><div class="list-top" ><div class="top-left">';
                        liHtmlStr +=
                        '<span><img src="' + data[i].picture + '" alt="" /></span>';
                        liHtmlStr += '</div><div class="top-right">';
                        liHtmlStr +=
                        '<p class="right-ttl trade-font-black">' + data[i].name + '</p>';
                        liHtmlStr +=
                        '<p class="right-pra trade-font-black  mui-ellipsis">'
                        + data[i].description
                        + '</p>';
                        liHtmlStr +=
                        '<p class="right-pra trade-font-red">￥<font>' + data[i].price / 100
                        + '</font></p>';
                        liHtmlStr += '</div></div></div>';
                        li.innerHTML = liHtmlStr;
                        table.appendChild(li);
                        index++;
                    }
                    $(".detailImg").each(function (i) {
                        $(".detailImg").eq(i).bind("tap", function () {
                            index = $(this).index();
                            var yuanma = document.getElementById("ul-page").innerHTML;
                            var type = document.getElementById("type-page").innerHTML;
                            window.localStorage.setItem('pages', yuanma);
                            window.localStorage.setItem('types', type);
                            window.localStorage.setItem('count', count);
                            window.localStorage.setItem('index', index);
                            window.localStorage.setItem('indexVal', indexVal);
                            window.localStorage.setItem('height', $(this).height());
                            location.href =
                            "/weixin/product/" + $(this).find(".hidden-id").val();
                        });
                    })
                    //count判断是第几次加载
                    count++;
                },
                error: function (xhr, type, errorThrown) {
                    //异常处理；
                    console.log(type);
                }
            });
        }, 0);
    }

    if (mui.os.plus) {
        mui.plusReady(function () {
            setTimeout(function () {
                mui('#pullrefresh').pullRefresh().pullupLoading();
                var height = window.localStorage.getItem('height');
                var initIndex = window.localStorage.getItem('index');
                var scrollTopVal = '-' + height * initIndex;
                var mui_scroll_height = document.getElementsByClassName('mui-scroll')[0].offsetHeight;
                var mui_pullrefresh_height = document.getElementsByClassName('mui-content')[0].offsetHeight;
                if (-scrollTopVal < (mui_scroll_height - mui_pullrefresh_height)) {
                    $('.mui-scroll').css({
                                             'transform': 'translate3d(0px,' + scrollTopVal
                                                          + 'px, 0px)'
                                         });
                } else {
                    $('.mui-scroll').css({
                                             'transform': 'translate3d(0px,' + '-'
                                                          + (mui_scroll_height
                                                             - mui_pullrefresh_height)
                                             + 'px, 0px)'
                                         });
                }
                mui('#pullrefresh').pullRefresh().refresh(true);

            }, 0);
        });
    } else {
        mui.ready(function () {
            mui('#pullrefresh').pullRefresh().pullupLoading();
            var height = window.localStorage.getItem('height');
            var initIndex = window.localStorage.getItem('index');
            var scrollTopVal = '-' + height * initIndex;
            var mui_scroll_height = document.getElementsByClassName('mui-scroll')[0].offsetHeight;
            var mui_pullrefresh_height = document.getElementsByClassName('mui-content')[0].offsetHeight;
            if (-scrollTopVal < (mui_scroll_height - mui_pullrefresh_height)) {
                $('.mui-scroll').css({'transform': 'translate3d(0px,' + scrollTopVal + 'px, 0px)'});
            } else {
                $('.mui-scroll').css({
                                         'transform': 'translate3d(0px,' + '-' + (mui_scroll_height
                                                                                  - mui_pullrefresh_height)
                                                      + 'px, 0px)'
                                     });
            }
            mui('#pullrefresh').pullRefresh().refresh(true);

        });
    }

    $('#mask-success .mask-btn div:last-child').bind('touchstart', function () {
        $('#mask-success').css('display', 'none');
    })

    $('#mask-failed .mask-btn div:last-child').bind('touchstart', function () {
        $('#mask-failed').css('display', 'none');
    })

</script>

</html>