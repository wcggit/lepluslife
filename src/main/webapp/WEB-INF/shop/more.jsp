<%--
  Created by IntelliJ IDEA.
  User: zhangwen
  Date: 2017/4/17
  Time: 17:20
  好店推荐查看更多
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
    <title>好店推荐</title>
    <link rel="stylesheet" href="${resourceUrl}/frontRes/css/reset.css">
    <link rel="stylesheet" href="${resourceUrl}/frontRes/shop/more/css/forMore.css">
</head>
<body>
<section class="positioning">
    <div class="upDown fixClear">
        <div><img src="${resourceUrl}/frontRes/shop/index/img/addressIcon.png" alt=""></div>
        <div class="thisCity">${city.name}</div>
        <div class="arrowUp"></div>
    </div>
    <div class="cityList"></div>
</section>
<section class="blank">

</section>
<section class="commodityList">
</section>
</body>
<script src="${resourceUrl}/js/jquery.min.js"></script>
<script>

    /**异步加载城市列表*/
    var cityList = '', citySize = 0, cityId = ${city.id};
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
                       cityId = $(this).attr('id').split('_')[1];
                       goodShopList();
                   });
               }
           });
    /**加载好店推荐*/
    function goodShopList() {
        $('.commodityList').html('');
        $.ajax({
                   type: "get",
                   url: "/app/banner/newShop?cityId=" + cityId,
                   success: function (data) {
                       var list = data.data, content = '';
                       if (list != null && list.length > 0) {
                           for (var i = 0; i < list.length; i++) {
                               content += '<div onclick="bannerType(' + list[i].afterType
                                          + ','
                                          + list[i].url + ',' + list[i].merchantId
                                          + ')"><div><img src="' + list[i].picture
                                          + '" alt=""></div><p>' + list[i].shopName
                                          + '</p><p>' + list[i].title
                                          + '</p><p>' + list[i].content
                                          + '</p></div>';
                           }
                           $('.commodityList').html(content);
                       }
                   }
               });
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
    goodShopList();
</script>
</html>
