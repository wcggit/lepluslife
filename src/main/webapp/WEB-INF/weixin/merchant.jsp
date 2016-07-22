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
    <link rel="stylesheet" type="text/css" href="${resourceUrl}/css/location.css"/>
    <link rel="stylesheet" href="${resourceUrl}/frontRes/merchant/css/reset.css">
    <link rel="stylesheet" href="${resourceUrl}/frontRes/merchant/css/w-1.css">
    <script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
</head>

<body>
<!--头部-->
<header>
    <div>
        <img src="${resourceUrl}/frontRes/merchant/img/where.png" alt="">
    </div>
    <div id="currentCity"></div>
</header>
<section class="tab">
    <div onclick="findByType(1)">
        <div>
            <img src="${resourceUrl}/frontRes/merchant/img/type1.png" alt="">
        </div>
        <p>美 食</p>
    </div>
    <div onclick="findByType(3)">
        <div>
            <img src="${resourceUrl}/frontRes/merchant/img/type3.png" alt="">
        </div>
        <p>娱 乐</p>
    </div>
    <div onclick="findByType(2)">
        <div>
            <img src="${resourceUrl}/frontRes/merchant/img/type2.png" alt="">
        </div>
        <p>购 物</p>
    </div>
    <div onclick="findByType(4)">
        <div>
            <img src="${resourceUrl}/frontRes/merchant/img/type4.png" alt="">
        </div>
        <p>丽 人</p>
    </div>
</section>

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
    wx.config({
                  debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                  appId: '${wxConfig['appId']}', // 必填，公众号的唯一标识
                  timestamp: ${wxConfig['timestamp']}, // 必填，生成签名的时间戳
                  nonceStr: '${wxConfig['noncestr']}', // 必填，生成签名的随机串
                  signature: '${wxConfig['signature']}',// 必填，签名，见附录1
                  jsApiList: [
                      'getLocation'
                  ] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
              });
    wx.ready(function () {
        // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
        //获取地理位置
        wx.getLocation({
                           type: 'wgs84', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
                           success: function (res) {
                               var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
                               var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
                               gps.status = 1;
                               gps.lat = latitude;
                               gps.lon = longitude;
                               $.ajax({
                                          async: false,
                                          url: "http://api.map.baidu.com/geocoder/v2/?ak=UjkyChtGyFq9Gm1BOPp1jg9Y9j4PrZ72&callback=renderReverse&location="
                                               +
                                               "" + latitude + "," + longitude
                                               + "&output=json&pois=1",
                                          type: "GET",
                                          dataType: 'jsonp',
                                          //jsonp的值自定义,如果使用jsoncallback,那么服务器端,要返回一个jsoncallback的值对应的对象.
                                          //   jsonp: 'jsoncallback',
                                          //要传递的参数,没有传参时，也一定要写上
                                          data: null,
                                          timeout: 5000,
                                          //返回Json类型
                                          contentType: "application/json;utf-8",
                                          //服务器段返回的对象包含name,data属性.
                                          success: function (result) {
                                              var currCity = result.result.addressComponent.city;
                                              $("#currentCity").html(currCity);
                                              gps.cityName = currCity;
                                              pullupRefresh();
                                          },
                                          error: function (jqXHR, textStatus, errorThrown) {
                                              pullupRefresh();
                                          }
                                      });
                           },
                           fail: function (res) {
                               gps.status = 0;
                               //  alert("fail： ");
                               pullupRefresh();
                           }
                           ,
                           cancel: function (res) {
                               gps.status = 0;
                               //   alert("cancel: ");
                               pullupRefresh();
                           }
                       });
    })
    ;
    wx.error(function (res) {
        // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。

    });

</script>
<script>
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

    function findByType(type) {
        if (gps.status == 1) {
            location.href = "${wxRootUrl}/merchant/type?status=" +
                            gps.status + "&type=" + type + "&lat=" + gps.lat + "&lon=" + gps.lon
                            + "&cityName=" + gps.cityName;
        } else {
            location.href =
            "${wxRootUrl}/merchant/type?status=" + gps.status + "&type=" + type;
        }
    }

</script>
</html>
