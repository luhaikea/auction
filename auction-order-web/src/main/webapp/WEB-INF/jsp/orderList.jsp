<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>我的拍卖</title>
    <link rel="stylesheet" href="../../resources/order/css/orderList.css">
    <link rel="stylesheet" type="text/css" href="../../resources/bootstrap/css/bootstrap.css"/>
    <script type="text/javascript" src="../../resources/order/js/jquery-3.1.1.min.js"></script>
</head>
<body>
<div class="header">
    <div class="header-top">
        <div class="header-top-left">
            <div class="glyphicon glyphicon-home"><a href="http://search.auction.com:8883/index">首页</a></div>
        </div>
        <div class="header-top-right">
            |<a href="#">你好，${nickname}</a>
            |<a href="http://survey.auction.com:8887/getSurvey">用户反馈</a>
            |<a href="http://survey.auction.com:8887/getMyMessage">我的信息</a>|
        </div>
    </div>
    <div class="header-bottom">
        <div class="header-bottom-left">
            <img src="../../resources/order/img/auctionlog.jpeg"/>
        </div>
        <div class="header-bottom-right">
            <input type="text"/><button>搜索</button>
        </div>
    </div>
    <hr class="division1"/>
    <div class="header-down">
        <div class="header-item">我的拍卖</div>
    </div>
</div>

<div class="myauction">
    <table class="table table-hover">
        <caption class="list-title">全部拍品</caption>
        <tr>
            <th><input type="checkbox" class="allCheck">全选</th>
            <th>拍品</th>
            <th>当前出价</th>
            <th>保证金</th>
            <th>保证金支付状态</th>
            <th>竞拍状态</th>
            <th>补款状态</th>
            <th>操作</th>
        </tr>
        <c:forEach items="${omsOrders}" var="omsOrder">
            <tr>
                <td>
                    <input type="checkbox" class="check">选择
                </td>
                <td>
                    <a href="http://item.auction.com:8882/${omsOrder.productId}.html" ><img style="vertical-align: middle" width="50px" height="40px" src="${omsOrder.pmsProductInfo.productDefaultImg}"><span style="vertical-align: middle">${omsOrder.pmsProductInfo.productName}</span></a>
                </td>
                <td>
                    <p>￥${omsOrder.bidPrice}</p>
                </td>
                <td>
                    <p>￥${omsOrder.pmsProductInfo.earnest}</p>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${omsOrder.earnestStatus==1}">
                            <p>已支付</p>
                        </c:when>
                        <c:otherwise>
                            <p>未支付</p>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${omsOrder.auctionStatus==0}">
                            <p>待参拍</p>
                        </c:when>
                        <c:when test="${omsOrder.auctionStatus==1}">
                            <p>正在竟拍</p>
                        </c:when>
                        <c:when test="${omsOrder.auctionStatus==2}">
                            <p>获拍</p>
                        </c:when>
                        <c:otherwise>
                            <p>未获拍</p>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${omsOrder.finalPayStatus==0}">
                            <p>待参拍</p>
                        </c:when>
                        <c:when test="${omsOrder.finalPayStatus==1}">
                            <p>已支付</p>
                        </c:when>
                        <c:otherwise>
                            <p>未支付</p>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${omsOrder.earnestStatus==0}">
                            <a href="#" onclick="earnestMoney(${omsOrder.id})">支付保证金</a>
                        </c:when>

                        <c:when test="${omsOrder.earnestStatus==1 && omsOrder.auctionStatus==3}">
                            <p>订单完成</p>
                        </c:when>
                        <c:when test="${omsOrder.earnestStatus==1 && omsOrder.auctionStatus==2 && omsOrder.finalPayStatus==1}">
                            <p>订单完成</p>
                        </c:when>
                        <c:when test="${omsOrder.earnestStatus==1 && omsOrder.auctionStatus==2 && omsOrder.finalPayStatus==2}">
                            <a href="#" onclick="finallyMoney(${omsOrder.id})">补款</a>
                        </c:when>
                        <c:otherwise>
                            <p>等待结果</p>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
<form action="./earnestPay" method="post" id="earnestPay">
    <input name="orderId" id="orderId1" type="hidden"/>
</form>
<form action="./finalPay" method="post" id="finalPay">
    <input name="orderId" id="orderId2" type="hidden"/>
</form>
</body>
<script type="text/javascript">
    function finallyMoney(orderId) {
        $("#orderId2").val(orderId);
        $("#finalPay").submit();
    }
    function earnestMoney(orderId) {
        $("#orderId1").val(orderId);
        $("#earnestPay").submit();
    }
</script>
</html>



