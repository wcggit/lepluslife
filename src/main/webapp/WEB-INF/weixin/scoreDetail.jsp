<%--
  Created by IntelliJ IDEA.
  User: wcg
  Date: 16/3/18
  Time: 下午9:01
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/WEB-INF/commen.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    <link rel="stylesheet" type="text/css" href="${resourceUrl}/css/tradeDetail.css"/>
    <script src="${resourceUrl}/js/jquery-1.11.3.min.js"></script>
</head>

<body>
<!--头部-->
    <!--明细记录-->
    <div id="tabbar-with-detail2" class="mui-control-content mui-active" style="padding-top: 0vw;" >
        <c:forEach items="${scoreADetails}" var="scoreADetail">
            <c:if test="${scoreADetail.number>0}">
                <div class="list">
                    <div class="list-top">
                        <div class="top-left">
                            <span class="hongbaoadd"></span>
                        </div>
                        <div class="top-right">
                            <p class="right-ttl">
                                <span class="trade-font-red">+<font>${scoreADetail.number/100}</font>红包</span>
                                <span class="trade-font-red">${scoreADetail.operate}</span>
                            </p>

                            <p class="right-pra"><font><fmt:formatDate
                                    value="${scoreADetail.dateCreated}" type="both"/></font></p>
                        </div>
                    </div>
                </div>
            </c:if>
            <c:if test="${scoreADetail.number<0}">
                <div class="list">
                    <div class="list-top">
                        <div class="top-left">
                            <span class="hongbaocut"></span>
                        </div>
                        <div class="top-right">
                            <p class="right-ttl">
                                <span class="trade-font-black"><font>${scoreADetail.number/100}</font>红包</span>
                                <span class="trade-font-black">${scoreADetail.operate}</span>
                            </p>

                            <p class="right-pra"><font><fmt:formatDate
                                    value="${scoreADetail.dateCreated}" type="both"/></font></p>
                        </div>
                    </div>
                </div>
            </c:if>
        </c:forEach>
        <c:forEach items="${scoreBDetails}" var="scoreBDetail">
            <c:if test="${scoreBDetail.number>0}">
                <div class="list">
                    <div class="list-top">
                        <div class="top-left">
                            <span class="jifenadd"></span>
                        </div>
                        <div class="top-right">
                            <p class="right-ttl">
                                <span class="trade-font-red">+<font>${scoreBDetail.number}</font>积分</span>
                                <span class="trade-font-red">${scoreBDetail.operate}</span>
                            </p>

                            <p class="right-pra"><font><fmt:formatDate
                                    value="${scoreBDetail.dateCreated}" type="both"/></font></p>
                        </div>
                    </div>
                </div>
            </c:if>
            <c:if test="${scoreBDetail.number<0}">
                <div class="list">
                    <div class="list-top">
                        <div class="top-left">
                            <span class="jifencut"></span>
                        </div>
                        <div class="top-right">
                            <p class="right-ttl">
                                <span class="trade-font-black"><font>${scoreBDetail.number}</font>积分</span>
                                <span class="trade-font-black">${scoreBDetail.operate}</span>
                            </p>

                            <p class="right-pra"><font><fmt:formatDate
                                    value="${scoreBDetail.dateCreated}" type="both"/></font></p>
                        </div>
                    </div>
                </div>
            </c:if>
        </c:forEach>
    </div>
</div>

</body>
<script type="text/javascript" src="${resourceUrl}/js/mui.min.js"></script>

<script type="text/javascript">
    document.title = "明细记录";
</script>

</html>
