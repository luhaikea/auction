<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <link type="text/css" rel="stylesheet" href="../../resources/bootstrap/css/bootstrap.css">
    <script src="../../resources/survey/js/jquery-3.1.1.min.js"></script>
    <link rel="stylesheet" type="text/css" href="../../resources/message/css/message.css"/>
    <title>系统消息</title>
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
            |<a href="http://order.auction.com:8884/orderList">我的拍卖</a>|
        </div>
    </div>
    <div class="header-bottom">
        <div class="header-bottom-left">
            <img src="../../resources/survey/img/auctionlog.jpeg"/>
        </div>
    </div>
    <hr class="division1"/>
    <div class="header-down">
        <div class="header-item">系统信息</div>
    </div>
</div>
<div class="msg-body">
    <c:forEach items="${umsReplayInfos}" var="umsReplayInfo">
        <div class="mssage">
            <div class="title">${umsReplayInfo.replayTitle}</div>
            <div class="time"></div>
            <div class="content">
                    ${umsReplayInfo.replayMsg}
            </div>
        </div>
    </c:forEach>
</div>
</body>
<script>
    $(function () {
        var createTime = new Date(${umsReplayInfo.createTime});
        var year = createTime.getFullYear(); //获取完整的年份(4位,1970-????)
        var month = createTime.getMonth(); //获取当前月份(0-11,0代表1月)
        var date = createTime.getDate(); //获取当前日(1-31)
        var hours = createTime.getHours(); //获取当前小时数(0-23)
        var minutes =createTime.getMinutes(); //获取当前分钟数(0-59)
        var time = year+"年 "+month+"月"+date+"日 "+hours+":"+minutes;
        $(".time").text(time);
    })
</script>
</html>