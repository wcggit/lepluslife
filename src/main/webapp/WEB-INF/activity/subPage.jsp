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
    <meta name="viewport"
          content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
    <meta name="format-detection" content="telephone=no">
    <meta name="format-detection" content="telephone=yes"/>
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="black"/>
    <title></title>
    <link rel="stylesheet" href="${resourceUrl}/frontRes/css/reset.css">
    <link rel="stylesheet" href="${resourceUrl}/frontRes/hongbao/css/active.css">
    <link rel="stylesheet" href="${resourceUrl}/frontRes/activity/subPage/css/active6.css">
</head>
<body>
<section class="headBg">
    <div class="headHb" style="display: none;">
        <img src="${resourceUrl}/frontRes/activity/subPage/img/before.png" alt=""/>

        <div class="checkPhone">
            <div>
                <input type="number" name="phoneNumber" placeholder="请输入手机号">
            </div>
            <div>
                <input type="number" name="validateCode" placeholder="请输入验证码">
                <button onclick="getVerify()" id="sendCode">获取验证码</button>
            </div>
            <div style="margin-top: -3px;">
                <button class="ljlq" onclick="openHongbao()">立即领取</button>
            </div>
        </div>
    </div>
    <div class="headHbEd" style="display: none;">
        <div class="hb-text">
            <p>恭喜您</p>

            <p>获得<span id="A_status"><span id="scoreA"></span>鼓励金和</span><span id="scoreC"></span>金币
            </p>
        </div>
        <img id="headImg" src="${resourceUrl}/frontRes/activity/subPage/img/after.png" alt=""/>

        <div class="checkDown">
            <button onclick="goUser()">查看我的钱包</button>
        </div>
        <div class="dang"></div>
    </div>
</section>
<section class="js">
    <img src="${resourceUrl}/frontRes/activity/subPage/img/gz.png" alt=""/>
    <img src="${resourceUrl}/frontRes/activity/subPage/img/5.png" alt=""/>
    <img src="${resourceUrl}/frontRes/activity/subPage/img/6.png" alt=""/>
</section>
<section class="sj" style="display: none;">
    <div class="tab">
        <div>
            <img name="jf" src="${resourceUrl}/frontRes/activity/subPage/img/hjf-2.png" alt="">
            <img name="hb" src="${resourceUrl}/frontRes/activity/subPage/img/hhb-1.png" alt="">
        </div>
    </div>
    <div class="list jf">

    </div>
    <div class="list hb" style="display: none;">

    </div>
</section>
<section>
    <img src="${resourceUrl}/frontRes/activity/subPage/img/cloud.png" alt=""/>
</section>
</body>
<script src="${resourceUrl}/js/jquery-1.11.3.min.js"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script>
    $(".headBg").css("height", $(window).width() / 750 * 972 + "px");
    $(".headBgEd").css("height", $(".headBgEd").width() / 650 * 675 + "px");
    $(".headHb").css("height", $(".headHb").width() / 650 * 849 + "px");
    $(".checkPhone").css("margin-top", -($(window).width() / 375 * 183 - 5) + "px");
    $(".checkDown").css("margin-top", -($(window).width() / 375 * 125 - 25) + "px");
    //count判断是第几次加载
    var imgLength = 10, url = '/merchant/list', gps = {}, shopList = $(".hb");
    var pic = '${resourceUrl}/frontRes/activity/subPage/img/lightning.png', hasHot = 0,
        hasMerchant = 0;
    function initPage() {
        var status = '${status}';
        if (status == 1) {
            $(".headHb").hide();
            $(".hb-text").hide();
            $(".headHbEd").show();
            $(".js").hide();
            $(".sj").show();
            $('#headImg').attr('src', '${resourceUrl}/frontRes/activity/subPage/img/hb3.png');
            hotList();
        } else {
            $(".headHb").show();
        }
    }
    window.onload = initPage;//不要括号
</script>

<script>

    $(".tab img").click(function () {
        var src = $(this).attr("src");
        var name = $(this).attr("name");
        var src_ = src.split("-");
        reSetSrcImg();
        $(this).attr("src", src_[0] + "-2.png");
        switch (name) {
            case"jf":
                if (hasHot == 0) {
                    hotList();
                }
                $(".jf").show();
                $(".hb").hide();
                break;
            case"hb":
                if (hasMerchant == 0) {
                    shopList.html(
                        '<div class="loading"><span></span><span></span><span></span><span></span><span></span></div>');
                    getLocation()
                }
                $(".hb").show();
                $(".jf").hide()
        }
    });
    function getLocation() {
        wx.config({
                      debug: false,
                      appId: '${wxConfig.appId}',
                      timestamp: '${wxConfig.timestamp}',
                      nonceStr: '${wxConfig.noncestr}',
                      signature: '${wxConfig.signature}',
                      jsApiList: ['getLocation']
                  });
        wx.ready(function () {
            wx.getLocation({
                               type: 'wgs84', success: function (res) {
                    var latitude = res.latitude;
                    var longitude = res.longitude;
                    gps.status = 1;
                    gps.lat = latitude;
                    gps.lon = longitude;
                    merchantList()
                }, fail: function (res) {
                    gps.status = 0;
                    merchantList()
                }, cancel: function (res) {
                    gps.status = 0;
                    merchantList()
                }
                           })
        });
        wx.error(function (res) {
        })
    }
    function reSetSrcImg() {
        $(".tab > div > img:first-child").attr("src",
                                               "${resourceUrl}/frontRes/activity/subPage/img/hjf-1.png");
        $(".tab > div > img:last-child").attr("src",
                                              "${resourceUrl}/frontRes/activity/subPage/img/hhb-1.png")
    }
    function hotList() {
        $.ajax({
                   type: "get",
                   url: "/shop/productList?page=1&typeId=0",
                   success: function (data) {
                       hasHot = 1;
                       var list = data.data, content = '';
                       if (list != null) {
                           var length = list.length;
                           if (list.length > 5) {
                               length = 5
                           }
                           for (var i = 0; i < length; i++) {
                               content +=
                                   '<div  onclick="goProductDetail(' + list[i].id
                                   + ')"><div><img class="w-imgSize" src="' + list[i].picture
                                   + '" alt=""></div><div class="information"><p>' + list[i].name
                                   + '</p><div>' + toDecimal(list[i].minPrice / 100)
                                   + '<span style="font-size: 12px;">元</span>+<span style="color: #fb991a;">'
                                   + toDecimal(list[i].minScore / 100)
                                   + '<span style="font-size: 12px;">金币</span></span><span style ="font-size: 11px;color: #AEAEAE;margin-left: 2%;text-decoration:line-through;">市场价'
                                   + toDecimal(list[i].price / 100)
                                   + '元</span></div><div><div><img src = "' + pic
                                   + '"></div><div>已售'
                                   + list[i].saleNumber + '份</div><div>马上抢</div></div></div></div>'
                           }
                           $(".jf").html(content);
                           $(".w-imgSize").css("height", $(".w-imgSize").width() + "px")
                       }
                   }
               })
    }
    function merchantList() {
        gps.partnership = 1;
        $.ajax(url + '?page=1', {
            dataType: 'json',
            type: 'post',
            data: gps,
            timeout: 10000,
            success: function (data) {
                imgLength = data.length > 5 ? 5 : data.length;
                hasMerchant = 1;
                shopList.html('');
                for (var i = 0; i < imgLength; i++) {
                    shopList.append($("<div></div>").attr("onclick",
                                                          "merchantInfo('" + data[i].id + "-"
                                                          + data[i].distance + "')").attr("id",
                                                                                          "aaa-"
                                                                                          + data[i].id
                                                                                          + "-"
                                                                                          + data[i].distance).append(
                        $("<div></div>").append($("<img>").attr("src",
                                                                (data[i].picture
                                                                 == null
                                                                 || data[i].picture
                                                                    == "null"
                                                                 || data[i].picture
                                                                    == "")
                                                                    ? "${resourceUrl}/frontRes/merchant/img/listLogo.jpg"
                                                                    : data[i].picture))).append(
                        $("<div></div>").attr("class",
                                              "shopInformation").append(
                            $("<div></div>").append($("<div></div>").html(data[i].name))).append(
                            $("<div></div>").attr("class",
                                                  "star").attr("id",
                                                               "merchant"
                                                               + data[i].id)).append(
                            $("<div></div>").attr("class",
                                                  "w").append($("<div></div>").attr("class",
                                                                                    "tabb").append(
                                $("<div></div>").append($("<img>").attr("src",
                                                                        "${resourceUrl}/frontRes/merchant/img/food.png"))).append(
                                $("<div></div>").html(data[i].typeName))).append(
                                $("<div></div>").attr("class",
                                                      "tabb").append(
                                    $("<div></div>").append($("<img>").attr("src",
                                                                            "${resourceUrl}/frontRes/merchant/img/address.png"))).append(
                                    $("<div></div>").html(data[i].areaName))).append(
                                $("<div></div>").attr("class",
                                                      "tabb").append($("<div></div>").attr("style",
                                                                                           "margin-right:9px;color:#8d8d8d;").append(
                                    gps.status
                                    == 1
                                        ? $("<img>").attr("src",
                                                          "${resourceUrl}/frontRes/merchant/img/juli.png")
                                        : "")).append(gps.status
                                                      == 1
                                                          ? $("<span></span>").html(data[i].distance
                                                                                    > 1000
                                                                                        ? ((data[i].distance
                                                                                            / 1000).toFixed(
                                            1)
                                                                                           + "km")
                                                                                        : data[i].distance
                                                                                          + "m")
                                                          : "")))));
                    var star = parseInt(data[i].star);
                    var merId = "#merchant" + data[i].id;
                    if (star > 5) {
                        star = 5
                    } else if (star < 0) {
                        star = 0
                    }
                    drawStar(star, "${resourceUrl}/frontRes/merchant/img/star.png", merId);
                    drawStar(5 - star, "${resourceUrl}/frontRes/merchant/img/n-star.png", merId)
                }
            },
            error: function (xhr, type, errorThrown) {
                console.log(type)
            }
        })
    }
    function merchantInfo(val) {
        var str = val.split('-');
        window.location.href = "/front/shop/weixin/m?id=" + str[0]
                               + "&distance="
                               + str[1] + "&status=" + gps.status;
    }
    function drawStar(num, url, merId) {
        for (var i = 0; i < num; i++) {
            $(merId).append($("<div></div>").append($("<img>").attr("src", url)))
        }
    }
    function goProductDetail(id) {
        location.href = "/weixin/product/" + id;
    }
    function toDecimal(x) {
        var f = parseFloat(x);
        if (isNaN(f)) {
            return false
        }
        f = Math.round(x * 100) / 100;
        var s = f.toString();
        var rs = s.indexOf('.');
        if (rs < 0) {
            rs = s.length;
            s += '.'
        }
        while (s.length <= rs + 2) {
            s += '0'
        }
        return s
    }
    function openHongbao() {
        var phoneNumber = $("input[name='phoneNumber']").val();
        var code = $("input[name='validateCode']").val();
        if ((!(/^1[3|4|5|6|7|8]\d{9}$/.test(phoneNumber)))) {
            alert("请输入正确的手机号");
            return false
        }
        if (code == null || code == '') {
            alert("请输入验证码");
            return false
        }
        $.post("/user/validate", {phoneNumber: phoneNumber, code: code}, function (res) {
            if (res.status == 200) {
                $.ajax({
                           type: "get",
                           url: "/weixin/subPage/open?phoneNumber=" + phoneNumber,
                           success: function (data) {
                               if (data.status == 200) {
                                   var map = data.data;
                                   if (map.scoreA == 0) {
                                       $("#A_status").css('display', 'none');
                                   }
                                   $("#scoreA").html(map.scoreA / 100);
                                   $("#scoreC").html(map.scoreC / 100);
                                   hotList();
                                   $(".headHb").hide();
                                   $(".headHbEd").show();
                                   $(".js").hide();
                                   $(".sj").show()
                               } else {
                                   alert(data.msg)
                               }
                           }
                       })
            } else {
                alert(res.msg)
            }
        })
    }
    function goMerchant() {
        window.location.href = "/merchant/index"
    }
    function goUser() {
        window.location.href = "/weixin/user"
    }
    function getVerify() {
        var phoneNumber = $("input[name='phoneNumber']").val();
        if ((!(/^1[3|4|5|6|7|8]\d{9}$/.test(phoneNumber)))) {
            alert('请输入正确的手机号！');
            return true
        }
        $("#sendCode").addClass("disClick");
        f_timeout();
        $.post("/user/sendCode", {
            phoneNumber: phoneNumber,
            type: 3
        }, function (res) {
            if (res.status != 200) {
                alert(res.msg);
            }
        });
    }
    function f_timeout() {
        $('#sendCode').attr('onclick', '');
        $('#sendCode').html('<span id="timeb2">60</span>秒后重发');
        $('#sendCode').attr({disabled: 'true'});
        timer = self.setInterval(addsec, 1000)
    }
    function addsec() {
        var t = $('#timeb2').html();
        if (t > 0) {
            $('#timeb2').html(t - 1)
        } else {
            window.clearInterval(timer);
            $("#sendCode").removeClass("disClick");
            $('#sendCode').html('<span id="timeb2"></span>重获验证码');
            $('#sendCode').attr({disabled: false});
            $('#sendCode').attr('onclick', 'getVerify()');
        }
    }
</script>
</html>
