<%--
  Created by IntelliJ IDEA.
  User: wcg
  Date: 16/3/24
  Time: 上午10:05
  content:关注领取红包
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/commen.jsp" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="viewport" id="viewport" content="width=device-width, initial-scale=1">
    <meta name="format-detection" content="telephone=no">
    <meta name="format-detection" content="telephone=yes"/>
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="black"/>
    <title>Title</title>
    <link rel="stylesheet" href="${resourceUrl}/frontRes/css/reset.css">
    <link rel="stylesheet" href="${resourceUrl}/frontRes/hongbao/css/active.css">
    <link rel="stylesheet" href="${resourceUrl}/frontRes/hongbao/css/active4.css">
</head>
<body>
<section>
    <img src="${resourceUrl}/frontRes/hongbao/img2/1.png" alt="">
</section>
<section class="hongbao">
    <div id="div1" class="dis">
        <div><input type="number" name="phoneNumber" placeholder="请输入您的手机号"/></div>
        <div>
            <button onclick="openHongbao()">领取红包</button>
        </div>
    </div>
    <div id="div2" class="dis">
        <p>获得<span style="font-size: 30px" id="scoreA">10</span>元红包</p>

        <p>已放入账户</p>
    </div>
</section>
<div id="div3" class="dis">
    <section>
        <img src="${resourceUrl}/frontRes/hongbao/img2/2.png" alt="">
    </section>
    <section>
        <img src="${resourceUrl}/frontRes/hongbao/img2/3.png" alt="">
    </section>
    <section>
        <img src="${resourceUrl}/frontRes/hongbao/img2/4.png" alt="">
    </section>
    <section class="marginFix">
        <img src="${resourceUrl}/frontRes/hongbao/img2/5.png" alt="">
    </section>
    <section class="marginFix">
        <img src="${resourceUrl}/frontRes/hongbao/img2/6.png" alt="">
    </section>
    <section class="bgFix marginFix">
        <img src="${resourceUrl}/frontRes/hongbao/img2/7.png" alt="">
    </section>
</div>
<div id="div4" class="dis">
    <section>
        <img src="${resourceUrl}/frontRes/hongbao/img2/bt.png" alt="">
    </section>
    <section class="shopList">

    </section>
    <section class="more">
        <p onclick="goMerchant()">查看更多</p>

        <div>
            <img src="${resourceUrl}/frontRes/hongbao/img2/down.png" alt="">
        </div>
    </section>
</div>
</body>
<script src="${resourceUrl}/js/jquery-1.11.3.min.js"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script>
    function initPage() {
        var status = '${status}';
        if (status == 1) {
            var scoreA = '${scoreA}';
            $("#scoreA").html(scoreA / 100);
            $("#div2").removeClass("dis").addClass("zi");
            $("#div4").removeClass("dis");
        } else {
            $("#div1").removeClass("dis").addClass("zi");
            $("#div3").removeClass("dis");
        }
    }
    window.onload = initPage;//不要括号
    $(".hongbao").css("height", $(window).width() / 750 * 734 + "px");
    $(".hongbao > div").css("padding-top", $(window).width() / 375 * 180 + "px");
</script>

<script>
    //count判断是第几次加载
    var imgLength = 10, shangshu, yushu, url = '${wxRootUrl}/merchant/list', gps = {};
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
        //获取地理位置
        wx.getLocation({
                           type: 'wgs84', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
                           success: function (res) {
                               var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
                               var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
                               gps.status = 1;
                               gps.lat = latitude;
                               gps.lon = longitude;
                               pullupRefresh();
                           },
                           fail: function (res) {
                               gps.status = 0;
                               pullupRefresh();
                           }
                           ,
                           cancel: function (res) {
                               gps.status = 0;
                               pullupRefresh();
                           }
                       });
    })
    ;
    wx.error(function (res) {
        // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。

    });

</script>

<script type="text/javascript">


    function pullupRefresh() {
        setTimeout(function () {
            var shopList = $(".shopList");
            gps.partnership = 1;

            $.ajax(url + '?page=1', {
                dataType: 'json',//服务器返回json格式数据
                type: 'post',//HTTP请求类型
                data: gps,
                timeout: 10000,//超时时间设置为10秒；
                success: function (data) {
                    imgLength = data.length > 3 ? 3 : data.length;

                    for (var i = 0; i < imgLength; i++) {
                        shopList.append(
                                $("<div></div>").attr("id", "aaa-" + data[i].id + "-"
                                                            + data[i].distance).append(
                                        $("<div></div>").append(
                                                $("<img>").attr("src",
                                                                (data[i].picture == null
                                                                 || data[i].picture == "null"
                                                                 || data[i].picture == "")
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
                        var tests = "aaa-" + data[i].id + "-" + data[i].distance;
                        document.getElementById(tests).addEventListener('tap', function () {
                            var str = $(this).attr("id").split('-');
                            location.href = "${wxRootUrl}/weixin/merchant/info/" + str[1]
                                            + "?distance="
                                            + str[2] + "&status=" + gps.status;
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

    function openHongbao() {

        var phoneNumber = $("input[name='phoneNumber']").val();

        if ((!(/^1[3|4|5|6|7|8]\d{9}$/.test(phoneNumber)))) {
            alert("请输入正确的手机号");
            return false;
        }

        $.ajax({
                   type: "get",
                   url: "/weixin/subPage/open?phoneNumber=" + phoneNumber,
                   success: function (data) {
                       if (data.status == 200) {
                           var a = data.msg;
                           $("#scoreA").html(a / 100);
                           $("#div1").removeClass("zi").addClass("dis");
                           $("#div2").removeClass("dis").addClass("zi");
                           $("#div3").addClass("dis");
                           $("#div4").removeClass("dis");
                       } else {
                           alert(data.status + "手机号被占用或已领取");
                       }
                   }
               });
    }

    function goMerchant() {
        window.location.href = "http://www.lepluslife.com/merchant/index";
    }
</script>
</html>
