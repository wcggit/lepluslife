<%--
  Created by IntelliJ IDEA.
  User: wcg
  Date: 16/4/8
  Time: 下午4:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/WEB-INF/commen.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<script type="application/javascript">
    function goPage(page) {
        location.href = "${wxRootUrl}/weixin/" + page;
    }
</script>
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
    <link rel="stylesheet" href="${resourceUrl}/frontRes/merchant/css/reset.css">
    <link rel="stylesheet" href="${resourceUrl}/frontRes/merchant/css/w-2.css">
</head>

<body>
<!--头部-->
<header>
    <section class="header">
        <div class="s active" onclick="changeCondition(0)">离我最近</div>
        <div class="s" onclick="changeCondition(2)">送红包最多</div>
        <div class="s" onclick="changeCondition(3)">评价最高</div>
    </section>
</header>

<!--下拉刷新容器-->
<div id="pullrefresh" class="mui-content mui-scroll-wrapper">
    <div class="mui-scroll">
        <div class="shopList" id="shopList">

        </div>
    </div>
</div>
</body>
<script src="${resourceUrl}/js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="${resourceUrl}/js/mui.min.js"></script>

<script>
    //count判断是第几次加载
    var count = 1, imgLength = 10, shangshu, yushu, url = '${wxRootUrl}/merchant/list', gps = {};
    function initPage() {
        gps.status = '${status}';
        gps.lat = '${lat}';
        gps.lon = '${lon}';
        gps.cityName = '${cityName}';
        gps.condition = '${condition}';
        gps.type = '${type}';

//        if (gps.condition == 2) {
//            $(".s").removeClass("active");
//            $("#condition2").addClass("active");
//        } else if (gps.condition == 3) {
//            $(".s").removeClass("active");
//            $("#condition3").addClass("active");
//        }

        pullupRefresh();
    }
    window.onload = initPage;//不要括号

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
            mui('#pullrefresh').pullRefresh().endPullupToRefresh((imgLength < 10)); //参数为true代表没有更多数据了。
            var shopList = $("#shopList");

            mui.ajax(url + '?page=' + count, {
                dataType: 'json',//服务器返回json格式数据
                type: 'post',//HTTP请求类型
                data: gps,
                timeout: 10000,//超时时间设置为10秒；
                success: function (data) {
                    imgLength = data.length;
                    for (var i = 0; i < data.length; i++) {
                        shopList.append(
                                $("<div></div>").attr("id", "aaa" + data[i].id).append(
                                        $("<div></div>").append(
                                                $("<img>").attr("src",
                                                                (data[i].picture == null
                                                                 || data[i].picture == "null")
                                                                        ? "${resourceUrl}/frontRes/merchant/img/listLogo.jpg"
                                                                        : data[i].picture)
                                        )
                                ).append(
                                        $("<div></div>").attr("class",
                                                              "shopInformation").append(
                                                $("<div></div>").append(
                                                        $("<div></div>").html(data[i].name)
                                                )
                                        ).append(
                                                $("<div></div>").attr("class",
                                                                      "star").attr("id",
                                                                                   "merchant"
                                                                                   + data[i].id)
                                        ).append(
                                                $("<div></div>").attr("class", "w").append(
                                                        $("<div></div>").attr("class",
                                                                              "tabb").append(
                                                                $("<div></div>").append(
                                                                        $("<img>").attr("src",
                                                                                        "${resourceUrl}/frontRes/merchant/img/food.png")
                                                                )
                                                        ).append(
                                                                $("<div></div>").html(data[i].typeName)
                                                        )
                                                ).append(
                                                        $("<div></div>").attr("class",
                                                                              "tabb").append(
                                                                $("<div></div>").append(
                                                                        $("<img>").attr("src",
                                                                                        "${resourceUrl}/frontRes/merchant/img/address.png")
                                                                )
                                                        ).append(
                                                                $("<div></div>").html(data[i].areaName)
                                                        )
                                                ).append(
                                                        $("<div></div>").attr("class",
                                                                              "tabb").append(
                                                                $("<div></div>").attr("style",
                                                                                      "margin-right:9px;color:#8d8d8d;").append(
                                                                        gps.status == 1 ?
                                                                        $("<img>").attr("src",
                                                                                        "${resourceUrl}/frontRes/merchant/img/juli.png")
                                                                                : ""
                                                                )
                                                        ).append(
                                                                gps.status == 1
                                                                        ? $("<span></span>").html(data[i].distance
                                                                                                  > 1000
                                                                                                          ? ((data[i].distance
                                                                                                              / 1000).toFixed(1)
                                                                                                             + "km")
                                                                                                          : data[i].distance
                                                                                                            + "m"
                                                                ) : ""
                                                        )
                                                )
                                        )
                                )
                        );
                        var tests = "aaa" + data[i].id;
                        var locationIt = "${wxRootUrl}/weixin/merchant/info/" + data[i].id
                                         + "?distance="
                                         + data[i].distance + "&status=" + gps.status;
                        document.getElementById(tests).addEventListener('tap', function () {
                            location.href = locationIt;
                        }, false);
                        var star = parseInt(data[i].star);

                        var merId = "#merchant" + data[i].id;
                        if (star > 5) {
                            star = 5;
                        } else if (star < 0) {
                            star = 0;
                        }
                        drawStar(star, "${resourceUrl}/frontRes/merchant/img/star.png", merId);
                        drawStar(5 - star, "${resourceUrl}/frontRes/merchant/img/n-star.png",
                                 merId);
                    }

                    count++;
                },
                error: function (xhr, type, errorThrown) {
                    //异常处理；
                    console.log(type);
                }
            });
        }, 0);
    }

    function drawStar(num, url, merId) {
        for (var i = 0; i < num; i++) {
            $(merId).append(
                    $("<div></div>").append(
                            $("<img>").attr("src", url)
                    )
            )
        }
    }

    $(".s").click(function (e) {
        $(".s").removeClass("active");
        $(this).addClass("active");
    });

    function changeCondition(condition) {
        gps.condition = condition;
        count = 1;
        imgLength = 10;
        $("#shopList").html('');
        mui('#pullrefresh').pullRefresh().refresh(true);

        <%--if (gps.status == 1) {--%>
        <%--location.href = "${wxRootUrl}/wxReply/merchant/type?status=" +--%>
        <%--gps.status + "&type=" + gps.type + "&lat=" + gps.lat + "&lon=" + gps.lon--%>
        <%--+ "&cityName=" + gps.cityName + "&condition=" + gps.condition;--%>
        <%--} else {--%>
        <%--location.href =--%>
        <%--"${wxRootUrl}/wxReply/merchant/type?status=" + gps.status + "&type=" + gps.type--%>
        <%--+ "&condition=" + gps.condition;--%>
        <%--}--%>
        pullupRefresh();
    }
</script>
</html>
