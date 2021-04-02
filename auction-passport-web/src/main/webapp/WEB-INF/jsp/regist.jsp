<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>一锤定价用户注册</title>
    <link rel="stylesheet" type="text/css" href="../../resources/static/regist/css/regist.css">
    <link rel="stylesheet" type="text/css" href="../../resources/static/sweetalert/sweetalert.css">
    <link type="text/css" rel="stylesheet" href="../../resources/bootstrap/css/bootstrap.css">
    <script type="text/javascript" src="../../resources/static/sweetalert/sweetalert-dev.js"></script>
    <script type="text/javascript" src="../../resources/static/js/jquery-3.1.1.min.js"></script>
</head>
<body>
<div id="regist-view">
    <div class="regist-dialog">
        <div class="regist-header">
            用户注册
        </div>
        <div class="regist-com">
            <div class="input-group">
                <span class="input-group-addon">用户账户</span>
                <input id="username" onchange="checkusername()" type="text" class="form-control" placeholder="请输入您的用户名"
                       onfocus="this.placeholder=&#39;&#39;" onblur="this.placeholder=&#39;请输入您的用户名&#39;" required>
                <span class="input-group-addon" id="name_trip"></span>
            </div>
        </div>
        <div class="regist-com">
            <div class="input-group">
                <span class="input-group-addon">用户昵称</span>
                <input id="nickname" onchange="checknickname()" type="text" class="form-control" placeholder="请输入您的用户昵称"
                       onfocus="this.placeholder=&#39;&#39;" onblur="this.placeholder=&#39;请输入您的用户昵称&#39;" required>
                <span class="input-group-addon" id="nick_trip"></span>
            </div>
        </div>
        <div class="regist-com">
            <div class="input-group">
                <span class="input-group-addon">用户密码</span>
                <input id="password" type="password" onchange="checkpassword()" class="form-control"
                       placeholder="请输入您的用户密码" onfocus="this.placeholder=&#39;&#39;"
                       onblur="this.placeholder=&#39;请输入您的用户密码&#39;" required>
                <span class="input-group-addon" id="password_trip"></span>
            </div>
        </div>
        <div class="regist-com">
            <div class="input-group">
                <span class="input-group-addon">确认密码</span>
                <input id="surePassword" type="password" onchange="checksurePassword()" class="form-control"
                       placeholder="请输入您的确认密码" onfocus="this.placeholder=&#39;&#39;"
                       onblur="this.placeholder=&#39;请输入您的确认密码&#39;" required>
                <span class="input-group-addon" id="surePassword_trip"></span>
            </div>
        </div>
        <div class="regist-com">
            <div class="input-group">
                <span class="input-group-addon">电话号码</span>
                <input id="phone" type="text" onchange="checkphone()" class="form-control" placeholder="请输入您的电话号码"
                       onfocus="this.placeholder=&#39;&#39;" onblur="this.placeholder=&#39;请输入您的电话号码&#39;" required>
                <span class="input-group-addon" id="phone_trip"></span>
            </div>
        </div>
        <div class="regist-com">
            <div class="input-group">
                <span class="input-group-addon">电子邮件</span>
                <input id="email" type="text" onchange="checkemail()" class="form-control" placeholder="请输入您的电子邮件"
                       onfocus="this.placeholder=&#39;&#39;" onblur="this.placeholder=&#39;请输入您的电子邮件&#39;" required>
                <span class="input-group-addon" id="emil_trip"></span>
            </div>
        </div>
        <div class="regist-com">
            <div class="radio input-group">
                <span class="input-group-addon">用户性别</span>
                <div class="radiocl">
                    <label><input type="radio" name="sex" onchange="checksex()" value="1">男</label>
                    <label><input type="radio" name="sex" onchange="checksex()" value="2">女</label>
                    <label><input type="radio" name="sex" onchange="checksex()" value="0">保密</label>
                </div>
                <span class="input-group-addon" id="sex_trip"></span>
            </div>
        </div>
        <div class="regist-com">
            <div class="input-group">
                <span class="input-group-addon">出生日期</span>
                <div style="background-color: white">
                    <input id="birthday" type="date" onchange="checkbirthday()" style="width: 250px;height: 30px;"
                           required>
                </div>
                <span class="input-group-addon" id="birthday_trip"></span>
            </div>
        </div>
        <div class="regist-bottom" style="display: inline-block">
            <div class="regist-button1" style="float: left" onclick="submitRegist()">
                注册
            </div>
            <div class="regist-button2" style="float: left" onclick="login()">
                登录
            </div>
        </div>

        <!--        出生日期:-->
        <!--        <input id="birthday" type="date" onchange="checkbirthday()" required>-->
        <!--        <label id="birthday_trip"></label>-->

    </div>
</div>
</body>
<script type="text/javascript">

    function login() {
        window.location.href = "http://passport.auction.com:8885/getLogin?ReturnUrl=http://search.auction.com:8883/index";
    }

    function trip(obj, trip) {
        $("#" + obj).html("<b>" + trip + "</b>")
    }

    function checkusername() {
        //检查用户账号格式
        var username = $("#username").val();
        if (username.length < 1 || username.length > 10) {
            trip("name_trip", "账号长度为1-10位");
            return false;
        } else {
            return usernameExitCheck();
        }
    }

    //检查用户名是否已经存在
    function usernameExitCheck() {

        var ret = true;
        var username = $("#username").val();
        $.ajax({
            url: 'usernameExitCheck',
            dataType: 'json',
            contentType: 'application/json',
            type: 'post',
            data: username,
            success: function (data) {
                if (data.type == 'success') {
                    trip("name_trip", "OK");
                } else {
                    trip("name_trip", "用户账号已经存在");
                    ret = false;
                }
            }
        });
        return ret;
    }

    function checknickname() {
        //检查用户昵称
        var nickname = $("#nickname").val();
        if (nickname.length == 0) {
            trip("nick_trip", "填写用户昵称");
            return false;
        } else {
            trip("nick_trip", "OK");
            return true;
        }
    }

    function checksex() {
        //检查性别
        var sexNum = $('[name="sex"]');
        var sex = "";
        for (let i = 0; i < sexNum.length; ++i) {
            if (sexNum[i].checked) {
                sex = sexNum[i];
            }
        }
        if (sex == "") {
            trip("sex_trip", "未选择性别");
            return false;
        } else {
            trip("sex_trip", "OK");
            return true;
        }
    }

    function checkpassword() {
        //检查密码
        var password = $("#password").val();
        if (password.length < 6) {
            trip("password_trip", "密码要大于6位");
            return false;
        } else {
            trip("password_trip", "OK");
            return true;
        }
    }

    function checksurePassword() {
        //检查确认密码
        var password = $("#password").val();
        var surePassword = $("#surePassword").val();
        if (surePassword < 6 || password != surePassword) {
            trip("surePassword_trip", "两次密码不一致");
            return false;
        } else {
            trip("surePassword_trip", "OK");
            return true;
        }
    }

    function checkemail() {
        //检查邮箱
        //校验邮箱:/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/
        var email = $("#email").val();
        if (!/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/.test(email)) {
            trip("emil_trip", "邮箱不合法");
            return false;
        } else {
            trip("emil_trip", "OK");
            return true;
        }
    }

    function checkphone() {
        //检查电话号码
        var phone = $("#phone").val();
        if (phone.length != 11) {
            trip("phone_trip", "电话号码长度为11位");
            return false;
        } else {
            trip("phone_trip", "OK");
            return true;
        }
    }

    function checkbirthday() {
        //检查生日
        var birthday = $("#birthday").val();
        if (birthday.length == 0) {
            trip("birthday_trip", "填写出生日期");
            return false;
        } else {
            trip("birthday_trip", "OK");
            return true;
        }
    }

    function checkForm() {

        var ret = true;
        if (!checkusername()) {
            alert("checkusername");
            ret = false;
        }
        if (!checkbirthday()) {
            alert("checkbirthday");
            ret = false;
        }
        if (!checknickname()) {
            alert("checknickname");
            ret = false;
        }
        if (!checksex()) {
            alert("checksex");
            ret = false;
        }
        if (!checkpassword()) {
            alert("checkpassword");
            ret = false;
        }
        if (!checksurePassword()) {
            alert("checksurePassword");
            ret = false;
        }
        if (!checkemail()) {
            alert("checkemail");
            ret = false;
        }
        if (!checkphone()) {
            alert("checkphone");
            ret = false;
        }

        return ret;
    }

    function submitRegist() {

        if (checkForm()) {

            var username = $("#username").val();
            var password = $("#surePassword").val();
            var nickname = $("#nickname").val();
            var phone = $("#phone").val();
            var gender = $('input:radio:checked').val();
            var birthday = $("#birthday").val();

            var email = $("#email").val();

            var umsMember = {
                'username': username,
                'password': password,
                'nickname': nickname,
                'phone': phone,
                'gender': gender,
                'birthday': birthday
            };
            var umsMemberJSON = JSON.stringify(umsMember);

            $.ajax({
                url: 'regist',
                dataType: 'json',
                contentType: 'application/json',
                type: 'post',
                data: umsMemberJSON,
                success: function (data) {
                    if (data.type == 'success') {
                        swal("注册成功", "success");
                        window.location.href = "http://passport.auction.com:8885/getLogin?ReturnUrl=http://search.auction.com:8883/index";
                    } else {
                        swal("注册失败", "error");
                    }
                }
            });


        } else {
            swal("信息填写错误", "error");
        }
    }
</script>
</html>
