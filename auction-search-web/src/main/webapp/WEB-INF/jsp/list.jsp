<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="../../resources/bootstrap/css/bootstrap.css"/>
    <link rel="stylesheet" href="../../resources/static/list/css/list.css">
    <script src="../../resources/static/list/js/jquery-3.1.1.min.js"></script>
    <title>物品列表</title>
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
            <c:if test="${nickname==null}">
                |<a href="http://passport.auction.com:8885/getLogin?ReturnUrl=http://search.auction.com:8883/list">你好，请登录</a>
                |<a href="http://passport.auction.com:8885/toRegist">免费注册</a>
            </c:if>
            |<a href="http://order.auction.com:8884/orderList">我的拍卖</a>
            |<a href="http://survey.auction.com:8887/getSurvey">用户反馈</a>
            |<a href="http://survey.auction.com:8887/getMyMessage">我的信息</a>|
        </div>
    </div>
    <div class="header-bottom">
        <div class="header-bottom-left">
            <img src="../../resources/static/index/img/auctionlog.jpeg"/>
        </div>
        <div class="header-bottom-right">
            <input id="keyword" name="keyword" placeholder="请输入要搜索的物品关键字" type="text"/><button onclick="searchList()">搜索</button>
        </div>
    </div>
    <hr class="division1"/>
    <div class="header-down">
        <div class="header-item">物品列表</div>
    </div>
</div>

<div class="GM_ipone">
    <div class="GM_ipone_bar">
        <div class="GM_ipone_one a">
            筛选条件
        </div>
        <i><img src="../../resources/static/list/img/auctionright-@1x.png" alt=""></i>

        <c:if test="${keyword!=null}">
            <span>&quot;${keyword}&quot;</span>
        </c:if>
       <!--面包屑-->
        <c:forEach items="${pmsSearchCrumbs}" var="pmsSearchCrumbs">
            <a class="select-attr" href="list?${pmsSearchCrumbs.urlParam}">${pmsSearchCrumbs.valueName}<b>
                ✖ </b></a>
        </c:forEach>

    </div>
</div>

<!--商品筛选和排序-->
<div class="GM_banner w">
    <div class="GM_nav">
        <div class="GM_selector">

            <div class="GM_nav_logo">

                <c:forEach items="${pmsAttrInfos}" var="pmsAttrInfo">
                    <div class="GM_pre">
                        <div class="sl_key">
                            <span>${pmsAttrInfo.attrName}：</span>
                        </div>
                        <div class="sl_value">
                            <ul>
                                <c:forEach items="${pmsAttrInfo.attrValueList}" var="attrValue">
                                    <li><a href="list?${urlParam}&valueId=${attrValue.id}">${attrValue.valueName}</a>
                                    </li>
                                </c:forEach>
                            </ul>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>


        <!--排序-->
        <div class="GM_banner_main">
            <!--综合排序-->
            <div class="GM_con_right">
                <div class="filter">
                    <!--综合排序-->
                    <div class="filter_top">
                        <div class="filter_top_left">
                            <a href="#">搜索结果</a>
                        </div>
                    </div>
                    <!--排序内容-->
                    <div class="rig_tab" style="width:100%">
                        <c:forEach items="${pmsSearchProductInfos}" var="pmsSearchProductInfo">
                            <div style="width:215px">
                                <p class="da">
                                    <a href="#" onclick="item(${pmsSearchProductInfo.id})">
                                        <img src="${pmsSearchProductInfo.productDefaultImg}" class="dim">
                                    </a>
                                </p>
                                <a href="#" style="font-size: 14px;font-weight: 500;" onclick="item(${pmsSearchProductInfo.id})"
                                   class="tab_JE">【${pmsSearchProductInfo.productName}】</a>
                                <p class="tab_R">
                                    <span>当前最高价:￥${pmsSearchProductInfo.startingPrice}</span>
                                </p>
                                <p class="tab_R">
                                    <span>出价次数:${pmsSearchProductInfo.hotScore}</span>
                                </p>
                            </div>
                        </c:forEach>
                    </div>
                    <!--分页-->
                    <div class="filter_page">
                        <div class="page_wrap">
                            <span class="page_span1">
                                <a href="#">
                                    < 上一页
                                </a>
                                <a href="#" style="border: 0;color:#ee2222;background: #fff">1</a>
                                <a href="#">2</a>
                                <a href="#">3</a>
                                <a href="#" style="border: 0;font-size: 20px;color: #999;background: #fff">...</a>
                                <a href="#">169</a>
                                <a href="#">
                                    下一页 >
                                </a>
                            </span>
                            <span class="page_span2">
                                <em>共<b>169</b>页&nbsp;&nbsp;到第</em>
                                <input type="number" value="1">
                                <em>页</em>
                                <a href="#">确定</a>
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script language="JavaScript">
    function item(productId) {
        window.location.href = "http://item.auction.com:8882/" + productId + ".html";
    }

    function searchList() {
        var keyword = $("#keyword").val();
        window.location.href = "/list?keyword=" + keyword;
    }

    $(".sl_ext a:nth-child(1)").hover(function () {
        $(this).children("b").stop(true).animate({top: "3px"}, 50);
        $(this).children("i").stop(true).animate({top: "-23px"}, 50)
    }, function () {
        $(this).children("b").stop(true).animate({top: "24px"}, 50);
        $(this).children("i").stop(true).animate({top: "3px"}, 50)
    });
    $(".sl_ext a:nth-child(2)").hover(function () {
        $(this).children("span").stop(true).animate({top: "-1px"}, 100);
        $(this).children("i").stop(true).animate({top: "-14px"}, 100).css({display: "none"})
    }, function () {
        $(this).children("span").stop(true).animate({top: "14px"}, 100);
        $(this).children("i").stop(true).animate({top: "-1px"}, 100).css({display: "block"})
    });
    $('.tab_im img').hover(function () {
        var a = $(this).prop('src');
        var index = $(this).parents('li').index();
        $(this).parents('li').css('border', '2px solid red').siblings('li').css('border', '1px solid #ccc');
        $(this).parents('ul').prev().find('img').prop('src', a);
        $(this).parents('ul').siblings('.tab_JE').find('a').eq(index).css('display', 'block').siblings('a').css('display', 'none');
        $(this).parents('ul').siblings('.tab_R').find('span').eq(index).css('display', 'block').siblings('span').css('display', 'none')
    });

    $(".GM_ipone_one").hover(function () {
        $(this).children("div").css({display: "block"})
    }, function () {
        $(this).children("div").css({display: "none"})
    });

    $("#tab>li").click(function () {
        var i = $(this).index();
        $("#container>div").hide().eq(i).show()
    });
    $(".dizhi_show").hover(function () {
        $(".dizhi_con").css({display: "block"})
    }, function () {
        $(".dizhi_con").css({display: "none"})
    });
    $(".dizhi_con").hover(function () {
        $(this).css({display: "block"})
    }, function () {
        $(this).css({display: "none"})
    });
    //显示隐藏
    var $li = $(".GM_nav_logo>div:gt(3)").show();
    $('.GM_show span').click(function () {
        if ($li.is(':hidden')) {
            $li.show();
            $(this).css({width: "86px"}).text('收起 ^');
        } else {
            $li.hide();
            $('.GM_show span').css({width: "291px"}).text('更多选项（ CPU核数、网络、机身颜色 等）');
        }
        return false;
    });

    $(".rig_tab>div").hover(function () {
        var i = $(this).index();
        $(this).find('.ico').css({display: 'block'}).stop(true).animate({top: "190px"}, 300)
    }, function () {
        var i = $(this).index();
        $(this).find('.ico').css({display: 'none'}).stop(true).animate({top: "230px"})
    });

    $('.header_main_left>ul>li').hover(function () {
        $(this).css({
            background: "#f0f0f0"
        }).find('.header_main_left_main').stop(true).fadeIn(300)
    }, function () {
        $(this).css({
            background: "#fff"
        }).find(".header_main_left_a").css({
            color: "#000"
        });
        $(this).find('.header_main_left_main').stop(true).fadeOut(100)
    });
    $(".header_sj a").hover(function () {
        $(this).css({
            background: "#444"
        })
    }, function () {
        $(this).css({
            background: "#6e6568"
        })
    });

    $(".nav_li1 a").hover(function () {
        $(".header_main_left").stop(true).fadeIn()
    }, function () {
        $(".header_main_left").stop(true).fadeOut()
    });
    $(".header_main_left").hover(function () {
        $(this).stop(true).fadeIn()
    }, function () {
        $(this).stop(true).fadeOut()
    });

    //右侧侧边栏
    $(".header_bar_box ul li").hover(function () {
        $(this).css({
            background: "#7A6E6E"
        }).children(".div").css({
            display: "block"
        }).stop(true).animate({
            left: "-60px"
        }, 300)
    }, function () {
        $(this).css({
            background: "#7A6E6E"
        }).children(".div").css({
            display: "none"
        }).stop(true).animate({
            left: "0"
        }, 300)
    });

    //底部
    $(".footer_foot .p1 a").hover(function () {
        $(this).css("color", "#D70B1C")
    }, function () {
        $(this).css("color", "#727272")
    });

    $(".footer .footer_center ol li a").hover(function () {
        $(this).css("color", "#D70B1C")
    }, function () {
        $(this).css("color", "#727272")
    })
</script>
</body>
</html>