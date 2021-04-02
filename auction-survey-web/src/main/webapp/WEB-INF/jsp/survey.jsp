<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <link type="text/css" rel="stylesheet" href="../../resources/bootstrap/css/bootstrap.css">
    <script src="../../resources/survey/js/jquery-3.1.1.min.js"></script>
    <link rel="stylesheet" type="text/css" href="../../resources/survey/css/survey.css"/>
    <link rel="stylesheet" type="text/css" href="../../resources/sweetalert/sweetalert.css">
    <script src="../../resources/sweetalert/sweetalert-dev.js"></script>
    <title>用户反馈调查</title>
</head>
<body>

<div class="header">
    <div class="header-top">
        <div class="header-top-left">
            <div class="glyphicon glyphicon-home"><a href="http://search.auction.com:8883/index">首页</a></div>
        </div>
        <div class="header-top-right">
            <c:if test="${nickname==null}">
                |<a href="http://passport.auction.com:8885/getLogin?ReturnUrl=http://survey.auction.com:8887/getSurvey">你好，请登录</a>
                |<a href="http://passport.auction.com:8885/toRegist">免费注册</a>
            </c:if>
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
        <div class="header-item">用户反馈调查</div>
    </div>
</div>

<div class="feedback">
    <div class="feedback-title">
        如果您在使用一锤定价拍卖时，有什么好或不好的地方，请大声说出来！我们会关注您的反馈，不断优化产品，为您提供更好的服务！
    </div>
    <div class="feedback-textarea">
        <textarea id="userFeedback" name="feedbackText" rows="5"></textarea>
    </div>
</div>
<div class="satisfaction">
    <div class="satisfaction-title">
        您对一锤定价拍卖的整体满意度如何？
    </div>
    <div class="satisfaction-body">
        <input type="radio" value="1" name="satisfaction-degree">非常满意<br/>
        <input type="radio" value="2" name="satisfaction-degree">满意<br/>
        <input type="radio" value="3" name="satisfaction-degree">一般<br/>
        <input type="radio" value="4" name="satisfaction-degree">不满意<br/>
        <input type="radio" value="5" name="satisfaction-degree">非常不满意<br/>
    </div>
</div>
<div class="user-info">
    <div class="user-info-title">
        我们会不定期邀请用户参与体验交流。如果您有意参与，请填写如下信息，方便我们与您联系，谢谢！（信息仅作为内部资料绝不外泄）
    </div>
    <div class="user-info-input">
        <div class="input-group">
            <span class="input-group-addon">姓&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp名</span>
            <input id="username"  type="text" class="form-control" style="width: 300px">
        </div>
        <div class="input-group">
            <span class="input-group-addon">电话号码</span>
            <input id="phone" type="text" class="form-control" style="width: 300px">
        </div>
    </div>
</div>
<div class="res-btn">
    <div class="res-btn-sub">
        <input id="saveSurveyInfoBtn" class="btn btn-primary" type="button" value="提交">
    </div>
</div>
<script>
    $("#saveSurveyInfoBtn").click(function (){
        var phone = $('#phone').val();
        var username = $('#username').val();
        var userFeedback = $('#userFeedback').val();
        var satisfaction = $('input[name="satisfaction-degree"]:checked').val();
        var surveyInfo = {'phone':phone, 'username':username,'userFeedback':userFeedback,'satisfaction':satisfaction};
        var surveyInfoJSON = JSON.stringify(surveyInfo);
        $.ajax({
            url:'saveSurveyInfo',
            dataType:'json',
            contentType:'application/json',
            type:'post',
            data:surveyInfoJSON,
            success:function(data){
                if(data.type == 'success'){
                    swal(data.msg, "success");
                    window.location.href ="http://search.auction.com:8883/index";
                }else{
                    swal(data.msg, "error");
                }
            }
        });
    })
</script>
</body>
</html>