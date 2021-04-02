<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <link type="text/css" rel="stylesheet" href="../../resources/bootstrap/css/bootstrap.css">
    <script src="../../resources/order/js/jquery-3.1.1.min.js"></script>
    <link rel="stylesheet" type="text/css" href="../../resources/order/css/orderConfirm.css"/>
    <script src="../../resources/order/js/city.js"></script>
    <link rel="stylesheet" type="text/css" href="../../resources/sweetalert/sweetalert.css">
    <script type="text/javascript" src="../../resources/sweetalert/sweetalert-dev.js"></script>
    <title>订单确认</title>
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
            <img src="../../resources/order/img/auctionlog.jpeg"/>
        </div>
        <div class="header-bottom-right">
            <img src="../../resources/order/img/auctionPlan.PNG">
        </div>
    </div>
    <hr class="division1"/>
    <div class="header-down">
        <div class="header-item">保证金结算页</div>
    </div>
</div>
<!--主体部分-->
<p class="title">填写并核对一下信息</p>
<div class="section">
    <!--收货人信息-->
    <div class="address-msg">
        <span>收货人信息</span>
        <span onclick="addAddress()">+新增收货地址</span>
    </div>
    <!--地址-->
    <div class="address-list">
        <ul>
            <c:forEach items="${umsMemberReceiveAddressList}" var="umsMemberReceiveAddress">
                <li class=".address default selected">
                    <input name="deliveryAddress" type="radio" value="${umsMemberReceiveAddress.id}"
                           onclick="changeAddress()">
                    <span>${umsMemberReceiveAddress.name}&nbsp;&nbsp;&nbsp;</span>
                    <span>${umsMemberReceiveAddress.province}${umsMemberReceiveAddress.city}${umsMemberReceiveAddress.region}${umsMemberReceiveAddress.detailAddress}&nbsp;&nbsp;&nbsp;${umsMemberReceiveAddress.phoneNumber}</span>
                </li>
            </c:forEach>
        </ul>
    </div>
    <!--分割线-->
    <div class="cross-line"/>
    <div class="product-title">
        <span>拍品信息</span>
    </div>
    <div class="product-msg">
        <div class="product-img">
            <img src="${pmsProductInfo.productDefaultImg}"/>
        </div>
        <div class="product-name">${pmsProductInfo.productName}</div>
        <div class="product-quentity">确认出价:￥${quentity}</div>
        <div class="product-earnest">保证金:￥${pmsProductInfo.earnest}</div>
    </div>
    <div class="aggree-auction">
        <div class="auction-earnest">保证金合计：<span>￥${pmsProductInfo.earnest}</span></div>
        <div class="aggree-but">
            <button id="submitButton">同意参拍</button>
        </div>
    </div>
</div>

<div class="addAddr" id='addAddr'>
    <div class="dialog">
        <div id="header">
            <div class="header-title">新增收货地址</div>
            <div id="header-right" onclick="closeWindow()">x</div>
        </div>
        <form id="addressForm" method="post" action="./addUmsAddress">
            <div class="addrInfo">

                <div class="input-group inp">
                    <span class="input-group-addon">收货人:</span>
                    <input type="text" name="name" class="form-control" placeholder="请输入收货人姓名">
                </div>
                <div class="input-group inp">
                    <span class="input-group-addon">手机:</span>
                    <input type="text" name="phoneNumber" class="form-control" placeholder="请输入电话号码">
                </div>
                <div class="input-group inp">
                    <span class="input-group-addon">邮编:</span>
                    <input type="text" name="postCode" class="form-control" placeholder="请输入邮编">
                </div>

                <div class="inp">
                    <select style="height: 30px;" id="province" onchange="doProvAndCityRelation();">
                        <option id="choosePro">请选择您所在省份</option>
                    </select>
                    <input type="hidden" name="province" id="provinceInput">  <!--用来提交数据-->
                    <select style="height: 30px;" id="citys" onchange="doCityAndCountyRelation();">
                        <option id='chooseCity'>请选择您所在城市</option>
                    </select>
                    <input type="hidden" name="city" id="cityInput">
                    <select style="height: 30px;" id="county">　　　　　　　
                        <option id='chooseCounty'>请选择您所在区/县</option>
                    </select>
                    <input type="hidden" name="region" id="regionInput">
                </div>

                <div class="input-group inp">
                    <span class="input-group-addon">详细地址:</span>
                    <input type="text" name="detailAddress" class="form-control" placeholder="请输入详细地址">
                </div>

                <div class="inp">
                    <button id="submitAddress" type="button" class="btn btn-primary">新建地址</button>
                </div>
                <input type="hidden" name="pmsProductInfoId" value="${pmsProductInfo.id}">
                <input type="hidden" name="quentity" value="${quentity}">
            </div>
        </form>
    </div>
</div>

<form action="./agreeAuction" method="post" id="orderForm">
    <input name="receiveAddressId" id="receiveAddressId" value="1" type="hidden"/>
    <input name="quentity" id="quentity" type="hidden" value="${quentity}"/>
    <input name="pmsProductInfoId" type="hidden" value="${pmsProductInfo.id}"/>
</form>
<script>

    $("#addAddr").css('display', 'none');
    function addAddress() {
        $("#addAddr").css('display', 'block');
    }
    function closeWindow() {
        $("#addAddr").css('display', 'none');
    }
    function changeAddress() {
        var receiveAddressId = $("input[name='deliveryAddress']:checked").val();
        $("#receiveAddressId").val(receiveAddressId);
    }
    $(function () {
        $("#submitAddress").click(function () {
            var province = $("#province").find("option:selected").text();
            var city = $("#citys").find("option:selected").text();
            var county = $("#county").find("option:selected").text();
            $("#provinceInput").val(province);
            $("#cityInput").val(city);
            $("#regionInput").val(county);
            $("#addressForm").submit();
        });

        $("#submitButton").click(function () {
            //这里要检查地址有没有选择
            var receiveAddressId = $("input[name='deliveryAddress']:checked").val();
            if (receiveAddressId == null) {
                swal("请选择地址！" ,"error");
                return;
            }
            $("#orderForm").submit();
        });
    })
</script>
</body>

</html>