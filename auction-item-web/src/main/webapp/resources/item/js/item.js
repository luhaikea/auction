$(function () {

    //左右滚动
    $("#left").click(function () {
        $(".product-item-img-list-dis ul").stop().animate({
            "left": "-297px"
        })
        $(this).css({
            "color": "#000"
        })
        $("#right").css({
            "color": "#ccc"
        })
    })
    $("#right").click(function () {
        $(".product-item-img-list-dis ul").stop().animate({
            "left": 0
        })
        $(this).css({
            "color": "#000"
        })
        $("#left").css({
            "color": "#ccc"
        })
    })

    //换图片
    $(".product-item-img-list-dis li").mouseover(function () {
        $(this).css({
            "padding": "0",
            "border": "solid 1px red"

        }).siblings().css({
            "padding": "1px",
            "border": "none"
        })
    })

    function Zoom(imgbox, hoverbox, l, t, x, y, h_w, h_h, showbox) {
        var moveX = x - l - (h_w / 2);
        //鼠标区域距离
        var moveY = y - t - (h_h / 2);
        //鼠标区域距离
        if (moveX < 0) {
            moveX = 0
        }
        if (moveY < 0) {
            moveY = 0
        }
        if (moveX > imgbox.width() - h_w) {
            moveX = imgbox.width() - h_w
        }
        if (moveY > imgbox.height() - h_h) {
            moveY = imgbox.height() - h_h
        }
        //判断鼠标使其不跑出图片框
        var zoomX = showbox.width() / imgbox.width()
        //求图片比例
        var zoomY = showbox.height() / imgbox.height()

        showbox.css({
            left: -(moveX * zoomX),
            top: -(moveY * zoomY)
        })
        hoverbox.css({
            left: moveX,
            top: moveY
        })
        //确定位置

    }

    function Zoomhover(imgbox, hoverbox, showbox) {
        var l = imgbox.offset().left;
        var t = imgbox.offset().top;
        var w = hoverbox.width();
        var h = hoverbox.height();
        var time;
        $(".probox img,.hoverbox").mouseover(function (e) {
            var x = e.pageX;
            var y = e.pageY;
            $(".hoverbox,.showbox").show();
            hoverbox.css("opacity", "0.3")
            time = setTimeout(function () {
                Zoom(imgbox, hoverbox, l, t, x, y, w, h, showbox)
            }, 1)
        }).mousemove(function (e) {
            var x = e.pageX;
            var y = e.pageY;
            time = setTimeout(function () {
                Zoom(imgbox, hoverbox, l, t, x, y, w, h, showbox)
            }, 1)
        }).mouseout(function () {
            showbox.parent().hide()
            hoverbox.hide();
        })
    }

    $(function () {
        Zoomhover($(".probox img"), $(".hoverbox"), $(".showbox img"));
        $(".product-item-img-list-dis ul li").hover(function () {
            $('.img1').attr("src", $(this).find('img').attr('src'));
        })
    })

})


