<%--
  Created by IntelliJ IDEA.
  User: wcg
  Date: 16/3/28
  Time: 下午3:05
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
    <link href="${resourceUrl}/css/mui.poppicker.css" rel="stylesheet"/>
    <link rel="stylesheet" type="text/css" href="${resourceUrl}/css/createAddress.css" />
    <link rel="stylesheet" type="text/css" href="${resourceUrl}/css/mui.picker.min.css"/>
    <script src="${resourceUrl}/js/jquery-1.11.3.min.js"></script>
</head>

<body>
<!--底部菜单-->
<nav class="mui-bar mui-bar-tab" style="background: #323232;" onclick="formSubmit()">
    <a class="mui-tab-item mui-active">
        <span class="mui-tab-label" style=" color: #cca97a">保存</span>
    </a>
</nav>
<div class="mui-content">
    <div class="mui-content-padded">
        <form class="mui-input-group" action="/weixin/order/${orderId}" method="post">
            <div class="mui-input-row">
                <label>姓名:</label>
                <input type="text" name="name" class="mui-input-clear" id="name"
                       placeholder="请输入您的姓名">
            </div>
            <div class="mui-input-row">
                <label>手机号码:</label>
                <input type="text" name="phoneNumber" class="mui-input-clear"
                       placeholder="请输入你的手机号码" id="phoneNumber">
            </div>
            <div class="mui-input-row" id='showCityPicker3'>
                <label>所在地区:</label>
                <span id='cityResult3' type="text" class="mui-input-clear"></span>
                <a class="mui-action-back mui-icon mui-icon-arrowdown mui-pull-right"></a>
            </div>
            <div class="mui-input-row" style="height: auto">
                <label>详细地址:</label>
                <textarea id="location" name="location" placeholder="请输入您的详细地址"></textarea>
                <a id="texrarea-close" class=" mui-icon mui-icon-clear mui-pull-right"></a>
            </div>
            <input type="hidden" name="province" id="province"/>
            <input type="hidden" name="city" id="city"/>
            <input type="hidden" name="county" id="county"/>
            <input type="hidden" name="id" id="id"/>
        </form>
    </div>
</div>
<script src="${resourceUrl}/js/mui.min.js"></script>
<script src="${resourceUrl}/js/mui.picker.min.js"></script>
<script src="${resourceUrl}/js/mui.poppicker.js"></script>
<script src="${resourceUrl}/js/city.data.js" type="text/javascript" charset="utf-8"></script>
<script src="${resourceUrl}/js/city.data-3.js" type="text/javascript" charset="utf-8"></script>
<script>
    document.title="收货地址";
    if (${address!=null}) {
        var province = "${address.province}" + " " + "${address.city}" + " " + "${address.county}";
        $("#cityResult3").text(province);
        $("#name").val("${address.name}");
        $("#phoneNumber").val("${address.phoneNumber}");
        $("#location").val("${address.location}");
    }
    (function ($, doc) {
        $.init();
        $.ready(function () {
            var cityPicker3 = new $.PopPicker({
                                                  layer: 3
                                              });
            cityPicker3.setData(cityData3);
            var showCityPickerButton = doc.getElementById('showCityPicker3');
            var cityResult3 = doc.getElementById('cityResult3');
            showCityPickerButton.addEventListener('tap', function (event) {
                cityPicker3.show(function (items) {
                    cityResult3.innerText =
                    (items[0] || {}).text + " " + (items[1] || {}).text + " " + (items[2]
                                                                                 || {}).text;
                    //返回 false 可以阻止选择框的关闭
                    //return false;
                });
            }, false);
        });
    })(mui, document);

    $(".mui-bar-tab").bind("tap", function () {
        if ($("#name").val() == null || $("#name").val() == "") {
            alert("请输入姓名");
            return;
        }
        if ($("#phoneNumber").val() == null || $("#phoneNumber").val() == "") {
            alert("请输入手机号");
            return;
        }
        var re = /^1\d{10}$/;
        if (!re.test($("#phoneNumber").val())) {
            alert("请输入正确的手机号");
            return;
        }
        if ($("#location").val() == null || $("#location").val() == "") {
            alert("请输入详细地址");
            return;
        }
        if ($("#name").val() == null || $("#name").val() == "") {
            alert("请输入姓名");
            return;
        }
        var cityResult3 = $("#cityResult3");
        var strs = cityResult3.text().split(" ");
        if (strs[0] == null || strs[0] == "") {
            alert("请输入省");
            return;
        }
        if (strs[1] == null || strs[1] == "") {
            alert("请输入市");
            return;
        }

        $("#province").val(strs[0]);
        $("#city").val(strs[1]);
        $("#county").val(strs[2]);

        if (${address!=null}) {
            $("#id").val(${address.id});
        }
        $(".mui-input-group").submit();
        $(".mui-bar-tab").unbind("tap");

    });
    //判断是否为苹果
    var isIPHONE = navigator.userAgent.toUpperCase().indexOf("IPHONE")!= -1;

    // 元素失去焦点隐藏iphone的软键盘
    function objBlur(id){
        if(typeof id != "string") throw new Error("objBlur()参数错误");
        var obj = document.getElementById(id),
                docTouchend = function(event){
                    if(event.target!= obj){
                        setTimeout(function(){
                            obj.blur();
                            document.removeEventListener("touchend", docTouchend,false);
                        },0);
                    }
                };
        if(obj){
            obj.addEventListener("focus", function(){
                document.addEventListener("touchend", docTouchend,false);
            },false);
        }else{
            throw new Error("objBlur()没有找到元素");
        }
    }

    if(isIPHONE){
        var input = new objBlur("location");
        var input = new objBlur("name");
        var input = new objBlur("phoneNumber");
        input=null;
    }

    $.fn.autoHeight = function(){

        function autoHeight(elem){
            elem.style.height = 'auto';
            elem.scrollTop = 0; //防抖动
            elem.style.height = elem.scrollHeight + 'px';
        }

        this.each(function(){
            autoHeight(this);
            $(this).on('input  propertychange', function(){
                autoHeight(this);
                $('#texrarea-close').css({'display':'block'});
                $(document).one("click", function () {//对document绑定一个影藏Div方法
                    $(".mui-content .page_more").fadeOut();
                    $('#texrarea-close').css({'display':'none'});
                });
                event.stopPropagation();//阻止事件向上冒泡
            });
        });

    }
    $('#location').autoHeight();
    $('#texrarea-close').click(function () {
        $('#location').val('').height(32);
        $(this).css({'display':'none'});
    })
</script>
</body>
</html>
