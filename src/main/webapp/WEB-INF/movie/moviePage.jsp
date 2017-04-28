<%--
  Created by IntelliJ IDEA.
  User: xf
  Date: 2017/4/27
  Time: 14:37
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<html>
<head>
    <title>乐加电影页面</title>
</head>
<body>
<div>上方轮播图</div>
<div id="topBanners">
</div>
<div>下方电影图</div>
<div id="hotBanners">
</div>
</body>
<script src="http://www.lepluslife.com/resource/backRes/js/jquery.min.js"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
<script type="text/javascript"
        src="http://webapi.amap.com/maps?v=1.3&key=48f94cad8f49fc73c9ba59b281bb1e84"></script>
<script>
    loadBanner();
    function loadBanner() {
        $.ajax({
            type: "get",
            url: "/front/movie/topBanner",
            success: function (result) {
                var data = result.data;
                var content = "";
                for(var i=0;i<data.length;i++) {
                    content+="<span><img src="+data[i].picture+"></span>";
                }
//                console.log(JSON.stringify());
                document.getElementById("topBanners").innerHTML+=content;
            }
        });

        $.ajax({
            type: "get",
            url: "/front/movie/hotMovieBanner",
            success: function (result) {
                var data = result.data;
                var content = "";
                for(var i=0;i<data.length;i++) {
                    content+="<span><img src="+data[i].picture+">"+data[i].title+"-"+data[i].introduce+"</span>";
                }
//                console.log(JSON.stringify());
                document.getElementById("hotBanners").innerHTML+=content;
            }
        });
    }
</script>
</html>
