<%--
  Created by IntelliJ IDEA.
  User: wcg
  Date: 16/4/18
  Time: 下午2:18
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
    <link rel="stylesheet" type="text/css" href="${resourceUrl}/css/shoppingCart.css"/>
    <script type="text/javascript" src="${resourceUrl}/js/jquery-2.0.3.min.js"></script>
</head>

<body>
<!--底部菜单-->
<%@include file="/WEB-INF/weixin/common/shopfoot.jsp" %>
<div class="mui-content">
    <div class="empty" id="empty" style="display: none">
        <div class="empty-icon"></div>
        <p class="empty-ttl">您的购物车中没有商品，快去选购吧！</p>
        <a class="empty-btn" href="/weixin/shop">去逛逛</a>
    </div>

    <ul id="list1" class="mui-table-view"></ul>
    <%--<p class="empty" ></p>--%>

    <div class="mui-input-row mui-checkbox mui-left" id="chose">
        <input name="checkbox" value="Item 1" type="checkbox" id="checkboxAll">

        <div class="mui-table mui-pull-right">
            <div class="mui-table-cell mui-col-xs-6">
                合计：￥<font class="priceSum">0</font>
            </div>
            <div class="mui-table-cell mui-col-xs-6" id="buy">
                去下单（<font class="orderNum">0</font>）
            </div>
        </div>
    </div>

</div>
<script src="${resourceUrl}/js/mui.min.js"></script>
<script>
    document.title="购物车";
    //		左滑删除
    mui.init();
    (function ($) {
        //拖拽后显示操作图标，点击操作图标删除元素；
        $('#list1').on('tap', '.mui-btn', function (event) {
            var elem = this;
            var li = elem.parentNode.parentNode;
            var productId = li.getElementsByClassName("productId")[0].value;
            var productSpectId = li.getElementsByClassName("productSpecId")[0].value;
            $.ajax({
                       type: "get",
                       url: "/weixin/cart/deleteCart?product=" + productId + "&productSpec="
                            + productSpectId,
                       contentType: "application/json",
                       success: function (data) {
                           if (data.status == 200) {
                               li.parentNode.removeChild(li);
                               getTotalPrice();
                               if ($(".mui-table-view-cell").length == 0) {
                                   document.getElementById("empty").style.display = "block";
                                   document.getElementById("empty").style.display = "block";
                                   document.getElementById("chose").style.display = "none";
                                   document.getElementById("cart-number").style.display = "none";
                               }
                           } else {
                               alert("出现未知错误");
                           }
                       }
                   });
        });
        var btnArray = ['确认', '取消'];
    })(mui);

    //请求ajax
    var table = document.body.querySelector('#list1');
    var url = '/weixin/cart/ajax';
    mui.ajax(url, {
        dataType: 'json',//服务器返回json格式数据
        async: false,//异步
        type: 'get',//HTTP请求类型
        timeout: 10000,//超时时间设置为10秒；
        success: function (data) {
            //判断数据是否为空
            if (data == "" || data == null) {
                document.getElementById("empty").style.display = "block";
                document.getElementById("empty").style.display = "block";
                document.getElementById("chose").style.display = "none";
            } else {
                $('.empty').css({'display': 'none'});
                for (var i = 0; i < data.length; i++) {
                    var li = document.createElement('li');
                    var liHtmlStr = '<li class="mui-table-view-cell"><div class="mui-slider-right mui-disabled">';
                    liHtmlStr +=
                    '<input type="hidden" class="productId" value="' + data[i].product.id + '" />';
                    liHtmlStr +=
                    '<input type="hidden" class="productSpecId" value="' + data[i].productSpec.id
                    + '" />';
                    liHtmlStr +=
                    '<a class="mui-btn mui-btn-red">删除</a></div><div class="mui-slider-handle">';
                    liHtmlStr += '<div class="mui-input-row mui-checkbox mui-left">';
                    liHtmlStr +=
                    '<input name="checkbox" class="checboxOne" value="Item 1" type="checkbox" >';
                    liHtmlStr +=
                    '<div class="mui-table mui-pull-right"><div class="mui-table-cell mui-col-xs-3">';
                    liHtmlStr +=
                    '<span class="order-img"><img src="' + data[i].productSpec.picture
                    + '" alt=""></span>';
                    liHtmlStr +=
                    '</div><div class="mui-table-cell mui-col-xs-11 mui-text-left mui-pull-right">';
                    liHtmlStr +=
                    '<h5 class="mui-ellipsis">' + data[i].product.name + '</h5><h6>规格：<font>'
                    + data[i].productSpec.specDetail + '</font></h6>';
                    liHtmlStr +=
                    '<p class="chose_right right_all"><button class="btnCut"></button>';
                    liHtmlStr +=
                    '<input type="number" value="' + data[i].productNumber
                    + '" class="num"/><button class="btnAdd"></button>';
                    liHtmlStr +=
                    '<span class="order-price mui-pull-right">￥<font class="price">'
                    + toDecimal(data[i].productSpec.price
                                / 100)
                    + '<font></span></p><p class="repository">' + data[i].productSpec.repository
                    + '</p>';
                    liHtmlStr += '</div></div></div></div></li>';
                    li.innerHTML = liHtmlStr;
                    table.appendChild(li);
                }
            }

        },
        error: function (xhr, type, errorThrown) {
            //异常处理；
            console.log(type);
        }
    });

    //			全选功能
    $(function () {

//				全选和取消反选
        $("#checkboxAll").click(function () {
            if (this.checked) {
                $(".checboxOne").prop("checked", true);
                getTotalPrice();
            } else {
                $(".checboxOne").prop("checked", false);
                getTotalPrice();
            }
        });
        $(".checboxOne").click(function (thisObj) {
            var price = toDecimal($(this).parent().find('.price').text());
            if (!this.checked) {
                $("#checkboxAll").prop("checked", false);
                getTotalPrice();
            } else {
                if ($(".checboxOne:checked").length == $(".mui-table-view-cell").length) {
                    $("#checkboxAll").prop("checked", true);
                }
                getTotalPrice();
            }
        });
//		        获取选中的li整合成一个数组valArr
        $("#buy").click(function () {
            var valArr = [];
            $(".checboxOne:checked").each(function (i) {
                valArr.push($(this).parents('li'));
            });
            console.log(valArr);
        });

//		        手动输入购买数量
        $('.num').each(function (i) {
            $('.num').eq(i).bind('blur', function () {
                judgeFun($(this), $(this).parent().next().text());
                $(this).parents('li').find('.checboxOne').prop("checked", true);
                if ($(".checboxOne:checked").length == $(".mui-table-view-cell").length) {
                    $("#checkboxAll").prop("checked", true);
                }
                getTotalPrice();
                changeProductNumber($(this).parents('li').find(".productId").val(),
                                    $(this).parents('li').find(".productSpecId").val(),
                                    $(this).val())
            })
        });
    })

    function numAdd(num1, num2) {
        var baseNum, baseNum1, baseNum2;
        try {
            baseNum1 = num1.toString().split(".")[1].length;
        } catch (e) {
            baseNum1 = 0;
        }
        try {
            baseNum2 = num2.toString().split(".")[1].length;
        } catch (e) {
            baseNum2 = 0;
        }
        baseNum = Math.pow(10, Math.max(baseNum1, baseNum2));
        return (num1 * baseNum + num2 * baseNum) / baseNum;
    }
    ;

    //强制保留两位小数
    function toDecimal(x) {
        var f = parseFloat(x);
        if (isNaN(f)) {
            return false;
        }
        var f = x * 100 / 100;
        var s = f.toString();
        var rs = s.indexOf('.');
        if (rs < 0) {
            rs = s.length;
            s += '.';
        }
        while (s.length <= rs + 2) {
            s += '0';
        }

        return s.substring(0, s.indexOf(".") + 3);
        ;

    }

    //			加减
    var btnAdds = $('.btnAdd');
    var btnCuts = $('.btnCut');
    clickAdd(btnAdds);
    clickCut(btnCuts);
    //规格选择
    function clickAdd(s) {
        s.each(function (i) {
            s.eq(i).bind('touchstart', function () {
                $(this).parents('li').find('.checboxOne').prop("checked", true);
                if ($(".checboxOne:checked").length == $(".mui-table-view-cell").length) {
                    $("#checkboxAll").prop("checked", true);
                }
                //获取当前的规格的dom节点序号
                var index = $(this).index();
                $(this).prev().val(eval($(this).prev().val()) + 1);
                judgeFun($(this).prev(), $(this).parent().next().text());
                getTotalPrice();
                changeProductNumber($(this).parents('li').find(".productId").val(),
                                    $(this).parents('li').find(".productSpecId").val(),
                                    $(this).prev().val())

            })
        });
    }
    function clickCut(s) {
        s.each(function (i) {
            s.eq(i).bind('touchstart', function () {
                //获取当前的规格的dom节点序号
                var index = $(this).index();
                $(this).parents('li').find('.checboxOne').prop("checked", true);
                if ($(".checboxOne:checked").length == $(".mui-table-view-cell").length) {
                    $("#checkboxAll").prop("checked", true);
                }
                if ($(this).next().val() > 1) {
                    $(this).next().val(eval($(this).next().val()) - 1);
                    judgeFun($(this).next(), $(this).parent().next().text());
                    changeProductNumber($(this).parents('li').find(".productId").val(),
                                        $(this).parents('li').find(".productSpecId").val(),
                                        $(this).next().val())
                }
                getTotalPrice();

            })
        });
    }
    function judgeFun(num, maxNum) {
        var maxVal;
        maxNum > 50 ? maxVal = 50 : maxVal = eval(maxNum);
        if (num.val() > maxVal) {
            alert('此商品的最多可购买' + maxVal + '件');
            num.val(maxVal);
            return;
        } else if (num.val() < 1) {
            num.val(1);
        }
    }
    var count = 0;
    var totalPrice = 0;
    var totalProduct = 0;
    function getTotalPrice() {
        count = 0;
        totalPrice = 0;
        totalProduct = 0;
        $(".num").each(function () {
            totalProduct += eval($(this).val());
        });
        $("#cart-number").text(totalProduct);
        $(".checboxOne:checked").each(function (i) {
            var price = $(this).parents('li').find(".price").text();
            var number = $(this).parents('li').find(".num").val();
            totalPrice += toDecimal(price) * number;
            count++;
        });
        $('.priceSum').text(toDecimal(totalPrice));
        $('.orderNum').text(count);
    }

    function changeProductNumber(productId, productSpecId, number) {
        $.ajax({
                   type: "get",
                   url: "/weixin/cart/changeNumber?product=" + productId + "&productSpec="
                        + productSpecId + "&number=" + number,
                   contentType: "application/json",
                   success: function (data) {
                       if (data.status == 200) {
                           $("#cart-number").attr("style", "display:block")
                           $("#cart-number").text(data.msg);
                       } else {
                           alert("出现未知错误");
                       }
                   }
               });
    }
    $("#buy").bind("tap", function () {
        var cartDetailDtos = [];
        if ($(".checboxOne:checked").length != 0) {
            $(".checboxOne:checked").each(function (i) {
                var cartDetailDto = {};
                var product = {};
                product.id = $(this).parents('li').find(".productId").val();
                var productSpec = {};
                productSpec.id = $(this).parents('li').find(".productSpecId").val();
                cartDetailDto.product = product;
                cartDetailDto.productSpec = productSpec;
                judgeFun($(this).parents('li').find(".num"),
                         eval($(this).parents('li').find(".repository").text()));
                cartDetailDto.productNumber = $(this).parents('li').find(".num").val();
                cartDetailDtos.push(cartDetailDto);
            });
            $.ajax({
                       type: "post",
                       url: "/weixin/cart/createCartOrder",
                       contentType: "application/json",
                       data: JSON.stringify(cartDetailDtos),
                       success: function (data) {
                           if (data.status == 200) {
                               location.href = "${wxRootUrl}/weixin/order/" + data.msg+"?flag=false";
                           } else {
                               alert(data.msg);
                               location.href = "${wxRootUrl}/weixin/orderDetail";
                           }
                       }
                   });
        } else {
            alert("请选择商品");
        }

    });
</script>
</body>

</html>
