package com.lhk.auction.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lhk.auction.annotations.LoginRequired;
import com.lhk.auction.bean.OmsOrder;
import com.lhk.auction.bean.PmsProductInfo;
import com.lhk.auction.bean.UmsMemberReceiveAddress;
import com.lhk.auction.service.MemberService;
import com.lhk.auction.service.OrderService;
import com.lhk.auction.service.PmsProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
/*
 一个系统数据库结构确定下来后这个系统的功能大致就确定下来了，
*/
@Controller
public class OrderController {

    @Reference
    PmsProductService pmsProductService;
    @Reference
    MemberService memberService;
    @Reference
    OrderService orderService;

    @RequestMapping("addUmsAddress")  //添加之后依然返回orderConfirm
    @LoginRequired(loginSuccess = true)
    public String addUmsAddress(String quentity, String pmsProductInfoId, UmsMemberReceiveAddress umsMemberReceiveAddress,HttpServletRequest request,ModelMap modelMap){

        String memberId = (String) request.getAttribute("memberId");
        String nickname = (String) request.getAttribute("nickname");
        umsMemberReceiveAddress.setMemberId(memberId);
        memberService.addUmsMemberReceiveAddress(umsMemberReceiveAddress);

        PmsProductInfo pmsProductInfo = pmsProductService.getPmsProductInfoById(pmsProductInfoId);
        List<UmsMemberReceiveAddress> umsMemberReceiveAddressList = memberService.getUmsMemberReceiveAddressByMemberId(memberId);
        modelMap.put("nickname", nickname);
        modelMap.put("quentity", quentity);
        modelMap.put("pmsProductInfo", pmsProductInfo);
        modelMap.put("umsMemberReceiveAddressList",umsMemberReceiveAddressList);
        return "orderConfirm";
    }

    @RequestMapping("finalPay")
    @LoginRequired(loginSuccess = true)
    public ModelAndView finalPay(String orderId, HttpServletRequest request){
        //这两个参数是在请求被拦截器拦截后在拦截器中加入的
        String memberId = (String) request.getAttribute("memberId");
        String nickname = (String) request.getAttribute("nickname");

        String orderSnFinal = "auctionOrderSnFinal";  //生成商户尾款订单号
        orderSnFinal = orderSnFinal + System.currentTimeMillis();// 将毫秒时间戳拼接到外部订单号
        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMDDHHmmss");
        orderSnFinal = orderSnFinal + sdf.format(new Date());// 将时间字符串拼接到外部订单号

        OmsOrder omsOrder = new OmsOrder();
        omsOrder.setId(orderId);
        omsOrder.setOrderSnFinal(orderSnFinal);
        orderService.updateOrder(omsOrder);
        OmsOrder orderById = orderService.getOrderById(orderId);

        ModelAndView mv = new ModelAndView("redirect:http://payment.auction.com:8886/paymentFinalSelect");
        mv.addObject("orderSnFinal", orderSnFinal);
        mv.addObject("bidPrice",orderById.getBidPrice());
        System.out.println("出价"+orderById.getBidPrice());
        return mv;
    }

    //确认订单，同意拍卖之后，生成订单 然后重定向到支付宝结算
    @RequestMapping("agreeAuction")
    @LoginRequired(loginSuccess = true)
    public ModelAndView submitOrder(String receiveAddressId, String quentity, String pmsProductInfoId, HttpServletRequest request){

        //这两个参数是在请求被拦截器拦截后在拦截器中加入的
        String memberId = (String) request.getAttribute("memberId");
        String nickname = (String) request.getAttribute("nickname");

        PmsProductInfo pmsProductInfoById = pmsProductService.getPmsProductInfoById(pmsProductInfoId);

        OmsOrder omsOrder = new OmsOrder();
        omsOrder.setMemberId(memberId);
        omsOrder.setProductId(pmsProductInfoId);
        omsOrder.setBidPrice(new BigDecimal(quentity));
        omsOrder.setReceiveId(receiveAddressId);

        String orderSn = "auctionOrderSnEarnest";  //生成商户订单号
        orderSn = orderSn + System.currentTimeMillis();// 将毫秒时间戳拼接到外部订单号
        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMDDHHmmss");
        orderSn = orderSn + sdf.format(new Date());// 将时间字符串拼接到外部订单号
        omsOrder.setOrderSn(orderSn);

        omsOrder.setCreateTime(new Date());
        omsOrder.setAuctionStatus("0");
        omsOrder.setEarnestStatus("0");
        omsOrder.setFinalPayStatus("0");
        orderService.saveOmsOrder(omsOrder);

        ModelAndView mv = new ModelAndView("redirect:http://payment.auction.com:8886/paymentSelect");
        mv.addObject("orderSn", orderSn);
        mv.addObject("earnest",pmsProductInfoById.getEarnest());

        return mv;
    }
    //在订单列表补交保证金
    @RequestMapping("earnestPay")
    @LoginRequired(loginSuccess = true)
    public ModelAndView earnestPay(String orderId, HttpServletRequest request){

        //这两个参数是在请求被拦截器拦截后在拦截器中加入的
        String memberId = (String) request.getAttribute("memberId");
        String nickname = (String) request.getAttribute("nickname");

        OmsOrder omsOrder = orderService.getOrderById(orderId);
        PmsProductInfo pmsProductInfo = pmsProductService.getPmsProductInfoById(omsOrder.getProductId());
        ModelAndView mv = new ModelAndView("redirect:http://payment.auction.com:8886/paymentSelect");
        mv.addObject("orderSn", omsOrder.getOrderSn());
        mv.addObject("earnest",pmsProductInfo.getEarnest());

        return mv;
    }


    //确认出价 由物品详情页调用
    @RequestMapping("confirmOffer")
    @LoginRequired(loginSuccess = true)
    public String confirmOffer(String quentity, String pmsProductInfoId, HttpServletRequest request, ModelMap modelMap){
        //这两个参数是在请求被拦截器拦截后在拦截器中加入的
        String memberId = (String) request.getAttribute("memberId");
        String nickname = (String) request.getAttribute("nickname");

        //一个客户只能对一个商品出一次价 如果监测到该客户已经对该物品出过价应在这里将其重定向到我的拍卖【订单列表】
        boolean userOfferStatus = orderService.getOrderByProductIdAndMemberId(pmsProductInfoId, memberId);
        if(userOfferStatus ==true){
            return "redirect:orderList";
        }
        PmsProductInfo pmsProductInfo = pmsProductService.getPmsProductInfoById(pmsProductInfoId);
        List<UmsMemberReceiveAddress> umsMemberReceiveAddressList = memberService.getUmsMemberReceiveAddressByMemberId(memberId);
        modelMap.put("nickname", nickname);
        modelMap.put("quentity", quentity);
        modelMap.put("pmsProductInfo", pmsProductInfo);
        modelMap.put("umsMemberReceiveAddressList",umsMemberReceiveAddressList);
        //返回到订单确认页面
        return "orderConfirm";
    }

    @RequestMapping("orderList")
    @LoginRequired(loginSuccess = true)
    public String orderList(HttpServletRequest request, ModelMap modelMap){

        String memberId = (String) request.getAttribute("memberId");
        String nickname = (String) request.getAttribute("nickname");

        List<OmsOrder> omsOrders = orderService.getOrderByMemberId(memberId);
        for (OmsOrder omsOrder : omsOrders) {
            PmsProductInfo pmsProductInfoById = pmsProductService.getPmsProductInfoById(omsOrder.getProductId());
            omsOrder.setPmsProductInfo(pmsProductInfoById);
            System.out.println(omsOrder.getId()+" "+omsOrder.getBidPrice());
        }
        modelMap.put("omsOrders",omsOrders);
        modelMap.put("nickname",nickname);
        return "orderList";
    }
    //earnestPay


}
