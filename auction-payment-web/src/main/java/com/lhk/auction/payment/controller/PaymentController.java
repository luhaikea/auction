package com.lhk.auction.payment.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.lhk.auction.annotations.LoginRequired;
import com.lhk.auction.bean.OmsOrder;
import com.lhk.auction.bean.PaymsPayment;
import com.lhk.auction.bean.PmsProductInfo;
import com.lhk.auction.config.AlipayConfig;
import com.lhk.auction.service.OrderService;
import com.lhk.auction.service.PaymsPaymentService;
import com.lhk.auction.service.PmsProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PaymentController {

    @Reference
    OrderService orderService;

    @Autowired
    AlipayClient alipayClient;

    @Reference
    PmsProductService pmsProductService;

    @Reference
    PaymsPaymentService paymsPaymentService;

    @RequestMapping("alipayCallback")
    @LoginRequired(loginSuccess = true)
    public ModelAndView alipayCallback(HttpServletRequest request){

        System.out.println("回调");
        //签名
        String sign = request.getParameter("sign");
        String orderSn = request.getParameter("out_trade_no");
        //支付宝交易号
        String alipayNo = request.getParameter("trade_no");
        String callbackContent = request.getQueryString();

        //此处需用sign验签
        if(StringUtils.isNotBlank(sign)){

            PaymsPayment paymsPayment = new PaymsPayment();
            paymsPayment.setOrderSn(orderSn);
            paymsPayment.setAlipayNo(alipayNo);
            paymsPayment.setPaymentStatus("1");
            paymsPayment.setCallbackContent(callbackContent);
            paymsPayment.setCallbackTime(new Date());
            //保证金支付成功 更新支付信息
            if(orderSn.startsWith("auctionOrderSnEarnest")){
                paymsPaymentService.updatePaymsPayment(paymsPayment);
            } else if(orderSn.startsWith("auctionOrderSnFinal")){
                paymsPaymentService.updatePaymsPaymentFinal(paymsPayment);
            }
        }

        ModelAndView mv = new ModelAndView("redirect:http://order.auction.com:8884/orderList");
        return mv;
    }

    @RequestMapping("alipay")
    @LoginRequired(loginSuccess = true)
    @ResponseBody
    public String alipay(String orderSn, BigDecimal earnest, HttpServletRequest request, ModelMap modelMap){

        //由商户订单号查找出订单和物品信息
        OmsOrder omsOrder = orderService.getOrderByOrderSn(orderSn);
        PmsProductInfo pmsProductInfo = pmsProductService.getPmsProductInfoById(omsOrder.getProductId());

        String form = null;
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        // "http://payment.auction.com:8886/alipayCallback"
        alipayRequest.setReturnUrl(AlipayConfig.return_payment_url);

        Map<String,Object> bizContentMap = new HashMap<>();
        //商户订单号  这些put键由支付宝已经规定好了
        bizContentMap.put("out_trade_no",orderSn);
        bizContentMap.put("product_code","FAST_INSTANT_TRADE_PAY");
        bizContentMap.put("total_amount",earnest);
        bizContentMap.put("subject",pmsProductInfo.getProductName());

        String bizContent = JSON.toJSONString(bizContentMap);

        alipayRequest.setBizContent(bizContent);

        try {
            form = alipayClient.pageExecute(alipayRequest).getBody();

        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        // 生成并且保存用户的支付信息
        PaymsPayment paymsPayment = new PaymsPayment();

        paymsPayment.setOrderSn(orderSn);
        paymsPayment.setOrderId(omsOrder.getId());
        paymsPayment.setAmount(earnest);
        paymsPayment.setPaymentStatus("0");
        paymsPayment.setTradeContent("保证金:"+pmsProductInfo.getProductName());
        paymsPayment.setCreateTime(new Date());

        paymsPaymentService.savePaymsPayment(paymsPayment);

        // 向消息中间件发送一个 检查支付状态(支付服务消费)的延迟消息队列   检查5次 每次间隔两分钟
        paymsPaymentService.sendCheckPaymentStatusDelayMessageQueue(orderSn,5);
        return form;
    }

    @RequestMapping("alipayFinal")
    @LoginRequired(loginSuccess = true)
    @ResponseBody
    public String alipayFinal(String orderSnFinal, BigDecimal bidPrice, HttpServletRequest request, ModelMap modelMap){

        //由商户订单号查找出订单和物品信息
        OmsOrder omsOrder = orderService.getOrderByorderSnFinal(orderSnFinal);
        PmsProductInfo pmsProductInfo = pmsProductService.getPmsProductInfoById(omsOrder.getProductId());

        String form = null;
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        // "http://payment.auction.com:8886/alipayCallback"
        alipayRequest.setReturnUrl(AlipayConfig.return_payment_url);

        Map<String,Object> bizContentMap = new HashMap<>();
        //商户订单号  这些put键由支付宝已经规定好了
        bizContentMap.put("out_trade_no",orderSnFinal);
        bizContentMap.put("product_code","FAST_INSTANT_TRADE_PAY");
        bizContentMap.put("total_amount",bidPrice);
        bizContentMap.put("subject",pmsProductInfo.getProductName());

        String bizContent = JSON.toJSONString(bizContentMap);

        alipayRequest.setBizContent(bizContent);

        try {
            form = alipayClient.pageExecute(alipayRequest).getBody();

        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        // 生成并且保存用户的支付信息
        PaymsPayment paymsPayment = new PaymsPayment();

        paymsPayment.setOrderSn(orderSnFinal);
        paymsPayment.setOrderId(omsOrder.getId());
        paymsPayment.setAmount(bidPrice);
        paymsPayment.setPaymentStatus("0");
        paymsPayment.setTradeContent("尾款:"+pmsProductInfo.getProductName());
        paymsPayment.setCreateTime(new Date());

        paymsPaymentService.savePaymsPayment(paymsPayment);

        // 向消息中间件发送一个 检查支付状态(支付服务消费)的延迟消息队列   检查5次 每次间隔两分钟
        paymsPaymentService.sendCheckPaymentFinalStatusDelayMessageQueue(orderSnFinal,5);
        return form;
    }

    @RequestMapping("paymentSelect")
    @LoginRequired(loginSuccess = true)
    public String paymentSelect(String orderSn, String earnest, HttpServletRequest request, ModelMap modelMap){

        String memberId = (String) request.getAttribute("memberId");
        String nickname = (String) request.getAttribute("nickname");
        modelMap.put("nickname",nickname);
        modelMap.put("orderSn",orderSn);
        modelMap.put("earnest",earnest);
        return "paymentSelect";
    }

    @RequestMapping("paymentFinalSelect")
    @LoginRequired(loginSuccess = true)
    public String paymentFinalSelect(String orderSnFinal, String bidPrice, HttpServletRequest request, ModelMap modelMap){

        String memberId = (String) request.getAttribute("memberId");
        String nickname = (String) request.getAttribute("nickname");
        modelMap.put("nickname",nickname);
        modelMap.put("orderSnFinal",orderSnFinal);
        modelMap.put("bidPrice",bidPrice);
        return "paymentFinalSelect";
    }
}
/*
<form name="punchout_form" method="post" action="https://openapi.alipaydev.com/gateway.do?charset=utf-8&method=alipay.trade.page.pay&sign=D0Y1SWmOL%2Fb90w1rptpVEHGslaK5DonQTkNevG4Bg6mzuyr3up6ra%2BXMcba7yhVglwJ8cFjgpTGI2CUTTe7vv2UjtCuWk%2BaLw9HY1o7ZW9ZHX7Zgv3Fe1Lm6vPvjnCu60cJehisHIE54vNC4Y5IQuSv5AoGKSIvziJyydcooB2bkLVanYtA%2Fb6hu7Y1Rr5nwpIzYKNoRNUQ3B7sLQOMGrxpj13ImhKkfJm%2FQqTq0yqEEzKKfeoV0ka2kmZ4pISmyZvCDE5ZKYfnxacwh7Vcg5WXEpzbIIC5ghzNdbJaB7ol18Xkyz6ILzM0BLbm4MU0a5HJURepuxIWmGsmhEl1piA%3D%3D&return_url=http%3A%2F%2Fpayment.auction.com%3A8886%2FalipayCallback&version=1.0&app_id=2021000116683318&sign_type=RSA2&timestamp=2021-02-16+20%3A51%3A37&alipay_sdk=alipay-sdk-java-dynamicVersionNo&format=json">
<input type="hidden" name="biz_content" value="{&quot;out_trade_no&quot;:&quot;auction161347988999920210247205129&quot;,&quot;total_amount&quot;:100.0,&quot;subject&quot;:&quot;寒素&quot;,&quot;product_code&quot;:&quot;FAST_INSTANT_TRADE_PAY&quot;}">
<input type="submit" value="立即支付" style="display:none" >
</form>
<script>document.forms[0].submit();</script>
 */
