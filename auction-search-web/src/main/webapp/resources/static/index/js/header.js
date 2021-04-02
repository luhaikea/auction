$(".header_banner1").hover(function() {
	$(".header_banner1_div").stop(true).animate({
		width:"990px"
	},500)
}, function() {
	$(".header_banner1_div").stop(true).animate({
		width:"0"
	},300)
})
$(".head p").on("click", function() {
	$(".head").fadeOut(500)
})
$(".header_banner1_div p").on("click", function() {
	$(".header_banner1_div").stop(true).animate({
		width:"0"
	},200)
})
$(".header_ol a").hover(function() {
	$(this).css({
		color: "#c81623"
	})
}, function() {
	$(this).css({
		color: "#999"
	})
	$(".aaa").css({
		color: "#111"
	})
})
//轮播图
var swiper1 = new Swiper(".swiper1", {
	loop: true,
	autoplay: 2000,
	effect: 'fade',
	fade: {
		crossFade: false,
	},
	pagination: ".swiper-pagination",
	paginationClickable: true,
	prevButton: '.swiper-button-prev',
	nextButton: '.swiper-button-next',
	autoplayDisableOnInteraction: false,
})

