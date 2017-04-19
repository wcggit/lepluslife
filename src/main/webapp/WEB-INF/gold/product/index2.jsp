<%--
  Created by IntelliJ IDEA.
  User: zhangwen
  Date: 2017/4/10
  Time: 17:48
  新版金币商城首页
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
    <title>臻品</title>
    <link rel="stylesheet" type="text/css" href="${resourceUrl}/frontRes/gold/index/css/reset.css">
    <link rel="stylesheet" type="text/css" href="${resourceUrl}/frontRes/gold/index/css/home2.css">
    <link rel="stylesheet" href="${resourceUrl}/frontRes/css/swiper.min.css">
</head>
<body>
<div class="main">

    <div class="wdjb">
        <p class="clearfix">
            <span class="left">我的金币</span>
            <span class="right icon-problem showGetScore"><span
                    class="btn-problem"></span>如何赚金币</span>
        </p>

        <h1 class="clearfix"><fmt:formatNumber
                type="number" value="${score/100}" pattern="0.00"
                maxFractionDigits="2"/><span></span><span class="right"
                                                          onclick="goOrderList()">查看订单</span>
        </h1>
    </div>

    <!--新品推荐-->
    <div class="xptj kind" style="margin-top: 0;">
        <h3 class="clearfix kind-title">
            <p class="left_">新品推荐</p>

            <p class="right_">每天告诉你大家喜欢的和想要的</p>
        </h3>
        <div class="banner-img">
            <!-- Swiper -->
            <div class="swiper-container">
                <div class="swiper-wrapper">
                </div>
                <!-- Add Pagination -->
                <div class="swiper-pagination"></div>
            </div>
        </div>
        <h3 class="clearfix kind-title" style="padding: 0;">
            <p class="left_" id="newPush-name"></p>

            <p class="right_" id="newPush-dis"></p>
        </h3>
    </div>

    <!--甄选好货-->
    <div class="zxhh kind">
        <p class="w-title">甄选好货，过品质生活</p>

        <div class="clearfix">
            <div class="w-type" onclick="typeList(4)">
                <div><img src="${resourceUrl}/frontRes/gold/index/img/digital.png" alt=""></div>
                <p>数码</p>
            </div>
            <div class="w-type" onclick="typeList(2)">
                <div><img src="${resourceUrl}/frontRes/gold/index/img/beauty.png" alt=""></div>
                <p>美妆</p>
            </div>
            <div class="w-type" onclick="typeList(3)">
                <div><img src="${resourceUrl}/frontRes/gold/index/img/food.png" alt=""></div>
                <p>食品</p>
            </div>
            <div class="w-type" onclick="typeList(1)">
                <div><img src="${resourceUrl}/frontRes/gold/index/img/home.png" alt=""></div>
                <p>居家</p>
            </div>
        </div>
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

                <p class="true-price">100元</p>
                <c:if test="${score >= 10000}">
                    <p class="now-price">或：0元+100金币</p>
                </c:if>
                <c:if test="${score < 10000}">
                    <p class="now-price">或：<fmt:formatNumber
                            type="number" value="${(10000-score3)/100}"
                            maxFractionDigits="0"/>元+<fmt:formatNumber
                            type="number" value="${score3/100}"
                            maxFractionDigits="0"/>金币</p>
                </c:if>

            </div>
            <div class="product-item border-right">
                <h1 class="ttl"><span>50</span>元话费</h1>

                <p class="true-price">50</p>
                <c:if test="${score >= 5000}">
                    <p class="now-price">或：0元+50金币</p>
                </c:if>
                <c:if test="${score < 5000}">
                    <p class="now-price">或：<fmt:formatNumber
                            type="number" value="${(5000-score3)/100}" pattern="0.00"
                            maxFractionDigits="0"/>元+<fmt:formatNumber
                            type="number" value="${score3/100}" pattern="0.00"
                            maxFractionDigits="0"/>金币</p>
                </c:if>
            </div>
            <div class="product-item">
                <h1 class="ttl"><span>20</span>元话费</h1>

                <p class="true-price">20元</p>

                <c:if test="${score >= 2000}">
                    <p class="now-price">或：0元+20金币</p>
                </c:if>
                <c:if test="${score < 2000}">
                    <p class="now-price">或：<fmt:formatNumber
                            type="number" value="${(2000-score3)/100}" pattern="0.00"
                            maxFractionDigits="0"/>元+<fmt:formatNumber
                            type="number" value="${score3/100}" pattern="0.00"
                            maxFractionDigits="0"/>金币</p>
                </c:if>
            </div>
        </div>
    </div>

    <!--加油卡-->
    <c:if test="${m.key1 != null}">
        <div class="jyk kind">
            <h3 class="clearfix kind-title border-1px_bottom">
                <p class="left_">${m.key1.title}</p>

                <p class="right_">${m.key1.detail}</p>
            </h3>
            <div class="banner-img">
                <img src="${m.key1.banner}" alt="">
            </div>
            <div class="products">
                <div class="product-item border-right"
                     onclick="detail(${m.key1.list[0].pruductId})">
                    <div class="good-img w-bo">
                        <img src="${m.key1.list[0].pic}" alt="">
                    </div>
                    <h1 class="ttl">${m.key1.list[0].name}</h1>

                    <p class="true-price"><fmt:formatNumber
                            type="number" value="${m.key1.list[0].price/100}" pattern="0.00"
                            maxFractionDigits="0"/>元</p>

                    <c:if test="${score >= m.key1.list[0].price}">
                        <p class="now-price">或：0元+<fmt:formatNumber
                                type="number" value="${m.key1.list[0].price/100}" pattern="0.00"
                                maxFractionDigits="0"/>金币</p>
                    </c:if>
                    <c:if test="${score < m.key1.list[0].price}">
                        <p class="now-price">或：<fmt:formatNumber
                                type="number" value="${(m.key1.list[0].price-score3)/100}"
                                pattern="0.00"
                                maxFractionDigits="0"/>元+<fmt:formatNumber
                                type="number" value="${score3/100}" pattern="0.00"
                                maxFractionDigits="0"/>金币</p>
                    </c:if>
                </div>
                <div class="product-item" onclick="detail(${m.key1.list[1].pruductId})">
                    <div class="good-img w-bo">
                        <img src="${m.key1.list[1].pic}" alt="">
                    </div>
                    <h1 class="ttl">${m.key1.list[1].name}</h1>

                    <p class="true-price"><fmt:formatNumber
                            type="number" value="${m.key1.list[1].price/100}" pattern="0.00"
                            maxFractionDigits="0"/>元</p>

                    <c:if test="${score >= m.key1.list[1].price}">
                        <p class="now-price">或：0元+<fmt:formatNumber
                                type="number" value="${m.key1.list[1].price/100}" pattern="0.00"
                                maxFractionDigits="0"/>金币</p>
                    </c:if>
                    <c:if test="${score < m.key1.list[1].price}">
                        <p class="now-price">或：<fmt:formatNumber
                                type="number" value="${(m.key1.list[1].price-score3)/100}"
                                pattern="0.00"
                                maxFractionDigits="0"/>元+<fmt:formatNumber
                                type="number" value="${score3/100}" pattern="0.00"
                                maxFractionDigits="0"/>金币</p>
                    </c:if>
                </div>
            </div>
        </div>
    </c:if>

    <!--视频会员-->
    <c:if test="${m.key2 != null}">
        <div class="sphy kind">
            <h3 class="clearfix kind-title">
                <p class="left_">${m.key2.title}</p>

                <p class="right_">${m.key2.detail}</p>
            </h3>
            <div class="banner-img">
                <img src="${m.key2.banner}" alt="">
            </div>
            <div class="products">
                <div class="product-item border-right"
                     onclick="detail(${m.key2.list[0].pruductId})">
                    <div class="good-img w-sp w-bo">
                        <img src="${m.key2.list[0].pic}" alt="">
                    </div>
                    <h1 class="ttl">${m.key2.list[0].name}</h1>

                    <p class="true-price"><fmt:formatNumber
                            type="number" value="${m.key2.list[0].price/100}" pattern="0.00"
                            maxFractionDigits="0"/>元</p>

                    <c:if test="${score >= m.key2.list[0].price}">
                        <p class="now-price">或：0元+<fmt:formatNumber
                                type="number" value="${m.key2.list[0].price/100}" pattern="0.00"
                                maxFractionDigits="0"/>金币</p>
                    </c:if>
                    <c:if test="${score < m.key2.list[0].price}">
                        <p class="now-price">或：<fmt:formatNumber
                                type="number" value="${(m.key2.list[0].price-score3)/100}"
                                pattern="0.00"
                                maxFractionDigits="0"/>元+<fmt:formatNumber
                                type="number" value="${score3/100}" pattern="0.00"
                                maxFractionDigits="0"/>金币</p>
                    </c:if>
                </div>
                <div class="product-item border-right" onclick="detail(${m.key2.list[1].pruductId})">
                    <div class="good-img w-sp w-bo">
                        <img src="${m.key2.list[1].pic}" alt="">
                    </div>
                    <h1 class="ttl">${m.key2.list[1].name}</h1>

                    <p class="true-price"><fmt:formatNumber
                            type="number" value="${m.key2.list[1].price/100}" pattern="0.00"
                            maxFractionDigits="0"/>元</p>

                    <c:if test="${score >= m.key2.list[1].price}">
                        <p class="now-price">或：0元+<fmt:formatNumber
                                type="number" value="${m.key1.list[1].price/100}" pattern="0.00"
                                maxFractionDigits="0"/>金币</p>
                    </c:if>
                    <c:if test="${score < m.key2.list[1].price}">
                        <p class="now-price">或：<fmt:formatNumber
                                type="number" value="${(m.key2.list[1].price-score3)/100}"
                                pattern="0.00"
                                maxFractionDigits="0"/>元+<fmt:formatNumber
                                type="number" value="${score3/100}" pattern="0.00"
                                maxFractionDigits="0"/>金币</p>
                    </c:if>
                </div>
                <div class="product-item" onclick="detail(${m.key2.list[2].pruductId})">
                    <div class="good-img w-sp w-bo">
                        <img src="${m.key2.list[2].pic}" alt="">
                    </div>
                    <h1 class="ttl">${m.key2.list[2].name}</h1>

                    <p class="true-price"><fmt:formatNumber
                            type="number" value="${m.key2.list[2].price/100}" pattern="0.00"
                            maxFractionDigits="0"/>元</p>

                    <c:if test="${score >= m.key2.list[2].price}">
                        <p class="now-price">或：0元+<fmt:formatNumber
                                type="number" value="${m.key2.list[2].price/100}" pattern="0.00"
                                maxFractionDigits="0"/>金币</p>
                    </c:if>
                    <c:if test="${score < m.key2.list[2].price}">
                        <p class="now-price">或：<fmt:formatNumber
                                type="number" value="${(m.key2.list[2].price-score3)/100}"
                                pattern="0.00"
                                maxFractionDigits="0"/>元+<fmt:formatNumber
                                type="number" value="${score3/100}" pattern="0.00"
                                maxFractionDigits="0"/>金币</p>
                    </c:if>
                </div>
            </div>
        </div>
    </c:if>

    <!--北京公交卡-->
    <c:if test="${m.key3 != null}">
        <div class="bjgjk kind">
            <h3 class="clearfix kind-title border-1px_bottom">
                <p class="left_">${m.key3.title}</p>

                <p class="right_">${m.key3.detail}</p>
            </h3>
            <div class="banner-img clearfix">
                <div class="product-desc-w w-l" onclick="detail(${m.key3.list[0].pruductId})">
                    <h1 class="ttl">${m.key3.list[0].name}</h1>

                    <p class="desc"></p>

                    <p class="true-price"><fmt:formatNumber
                            type="number" value="${m.key3.list[0].price/100}" pattern="0.00"
                            maxFractionDigits="0"/>元</p>

                    <c:if test="${score >= m.key3.list[0].price}">
                        <p class="now-price">或：0元+<fmt:formatNumber
                                type="number" value="${m.key3.list[0].price/100}" pattern="0.00"
                                maxFractionDigits="0"/>金币</p>
                    </c:if>
                    <c:if test="${score < m.key3.list[0].price}">
                        <p class="now-price">或：<fmt:formatNumber
                                type="number" value="${(m.key3.list[0].price-score3)/100}"
                                pattern="0.00"
                                maxFractionDigits="0"/>元+<fmt:formatNumber
                                type="number" value="${score3/100}" pattern="0.00"
                                maxFractionDigits="0"/>金币</p>
                    </c:if>

                    <div class="w-img">
                        <img src="${m.key3.list[0].pic}" alt="">
                    </div>
                </div>
                <div class="w-r">
                    <div class="product-desc-w clearfix"
                         onclick="detail(${m.key3.list[1].pruductId})">
                        <div class="w-l w-value_">
                            <h1 class="ttl">${m.key3.list[1].name}</h1>

                            <p class="true-price"><fmt:formatNumber
                                    type="number" value="${m.key3.list[1].price/100}" pattern="0.00"
                                    maxFractionDigits="0"/>元</p>

                            <c:if test="${score >= m.key3.list[1].price}">
                                <p class="now-price">或：0元+<fmt:formatNumber
                                        type="number" value="${m.key3.list[1].price/100}"
                                        pattern="0.00"
                                        maxFractionDigits="0"/>金币</p>
                            </c:if>
                            <c:if test="${score < m.key3.list[1].price}">
                                <p class="now-price">或：<fmt:formatNumber
                                        type="number" value="${(m.key3.list[1].price-score3)/100}"
                                        pattern="0.00"
                                        maxFractionDigits="0"/>元+<fmt:formatNumber
                                        type="number" value="${score3/100}" pattern="0.00"
                                        maxFractionDigits="0"/>金币</p>
                            </c:if>
                        </div>
                        <div class="w-r w-img_">
                            <img src="${m.key3.list[1].pic}" alt="">
                        </div>
                    </div>
                    <div class="product-desc-w clearfix"
                         onclick="detail(${m.key3.list[2].pruductId})">
                        <div class="w-l w-value_">
                            <h1 class="ttl">${m.key3.list[2].name}</h1>

                            <p class="true-price"><fmt:formatNumber
                                    type="number" value="${m.key3.list[2].price/100}" pattern="0.00"
                                    maxFractionDigits="0"/>元</p>

                            <c:if test="${score >= m.key3.list[2].price}">
                                <p class="now-price">或：0元+<fmt:formatNumber
                                        type="number" value="${m.key3.list[2].price/100}"
                                        pattern="0.00"
                                        maxFractionDigits="0"/>金币</p>
                            </c:if>
                            <c:if test="${score < m.key3.list[2].price}">
                                <p class="now-price">或：<fmt:formatNumber
                                        type="number" value="${(m.key3.list[2].price-score3)/100}"
                                        pattern="0.00"
                                        maxFractionDigits="0"/>元+<fmt:formatNumber
                                        type="number" value="${score3/100}" pattern="0.00"
                                        maxFractionDigits="0"/>金币</p>
                            </c:if>
                        </div>
                        <div class="w-r w-img_">
                            <img src="${m.key3.list[2].pic}" alt="">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </c:if>

    <!--说走就走的旅行-->
    <c:if test="${m.key4 != null}">
        <div class="szjz kind">
            <h3 class="clearfix kind-title border-1px_bottom">
                <p class="left_">${m.key4.title}</p>

                <p class="right_">${m.key4.detail}</p>
            </h3>
            <div class="banner-img" onclick="detail(${m.key4.list[0].pruductId})">
                <img src="${m.key4.list[0].pic}" alt="">
            </div>
            <div class="products" onclick="detail(${m.key4.list[0].pruductId})">
                <div class="product-item">

                    <p class="true-price"><fmt:formatNumber
                            type="number" value="${m.key4.list[0].price/100}" pattern="0.00"
                            maxFractionDigits="0"/>元</p>

                    <c:if test="${score >= m.key4.list[0].price}">
                        <p class="now-price">或：0元+<fmt:formatNumber
                                type="number" value="${m.key4.list[0].price/100}" pattern="0.00"
                                maxFractionDigits="0"/>金币</p>
                    </c:if>
                    <c:if test="${score < m.key4.list[0].price}">
                        <p class="now-price">或：<fmt:formatNumber
                                type="number" value="${(m.key4.list[0].price-score3)/100}"
                                pattern="0.00"
                                maxFractionDigits="0"/>元+<fmt:formatNumber
                                type="number" value="${score3/100}" pattern="0.00"
                                maxFractionDigits="0"/>金币</p>
                    </c:if>
                </div>
            </div>
        </div>
    </c:if>

    <!--数码-->
    <c:if test="${m.key5 != null}">
        <div class="sm kind">
            <h3 class="clearfix kind-title border-1px_bottom">
                <p class="left_">${m.key5.title}</p>

                <p class="right_ half">${m.key5.detail}</p>

            <span class="right_ w-right" onclick="typeList(4)">查看更多 <span><img
                    src="${resourceUrl}/frontRes/gold/index/img/back.png" alt=""></span></span>
            </h3>
            <div class="banner-img" onclick="detail(${m.key5.list[0].pruductId})">
                <img src="${m.key5.list[0].pic}" alt="">

                <div class="product-desc">
                    <h1 class="ttl">${m.key5.list[0].name}</h1>

                    <p class="desc"></p>

                    <p class="true-price"><fmt:formatNumber
                            type="number" value="${m.key5.list[0].price/100}" pattern="0.00"
                            maxFractionDigits="0"/>元</p>

                    <c:if test="${score >= m.key5.list[0].price}">
                        <p class="now-price">或：0元+<fmt:formatNumber
                                type="number" value="${m.key5.list[0].price/100}" pattern="0.00"
                                maxFractionDigits="0"/>金币</p>
                    </c:if>
                    <c:if test="${score < m.key5.list[0].price}">
                        <p class="now-price">或：<fmt:formatNumber
                                type="number" value="${(m.key5.list[0].price-score3)/100}"
                                pattern="0.00"
                                maxFractionDigits="0"/>元+<fmt:formatNumber
                                type="number" value="${score3/100}" pattern="0.00"
                                maxFractionDigits="0"/>金币</p>
                    </c:if>
                </div>
            </div>
            <div class="products">
                <div class="product-item border-right border-1px_bottom"
                     onclick="detail(${m.key5.list[1].pruductId})">
                    <div class="good-img">
                        <img src="${m.key5.list[1].pic}" alt="">
                    </div>
                    <h1 class="ttl">${m.key5.list[1].name}</h1>

                    <p class="true-price"><fmt:formatNumber
                            type="number" value="${m.key5.list[1].price/100}" pattern="0.00"
                            maxFractionDigits="0"/>元</p>

                    <c:if test="${score >= m.key5.list[1].price}">
                        <p class="now-price">或：0元+<fmt:formatNumber
                                type="number" value="${m.key5.list[1].price/100}" pattern="0.00"
                                maxFractionDigits="0"/>金币</p>
                    </c:if>
                    <c:if test="${score < m.key5.list[1].price}">
                        <p class="now-price">或：<fmt:formatNumber
                                type="number" value="${(m.key5.list[1].price-score3)/100}"
                                pattern="0.00"
                                maxFractionDigits="0"/>元+<fmt:formatNumber
                                type="number" value="${score3/100}" pattern="0.00"
                                maxFractionDigits="0"/>金币</p>
                    </c:if>
                </div>
                <div class="product-item border-1px_bottom"
                     onclick="detail(${m.key5.list[2].pruductId})">
                    <div class="good-img">
                        <img src="${m.key5.list[2].pic}" alt="">
                    </div>
                    <h1 class="ttl">${m.key5.list[2].name}</h1>

                    <p class="true-price"><fmt:formatNumber
                            type="number" value="${m.key5.list[2].price/100}" pattern="0.00"
                            maxFractionDigits="0"/>元</p>

                    <c:if test="${score >= m.key5.list[2].price}">
                        <p class="now-price">或：0元+<fmt:formatNumber
                                type="number" value="${m.key5.list[2].price/100}" pattern="0.00"
                                maxFractionDigits="0"/>金币</p>
                    </c:if>
                    <c:if test="${score < m.key5.list[2].price}">
                        <p class="now-price">或：<fmt:formatNumber
                                type="number" value="${(m.key5.list[2].price-score3)/100}"
                                pattern="0.00"
                                maxFractionDigits="0"/>元+<fmt:formatNumber
                                type="number" value="${score3/100}" pattern="0.00"
                                maxFractionDigits="0"/>金币</p>
                    </c:if>
                </div>
            </div>
        </div>
    </c:if>

    <!--美妆-->
    <c:if test="${m.key6 != null}">
        <div class="mz kind">
            <h3 class="clearfix kind-title border-1px_bottom">
                <p class="left_">${m.key6.title}</p>

                <p class="right_ half">${m.key6.detail}</p>

            <span class="right_ w-right" onclick="typeList(2)">查看更多 <span><img
                    src="${resourceUrl}/frontRes/gold/index/img/back.png" alt=""></span></span>
            </h3>
            <div class="banner-img" onclick="detail(${m.key6.list[0].pruductId})">
                <img src="${m.key6.list[0].pic}" alt="">

                <div class="product-desc">
                    <h1 class="ttl">${m.key6.list[0].name}</h1>

                    <p class="desc"></p>

                    <p class="true-price w-ty"><fmt:formatNumber
                            type="number" value="${m.key6.list[0].price/100}" pattern="0.00"
                            maxFractionDigits="0"/>元</p>

                    <c:if test="${score >= m.key6.list[0].price}">
                        <p class="now-price w-ty">或：0元+<fmt:formatNumber
                                type="number" value="${m.key6.list[0].price/100}" pattern="0.00"
                                maxFractionDigits="0"/>金币</p>
                    </c:if>
                    <c:if test="${score < m.key6.list[0].price}">
                        <p class="now-price w-ty">或：<fmt:formatNumber
                                type="number" value="${(m.key6.list[0].price-score3)/100}"
                                pattern="0.00"
                                maxFractionDigits="0"/>元+<fmt:formatNumber
                                type="number" value="${score3/100}" pattern="0.00"
                                maxFractionDigits="0"/>金币</p>
                    </c:if>
                </div>
            </div>
            <div class="products">
                <div class="product-item border-right border-1px_bottom"
                     onclick="detail(${m.key6.list[1].pruductId})">
                    <div class="good-img">
                        <img src="${m.key6.list[1].pic}" alt="">
                    </div>
                    <h1 class="ttl">${m.key6.list[1].name}</h1>

                    <p class="true-price w-ty"><fmt:formatNumber
                            type="number" value="${m.key6.list[1].price/100}" pattern="0.00"
                            maxFractionDigits="0"/>元</p>

                    <c:if test="${score >= m.key6.list[1].price}">
                        <p class="now-price w-ty">或：0元+<fmt:formatNumber
                                type="number" value="${m.key6.list[1].price/100}" pattern="0.00"
                                maxFractionDigits="0"/>金币</p>
                    </c:if>
                    <c:if test="${score < m.key6.list[1].price}">
                        <p class="now-price w-ty">或：<fmt:formatNumber
                                type="number" value="${(m.key6.list[1].price-score3)/100}"
                                pattern="0.00"
                                maxFractionDigits="0"/>元+<fmt:formatNumber
                                type="number" value="${score3/100}" pattern="0.00"
                                maxFractionDigits="0"/>金币</p>
                    </c:if>
                </div>
                <div class="product-item border-1px_bottom"
                     onclick="detail(${m.key6.list[2].pruductId})">
                    <div class="good-img">
                        <img src="${m.key6.list[2].pic}" alt="">
                    </div>
                    <h1 class="ttl">${m.key6.list[2].name}</h1>

                    <p class="true-price w-ty"><fmt:formatNumber
                            type="number" value="${m.key6.list[2].price/100}" pattern="0.00"
                            maxFractionDigits="0"/>元</p>

                    <c:if test="${score >= m.key6.list[2].price}">
                        <p class="now-price w-ty">或：0元+<fmt:formatNumber
                                type="number" value="${m.key6.list[2].price/100}" pattern="0.00"
                                maxFractionDigits="0"/>金币</p>
                    </c:if>
                    <c:if test="${score < m.key6.list[2].price}">
                        <p class="now-price w-ty">或：<fmt:formatNumber
                                type="number" value="${(m.key6.list[2].price-score3)/100}"
                                pattern="0.00"
                                maxFractionDigits="0"/>元+<fmt:formatNumber
                                type="number" value="${score3/100}" pattern="0.00"
                                maxFractionDigits="0"/>金币</p>
                    </c:if>
                </div>
            </div>
        </div>
    </c:if>

    <!--食品-->
    <c:if test="${m.key7 != null}">
        <div class="sp kind">
            <h3 class="clearfix kind-title border-1px_bottom">
                <p class="left_">${m.key7.title}</p>

                <p class="right_ half">${m.key7.detail}</p>

            <span class="right_ w-right" onclick="typeList(3)">查看更多 <span><img
                    src="${resourceUrl}/frontRes/gold/index/img/back.png" alt=""></span></span>
            </h3>
            <div class="banner-img" onclick="detail(${m.key7.list[0].pruductId})">
                <img src="${m.key7.list[0].pic}" alt="">

                <div class="product-desc">
                    <h1 class="ttl">${m.key7.list[0].name}</h1>

                    <p class="desc"></p>

                    <p class="true-price w-ty"><fmt:formatNumber
                            type="number" value="${m.key7.list[0].price/100}" pattern="0.00"
                            maxFractionDigits="0"/>元</p>

                    <c:if test="${score >= m.key7.list[0].price}">
                        <p class="now-price w-ty">或：0元+<fmt:formatNumber
                                type="number" value="${m.key7.list[0].price/100}" pattern="0.00"
                                maxFractionDigits="0"/>金币</p>
                    </c:if>
                    <c:if test="${score < m.key7.list[0].price}">
                        <p class="now-price w-ty">或：<fmt:formatNumber
                                type="number" value="${(m.key7.list[0].price-score3)/100}"
                                pattern="0.00"
                                maxFractionDigits="0"/>元+<fmt:formatNumber
                                type="number" value="${score3/100}" pattern="0.00"
                                maxFractionDigits="0"/>金币</p>
                    </c:if>
                </div>
            </div>
            <div class="products">
                <div class="product-item border-right border-1px_bottom"
                     onclick="detail(${m.key7.list[1].pruductId})">
                    <div class="good-img">
                        <img src="${m.key7.list[1].pic}" alt="">
                    </div>
                    <h1 class="ttl">${m.key7.list[1].name}</h1>

                    <p class="true-price w-ty"><fmt:formatNumber
                            type="number" value="${m.key7.list[1].price/100}" pattern="0.00"
                            maxFractionDigits="0"/>元</p>

                    <c:if test="${score >= m.key7.list[1].price}">
                        <p class="now-price w-ty">或：0元+<fmt:formatNumber
                                type="number" value="${m.key7.list[1].price/100}" pattern="0.00"
                                maxFractionDigits="0"/>金币</p>
                    </c:if>
                    <c:if test="${score < m.key7.list[1].price}">
                        <p class="now-price w-ty">或：<fmt:formatNumber
                                type="number" value="${(m.key7.list[1].price-score3)/100}"
                                pattern="0.00"
                                maxFractionDigits="0"/>元+<fmt:formatNumber
                                type="number" value="${score3/100}" pattern="0.00"
                                maxFractionDigits="0"/>金币</p>
                    </c:if>
                </div>
                <div class="product-item border-1px_bottom"
                     onclick="detail(${m.key7.list[2].pruductId})">
                    <div class="good-img">
                        <img src="${m.key7.list[2].pic}" alt="">
                    </div>
                    <h1 class="ttl">${m.key7.list[2].name}</h1>

                    <p class="true-price w-ty"><fmt:formatNumber
                            type="number" value="${m.key7.list[2].price/100}" pattern="0.00"
                            maxFractionDigits="0"/>元</p>

                    <c:if test="${score >= m.key7.list[2].price}">
                        <p class="now-price w-ty">或：0元+<fmt:formatNumber
                                type="number" value="${m.key7.list[2].price/100}" pattern="0.00"
                                maxFractionDigits="0"/>金币</p>
                    </c:if>
                    <c:if test="${score < m.key7.list[2].price}">
                        <p class="now-price w-ty">或：<fmt:formatNumber
                                type="number" value="${(m.key7.list[2].price-score3)/100}"
                                pattern="0.00"
                                maxFractionDigits="0"/>元+<fmt:formatNumber
                                type="number" value="${score3/100}" pattern="0.00"
                                maxFractionDigits="0"/>金币</p>
                    </c:if>
                </div>
            </div>
        </div>
    </c:if>

    <!--居家 -->
    <c:if test="${m.key8 != null}">
        <div class="jj kind">
            <h3 class="clearfix kind-title border-1px_bottom">
                <p class="left_">${m.key8.title}</p>

                <p class="right_ half">${m.key8.detail}</p>

            <span class="right_ w-right" onclick="typeList(1)">查看更多 <span><img
                    src="${resourceUrl}/frontRes/gold/index/img/back.png" alt=""></span></span>
            </h3>
            <div class="banner-img" onclick="detail(${m.key8.list[0].pruductId})">
                <img src="${m.key8.list[0].pic}" alt="">

                <div class="product-desc">
                    <h1 class="ttl">${m.key8.list[0].name}</h1>

                    <p class="desc"></p>

                    <p class="true-price w-ty"><fmt:formatNumber
                            type="number" value="${m.key8.list[0].price/100}" pattern="0.00"
                            maxFractionDigits="0"/>元</p>

                    <c:if test="${score >= m.key8.list[0].price}">
                        <p class="now-price w-ty">或：0元+<fmt:formatNumber
                                type="number" value="${m.key8.list[0].price/100}" pattern="0.00"
                                maxFractionDigits="0"/>金币</p>
                    </c:if>
                    <c:if test="${score < m.key8.list[0].price}">
                        <p class="now-price w-ty">或：<fmt:formatNumber
                                type="number" value="${(m.key8.list[0].price-score3)/100}"
                                pattern="0.00"
                                maxFractionDigits="0"/>元+<fmt:formatNumber
                                type="number" value="${score3/100}" pattern="0.00"
                                maxFractionDigits="0"/>金币</p>
                    </c:if>
                </div>
            </div>
            <div class="products">
                <div class="product-item border-right border-1px_bottom"
                     onclick="detail(${m.key8.list[1].pruductId})">
                    <div class="good-img">
                        <img src="${m.key8.list[1].pic}" alt="">
                    </div>
                    <h1 class="ttl">${m.key8.list[1].name}</h1>

                    <p class="true-price w-ty"><fmt:formatNumber
                            type="number" value="${m.key8.list[1].price/100}" pattern="0.00"
                            maxFractionDigits="0"/>元</p>

                    <c:if test="${score >= m.key8.list[1].price}">
                        <p class="now-price w-ty">或：0元+<fmt:formatNumber
                                type="number" value="${m.key8.list[1].price/100}" pattern="0.00"
                                maxFractionDigits="0"/>金币</p>
                    </c:if>
                    <c:if test="${score < m.key8.list[1].price}">
                        <p class="now-price w-ty">或：<fmt:formatNumber
                                type="number" value="${(m.key8.list[1].price-score3)/100}"
                                pattern="0.00"
                                maxFractionDigits="0"/>元+<fmt:formatNumber
                                type="number" value="${score3/100}" pattern="0.00"
                                maxFractionDigits="0"/>金币</p>
                    </c:if>
                </div>
                <div class="product-item border-1px_bottom"
                     onclick="detail(${m.key8.list[2].pruductId})">
                    <div class="good-img">
                        <img src="${m.key8.list[2].pic}" alt="">
                    </div>
                    <h1 class="ttl">${m.key8.list[2].name}</h1>

                    <p class="true-price w-ty"><fmt:formatNumber
                            type="number" value="${m.key8.list[2].price/100}" pattern="0.00"
                            maxFractionDigits="0"/>元</p>

                    <c:if test="${score >= m.key8.list[2].price}">
                        <p class="now-price w-ty">或：0元+<fmt:formatNumber
                                type="number" value="${m.key8.list[2].price/100}" pattern="0.00"
                                maxFractionDigits="0"/>金币</p>
                    </c:if>
                    <c:if test="${score < m.key8.list[2].price}">
                        <p class="now-price w-ty">或：<fmt:formatNumber
                                type="number" value="${(m.key8.list[2].price-score3)/100}"
                                pattern="0.00"
                                maxFractionDigits="0"/>元+<fmt:formatNumber
                                type="number" value="${score3/100}" pattern="0.00"
                                maxFractionDigits="0"/>金币</p>
                    </c:if>
                </div>
            </div>
        </div>
    </c:if>

    <!--弹窗-->
    <div class="shadow">
        <div class="content">
            <span class="btn-close"></span>
            <img src="${resourceUrl}/frontRes/gold/index/img/tanchuang@2x.png" alt="">
        </div>
    </div>
</div>
<script src="${resourceUrl}/js/zepto.min.js"></script>
<script src="${resourceUrl}/frontRes/js/swiper.min.js"></script>
<script>
    $('.showGetScore').on('touchstart', function () {
        $('.shadow').css('display', 'block')
    });
    $('.btn-close').on('touchstart', function (e) {
        setTimeout(function () {
            $('.shadow').css('display', 'none');
        }, 300);
    });
</script>
<script>
    /*********************** 加载轮播图 *************************/
    $.get('/app/banner/gold', function (res) {
        var content = '';
        var list = eval("(" + res + ")");
        for (var i = 0; i < list.length; i++) {
            content +=
            '<div class="swiper-slide" style="height:205px;" onclick="bannerType('
            + list[i].afterType + ',' + list[i].url + ',' + list[i].productId
            + ')"><img src="' + list[i].picture + '" alt=""></div>';
        }
        document.getElementById("newPush-name").innerHTML = list[0].title;
        document.getElementById("newPush-dis").innerHTML = list[0].introduce;
        $('.swiper-wrapper').html(content);
        var swiper = new Swiper('.swiper-container', {
            pagination: '.swiper-pagination',
            paginationClickable: true,
            spaceBetween: 0,
            centeredSlides: true,
            autoplay: 2500,
            autoplayDisableOnInteraction: false,
            onSlideChangeStart: function (swiper) {
                document.getElementById("newPush-name").innerHTML = list[swiper.activeIndex].title;
                document.getElementById("newPush-dis").innerHTML =
                list[swiper.activeIndex].introduce;
            }
        });
    });

</script>
<script>
    /*********************** 话费充值 *************************/
    function goRecharge() {
        /** 点击话费充值事件统计*/
        $.get("/front/visit/recharge/0");
        window.location.href = '/front/order/weixin/recharge';
    }
    /*********************** 查看订单 *************************/
    function goOrderList() {
        window.location.href = '/front/order/weixin/orderList';
    }
    /*********************** 查看商品详情 *************************/
    function detail(productId) {
        /** 点击商品详情事件统计*/
        $.get("/front/visit/product/" + productId);
        window.location.href = '/front/gold/weixin/p?productId=' + productId;
    }
    /*********************** 查看商品分类列表页 *************************/
    function typeList(productType) {
        window.location.href = '/front/gold/weixin/type?type=' + productType;
    }
    /*********************** 轮播图后置类型判断 *************************/
    function bannerType(afterType, url, productId) {
        console.log(afterType + '===' + url + '====' + productId);
        switch (afterType) {
            case 1:
                window.location.href = url;
                break;
            case 2:
                detail(productId);
                break;
            default :
                break
        }
    }
    /** 进入该页面统计*/
    $.get("/front/visit/product/0");
</script>
</body>
</html>
