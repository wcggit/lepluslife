<%--
  Created by IntelliJ IDEA.
  User: zhangwen
  Date: 2017/4/15
  Time: 17:12
  好店首页
--%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/commen.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="viewport" id="viewport" content="width=device-width, initial-scale=1">
    <meta name="format-detection" content="telephone=no">
    <meta name="format-detection" content="telephone=yes"/>
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="black"/>
    <title>周边好店</title>
    <link rel="stylesheet" href="${resourceUrl}/frontRes/css/reset.css">
    <link rel="stylesheet" href="${resourceUrl}/frontRes/shop/index/css/index.css">
</head>
<body>
<div class="waiting" id="loading">
    <div class="img">
        <img src="${resourceUrl}/frontRes/shop/index/img/waiting.gif" alt="">
    </div>
</div>
<section class="positioning">
    <div class="upDown fixClear">
        <div><img src="${resourceUrl}/frontRes/shop/index/img/addressIcon.png" alt=""></div>
        <div class="thisCity"></div>
        <div class="arrowUp"></div>
    </div>
    <div class="cityList"></div>
</section>
<section class="blank">

</section>
<section class="type">
    <div class="typeInfo">
        <div class="active" id="type_0">全部</div>
        <div id="type_1">美食</div>
        <div id="type_3">娱乐</div>
        <div id="type_2">购物</div>
        <div id="type_4">丽人</div>
    </div>
    <div class="typeInfo_">
        <div class="active" id="condition_0">离我最近</div>
        <div id="condition_2">金币最多</div>
        <div id="condition_3">综合排序</div>
    </div>
</section>
<section class="goodShop">
    <div>
        <img src="${resourceUrl}/frontRes/shop/index/img/goodShop.png" alt="">

        <p class="seeMore" onclick="forMore()">查看更多<span class="arrowUp_"></span></p>
    </div>
</section>
<section class="goodShopList">
</section>
<section class="goodBusinessmen">
    <div><img src="${resourceUrl}/frontRes/shop/index/img/goodBusinessmen.png" alt=""></div>
</section>
<section class="goodBusinessmenList">
</section>
</body>
<script src="${resourceUrl}/js/jquery.min.js"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
<script type="text/javascript"
        src="http://webapi.amap.com/maps?v=1.3&key=48f94cad8f49fc73c9ba59b281bb1e84"></script>
<script>
    var cityList = '', citySize = 0, gps = {};
    /**异步加载城市列表*/
    $.ajax({
               type: "post",
               url: "/city/list",
               success: function (data) {
                   cityList = data.data;
                   citySize = cityList.length;
                   var content = '';
                   for (var i = 0; i < citySize; i++) {
                       content +=
                       '<p id="city_' + cityList[i].id + '">' + cityList[i].name + '</p>';
                   }
                   $('.cityList').html(content);
                   $(".cityList > p").click(function () {
                       $(".cityList").slideUp();
                       $(".blank").fadeOut();
                       $(".blank").css("height", "0px");
                       $(".thisCity").html($(this).html());
                       gps.cityId = $(this).attr('id').split('_')[1];
                       currPage = 1;
                       goodShop(gps.cityId);
                       ajaxMerchantList();
                   });
               }
           });
    /******查看更多推荐*/
    function forMore() {
        var currCityId = gps.cityId;
        if (currCityId == null) {
            currCityId = 1;
        }
        window.location.href = "/front/shop/weixin/more?cityId=" + currCityId;
    }
    /******** 轮播图后置类型判断 ***/
    function bannerType(afterType, url, id) {
        console.log(afterType + '===' + url + '====' + id);
        switch (afterType) {
            case 1:
                window.location.href = url;
                break;
            case 3:
                window.location.href = "/front/shop/weixin/m?status=0&id=" + id;
                break;
            default :
                break
        }
    }
    /******进入商家详情页*/
    function detail(merchantId, distance) {
        window.location.href = "/front/shop/weixin/m?id=" + merchantId
                               + "&distance="
                               + distance + "&status=" + gps.status;
    }
    /**加载好店推荐*/
    function goodShop(cityId) {
        $('.goodShopList').html('');
        $.ajax({
                   type: "get",
                   url: "/app/banner/newShop?cityId=" + cityId,
                   success: function (data) {
                       var list = data.data;
                       if (list != null && list.length > 0) {
                           $('.goodShopList').html('<div onclick="bannerType(' + list[0].afterType
                                                   + ','
                                                   + list[0].url + ',' + list[0].merchantId
                                                   + ')"><img src="' + list[0].picture
                                                   + '" alt=""></div>');
                       }
                   }
               });
    }

    /**根据地域代码找到对应的城市*/
    function getCity(cityCode, adCode) {
        console.log('寻找三级地域中。。。。。。。');
        for (var i = 0; i < citySize; i++) {
            if (cityList[i].code == adCode) {
                console.log('寻找到三级地域=' + cityList[i]);
                return cityList[i];
            }
        }
        console.log('寻找二级地域中。。。。。。。');
        for (var j = 0; j < citySize; j++) {
            if (cityList[j].code == cityCode) {
                console.log('寻找到二级地域=' + cityList[j]);
                return cityList[j];
            }
        }
        return null;
    }
    /*****异步加载商家列表*/
    var mainContent = $('.goodBusinessmenList'), len = 0, currPage = 1;
    function ajaxMerchantList() {
        console.log('page======' + currPage);
        if (currPage == 1) {
            mainContent.html('');
            if (gps.type == null) {
                goodShop(gps.cityId);
            }
        }
        $.ajax({
                   type: "post",
                   url: "/merchant/reload?page=" + currPage,
                   data: gps,
                   success: function (data) {
                       var list = data.data, content = '';
                       if (list != null) {
                           len = list.length;
                           for (var i = 0; i < len; i++) {
                               content +=
                               '<div class="img" onclick="detail(' + list[i].id + ','
                               + list[i].distance + ')"><img src="' + list[i].doorPic
                               + '" alt=""></div><div class="text"><p>' + list[i].name
                               + '</p><div><p>' + list[i].typeName
                               + '</p><p>' + list[i].area + '</p>' + (
                                       gps.status == 1
                                               ? "<p>" + (list[i].distance > 1000
                                               ? ((list[i].distance
                                                   / 1000).toFixed(1)
                                                  + "km")
                                               : list[i].distance
                                                 + "m"
                                       ) + "</p>" : ""
                               ) + '</div></div>';
                           }
                           mainContent.html(mainContent.html() + content);
                       }
                   }
               });
    }
    /******下拉加载更多*/
    $(window).scroll(function () {
        var scrollTop = $(this).scrollTop();
        var scrollHeight = $(document).height();
        var windowHeight = $(this).height();
        if (scrollTop + windowHeight >= scrollHeight) {
            if (len >= 10) {
                //请求数据
                currPage++;
                console.log("this is in" + currPage);
                ajaxMerchantList(currPage)
            }
        }
    });
    /**微信获取用户位置信息*/
    wx.config({
                  debug: false,
                  appId: '${wxConfig['appId']}',
                  timestamp: ${wxConfig['timestamp']},
                  nonceStr: '${wxConfig['noncestr']}',
                  signature: '${wxConfig['signature']}',
                  jsApiList: [
                      'getLocation'
                  ]
              });
    wx.ready(function () {
        //获取地理位置
        wx.getLocation({
                           type: 'wgs84', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
                           success: function (res) {
                               var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
                               var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
                               gps.status = 1;
                               gps.latitude = latitude;
                               gps.longitude = longitude;
                               var lnglatXY = [longitude, latitude];
                               AMap.service('AMap.Geocoder', function () {
                                   geocoder = new AMap.Geocoder({});
                                   //逆地理编码
                                   geocoder.getAddress(lnglatXY, function (status, result) {
                                       if (status === 'complete' && result.info === 'OK') {
                                           //获得了有效的地址信息:
                                           var currCity = result.regeocode.addressComponent.city;
                                           var cityCode = result.regeocode.addressComponent.citycode;
                                           var district = result.regeocode.addressComponent.district;
                                           var adcode = result.regeocode.addressComponent.adcode;
                                           console.log('cityName=' + currCity);
                                           console.log('citycode=' + cityCode);
                                           console.log('district=' + district);
                                           console.log('adcode=' + adcode);
                                           console.log('addressComponent='
                                                       + result.regeocode.formattedAddress);
                                           var city = getCity(cityCode, adcode);
                                           if (city != null) {
                                               $(".thisCity").html(city.name);
                                               gps.cityId = city.id;
                                           } else {
                                               $(".thisCity").html(currCity);
                                           }
                                           $("#loading").hide();
                                           ajaxMerchantList();
                                       } else {
                                           //获取地址失败
                                           $("#loading").hide();
                                           ajaxMerchantList();
                                       }
                                   });
                               });
                           },
                           fail: function (res) {
                               gps.status = 0;
                               $("#loading").hide();
                               ajaxMerchantList();
                           },
                           cancel: function (res) {
                               gps.status = 0;
                               $("#loading").hide();
                               ajaxMerchantList();
                           }
                       });
    });
    wx.error(function (res) {
        gps.status = 0;
        ajaxMerchantList();
    });
</script>
<script>
    $(".typeInfo_").hide();
    $(".upDown").click(function () {
        $(".blank").css("height", "1000px");
        $(".blank").fadeIn();
        $(".cityList").slideToggle();
        $(".blank").fadeIn();
    });
    $(".positioning").click(function (event) {
        event.stopPropagation();
    });
    $(".blank").click(function (event) {
        event.stopPropagation();
        $(".cityList").slideUp();
        $(".blank").fadeOut();
        $(".blank").css("height", "0px");
    });
    $(".typeInfo > div").click(function () {
        var name = $(this).html();
        currPage = 1;
        if (name == "全部") {
            $(".typeInfo_").hide();
            $(".goodShop").show();
            $(".goodShopList").show();
            $(".goodBusinessmen").css("margin-top", "10px");
            $(".goodBusinessmenList").css("margin-top", "-20px");
            $(".goodBusinessmen").show();
            gps.type = null;
        } else {
            $(".typeInfo_").show();
            $(".goodShop").hide();
            $(".goodShopList").hide();
            $(".goodBusinessmenList").css("margin-top", "115px");
            $(".goodBusinessmen").hide();
            gps.type = $(this).attr('id').split('_')[1];
        }
        ajaxMerchantList();
    });
    $(".typeInfo_ > div").click(function () {
        currPage = 1;
        gps.condition = $(this).attr('id').split('_')[1];
        ajaxMerchantList();
    });
    function action(x) {
        $("." + x).children().click(function () {
            $("." + x).children().removeClass("active");
            $(this).addClass("active");
        });
    }
    action("typeInfo");
    action("typeInfo_");
</script>
</html>
