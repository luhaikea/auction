<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>物品详情</title>
    <link rel="stylesheet" type="text/css" href="../../resources/item/css/item.css"/>
    <link rel="stylesheet" type="text/css" href="../../resources/bootstrap/css/bootstrap.css"/>
    <script src="../../resources/item/js/jquery-3.1.1.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="../../resources/item/js/item.js" type="text/javascript" charset="utf-8"></script>
</head>
<body>
<input id="startTime" type="hidden" value="${pmsProductInfo.startTime}">
<input id="endTime" type="hidden" value="${pmsProductInfo.endTime}">
<div class="header">
    <div class="header-top">
        <div class="header-top-left">
            <div class="glyphicon glyphicon-home"><a href="http://search.auction.com:8883/index">首页</a></div>
        </div>
        <div class="header-top-right">
            <c:if test="${nickname!=null}">
                |<a href="#">你好，${nickname}</a>
            </c:if>
            <c:if test="${nickname==null}">
                |<a href="http://passport.auction.com:8885/getLogin?ReturnUrl=http://item.auction.com:8882/${pmsProductInfo.id}.html">你好，请登录</a>
                |<a href="http://passport.auction.com:8885/toRegist">免费注册</a>
            </c:if>
            |<a href="http://order.auction.com:8884/orderList">我的拍卖</a>
            |<a href="http://survey.auction.com:8887/getSurvey">用户反馈</a>
            |<a href="http://survey.auction.com:8887/getMyMessage">我的信息</a>|
        </div>
    </div>
    <div class="header-bottom">
        <div class="header-bottom-left">
            <img src="../../resources/item/img/auctionlog.jpeg"/>
        </div>
        <div class="header-bottom-right">
            <input type="text"/><button>搜索</button>
        </div>
    </div>
    <hr class="division1"/>
    <div class="header-down">
        <div class="header-item">拍品详情</div>
    </div>
</div>

<div class="product">
    <div class="product-item">
        <div class="product-item-img">
            <div class="product-item-img-display">
                <div class="probox">
                    <!--没有？会发生空指针异常  就是当skuInfo在服务器没有传过来的时候即为null时如果没有？就成了null.skuDefaultImg-->
                    <img class="img1" alt="" src="${pmsProductInfo.productDefaultImg}">
                    <div class="hoverbox"></div>
                </div>
                <div class="showbox">
                    <img class="img1" alt="" src="${pmsProductInfo.productDefaultImg}">
                </div>
            </div>
            <div class="product-item-img-list">
                <div class="product-item-img-list-dis">
                    <ul>
                        <c:forEach items="${pmsProductInfo.pmsProductImages}" var="pmsProductImage">
                            <li><img src="${pmsProductImage.imgUrl}"/></li>
                        </c:forEach>
                    </ul>
                </div>
                <div id="left">
                    <
                </div>
                <div id="right">
                    >
                </div>
            </div>
            <div class="product-attr">
                <table class="table table-hover">
                    <thead>
                    <th style="text-align: center">拍品信息</th>
                    <tr>
                        <th>拍品属性名称</th>
                        <th>拍品属性值</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${pmsProductInfo.pmsProductSaleAttrs}" var="pmsProductSaleAttr">
                        <tr>
                            <td>${pmsProductSaleAttr.saleAttrName}</td>
                            <td>${pmsProductSaleAttr.saleAttrValue}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="product-item-message">
            <div class="product-name">【${pmsProductInfo.productName}】 ${pmsProductInfo.productDesc}</div>
            <div class="product-statusAndtime">
                <div class="product-status">
                    <ul>
                        <li>拍卖状态</li>
                    </ul>
                </div>
                <div class="product-time">
                    <ul>
                        <li id="auctionStatus"></li>
                    </ul>
                </div>
            </div>
            <div class="product-price-messge">
                <ul>
                    <li>当前最高价：</li>
                    <li>
                        <span>￥</span>
                        <span>${pmsProductInfo.topPrice}</span>
                        <input id="topPrice" type="hidden" value="${pmsProductInfo.topPrice}">
                    </li>
                </ul>
            </div>
            <div class="product-price-messge">
                <ul>
                    <li>起拍价：</li>
                    <li>
                        <span>￥</span>
                        <span>${pmsProductInfo.startingPrice}</span>
                        <input id="startingPrice" type="hidden" value="${pmsProductInfo.startingPrice}">
                    </li>
                </ul>
            </div>
            <div class="product-price-messge">
                <ul>
                    <li>保证金：</li>
                    <li>
                        <span>￥</span>
                        <span>${pmsProductInfo.earnest}</span>
                    </li>
                </ul>
            </div>
            <div class="product-price-messge">
                <ul>
                    <li>加价幅度：</li>
                    <li>
                        <span>￥</span>
                        <span>10.00</span>
                    </li>
                </ul>
            </div>
            <div class="product-btns">
                <div class="product-btns-price">
                    <input type="text" id="quantity" name="quantity" readonly="readonly"
                           style="width: 100px;font-size: medium;color: #c9302c"/>
                    <div class="box-btns-AddSubtract">
                        <div>
                            <button type="button" id="add">+</button>
                        </div>
                        <div>
                            <button type="button" id="subtract">-</button>
                        </div>
                    </div>
                </div>
                <div class="product-btns-confirm" id="cartBtn" style="cursor:pointer;" canClick="1"
                     onclick="confirmOffer(this)">确认出价
                </div>
            </div>

            <div class="product-order">
                <table class="table table-hover">
                    <thead>
                    <th style="text-align: center">此拍品被出价${pmsProductInfo.numBid}次</th>
                    <tr>
                        <th>出价用户ID</th>
                        <th>出价</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${pmsProductInfo.omsOrders}" var="omsOrder">
                        <tr>
                            <td>${omsOrder.memberId}</td>
                            <td>￥${omsOrder.bidPrice}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript">

    function confirmOffer(obj) {
        var quentity = $("#quantity").val();
        window.location.href = "http://order.auction.com:8884/confirmOffer?quentity=" + quentity + "&pmsProductInfoId=" +${pmsProductInfo.id};
    }

    $(function () {

        fn();
        //初始化出价框  如果已经有出价，那最低价就是最高出价加一，如果没有出价那最低价就是起拍价
        var topPrice = eval($("#topPrice").val());
        var startingPrice = eval($("#startingPrice").val());
        if (topPrice > startingPrice) {
            $("#quantity").val(topPrice + 1);
        } else {
            $("#quantity").val(startingPrice);
        }

        function fn() {

            var startTime = new Date($("#startTime").val());
            var endTime = new Date($("#endTime").val());
            var nowTime = new Date();
            if (nowTime.getTime() < startTime.getTime()) {
                //拍卖未开始
                var diffTime = parseInt((startTime.getTime() - nowTime.getTime()) / 1000);
                //将时间转化为时 分 秒
                var hour, minute, second;
                second = diffTime % 60;//秒
                minute = parseInt(diffTime / 60) % 60;
                hour = parseInt(diffTime / 60 / 60);
                $("#auctionStatus").text('距离物品的拍卖开始剩余：' + hour + '小时' + minute + '分钟' + second + '秒');
                $("#cartBtn").css("pointer-events", "none");
            }
            if (nowTime.getTime() > endTime.getTime()) {
                //拍卖已结束
                $("#auctionStatus").text('该物品的拍卖已结束');
                //禁止点击确认出价按钮    none不可点击  auto可点击
                $("#cartBtn").css("pointer-events", "none");
            }
            if (nowTime.getTime() > startTime.getTime() && nowTime.getTime() < endTime.getTime()) {
                //正在进行拍卖
                var diffTime = parseInt((endTime.getTime() - nowTime.getTime()) / 1000);
                //将时间转化为时 分 秒
                var hour, minute, second;
                second = diffTime % 60;//秒
                minute = parseInt(diffTime / 60) % 60;
                hour = parseInt(diffTime / 60 / 60);
                $("#auctionStatus").text('距离物品的拍卖结束剩余：' + hour + '小时' + minute + '分钟' + second + '秒');
                $("#cartBtn").css("pointer-events", "auto");
            }
            //一秒之后调用fn
            setTimeout(fn, 1000);
        }
    });

    $("#add").click(function () {
        var value = eval($("#quantity").val()) + 9;
        $("#quantity").val(value);
    });

    $("#subtract").click(function () {
        var topPrice = eval($("#topPrice").val());
        var startingPrice = eval($("#startingPrice").val());
        if (topPrice < startingPrice) {
            topPrice = startingPrice;
        }
        var currentPrice = eval($("#quantity").val()) - 9;
        if (currentPrice >= topPrice) {
            $("#quantity").val(currentPrice);
        } else {
            $("#quantity").val(topPrice + 2);
        }
    });

</script>

</html>
