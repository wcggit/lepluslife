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
    <link rel="stylesheet" type="text/css" href="${resourceUrl}/css/tradeDetail.css"/>
    <script src="${resourceUrl}/js/jquery-1.11.3.min.js"></script>
</head>

<body>
<!--头部-->
<nav class="mui-bar  mui-bar-nav" style="background: #D62C2C;border-bottom: solid 1px #CBCACA;">
    <a class="mui-tab-item">
        <span class="mui-tab-label focusCol">鼓励金明细</span>
    </a>
    <a class="mui-tab-item">
        <span class="mui-tab-label initCol">金币明细</span>
    </a>
</nav>

<!--内容部分-->

<!--红包明细-->
<div id="pullrefresh" class="mui-content mui-scroll-wrapper">
    <div class="mui-scroll content">
        <article id="article">

        </article>
    </div>
</div>

</body>
<script type="text/javascript" src="${resourceUrl}/js/mui.min.js"></script>

<script type="text/javascript">
    document.title="明细记录";
    var dataDetail, count = 0, shangshu = 0, yushu = 0, dataLength = 0, url = "/score/scoreList";
    var box = "redbox", span = "redword", text = "鼓励金", jishu = 100;
    var table = $("#article");
    mui.ajax(url, {
        data: {
            type: 0,
            openId: '${openId}'
        },
        async: false,
        dataType: 'json',//服务器返回json格式数据
        type: 'post',//HTTP请求类型
        timeout: 10000,//超时时间设置为10秒；
        success: function (data) {
            //数据的总个数
            dataDetail = data.data.list;
            dataLength = data.data.list.length;
            count = 0;
            shangshu = Math.floor(dataLength / 5);
            yushu = dataLength % 5;
        },
        error: function (xhr, type, errorThrown) {
            //异常处理；
            console.log(type);
        }
    });
    var spans = $('.mui-bar-nav .mui-tab-item');
    click(spans);
    //点击事件
    function click(s) {
        s.each(function (i) {
            s.eq(i).bind('touchstart', function () {
                //重置下拉刷新
                mui('#pullrefresh').pullRefresh().refresh(true);
                table.empty();
                var index = $(this).index();
                clear(s);
                $(this).find('span').removeClass('initCol').addClass('focusCol');
                if (index == 0) {
                    box = "redbox";
                    span = "redword";
                    text = "鼓励金";
                    jishu = 100;
                }
                if (index == 1) {
                    box = "orangebox";
                    span = "orangeword";
                    text = "金币";
                    jishu = 100;
                    index = 2;
                }
                mui.ajax(url, {
                    data: {
                        type: index,
                        openId: '${openId}'
                    },
                    async: false,
                    dataType: 'json',//服务器返回json格式数据
                    type: 'post',//HTTP请求类型
                    timeout: 10000,//超时时间设置为10秒；
                    success: function (data) {
                        //数据的总个数
                        dataDetail = data.data.list;
                        dataLength = data.data.list.length;
                        count = 0;
                        shangshu = Math.floor(dataLength / 5);
                        yushu = dataLength % 5;

                    },
                    error: function (xhr, type, errorThrown) {
                        //异常处理；
                        console.log(type);
                    }
                });
                mui('#pullrefresh').pullRefresh().pullupLoading();
                $('.mui-scroll').css({'transform': 'translate3d(0px, 0px, 0px)'});
            })
        });
    }
    //清除
    function clear(s) {
        s.each(function (i) {
            s.eq(i).find('span').removeClass('focusCol').addClass('initCol');
        })
    }

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
    function pullupRefresh() {
        setTimeout(function () {
            mui('#pullrefresh').pullRefresh().endPullupToRefresh((++count > shangshu)); //参数为true代表没有更多数据了。

            var cells = document.body.querySelectorAll('.list');
            if (count <= shangshu) {

                for (var i = cells.length, len = i + 5; i < len; i++) {
                    list(table, i);
                }

            } else {
                for (var i = cells.length, len = i + yushu; i < len; i++) {
                    list(table, i);
                }

            }
        }, 200);

    }
    if (mui.os.plus) {
        mui.plusReady(function () {
            setTimeout(function () {
                mui('#pullrefresh').pullRefresh().pullupLoading();
            }, 1000);

        });
    } else {
        mui.ready(function () {
            mui('#pullrefresh').pullRefresh().pullupLoading();
        });
    }

    //		循环列表
    function list(table, i) {
        var section = '<section><time datetime="' + dataDetail[i].createDate + '" class="dt">'
                      + dataDetail[i].createDate;
        section +=
        '&nbsp;</time><aside class="first_aside"><span class="point point_dt"></span></aside></section>';

        for (var j = 0; j < dataDetail[i].list.length; j++) {
            var date = new Date(dataDetail[i].list[j].dateCreated);
            section += '<section><time datetime="' + dataDetail[i].list[j].dateCreated
                       + '">' + date.getHours() + ":"
                       + date.getMinutes() + ":" + date.getSeconds();
            section +=
            '</time><aside><span class="point"></span><div class="' + box + '"><p><span class="'
            + span + '">'
            + dataDetail[i].list[j].number / jishu + text + '</span><br>'
            + dataDetail[i].list[j].operate + '</p></div></aside></section>';
        }
        table.html(table.html() + section);
    }

</script>

</html>