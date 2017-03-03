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
    <link rel="stylesheet" href="${resourceUrl}/frontRes/css/tanChuang.css">
</head>
<style>
    .footer {
        width: 100%;
        position: fixed;
        bottom: 0;
        background-color: #FFFFFF;
        border-top: 1px solid #d6d6d6;
        padding: 9px 0 2px 0;
        z-index: 999;
    }

    .footer > div {
        float: left;
        width: 25%;
        text-align: center;
        font-size: 10px !important;
        color: #666;
    }

    .footer > div > div {
        width: 20%;
        margin: 0 auto;
    }

    .footer > div:last-child > div {
        width: 17%;
        margin: 0 auto;
    }

    .footer img {
        width: 100%;
    }

    .footer p {
        margin-top: -4px;
        margin-bottom: -2px !important;
        font-size: 10px !important;
        color: #666 !important;
        font-family: "Microsoft YaHei", tahoma, arial, "Hiragino Sans GB", "\5b8b\4f53", sans-serif !important;
    }
</style>
<body>
<!--底部菜单-->
<%--<%@include file="/WEB-INF/weixin/common/shopfoot.jsp" %>--%>
<div class="mui-content">
    <div class="empty" id="empty" style="display: none">
        <div class="empty-icon"></div>
        <p class="empty-ttl">您的购物车空空如也</p>
        <a class="empty-btn" href="/front/product/weixin/productIndex">去逛逛</a>
    </div>

    <ul id="list1" class="mui-table-view"></ul>
    <%--<p class="empty" ></p>--%>

    <div class="mui-input-row mui-checkbox mui-left" id="chose">
        <input name="checkbox" value="Item 1" type="checkbox" id="checkboxAll">

        <div class="mui-table mui-pull-right">
            <div class="mui-table-cell mui-col-xs-3" style="font-size: 15px;color:#666;">
                已选（<font class="orderNum">0</font>）
            </div>
            <div class="mui-table-cell mui-col-xs-5">
                ￥<font class="priceSum">0</font><span class="minScore"
                                                      style="font-size: 17px;color:#fb991a;">+<font id="totalScore">0</font>积分</span>
            </div>
            <div class="mui-table-cell mui-col-xs-4" id="buy" style="font-size: 15px;">
                下单
            </div>
        </div>
    </div>

</div>
<section class="footer">
    <div onclick="goMenu(1)">
        <div>
            <img src="${resourceUrl}/frontRes/product/hotIndex/img/zhenpin1.png" alt="">
        </div>
        <p>臻品</p>
    </div>
    <div onclick="goMenu(2)">
        <div>
            <img src="${resourceUrl}/frontRes/product/hotIndex/img/miaosha1.png" alt="">
        </div>
        <p>秒杀</p>
    </div>
    <div>
        <div>
            <img src="${resourceUrl}/frontRes/product/hotIndex/img/gouwuche2.png" alt="">
        </div>
        <p>购物车</p>
    </div>
    <div onclick="goMenu(4)">
        <div>
            <img src="${resourceUrl}/frontRes/product/hotIndex/img/dingdan1.png" alt="">
        </div>
        <p>订单</p>
    </div>
</section>
<%--弹窗--%>
<section class="shade-layer" style="display: none">
    <p id="warningInput"></p>

    <div>
        <div class="layerClose">取消</div>
        <div class="yes"></div>
    </div>
</section>
<script src="${resourceUrl}/js/mui.min.js"></script>
<script src="${resourceUrl}/frontRes/js/layer.js"></script>
<script>
    document.title = "购物车";
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
    var table = document.body.querySelector('#list1'), warnType = 0, warnText = '';
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
                    '<h5 class="mui-ellipsis" style="margin-top: 10px;">' + data[i].product.name
                    + '</h5><h6 style="color:#999 !important;margin: 8px 0;">规格：<font>'
                    + data[i].productSpec.specDetail + '</font></h6>';
                    liHtmlStr +=
                    '<p class="chose_right right_all"><span class="order-price mui-pull-left" style="width: 32vw;margin-left: -2%;font-size: 15px">￥<font class="price">'
                    + toDecimal(data[i].productSpec.minPrice
                                / 100)
                    + '</font><span style="font-size: 15px;color:#fb991a;">+<font class="minScore">'
                    + data[i].productSpec.minScore + '</font>积分</span></span>';
                    liHtmlStr +=
                    '<button class="btnCut"></button><input type="number" value="'
                    + data[i].productNumber
                    + '" class="num"/><button class="btnAdd"></button>';
                    liHtmlStr +=
                    '</p><p class="repository">' + data[i].productSpec.repository
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
//        $("#buy").click(function () {
//            var valArr = [];
//            $(".checboxOne:checked").each(function (i) {
//                valArr.push($(this).parents('li'));
//            });
//            console.log(valArr);
//        });

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
    var totalPrice = 0,totalScore = 0;
    var totalProduct = 0;
    function getTotalPrice() {
        count = 0;
        totalPrice = 0;
        totalScore = 0;
        totalProduct = 0;
        $(".num").each(function () {
            totalProduct += eval($(this).val());
        });
        $("#cart-number").text(totalProduct);
        $(".checboxOne:checked").each(function (i) {
            var price = $(this).parents('li').find(".price").text();
            var score = $(this).parents('li').find(".minScore").text();
            var number = $(this).parents('li').find(".num").val();
            totalPrice += toDecimal(price) * number;
            totalScore += score * number;
            count++;
        });
        $('.priceSum').text(toDecimal(totalPrice));
        $('#totalScore').text(totalScore);
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

    function buyNow() {
        var valArr = [];
        $(".checboxOne:checked").each(function (i) {
            valArr.push($(this).parents('li'));
        });
        console.log(valArr);
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
            $("#buy").attr("onclick", "");
            $.ajax({
                       type: "post",
                       url: "/weixin/cart/createCartOrder",
                       contentType: "application/json",
                       data: JSON.stringify(cartDetailDtos),
                       success: function (data) {
                           if (data.status == 200) {
                               location.href = "/front/order/weixin/confirmOrder?orderId=" + data.data.id;
                           } else if (data.status == 5001) {
                               warnType = 1;
                               showTanChuang();
                           } else if (data.status == 5005) {
                               $("#buy").attr("onclick", "buyNow()");
                               warnText = data.data;
                               warnType = 2;
                               showTanChuang();
                           } else if (data.status == 500) {
                               alert("购物车无数据");
                           }
                       }
                   });
        } else {
            alert("请选择商品");
        }
    }

    $("#buy").attr("onclick", "buyNow()");

    function showTanChuang() {
        if (warnType == 1) {
            $("#warningInput").html('未支付订单过多,请支付后再下单');
            $(".yes").html('去处理');
        } else if (warnType == 2) {
            var warnContents = warnText.split('_');
            if (warnContents[1] == 1) {
                $("#warningInput").html("抱歉," + warnContents[0] + "已无库存");
            } else {
                $("#warningInput").html(warnContents[0] + "库存不足" + warnContents[1] + "件");
            }
            $(".yes").html('知道了');
        }
        $(".shade-layer").show();
        layer.open({
                       type: 1,
                       area: ['78%', ''], //宽高
                       content: $(".shade-layer"),
                       title: false,
                       closeBtn: 0,
                       scrollbar: false
                   });
    }
    $(".layerClose").click(function (e) {
        layer.closeAll();
        $(".shade-layer").hide();
        if (warnType == 2) {
            location.reload(true);
        }
    });
    $(".yes").click(function (e) {
        layer.closeAll();
        $(".shade-layer").hide();
        if (warnType == 1) {
            location.href = "/front/order/weixin/orderList";
        } else if (warnType == 2) {
            location.reload(true);
        }
    });

    function goMenu(curIndex) { //go其他菜单页
        if (curIndex == 1) {
            location.href = "/front/product/weixin/productIndex";
        } else if (curIndex == 2) {
            location.href = "/front/product/weixin/hotIndex";
        } else if (curIndex == 4) {
            location.href = "/front/order/weixin/orderList";
        }
    }
</script>
</body>

</html>
