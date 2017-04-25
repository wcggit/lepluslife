<%--
  Created by IntelliJ IDEA.
  User: zhangwen
  Date: 2017/4/15
  Time: 11:18
  金币商品分类列表页
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
    <link rel="stylesheet" href="${resourceUrl}/frontRes/css/reset.css">
    <link rel="stylesheet" href="${resourceUrl}/frontRes/gold/list/css/weChatInfo.css">
    <title>臻品</title>
</head>
<body>
<section class="type">
    <c:forEach items="${typeList}" var="type">
        <div id="productType_${type.id}">${type.type}</div>
    </c:forEach>
</section>
<section class="ad">
    <c:forEach items="${typeList}" var="type">
        <div id="typePic_${type.id}" style="display: none"><img src="${type.picture}" alt=""></div>
    </c:forEach>
</section>
<section class="commodityList fixClear">

</section>
</body>
<script src="${resourceUrl}/js/jquery.min.js"></script>
<script>
    var type = eval('${type}'), score = Math.floor(eval('${score}') / 100);
    if (type == null) {
        type = 1;
    }
    var selectE = 'productType_', selectP = 'typePic_', mainContent = $('.commodityList');
    var len = 0, currPage = 1;
    $('#' + (selectE + type)).addClass('active');
    $(".type > div").click(function () {
        currPage = 1;
        $(".type > div").removeClass("active");
        $(".ad > div").css('display', 'none');
        $(this).addClass("active");
        type = $(this).attr('id').split('_')[1];
        $('#' + (selectP + type)).css('display', 'block');
        //分页获取首页臻品列表
        ajaxProductList(type, 1);
    });

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

    function ajaxProductList(typeId, page) {
        console.log('typeId======' + typeId + '&&&page======' + page);
        if (page == 1) {
            mainContent.html('');
        }
        $.ajax({
                   type: "get",
                   url: "/front/gold/list?productType=" + typeId + "&page=" + page,
                   success: function (data) {
                       var list = eval("(" + data + ")"), content = '';
                       if (list != null) {
                           len = list.length;
                           for (var i = 0; i < len; i++) {
                               content += '<div onclick="goProductDetail(' + list[i].id
                                          + ')"><div class="commodityTab">';
                               if (list[i].markType != null) {
                                   content +=
                                   '<img src="http://www.lepluslife.com/resource/frontRes/gold/list/img/'
                                   + list[i].markType
                                   + '.png" alt="">';
                               }
                               content += '</div><div class="commodityImg"><img src="'
                                          + list[i].picture
                                          + '" alt=""></div><p>' + list[i].name
                                          + '</p><p>'
                                          + toDecimal(list[i].price / 100)
                                          + '元</p><p>或' + toDecimal(list[i].price / 100 - score)
                                          + '元+' + score + '金币</p></div>';
                           }
                           mainContent.html(mainContent.html() + content);
                       }
                   }
               });
    }
    ajaxProductList(type, 1);

    function goProductDetail(productId) { //go详情页
        window.location.href = "/front/gold/weixin/p?productId=" + productId;
    }

    $(window).scroll(function () {
        var scrollTop = $(this).scrollTop();
        var scrollHeight = $(document).height();
        var windowHeight = $(this).height();
        if (scrollTop + windowHeight >= scrollHeight) {
            if (len >= 10) {
                console.log("this is in" + type);
                //请求数据
                currPage++;
                ajaxProductList(type, currPage);
            }
        }
    });
</script>
</html>
