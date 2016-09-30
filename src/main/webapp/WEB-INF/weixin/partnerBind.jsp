<%--
  Created by IntelliJ IDEA.
  User: wcg
  Date: 16/9/30
  Time: 下午3:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/commen.jsp" %>
<style>
    #main .top3 img{
        width: 90.667vw;
        height: 60.333vw;
        text-align: center;
        line-height: 13.333vw;
        color: #fff;
        font-size: 4.267vw;
        background: #333;
        border-radius: 5px;
    }
</style>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <!--强制以webkit内核来渲染-->
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <!--按设备宽度缩放，并且用户不允许手动缩放-->
    <meta name="format-detection" content="telephone=no">
    <!--不显示拨号链接-->
    <title>Title</title>
    <link rel="stylesheet" href="${resourceUrl}/css/bindWeixin.css">
    <script src="${resourceUrl}/js/jquery-2.0.3.min.js"></script>
</head>
<body>
<div id="main">
    <div class="faceImg">
        <img width="80vw" height="80vw" src="${weiXinUser.headImageUrl}" alt="">
    </div>
    <p class="ttl">${weiXinUser.nickname}</p>
    <!--第一种情况-->
    <div class="top top1">
        <p>该微信号已经绑定了合伙人，请联系乐加客服处理： 400-412-800</p>
    </div>
    <!--第二种情况-->
    <div class="top top2">
        <p>合伙人锁定会员名额已满，暂时无法绑定微信号</p>
    </div>
    <!--第三种情况-->
    <div class="top top2">
        <p>合伙人已绑定了微信号<br>请先解绑</p>
    </div>
    <!--第5种情况-->
    <div class="top top2">
        <p>合伙人已绑定该微信号</p>
    </div>
    <!--第4种情况 成功-->
    <div class="top top3">
        <p>确定要将该微信号和合伙人<br><font>${partner.partnerName}</font> 绑定吗？</p>
        <div onclick="bindPartner()">确认绑定</div>
    </div>
</div>
</body>
</html>
<script>
    if (${code==1}) {
        $(".top1")[0].style.display = "block";
        $(".top3")[0].style.display = "none";
    } else if (${code==2}) {
        $(".top2")[0].style.display = "block";
        $(".top3")[0].style.display = "none";
    } else if (${code==3}) {
        $(".top2")[1].style.display = "block";
        $(".top3")[0].style.display = "none";
    }else if (${code==5}) {
        $(".top2")[2].style.display = "block";
        $(".top3")[0].style.display = "none";
    }
    function bindPartner() {
        $.get("/weixin/partner/bind/"+${partner.partnerSid},function(result){
            if(result.status==200){
              var content =   $(".top3")[0];
                content.innerHTML = "";
                content.innerHTML+="<img class='contentImg' src='${resourceUrl}/images/bindSuccess.png'>"
            }else{
                alert(result.msg);
                var content =   $(".top3")[0];
                content.innerHTML = "";
                content.innerHTML+="<img class='contentImg' src='${resourceUrl}/images/bindFaild.png'>"
            }
                });
    }
</script>
