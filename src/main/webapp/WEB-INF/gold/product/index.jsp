<%--
  Created by IntelliJ IDEA.
  User: zhangwen
  Date: 2017/3/3
  Time: 17:48
  金币商城首页
--%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/commen.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no">
    <title>金币商城</title>
    <link rel="stylesheet" type="text/css" href="${resourceUrl}/frontRes/gold/index/css/reset.css">
    <link rel="stylesheet" type="text/css" href="${resourceUrl}/frontRes/gold/index/css/home.css">
</head>
<body>
<div class="main">
    <div class="wdjb">
        <p class="clearfix">
            <span class="left">我的金币</span>
            <span class="right icon-problem"><span class="btn-problem"></span>如何赚金币</span>
        </p>

        <h1 class="clearfix"><fmt:formatNumber
                type="number" value="${score/100}" pattern="0"
                maxFractionDigits="0"/><span>.${score2}</span><span class="right"
                                                                    onclick="goOrderList()">查看订单</span>
        </h1>
    </div>
    <!--话费充值-->
    <div class="hfcz kind" onclick="goRecharge()">
        <h3 class="clearfix kind-title">
            <span class="left">话费充值</span>
            <span class="right">即时到账</span>
        </h3>

        <div class="banner-img">
            <img src="${resourceUrl}/frontRes/gold/index/img/001.png" alt="">
        </div>
        <div class="products">
            <div class="product-item border-right">
                <h1 class="ttl"><span>100</span>元话费</h1>

                <p class="true-price">原价：100元</p>
                <c:if test="${score >= 10000}">
                    <p class="now-price">需付：0元+100金币</p>
                </c:if>
                <c:if test="${score < 10000}">
                    <p class="now-price">需付：<fmt:formatNumber
                            type="number" value="${(10000-score3)/100}"
                            maxFractionDigits="0"/>元+<fmt:formatNumber
                            type="number" value="${score3/100}"
                            maxFractionDigits="0"/>金币</p>
                </c:if>

            </div>
            <div class="product-item border-right">
                <h1 class="ttl"><span>50</span>元话费</h1>

                <p class="true-price">原价：50</p>
                <c:if test="${score >= 5000}">
                    <p class="now-price">需付：0元+50金币</p>
                </c:if>
                <c:if test="${score < 5000}">
                    <p class="now-price">需付：<fmt:formatNumber
                            type="number" value="${(5000-score3)/100}" pattern="0.00"
                            maxFractionDigits="0"/>元+<fmt:formatNumber
                            type="number" value="${score3/100}" pattern="0.00"
                            maxFractionDigits="0"/>金币</p>
                </c:if>
            </div>
            <div class="product-item">
                <h1 class="ttl"><span>20</span>元话费</h1>

                <p class="true-price">原价：20元</p>

                <c:if test="${score >= 2000}">
                    <p class="now-price">需付：0元+20金币</p>
                </c:if>
                <c:if test="${score < 2000}">
                    <p class="now-price">需付：<fmt:formatNumber
                            type="number" value="${(2000-score3)/100}" pattern="0.00"
                            maxFractionDigits="0"/>元+<fmt:formatNumber
                            type="number" value="${score3/100}" pattern="0.00"
                            maxFractionDigits="0"/>金币</p>
                </c:if>
            </div>
        </div>
    </div>


    <!--加油卡-->
    <div class="jyk kind">
        <h3 class="clearfix kind-title border-1px_bottom">
            <span class="left">加油卡</span>
            <span class="right">爆款热销</span>
        </h3>

        <div class="banner-img">
            <img src="${resourceUrl}/frontRes/gold/index/img/004.png" alt="">
        </div>
        <div class="products">
            <div class="product-item border-right" onclick="detail(${p_200.id})">
                <div class="good-img">
                    <img src="${p_200.picture}" alt="">
                </div>
                <h1 class="ttl">${p_200.name}</h1>

                <p class="true-price">原价：<fmt:formatNumber
                        type="number" value="${p_200.price/100}" pattern="0.00"
                        maxFractionDigits="0"/>元</p>

                <c:if test="${score >= p_200.minScore}">
                    <p class="now-price">需付：0元+<fmt:formatNumber
                            type="number" value="${p_200.minScore/100}" pattern="0.00"
                            maxFractionDigits="0"/>金币</p>
                </c:if>
                <c:if test="${score < p_200.minScore}">
                    <p class="now-price">需付：<fmt:formatNumber
                            type="number" value="${(p_200.minScore-score3)/100}"
                            pattern="0.00"
                            maxFractionDigits="0"/>元+<fmt:formatNumber
                            type="number" value="${score3/100}" pattern="0.00"
                            maxFractionDigits="0"/>金币</p>
                </c:if>
            </div>
            <div class="product-item" onclick="detail(${p_201.id})">
                <div class="good-img">
                    <img src="${p_201.picture}" alt="">
                </div>
                <h1 class="ttl">${p_201.name}</h1>

                <p class="true-price">原价：<fmt:formatNumber
                        type="number" value="${p_201.price/100}" pattern="0.00"
                        maxFractionDigits="0"/>元</p>

                <c:if test="${score >= p_201.minScore}">
                    <p class="now-price">需付：0元+<fmt:formatNumber
                            type="number" value="${p_201.minScore/100}" pattern="0.00"
                            maxFractionDigits="0"/>金币</p>
                </c:if>
                <c:if test="${score < p_201.minScore}">
                    <p class="now-price">需付：<fmt:formatNumber
                            type="number" value="${(p_201.minScore-score3)/100}"
                            pattern="0.00"
                            maxFractionDigits="0"/>元+<fmt:formatNumber
                            type="number" value="${score3/100}" pattern="0.00"
                            maxFractionDigits="0"/>金币</p>
                </c:if>
            </div>
        </div>
    </div>

    <%--<!--视频会员-->--%>
    <div class="sphy kind">
        <h3 class="clearfix kind-title">
            <span class="left">视频会员</span>
            <span class="right">专享特权</span>
        </h3>

        <div class="banner-img">
            <img src="${resourceUrl}/frontRes/gold/index/img/007.png" alt="">
        </div>
        <div class="products">
            <div class="product-item border-right" onclick="detail(${p_208.id})">
                <div class="good-img">
                    <img src="${p_208.picture}" alt="">
                </div>
                <h1 class="ttl">${p_208.name}</h1>

                <p class="true-price">原价：<fmt:formatNumber
                        type="number" value="${p_208.price/100}" pattern="0.00"
                        maxFractionDigits="0"/>元</p>

                <c:if test="${score >= p_208.minScore}">
                    <p class="now-price">需付：0元+<fmt:formatNumber
                            type="number" value="${p_208.minScore/100}" pattern="0.00"
                            maxFractionDigits="0"/>金币</p>
                </c:if>
                <c:if test="${score < p_208.minScore}">
                    <p class="now-price">需付：<fmt:formatNumber
                            type="number" value="${(p_208.minScore-score3)/100}"
                            pattern="0.00"
                            maxFractionDigits="0"/>元+<fmt:formatNumber
                            type="number" value="${score3/100}" pattern="0.00"
                            maxFractionDigits="0"/>金币</p>
                </c:if>
            </div>
            <div class="product-item border-right" onclick="detail(${p_207.id})">
                <div class="good-img">
                    <img src="${p_207.picture}" alt="">
                </div>
                <h1 class="ttl">${p_207.name}</h1>

                <p class="true-price">原价：<fmt:formatNumber
                        type="number" value="${p_207.price/100}" pattern="0.00"
                        maxFractionDigits="0"/>元</p>

                <c:if test="${score >= p_207.minScore}">
                    <p class="now-price">需付：0元+<fmt:formatNumber
                            type="number" value="${p_207.minScore/100}" pattern="0.00"
                            maxFractionDigits="0"/>金币</p>
                </c:if>
                <c:if test="${score < p_207.minScore}">
                    <p class="now-price">需付：<fmt:formatNumber
                            type="number" value="${(p_207.minScore-score3)/100}"
                            pattern="0.00"
                            maxFractionDigits="0"/>元+<fmt:formatNumber
                            type="number" value="${score3/100}" pattern="0.00"
                            maxFractionDigits="0"/>金币</p>
                </c:if>
            </div>
            <div class="product-item" onclick="detail(${p_209.id})">
                <div class="good-img">
                    <img src="${p_209.picture}" alt="">
                </div>
                <h1 class="ttl">${p_209.name}</h1>

                <p class="true-price">原价：<fmt:formatNumber
                        type="number" value="${p_209.price/100}" pattern="0.00"
                        maxFractionDigits="0"/>元</p>

                <c:if test="${score >= p_209.minScore}">
                    <p class="now-price">需付：0元+<fmt:formatNumber
                            type="number" value="${p_209.minScore/100}" pattern="0.00"
                            maxFractionDigits="0"/>金币</p>
                </c:if>
                <c:if test="${score < p_209.minScore}">
                    <p class="now-price">需付：<fmt:formatNumber
                            type="number" value="${(p_209.minScore-score3)/100}"
                            pattern="0.00"
                            maxFractionDigits="0"/>元+<fmt:formatNumber
                            type="number" value="${score3/100}" pattern="0.00"
                            maxFractionDigits="0"/>金币</p>
                </c:if>
            </div>
        </div>
    </div>


    <%--<!--北京公交卡-->--%>
    <div class="bjgjk kind">
        <h3 class="clearfix kind-title border-1px_bottom">
            <span class="left">北京公交卡</span>
            <span class="right">仅限北京</span>
        </h3>

        <div class="banner-img border-1px_bottom" onclick="detail(${p_198.id})">
            <img src="${p_198.picture}" alt="">

            <div class="product-desc">
                <h1 class="ttl">${p_198.name}</h1>

                <p class="desc">${p_198.description}</p>

                <p class="true-price">原价：<fmt:formatNumber
                        type="number" value="${p_198.price/100}" pattern="0.00"
                        maxFractionDigits="0"/>元</p>
                <c:if test="${score >= p_198.minScore}">
                    <p class="now-price">需付：0元+<fmt:formatNumber
                            type="number" value="${p_198.minScore/100}" pattern="0.00"
                            maxFractionDigits="0"/>金币</p>
                </c:if>
                <c:if test="${score < p_198.minScore}">
                    <p class="now-price">需付：<fmt:formatNumber
                            type="number" value="${(p_198.minScore-score3)/100}"
                            pattern="0.00"
                            maxFractionDigits="0"/>元+<fmt:formatNumber
                            type="number" value="${score3/100}" pattern="0.00"
                            maxFractionDigits="0"/>金币</p>
                </c:if>
            </div>
        </div>
        <div class="products">
            <div class="product-item border-right" onclick="detail(${p_197.id})">
                <div class="good-img">
                    <img src="${p_197.picture}" alt="">
                </div>
                <h1 class="ttl">${p_197.name}</h1>

                <p class="true-price">原价：<fmt:formatNumber
                        type="number" value="${p_197.price/100}" pattern="0.00"
                        maxFractionDigits="0"/>元</p>

                <c:if test="${score >= p_197.minScore}">
                    <p class="now-price">需付：0元+<fmt:formatNumber
                            type="number" value="${p_197.minScore/100}" pattern="0.00"
                            maxFractionDigits="0"/>金币</p>
                </c:if>
                <c:if test="${score < p_197.minScore}">
                    <p class="now-price">需付：<fmt:formatNumber
                            type="number" value="${(p_197.minScore-score3)/100}"
                            pattern="0.00"
                            maxFractionDigits="0"/>元+<fmt:formatNumber
                            type="number" value="${score3/100}" pattern="0.00"
                            maxFractionDigits="0"/>金币</p>
                </c:if>
            </div>
            <div class="product-item" onclick="detail(${p_199.id})">
                <div class="good-img">
                    <img src="${p_199.picture}" alt="">
                </div>
                <h1 class="ttl">${p_199.name}</h1>

                <p class="true-price">原价：<fmt:formatNumber
                        type="number" value="${p_199.price/100}" pattern="0.00"
                        maxFractionDigits="0"/>元</p>

                <c:if test="${score >= p_199.minScore}">
                    <p class="now-price">需付：0元+<fmt:formatNumber
                            type="number" value="${p_199.minScore/100}" pattern="0.00"
                            maxFractionDigits="0"/>金币</p>
                </c:if>
                <c:if test="${score < p_199.minScore}">
                    <p class="now-price">需付：<fmt:formatNumber
                            type="number" value="${(p_199.minScore-score3)/100}"
                            pattern="0.00"
                            maxFractionDigits="0"/>元+<fmt:formatNumber
                            type="number" value="${score3/100}" pattern="0.00"
                            maxFractionDigits="0"/>金币</p>
                </c:if>
            </div>
        </div>
    </div>


    <%--<!--数码产品-->--%>
    <div class="smcp kind">
        <h3 class="clearfix kind-title border-1px_bottom">
            <span class="left">数码产品</span>
            <span class="right">正品行货</span>
        </h3>

        <div class="banner-img border-1px_bottom" onclick="detail(${p_192.id})">
            <img src="${p_192.picture}" alt="">

            <div class="product-desc">
                <h1 class="ttl">${p_192.name}</h1>

                <p class="desc">${p_192.description}</p>

                <p class="true-price">原价：<fmt:formatNumber
                        type="number" value="${p_192.price/100}" pattern="0.00"
                        maxFractionDigits="0"/>元</p>
                <c:if test="${score >= p_192.minScore}">
                    <p class="now-price">需付：0元+<fmt:formatNumber
                            type="number" value="${p_192.minScore/100}" pattern="0.00"
                            maxFractionDigits="0"/>金币</p>
                </c:if>
                <c:if test="${score < p_192.minScore}">
                    <p class="now-price">需付：<fmt:formatNumber
                            type="number" value="${(p_192.minScore-score3)/100}"
                            pattern="0.00"
                            maxFractionDigits="0"/>元+<fmt:formatNumber
                            type="number" value="${score3/100}" pattern="0.00"
                            maxFractionDigits="0"/>金币</p>
                </c:if>
            </div>
        </div>

        <div class="products">
            <div class="product-item border-right border-1px_bottom" onclick="detail(${p_193.id})">
                <div class="good-img">
                    <img src="${p_193.picture}" alt="">
                </div>
                <h1 class="ttl">${p_193.name}</h1>

                <p class="true-price">原价：<fmt:formatNumber
                        type="number" value="${p_193.price/100}" pattern="0.00"
                        maxFractionDigits="0"/>元</p>

                <c:if test="${score >= p_193.minScore}">
                    <p class="now-price">需付：0元+<fmt:formatNumber
                            type="number" value="${p_193.minScore/100}" pattern="0.00"
                            maxFractionDigits="0"/>金币</p>
                </c:if>
                <c:if test="${score < p_193.minScore}">
                    <p class="now-price">需付：<fmt:formatNumber
                            type="number" value="${(p_193.minScore-score3)/100}"
                            pattern="0.00"
                            maxFractionDigits="0"/>元+<fmt:formatNumber
                            type="number" value="${score3/100}" pattern="0.00"
                            maxFractionDigits="0"/>金币</p>
                </c:if>
            </div>
            <div class="product-item border-1px_bottom" onclick="detail(${p_194.id})">
                <div class="good-img">
                    <img src="${p_194.picture}" alt="">
                </div>
                <h1 class="ttl">${p_194.name}</h1>

                <p class="true-price">原价：<fmt:formatNumber
                        type="number" value="${p_194.price/100}" pattern="0.00"
                        maxFractionDigits="0"/>元</p>

                <c:if test="${score >= p_194.minScore}">
                    <p class="now-price">需付：0元+<fmt:formatNumber
                            type="number" value="${p_194.minScore/100}" pattern="0.00"
                            maxFractionDigits="0"/>金币</p>
                </c:if>
                <c:if test="${score < p_194.minScore}">
                    <p class="now-price">需付：<fmt:formatNumber
                            type="number" value="${(p_194.minScore-score3)/100}"
                            pattern="0.00"
                            maxFractionDigits="0"/>元+<fmt:formatNumber
                            type="number" value="${score3/100}" pattern="0.00"
                            maxFractionDigits="0"/>金币</p>
                </c:if>
            </div>
        </div>
        <div class="products">
            <div class="product-item border-right" onclick="detail(${p_195.id})">
                <div class="good-img">
                    <img src="${p_195.picture}" alt="">
                </div>
                <h1 class="ttl">${p_195.name}</h1>

                <p class="true-price">原价：<fmt:formatNumber
                        type="number" value="${p_195.price/100}" pattern="0.00"
                        maxFractionDigits="0"/>元</p>

                <c:if test="${score >= p_195.minScore}">
                    <p class="now-price">需付：0元+<fmt:formatNumber
                            type="number" value="${p_195.minScore/100}" pattern="0.00"
                            maxFractionDigits="0"/>金币</p>
                </c:if>
                <c:if test="${score < p_195.minScore}">
                    <p class="now-price">需付：<fmt:formatNumber
                            type="number" value="${(p_195.minScore-score3)/100}"
                            pattern="0.00"
                            maxFractionDigits="0"/>元+<fmt:formatNumber
                            type="number" value="${score3/100}" pattern="0.00"
                            maxFractionDigits="0"/>金币</p>
                </c:if>
            </div>
            <div class="product-item" onclick="detail(${p_196.id})">
                <div class="good-img">
                    <img src="${p_196.picture}" alt="">
                </div>
                <h1 class="ttl">${p_196.name}</h1>

                <p class="true-price">原价：<fmt:formatNumber
                        type="number" value="${p_196.price/100}" pattern="0.00"
                        maxFractionDigits="0"/>元</p>

                <c:if test="${score >= p_196.minScore}">
                    <p class="now-price">需付：0元+<fmt:formatNumber
                            type="number" value="${p_196.minScore/100}" pattern="0.00"
                            maxFractionDigits="0"/>金币</p>
                </c:if>
                <c:if test="${score < p_196.minScore}">
                    <p class="now-price">需付：<fmt:formatNumber
                            type="number" value="${(p_196.minScore-score3)/100}"
                            pattern="0.00"
                            maxFractionDigits="0"/>元+<fmt:formatNumber
                            type="number" value="${score3/100}" pattern="0.00"
                            maxFractionDigits="0"/>金币</p>
                </c:if>
            </div>
        </div>
    </div>


    <!--说走就走的旅行-->
    <div class="szjz kind" onclick="goTravel()">
        <h3 class="clearfix kind-title border-1px_bottom">
            <span class="left">说走就走的旅行</span>
            <span class="right">猜你想去</span>
        </h3>

        <div class="banner-img">
            <img src="${resourceUrl}/frontRes/gold/index/img/005.png" alt="">
        </div>
        <div class="products">
            <div class="product-item">
                <h1 class="ttl">北京-柬埔寨6日游 全程五星艾美酒店 近距离探秘古老的</h1>

                <div class="prompt">
                    <span class="prompt-item">24小时客服服务</span>
                    <span class="prompt-item">赠送航空延误险</span>
                    <span class="prompt-item">落地签随时走</span>
                </div>
                <p class="true-price">原价：4,599元 <span>起/人</span></p>
                <c:if test="${score >= 459900}">
                    <p class="now-price">需付：0元+4599金币 <span>起/人</span></p>
                </c:if>
                <c:if test="${score < 459900}">
                    <p class="now-price">需付：<fmt:formatNumber
                            type="number" value="${(459900-score)/100}" pattern="0.00"
                            maxFractionDigits="0"/>元+<fmt:formatNumber
                            type="number" value="${score3/100}" pattern="0.00"
                            maxFractionDigits="0"/>金币 <span>起/人</span></p>
                </c:if>


            </div>
        </div>
    </div>


    <%--<!--美妆个护-->--%>
    <div class="mzgh kind">
        <h3 class="clearfix kind-title border-1px_bottom">
            <span class="left">美妆个护</span>
            <span class="right">爆款热销</span>
        </h3>

        <div class="banner-img">
            <img src="${resourceUrl}/frontRes/gold/index/img/006.png" alt="">
        </div>
        <div class="products">
            <div class="product-item border-right" onclick="detail(${p_191.id})">
                <div class="good-img">
                    <img src="${p_191.picture}" alt="">
                </div>
                <h1 class="ttl">${p_191.name}</h1>

                <p class="true-price">原价：<fmt:formatNumber
                        type="number" value="${p_191.price/100}" pattern="0.00"
                        maxFractionDigits="0"/>元</p>

                <c:if test="${score >= p_191.minScore}">
                    <p class="now-price">需付：0元+<fmt:formatNumber
                            type="number" value="${p_191.minScore/100}" pattern="0.00"
                            maxFractionDigits="0"/>金币</p>
                </c:if>
                <c:if test="${score < p_191.minScore}">
                    <p class="now-price">需付：<fmt:formatNumber
                            type="number" value="${(p_191.minScore-score3)/100}"
                            pattern="0.00"
                            maxFractionDigits="0"/>元+<fmt:formatNumber
                            type="number" value="${score3/100}" pattern="0.00"
                            maxFractionDigits="0"/>金币</p>
                </c:if>
            </div>
            <div class="product-item" onclick="detail(${p_202.id})">
                <div class="good-img">
                    <img src="${p_202.picture}" alt="">
                </div>
                <h1 class="ttl">${p_202.name}</h1>

                <p class="true-price">原价：<fmt:formatNumber
                        type="number" value="${p_202.price/100}" pattern="0.00"
                        maxFractionDigits="0"/>元</p>

                <c:if test="${score >= p_202.minScore}">
                    <p class="now-price">需付：0元+<fmt:formatNumber
                            type="number" value="${p_202.minScore/100}" pattern="0.00"
                            maxFractionDigits="0"/>金币</p>
                </c:if>
                <c:if test="${score < p_202.minScore}">
                    <p class="now-price">需付：<fmt:formatNumber
                            type="number" value="${(p_202.minScore-score3)/100}"
                            pattern="0.00"
                            maxFractionDigits="0"/>元+<fmt:formatNumber
                            type="number" value="${score3/100}" pattern="0.00"
                            maxFractionDigits="0"/>金币</p>
                </c:if>
            </div>
        </div>
    </div>


    <%--<!--神奇百货-->--%>
    <div class="sqbh kind">
        <h3 class="clearfix kind-title border-1px_bottom">
            <span class="left">神奇百货</span>
            <span class="right">爆款热销</span>
        </h3>

        <div class="products">
            <div class="product-item border-right border-1px_bottom" onclick="detail(${p_203.id})">
                <div class="good-img">
                    <img src="${p_203.picture}" alt="">
                </div>
                <h1 class="ttl">${p_203.name}</h1>

                <p class="true-price">原价：<fmt:formatNumber
                        type="number" value="${p_203.price/100}" pattern="0.00"
                        maxFractionDigits="0"/>元</p>

                <c:if test="${score >= p_203.minScore}">
                    <p class="now-price">需付：0元+<fmt:formatNumber
                            type="number" value="${p_203.minScore/100}" pattern="0.00"
                            maxFractionDigits="0"/>金币</p>
                </c:if>
                <c:if test="${score < p_203.minScore}">
                    <p class="now-price">需付：<fmt:formatNumber
                            type="number" value="${(p_203.minScore-score3)/100}"
                            pattern="0.00"
                            maxFractionDigits="0"/>元+<fmt:formatNumber
                            type="number" value="${score3/100}" pattern="0.00"
                            maxFractionDigits="0"/>金币</p>
                </c:if>
            </div>
            <div class="product-item border-1px_bottom" onclick="detail(${p_204.id})">
                <div class="good-img">
                    <img src="${p_204.picture}" alt="">
                </div>
                <h1 class="ttl">${p_204.name}</h1>

                <p class="true-price">原价：<fmt:formatNumber
                        type="number" value="${p_204.price/100}" pattern="0.00"
                        maxFractionDigits="0"/>元</p>

                <c:if test="${score >= p_204.minScore}">
                    <p class="now-price">需付：0元+<fmt:formatNumber
                            type="number" value="${p_204.minScore/100}" pattern="0.00"
                            maxFractionDigits="0"/>金币</p>
                </c:if>
                <c:if test="${score < p_204.minScore}">
                    <p class="now-price">需付：<fmt:formatNumber
                            type="number" value="${(p_204.minScore-score3)/100}"
                            pattern="0.00"
                            maxFractionDigits="0"/>元+<fmt:formatNumber
                            type="number" value="${score3/100}" pattern="0.00"
                            maxFractionDigits="0"/>金币</p>
                </c:if>
            </div>
        </div>
        <div class="products">
            <div class="product-item border-right" onclick="detail(${p_205.id})">
                <div class="good-img">
                    <img src="${p_205.picture}" alt="">
                </div>
                <h1 class="ttl">${p_205.name}</h1>

                <p class="true-price">原价：<fmt:formatNumber
                        type="number" value="${p_205.price/100}" pattern="0.00"
                        maxFractionDigits="0"/>元</p>

                <c:if test="${score >= p_205.minScore}">
                    <p class="now-price">需付：0元+<fmt:formatNumber
                            type="number" value="${p_205.minScore/100}" pattern="0.00"
                            maxFractionDigits="0"/>金币</p>
                </c:if>
                <c:if test="${score < p_205.minScore}">
                    <p class="now-price">需付：<fmt:formatNumber
                            type="number" value="${(p_205.minScore-score3)/100}"
                            pattern="0.00"
                            maxFractionDigits="0"/>元+<fmt:formatNumber
                            type="number" value="${score3/100}" pattern="0.00"
                            maxFractionDigits="0"/>金币</p>
                </c:if>
            </div>
            <div class="product-item" onclick="detail(${p_206.id})">
                <div class="good-img">
                    <img src="${p_206.picture}" alt="">
                </div>
                <h1 class="ttl">${p_206.name}</h1>

                <p class="true-price">原价：<fmt:formatNumber
                        type="number" value="${p_206.price/100}" pattern="0.00"
                        maxFractionDigits="0"/>元</p>

                <c:if test="${score >= p_206.minScore}">
                    <p class="now-price">需付：0元+<fmt:formatNumber
                            type="number" value="${p_206.minScore/100}" pattern="0.00"
                            maxFractionDigits="0"/>金币</p>
                </c:if>
                <c:if test="${score < p_206.minScore}">
                    <p class="now-price">需付：<fmt:formatNumber
                            type="number" value="${(p_206.minScore-score3)/100}"
                            pattern="0.00"
                            maxFractionDigits="0"/>元+<fmt:formatNumber
                            type="number" value="${score3/100}" pattern="0.00"
                            maxFractionDigits="0"/>金币</p>
                </c:if>
            </div>
        </div>
    </div>
    <!--弹窗-->
    <div class="shadow">
        <div class="content">
            <span class="btn-close"></span>
            <img src="${resourceUrl}/frontRes/gold/index/img/tanchuang@2x.png" alt="">
        </div>
    </div>
</div>
</body>
<script src="${resourceUrl}/js/zepto.min.js"></script>
<script>
    <%--var score = eval('${score}');--%>
    <%--//强制保留两位小数--%>
    <%--function toDecimal(x) {--%>
    <%--var f = parseFloat(x);--%>
    <%--if (isNaN(f)) {--%>
    <%--return false;--%>
    <%--}--%>
    <%--var f = Math.round(x * 100) / 100;--%>
    <%--var s = f.toString();--%>
    <%--var rs = s.indexOf('.');--%>
    <%--if (rs < 0) {--%>
    <%--rs = s.length;--%>
    <%--s += '.';--%>
    <%--}--%>
    <%--while (s.length <= rs + 2) {--%>
    <%--s += '0';--%>
    <%--}--%>
    <%--return s;--%>
    <%--}--%>
    <%--function goProductDetail(productId) {--%>
    <%--alert(productId);--%>
    <%--}--%>
    <%--function banner(e, p) {--%>
    <%--e.append('<div onclick="goProductDetail(' + p.id + ')" class="banner-img"><img src="'--%>
    <%--+ p.picture--%>
    <%--+ '" alt=""><div class="product-desc"><h1 class="ttl">' + p.name--%>
    <%--+ '</h1> <p class="desc">' + p.description--%>
    <%--+ '</p> <p class="true-price">原价：' + toDecimal(p.price / 100)--%>
    <%--+ '元</p>');--%>
    <%--if (score >= p.minScore) {--%>
    <%--e.append('<p class="now-price">需付：0元+' + toDecimal(p.minScore / 100) + '金币</p>');--%>
    <%--} else {--%>
    <%--e.append('<p class="now-price">需付：' + toDecimal((p.minScore - score) / 100) + '元+'--%>
    <%--+ toDecimal(score / 100) + '金币</p>');--%>
    <%--}--%>
    <%--e.append('</div></div>');--%>
    <%--}--%>
    <%--$.ajax({--%>
    <%--type: "get",--%>
    <%--url: "/front/gold/list?page=1",--%>
    <%--success: function (data) {--%>
    <%--var list = data.data, length = list.length;--%>
    <%--var smcp1 = '', smcp2 = ''; //数码产品拼接--%>
    <%--for (var i = 0; i < length; i++) {--%>
    <%--var type = list[i].type;--%>
    <%--switch (type) {--%>
    <%--case 2:--%>
    <%--banner(smcp1, list[i]);--%>
    <%--break;--%>
    <%--default :--%>
    <%--break;--%>
    <%--}--%>
    <%--}--%>
    <%--}--%>
    <%--});--%>

    <%--如何赚金币--%>
    $('.icon-problem').on('touchstart', function (event) {
        $('.shadow').css('display', 'block');
        $(document).one('touchstart', function () {
            setTimeout(function () {
                $('.shadow').css('display', 'none')
            }, 300)

        })
        event.stopPropagation();
    });
    $('.btn-close').on('touchstart', function () {
        setTimeout(function () {
            $('.shadow').css('display', 'none')
        }, 300)
    });

    function detail(productId) {
        /** 点击商品详情事件统计*/
        $.get("/front/visit/product/" + productId);
        window.location.href = '/front/gold/weixin/p?productId=' + productId;
    }
    function goRecharge() {
        /** 点击话费充值事件统计*/
        $.get("/front/visit/recharge/0");
        window.location.href = '/front/order/weixin/recharge';
    }
    function goTravel() {
        window.location.href = 'http://u.eqxiu.com/s/4puvrvwJ';
    }
    function goOrderList() {
        window.location.href = '/front/order/weixin/orderList';
    }
</script>
</html>
