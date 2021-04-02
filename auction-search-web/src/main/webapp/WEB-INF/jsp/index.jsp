<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>一锤定价</title>
    <link rel="stylesheet" type="text/css" href="../../resources/bootstrap/css/bootstrap.css"/>
    <link rel="stylesheet" href="../../resources/static/index/css/swiper-3.4.2.min.css">
    <link rel="stylesheet" href="../../resources/static/index/css/header.css">
    <link rel="stylesheet" href="../../resources/static/index/css/foot.css">
    <script src="../../resources/static/index/js/jquery-3.1.1.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="../../resources/static/index/js/swiper-3.4.2.jquery.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="../../resources/static/index/js/swiper-3.4.2.min.js" type="text/javascript" charset="utf-8"></script>
</head>

<body style="margin: 0px;padding: 0px">
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
                <!--直接去访问http://passport.auction.com:8885/index需要携带返回地址 直接去访问一个页面不需要携带返回地址 可以由request得到-->
                |<a href="http://passport.auction.com:8885/getLogin?ReturnUrl=http://search.auction.com:8883/index">你好，请登录</a>
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
            <input id="searchKeyword" placeholder="请输入要搜索的物品关键字" type="text"/><button onclick="search()">搜索</button>
        </div>
    </div>
    <hr class="division1"/>
    <div class="header-down">
        <div class="header-item">三级分类</div>
    </div>
</div>
<header>
    <div class="header_sous">
        <nav>
            <ul>
                <li>
                    <a href="#">珍品拍卖</a>
                </li>
                <li>
                    <a href="#">司法拍卖</a>
                </li>
                <li>
                    <a href="#">海关政府</a>
                </li>
                <li>
                    <a href="#">拍卖</a>
                </li>
            </ul>
            <ul>
                <li>
                    <a href="#">资产处置</a>
                </li>
                <li>
                    <a href="#">破产拍卖</a>
                </li>
                <li>
                    <a href="#">拍卖服务</a>
                </li>
                <li>
                    <a href="#">招标采购</a>
                </li>
            </ul>
        </nav>
    </div>
    <!--轮播主体内容-->
    <div class="header_main">
        <div class="header_banner">
            <!--导航栏 选择三级分类-->
            <div class="header_main_left">
                <ul>
                    <li>
                        <a href="#" class="header_main_left_a"> <b style="color:red">玉翠珠宝</b></a>
                    </li>
                    <li class="header_li2">
                        <a href="http://search.auction.com:8883/list?catalog3Id=1" class="header_main_left_a" ctg-data="1"><b>钻石</b> - </a>
                        <a href="http://search.auction.com:8883/list?catalog3Id=3" class="header_main_left_a" ctg-data="2"><b>琥珀</b> - </a>
                        <a href="http://search.auction.com:8883/list?catalog3Id=5" class="header_main_left_a" ctg-data="3"><b>翡翠</b></a>
                    </li>
                    <li class="header_li2">
                        <a href="http://search.auction.com:8883/list?catalog3Id=61" class="header_main_left_a" ctg-data="4"><b>珍珠</b> - </a>
                        <a href="http://search.auction.com:8883/list?catalog3Id=4" class="header_main_left_a" ctg-data="5"><b>祖母绿</b> - </a>
                        <a href="http://search.auction.com:8883/list?catalog3Id=2" class="header_main_left_a" ctg-data="6"><b>红蓝宝</b></a>
                    </li>
                    <li class="header_li2">
                        <a href="#" class="header_main_left_a"> <b style="color:red">文玩收藏</b></a>
                    </li>
                    <li class="header_li2">
                        <a href="http://search.auction.com:8883/list?catalog3Id=61" class="header_main_left_a" ctg-data="7"><b>六艺文房</b> - </a>
                        <a href="http://search.auction.com:8883/list?catalog3Id=61" class="header_main_left_a" ctg-data="8"><b>钱币</b> - </a>
                        <a href="http://search.auction.com:8883/list?catalog3Id=61" class="header_main_left_a" ctg-data="9"><b>文玩手串</b> </a>
                    </li>
                    <li class="header_li2">
                        <a href="http://search.auction.com:8883/list?catalog3Id=61" class="header_main_left_a" ctg-data="10"><b>核雕</b> - </a>
                        <a href="http://search.auction.com:8883/list?catalog3Id=61" class="header_main_left_a" ctg-data="11"><b>邮票</b> - </a>
                        <a href="http://search.auction.com:8883/list?catalog3Id=61" class="header_main_left_a" ctg-data="12"><b>趣味收藏</b></a>
                    </li>
                    <li class="header_li2">
                        <a href="#" class="header_main_left_a"> <b style="color:red">紫砂陶瓷</b></a>
                    </li>
                    <li class="header_li2">
                        <a href="http://search.auction.com:8883/list?catalog3Id=61" class="header_main_left_a" ctg-data="13"><b>紫砂</b> - </a>
                        <a href="http://search.auction.com:8883/list?catalog3Id=61" class="header_main_left_a" ctg-data="14"><b>景德镇瓷</b> - </a>
                        <a href="http://search.auction.com:8883/list?catalog3Id=61" class="header_main_left_a" ctg-data="15"><b>白瓷</b></a>
                    </li>
                    <li class="header_li2">
                        <a href="http://search.auction.com:8883/list?catalog3Id=61" class="header_main_left_a" ctg-data="16"><b>钓瓷</b> - </a>
                        <a href="http://search.auction.com:8883/list?catalog3Id=61" class="header_main_left_a" ctg-data="17"><b>陵瓷</b></a>
                    </li>
                    <li class="header_li2">
                        <a href="#" class="header_main_left_a"><b style="color:red">书画篆刻</b></a>
                    </li>
                    <li class="header_li2">
                        <a href="http://search.auction.com:8883/list?catalog3Id=61" class="header_main_left_a" ctg-data="18"><b>水墨画</b> - </a>
                        <a href="http://search.auction.com:8883/list?catalog3Id=61" class="header_main_left_a" ctg-data="19"><b>雕塑</b> - </a>
                        <a href="http://search.auction.com:8883/list?catalog3Id=61" class="header_main_left_a" ctg-data="20"><b>书法</b></a>
                    </li>
                    <li class="header_li2">
                        <a href="http://search.auction.com:8883/list?catalog3Id=61" class="header_main_left_a" ctg-data="21"><b>篆刻</b> - </a>
                        <a href="http://search.auction.com:8883/list?catalog3Id=61" class="header_main_left_a" ctg-data="22"><b>油画</b> - </a>
                        <a href="http://search.auction.com:8883/list?catalog3Id=61" class="header_main_left_a" ctg-data="23"><b>素描</b></a>
                    </li>
                    <li class="header_li2">
                        <a href="#" class="header_main_left_a"><b style="color:red">工艺品</b></a>
                    </li>
                    <li class="header_li2">
                        <a href="http://search.auction.com:8883/list?catalog3Id=61" class="header_main_left_a" ctg-data="24"><b>木雕</b> - </a>
                        <a href="http://search.auction.com:8883/list?catalog3Id=61" class="header_main_left_a" ctg-data="25"><b>印石</b> - </a>
                        <a href="http://search.auction.com:8883/list?catalog3Id=61" class="header_main_left_a" ctg-data="26"><b>琉璃</b> - </a>
                        <a href="http://search.auction.com:8883/list?catalog3Id=61" class="header_main_left_a" ctg-data="27"><b>金银器</b></a>
                    </li>
                    <li class="header_li2">
                        <a href="http://search.auction.com:8883/list?catalog3Id=61" class="header_main_left_a" ctg-data="28"><b>刺绣</b> - </a>
                        <a href="http://search.auction.com:8883/list?catalog3Id=61" class="header_main_left_a" ctg-data="29"><b>石雕</b> - </a>
                        <a href="http://search.auction.com:8883/list?catalog3Id=61" class="header_main_left_a" ctg-data="30"><b>铜铁锡器</b></a>
                    </li>
                </ul>
            </div>
            <!--轮播-->
            <div class="header_main_center">
                <div class="swiper-container swiper1">
                    <div class="swiper-wrapper">
                        <div class="swiper-slide">
                            <a href="#"><img src="../../resources/static/index/img/auctionlunbo1.jpg"/></a>
                        </div>
                        <div class="swiper-slide">
                            <a href="#"><img src="../../resources/static/index/img/auctionlunbo2.jpg"/></a>
                        </div>
                    </div>
                    <div class="swiper-pagination"></div>
                    <div class="swiper-button-next swiper-button-white"></div>
                    <div class="swiper-button-prev swiper-button-white"></div>
                </div>
            </div>
        </div>
    </div>
</header>
<!--首页底部  是从京东拍卖上扒拉下来的-->
<div style="float: left;width: 180px">
    <div style="float: left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div>
    <div style="float: left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div>
</div>
<div style="float: left" class="pm-footer-2018">
    <div class="w">
        <ul class="pm-footer-slogan">
            <li class="fore0">
                <i class="paimaiicon slogan-icon0"></i>
                <h5> 如实描述承诺</h5>
                <p>送拍机构在发拍品时会履行实物描述义务</p>
            </li>
            <li class="fore1">
                <i class="paimaiicon slogan-icon0"></i>
                <h5> 保证金保障</h5>
                <p>送拍机构都会缴纳足额保证金来确保安全</p>
            </li>
            <li class="fore2">
                <i class="paimaiicon slogan-icon0"></i>
                <h5> 售后服务保障</h5>
                <p>送拍机构在发拍品时会履行实物描述义务</p>
            </li>
        </ul>
        <div class="pm-footer-wrapper">
            <div class="faq">
                <dl class="fore0">
                    <dt>拍卖流程常见问题</dt>
                    <dd>
                        <a href="#" target="_blank"
                           clstag="pageclick|keycount|paimai_footer|faq_1_1">如何报名参加竞拍?</a>
                    </dd>
                    <dd>
                        <a href="#" target="_blank"
                           clstag="pageclick|keycount|paimai_footer|faq_1_2">报名后如何出价?</a>
                    </dd>
                    <dd>
                        <a href="#" target="_blank"
                           clstag="pageclick|keycount|paimai_footer|faq_1_3">如何才能获拍?</a>
                    </dd>
                    <dd>
                        <a href="#" target="_blank"
                           clstag="pageclick|keycount|paimai_footer|faq_1_4">获拍后如何支付货款?</a>
                    </dd>
                    <dd>
                        <a href="#" target="_blank"
                           clstag="pageclick|keycount|paimai_footer|faq_1_5">收货与售后</a>
                    </dd>
                </dl>
                <dl class="fore1">
                    <dt>保证金常见问题</dt>
                    <dd>
                        <a href="#" target="_blank"
                           clstag="pageclick|keycount|paimai_footer|faq_2_1">保证金的缴纳</a>
                    </dd>
                    <dd>
                        <a href="#" target="_blank"
                           clstag="pageclick|keycount|paimai_footer|faq_2_2">保证金的退还</a>
                    </dd>
                    <dd>
                        <a href="#" target="_blank"
                           clstag="pageclick|keycount|paimai_footer|faq_2_3">保证金的扣除</a>
                    </dd>
                    <dd>
                        <a href="#" target="_blank"
                           clstag="pageclick|keycount|paimai_footer|faq_2_4">保证金抵货款规则</a>
                    </dd>
                    <dd>
                        <a href="#" target="_blank"
                           clstag="pageclick|keycount|paimai_footer|faq_2_5">怎么查询我的保证金?</a>
                    </dd>
                </dl>
                <dl class="fore2">
                    <dt>拍卖行常见问题</dt>
                    <dd>
                        <a href="#" target="_blank"
                           clstag="pageclick|keycount|paimai_footer|faq_3_1">支付方式有哪些?</a>
                    </dd>
                    <dd>
                        <a href="#" target="_blank"
                           clstag="pageclick|keycount|paimai_footer|faq_3_2">在线支付常见问题?</a>
                    </dd>
                    <dd>
                        <a href="#" target="_blank"
                           clstag="pageclick|keycount|paimai_footer|faq_3_3">网银支付限额</a>
                    </dd>
                </dl>
                <dl class="fore3">
                    <dt>其他的常见问题</dt>
                    <dd>
                        <a href="#" target="_blank"
                           clstag="pageclick|keycount|paimai_footer|faq_3_1">竞拍的商品来源是什么?</a>
                    </dd>
                    <dd>
                        <a href="#" target="_blank"
                           clstag="pageclick|keycount|paimai_footer|faq_3_2">竞拍成功不想要了怎么办?</a>
                    </dd>
                    <dd>
                        <a href="#" target="_blank"
                           clstag="pageclick|keycount|paimai_footer|faq_3_3">竞拍商品的配送时间需要多久?</a>
                    </dd>
                    <dd>
                        <a href="#" target="_blank"
                           clstag="pageclick|keycount|paimai_footer|faq_3_4">竞拍商品支持七天无理由退货吗?</a>
                    </dd>
                    <dd>
                        <a href="#" target="_blank"
                           clstag="pageclick|keycount|paimai_footer|faq_3_5">更多常见问题</a>
                    </dd>
                </dl>
            </div>
            <div class="m feedback">
                <div class="mt">更多问题反馈</div>
                <div class="mc">欢迎您提出宝贵意见我们将不断完善产品谢谢～</div>
                <div class="mb">
                    <a href="#" target="_blank"
                       clstag="pageclick|keycount|paimai_footer|feedback">意见反馈</a>
                </div>
            </div>
            <div class="m follow">
                <div class="mt">关注拍卖公众号</div>
                <div class="mc">
                    <img width="73" height="73"
                         src="//img11.360buyimg.com/cms/jfs/t5794/345/1300270567/27895/10f95783/592583fcN9be5d3ce.jpg"
                         alt="">
                </div>
                <div class="mb">立即关注公众号<br>拍品捡漏不放过</div>
            </div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript">
    function search() {
        var searchKeyword = $("#searchKeyword").val()
        window.location.href = "http://search.auction.com:8883/list?keyword=" + searchKeyword;
    }
</script>
<script type="text/javascript" src="../../resources/static/index/js/index.js"></script>
<script type="text/javascript" src="../../resources/static/index/js/header.js"></script>
</html>


