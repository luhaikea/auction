<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title></title>
    <link rel="stylesheet" href="../../resources/payment/css/paymentSelect.css">
    <script src="../../resources/payment/js/jquery-3.1.1.min.js" charset="utf-8"></script>
    <link type="text/css" rel="stylesheet" href="../../resources/bootstrap/css/bootstrap.css">
</head>
<body>
<div class="header">
    <div class="header-top">
        <div class="header-top-left">
            <div class="glyphicon glyphicon-home"><a href="http://search.auction.com:8883/index">首页</a></div>
        </div>
        <div class="header-top-right">
            <c:if test="${nickname!=null}">
                |<a href="#">你好，${nickname}</a>
            </c:if>
            |<a href="http://order.auction.com:8884/orderList">我的拍卖</a>
            |<a href="http://survey.auction.com:8887/getSurvey">用户反馈</a>
            |<a href="http://survey.auction.com:8887/getMyMessage">我的信息</a>|
        </div>
    </div>
    <div class="header-bottom">
        <div class="header-bottom-left">
            <img src="../../resources/payment/img/auctionlog.jpeg"/>
        </div>
        <div class="header-bottom-right">
            <img src="../../resources/payment/img/auctionPlan.PNG">
        </div>
    </div>
    <hr class="division1"/>
    <div class="header-down">
        <div class="header-item">保证金支付页</div>
    </div>
</div>

<div class="paymentMsg">
    <div class="paymentOrderSn">
        <dl>
            <dd>
                <span>订单提交成功，请尽快付款！订单号：${orderSnFinal}</span>
                <span>应付金额${bidPrice}元</span>
            </dd>
            <dd>
                <span></span>
                <span>推荐使用支付宝扫码支付请您在<font>24小时</font>内完成支付，否则订单会被自动取消</span>
                <span></span>
            </dd>
        </dl>
    </div>
    <div class="payment-con">
        <div class="payment-select">
            <ul>
                <li>
                    <span>
                        <input name="paymentWay" type="radio" value="alipayFinal"/>
                        <img src="../../resources/payment/img/alipay.jpg" alt="">支付宝
                    </span>
                </li>
                <li>
                    <span>
                        <input name="paymentWay" type="radio" value="mx"/>
                        <img src="../../resources/payment/img/weixin.png" alt="">微信支付
                    </span>
                </li>
                <li>
                    <button id="paymentButton">立即支付</button>
                </li>
            </ul>
        </div>
    </div>
</div>


<form method="post" id="paymentForm">
    <input type="hidden" name="orderSnFinal" value="${orderSnFinal}">
    <input type="hidden" name="bidPrice" value="${bidPrice}">
</form>
</body>
<script type="text/javascript">
    $(function () {
        $("#paymentButton").click(function () {
            $("#paymentForm").attr("action", "/" + $("input[type='radio']:checked").val());
            $("#paymentForm").submit();
        })
    })

    //立即支付按钮效果
    $("#paymentButton").mouseover(function () {
        $(this).css({"cursor": "pointer", "background": "#FF5350"})
    }).mouseout(function () {
        $(this).css("background", "#FC6E6C")
    })

</script>
</html>
