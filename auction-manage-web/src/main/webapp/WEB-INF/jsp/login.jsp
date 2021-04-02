<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>后台管理系统</title>
    <link rel="stylesheet" type="text/css" href="../../resources/login/css/login.css">
    <link rel="stylesheet" type="text/css" href="../../resources/login/sweetalert/sweetalert.css">
    <link rel="stylesheet" type="text/css" href="../../resources/bootstrap/css/bootstrap.css">
    <script type="text/javascript" src="../../resources/login/sweetalert/sweetalert-dev.js"></script>
</head>
<body>

<div id="login-view">
    <div class="login-dialog">
        <div class="login-header">
            后台管理系统
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
                <img id="cpacha-img" title="点击切换验证码" style="cursor:pointer;"
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
        <div class="login-button">
            登录
        </div>

    </div>

</div>
<!-- 这个js文件必须在body中引入 -->
<script type="text/javascript" src="../../resources/login/js/particles.min.js"></script>
<script type="text/javascript" src="../../resources/login/js/particlesParm.js"></script>
<script type="text/javascript" src="../../resources/login/js/jquery-3.1.1.min.js"></script>

<script type="text/javascript">

    function changeCpacha() {
        //这个url后面必须加时间才使验证码刷新成功，也就是两次的url要不相同  幂等性
        $("#cpacha-img").attr("src", 'getCpacha?t=' + new Date().getTime());
    }

    $(".login-button").click(function () {

        var username = $("#username").val();
        var password = $("#password").val();
        var cpacha = $("#cpacha").val();
        if (username == '' || username == 'undefined') {
            swal("请填写用户名！" ,"error");
            return;
        }
        if (password == '' || password == 'undefined') {
            swal("请填写密码！" ,"error");
            return;
        }
        if (cpacha == '' || cpacha == 'undefined') {
            swal("请填写验证码！" ,"error");
            return;
        }

        $.ajax({
            url: 'login',
            data: {username: username, password: password, cpacha: cpacha},
            type: 'post',
            dataType: 'json',
            success: function (data) {
                if (data.type == 'success') {
                    window.parent.location = 'index';
                } else {
                    swal(data.msg ,"error");
                    changeCpacha();
                }
            }
        });

    });

</script>
</body>
</html>