<%--
  Created by IntelliJ IDEA.
  User: wcg
  Date: 16/4/15
  Time: 下午2:47
  To change this template use File | Settings | File Templates.
--%>
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
    <link rel="stylesheet" href="${resourceUrl}/css/lexuan.css">
</head>

<body>
<!--底部菜单-->
<%@include file="/WEB-INF/weixin/common/shopfoot.jsp" %>
<!--下拉刷新容器-->
<div id="pullrefresh" class="mui-content mui-scroll-wrapper">
    <div class="mui-scroll">
        <ul class="mui-table-view mui-table-view-chevron detail-hid"
            style="background: #EFEFF4;"></ul>
    </div>
</div>
</body>
<script type="text/javascript" src="${resourceUrl}/js/mui.min.js"></script>
<script src="${resourceUrl}/js/jquery-2.0.3.min.js"></script>

<script>

    document.title="乐选";
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

    //count判断是第几次加载
    var count = 1, imgLength, shangshu, yushu, url = '/topic/list';
    mui.ajax(url + '?page=' + count, {
        dataType: 'json',//服务器返回json格式数据
        type: 'get',//HTTP请求类型
        timeout: 10000,//超时时间设置为10秒；
        success: function (data) {
            //数据的总个数
            imgLength = data.length;
        },
        error: function (xhr, type, errorThrown) {
            //异常处理；
            console.log(type);
        }
    });

    /**
     * 上拉加载具体业务实现
     */
    function pullupRefresh() {
        setTimeout(function () {
            //参数为true代表没有更多数据了。
            var table = document.body.querySelector('.mui-table-view');
            var cells = document.body.querySelectorAll('.detailImg');
            table.className = "mui-table-view mui-table-view-chevron detail-show";

            mui.ajax(url + '?page=' + count, {
                dataType: 'json',//服务器返回json格式数据
                type: 'get',//HTTP请求类型
                timeout: 10000,//超时时间设置为10秒；
                success: function (data) {
                    for (var i = 0; i < data.length; i++) {
                        var li = document.createElement('li');
                        li.className = 'detailImg';
                        var liHtmlStr = '<input type="hidden" class="hidden-id"  value="'
                                        + data[i].id
                                        + '" /><li class="product-list"><img src="'
                                        + data[i].picture + '" alt="" />';
                        liHtmlStr +=
                        '<div class="shadow"></div><div class="leftTtl"><p>' + data[i].minPrice/100
                        + '元起</p></div>';
                        liHtmlStr +=
                        '<div class="rightTtl"><p>' + data[i].description + '</p><p></p><p>'
                        + data[i].title + '</p></div></li>';
                        li.innerHTML = liHtmlStr;
                        table.appendChild(li);
                    }
                    $(".detailImg").each(function (i) {
                        $(".detailImg").eq(i).bind("tap", function () {
                            location.href = "/weixin/topic/" + $(this).find(".hidden-id").val();
                        });
                    })
                    count++;
                },
                error: function (xhr, type, errorThrown) {
                    //异常处理；
                    console.log(type);
                }
            });
        }, 0);
        mui('#pullrefresh').pullRefresh().endPullupToRefresh((imgLength < 10));
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
</script>
</html>
