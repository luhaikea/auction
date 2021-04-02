<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="../../resources/static/login/css/login.css"/>
    <script src="../../resources/static/js/jquery-3.1.1.min.js" type="text/javascript" charset="utf-8"></script>
    <link rel="stylesheet" type="text/css" href="../../resources/static/sweetalert/sweetalert.css"/>
    <script src="../../resources/static/sweetalert/sweetalert-dev.js" type="text/javascript" charset="utf-8"></script>
    <link type="text/css" rel="stylesheet" href="../../resources/bootstrap/css/bootstrap.css">
    <title>用户登录</title>
</head>
<body>
<div id="login-view">
    <div class="login-dialog">
        <div class="login-header">
            用户登录
        </div>
        <div class="login-com">
            <div class="input-group">
                <span class="input-group-addon">用户名</span>
                <input id="username" type="text" class="form-control" placeholder="请输入您的用户名" onfocus="this.placeholder=&#39;&#39;"
                       onblur="this.placeholder=&#39;请输入您的用户名&#39;">
            </div>
        </div>
        <div class="login-com">
            <div class="input-group">
                <span class="input-group-addon">密&nbsp&nbsp&nbsp码</span>
                <input id="password" type="password" class="form-control" placeholder="请输入您的密码" onfocus="this.placeholder=&#39;&#39;"
                       onblur="this.placeholder=&#39;请输入您的密码&#39;">
            </div>
        </div>
        <div class="login-com">
            <div class="input-group">
                <span class="input-group-addon">点击图片切换验证码</span>
                <img id="cpacha-img" onclick="changeCpacha()" title="点击切换验证码" style="cursor:pointer;"
                     src="getCpacha?vl=4&w=150&h=40&type=loginCpacha" width="170px" height="35px"
                     onclick="changeCpacha()">
            </div>
        </div>
        <div class="login-com">
            <div class="input-group">
                <span class="input-group-addon">验证码</span>
                <input id="cpacha" type="text" class="form-control" placeholder="请输入您的验证码" onfocus="this.placeholder=&#39;&#39;"
                       onblur="this.placeholder=&#39;请输入您的验证码&#39;">
            </div>
        </div>
        <div class="login-button" onclick="submitLogin()">
            登录
        </div>
        <div class="login-bottom" style="display: inline-block">
            <div class="login-weibo" style="float: left" onclick="weiboLogin()">
                <img src="../../resources/static/login/img/weibolog2.png" height="40px" width="40px">
                微博登录
            </div>
            <div class="login-regist" style="float: left" onclick="regist()">
                注册
            </div>
        </div>

    </div>
</div>
<input type="hidden" value="${ReturnUrl}" id="ReturnUrl"/>
</body>

<script language="JavaScript">

    function changeCpacha() {
        //这个url后面必须加时间才使验证码刷新成功，也就是两次的url要不相同  幂等性
        $("#cpacha-img").attr("src", 'getCpacha?t=' + new Date().getTime());
    }

    function regist(){
        window.location.href ="http://passport.auction.com:8885/toRegist";
    }

    function weiboLogin(){
        window.location.href ="https://api.weibo.com/oauth2/authorize?client_id=2486814460&response_type=code&redirect_uri=http://passport.auction.com:8885/redirect";
    }

    function submitLogin() {
        var username = $("#username").val();
        if(username == '' || username == 'undefined'){
            swal("用户名为空！","error");
        }
        var password = $("#password").val();
        if(password == '' || password == 'undefined'){
            swal("密码为空！","error");
        }
        var cpacha = $("#cpacha").val();
        if(cpacha == '' || cpacha == 'undefined'){
            swal("验证码为空！","error");
        }
        var umsMember = {'username': username, 'password': password, 'cpacha':cpacha};
        var umsMemberJSON = JSON.stringify(umsMember);

        $.ajax({
            url: 'getToken',
            dataType: 'json',
            contentType: 'application/json',
            type: 'post',
            data: umsMemberJSON,
            success: function (data) {
                if (data.type == 'fail') {
                    swal(data.msg,"error");
                } else {
                    // 验证token是否为空或者异常    新token
                    //http://item.auction.com:8883/1.html  &token=eyJhbGciOiJIUzI1NiJ9.eyJuaWNrbmFtZSI6ImxoayIsIm1lbWJlcklkIjoiMSJ9.QYlFFyITs8x8U1eroCcwZhjlZhxkVgpGbVvgZ0V7D74
                    // http://order.auction.com:8884/toTrade?quentity=1&pmsProductInfoId=1 &token=eyJhbGciOiJIUzI1NiJ9.eyJuaWNrbmFtZSI6ImxoayIsIm1lbWJlcklkIjoiMSJ9.QYlFFyITs8x8U1eroCcwZhjlZhxkVgpGbVvgZ0V7D74
                    //这是在item页面点击登录的链接
                    //http://passport.auction.com:8885/getLogin?ReturnUrl=http://item.auction.com:8882/${pmsProductInfo.id}.html
                    var url = $("#ReturnUrl").val();
                    //对应两种加入登录页面的方式 一种是直接点击登录一种是被踢回到登录页面
                    //已经包含参数
                    if (url.indexOf('?') != -1) {
                        window.location.href = url + "&token=" + data.token;
                    }
                    //不含参数
                    else {
                        window.location.href = url + "?token=" + data.token;
                    }

                }
            }
        });

    }

</script>

</html>

