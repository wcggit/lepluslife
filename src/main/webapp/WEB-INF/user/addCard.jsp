<%--
  Created by IntelliJ IDEA.
  User: zhangwen
  Date: 2017/4/19
  Time: 13:22
  绑定银行卡
--%>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/commen.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="viewport" id="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">
    <meta name="format-detection" content="telephone=no">
    <meta name="format-detection" content="telephone=yes"/>
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="black"/>
    <title>添加银行卡</title>
    <link rel="stylesheet" href="${resourceUrl}/frontRes/css/reset.css">
    <link rel="stylesheet" href="${resourceUrl}/frontRes/user/addCard/css/addCard.css">
</head>
<body>
<section class="addInput">
    <input type="tel" id="cardNum" placeholder="请输入您的的银行卡">
</section>
<section class="addInput">
    <input type="tel" id="phoneNum" value="${user.phoneNumber}" placeholder="请输入您的手机号">
</section>
<section class="Button">
    <button class="addCard" onclick="checkCardNum()">添加</button>
</section>
<section class="layer">
    <div class="img"><img src="${resourceUrl}/frontRes/user/addCard/img/layer-bind.png" alt="">
    </div>
    <p>该卡号已经绑定乐加账户</p>

    <p>如有问题请致电客服：400-0412-800</p>

    <div class="Button">
        <button class="act">一键拨打</button>
        <button>关闭</button>
    </div>
</section>
</body>
<script src="${resourceUrl}/js/jquery.min.js"></script>
<script src="${resourceUrl}/frontRes/js/layer.js"></script>
<script>
    /**弹出异常对话框*/
    function showMsg() {
        layer.open({
                       type: 1,
                       title: false,
                       closeBtn: 0,
                       area: ['80%', '188px'], //宽高
                       content: $(".layer")
                   });
    }
    $(".layer .Button button").click(function () {
        layer.closeAll();
    });
    $("#cardNum").on("keyup", formatBC);
    function formatBC(e) {

        $(this).attr("data-oral", $(this).val().replace(/\ +/g, ""));
        //$("#bankCard").attr("data-oral")获取未格式化的卡号

        var self = $.trim(e.target.value);
        var temp = this.value.replace(/\D/g, '').replace(/(....)(?=.)/g, '$1 ');
        if (self.length > 35) {
            this.value = self.substr(0, 35);
            return this.value;
        }
        if (temp != this.value) {
            this.value = temp;
        }
    }
    setInterval(function () {
        var value = $("#cardNum").val();
        var phone = $("#phoneNum").val();
        if (value == '' || phone == '') {
            $(".addCard").addClass('opacity');
            $(".addCard").attr("disabled", "disabled");
        } else {
            $(".addCard").removeClass('opacity');
            $(".addCard").removeAttr("disabled");
        }
    }, 100);

    /**银行卡查询*/
    var cardInfo = {}, cardNum = '', phoneNum = '', token = '${user.userSid}';
    function checkCardNum() {
        cardNum = '';
        var str = $('#cardNum').val().split(" ");
        for (var i = 0; i < str.length; i++) {
            cardNum += str[i];
        }
        phoneNum = $('#phoneNum').val();
        if ((!(/^1[3|4|5|6|7|8]\d{9}$/.test(phoneNum)))) {
            alert("请输入正确的手机号");
            return false
        }
        console.log(cardNum + '==========' + phoneNum);
        $.ajax({
                   type: "get",
                   url: "/front/card/cardCheck?cardNum=" + cardNum,
                   success: function (data) {
                       if (data.status == 200) {
                           cardInfo = data.data;
                           if (cardInfo.code == 0) {
                               submitCard();
                           } else {
                               alert(cardInfo.msg);
                           }
                       } else {
                           alert(data.msg);
                       }
                   }
               });
    }
    /**银行卡保存信息*/
    function submitCard() {
        var info = cardInfo.data;
        $.ajax({
                   type: "post",
                   url: "/front/card/user/add",
                   data: {
                       token: token,
                       number: cardNum,
                       cardType: info.cardtype,
                       prefixNum: info.cardprefixnum,
                       cardName: info.cardname,
                       bankName: info.bankname,
                       phoneNum: phoneNum,
                       registerWay: 4
                   },
                   success: function (data) {
                       if (data.status == 200) {
                           window.location.href = "/front/user/weixin/cardList";
                       } else {
                           showMsg();
                       }
                   }
               });
    }
</script>
</html>
