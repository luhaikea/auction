package com.lhk.auction.payment.activemqListener;

import com.lhk.auction.bean.PaymsPayment;
import com.lhk.auction.service.PaymsPaymentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import java.util.Date;
import java.util.Map;

@Component
public class PaymsPaymentServiceActiveMqListener {

    @Autowired
    PaymsPaymentService paymsPaymentService;
    //监听器
    //产生异常消费七次之后，不会再消费 会在MQ中产生一个死信队列【也就是回反复监听七次】
    @JmsListener(destination = "PAYMENT_CHECK_QUEUE", containerFactory = "jmsQueueListener")
    public void consumePaymentCheckQueue(MapMessage mapMessage){

        String orderSn = null;
        int counter=0;
        try {
            orderSn = mapMessage.getString("orderSn");
            counter = mapMessage.getInt("counter");
        } catch (JMSException e) {
            e.printStackTrace();
        }

        Map<String, Object> resultMap = paymsPaymentService.checkPaymentResult(orderSn);

        if(resultMap.isEmpty()){  //调用不成功
            if(counter>0){
                counter--;
                paymsPaymentService.sendCheckPaymentStatusDelayMessageQueue(orderSn, counter);
            } else{
                System.out.println("检查次数用尽");
            }

        } else {
            //交易状态：WAIT_BUYER_PAY（交易创建，等待买家付款）、TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、TRADE_SUCCESS（交易支付成功）、TRADE_FINISHED（交易结束，不可退款）
            String paymentStatus = (String) resultMap.get("paymentStatus");

            //根据查询的支付状态结果，判断是否进行下一次的延迟任务还是支付成功更新数据和后续任务
            if(StringUtils.isNotBlank(paymentStatus)&&paymentStatus.equals("TRADE_SUCCESS")){
                //支付成功，更新支付 发送支付队列

                PaymsPayment paymsPayment = new PaymsPayment();

                paymsPayment.setOrderSn(orderSn);
                paymsPayment.setAlipayNo((String)resultMap.get("alipayNo"));
                paymsPayment.setPaymentStatus("1");
                paymsPayment.setCallbackContent((String) resultMap.get("callbackContent"));
                paymsPayment.setCallbackTime(new Date());

                //检查到支付宝中该笔订单已经支付然后更新本系统
                paymsPaymentService.updatePaymsPayment(paymsPayment);

                System.out.println("已经支付成功，支付成功，更新支付 发送支付队列");

            } else {

                //继续发送延迟检查任务，计算延迟时间等
                if(counter>0){
                    counter--;
                    paymsPaymentService.sendCheckPaymentStatusDelayMessageQueue(orderSn, counter);
                } else{
                    //检查次数用尽
                    System.out.println("检查次数用尽");
                }
            }

        }

    }

    @JmsListener(destination = "PAYMENTFINAL_CHECK_QUEUE", containerFactory = "jmsQueueListener")
    public void consumePaymentFinalCheckQueue(MapMessage mapMessage){

        String orderSnFinal = null;
        int counter=0;
        try {
            orderSnFinal = mapMessage.getString("orderSnFinal");
            counter = mapMessage.getInt("counter");
        } catch (JMSException e) {
            e.printStackTrace();
        }

        Map<String, Object> resultMap = paymsPaymentService.checkPaymentFinalResult(orderSnFinal);

        if(resultMap.isEmpty()){  //调用不成功
            if(counter>0){
                counter--;
                paymsPaymentService.sendCheckPaymentFinalStatusDelayMessageQueue(orderSnFinal, counter);
            } else{
                System.out.println("检查次数用尽");
            }

        } else {
            //交易状态：WAIT_BUYER_PAY（交易创建，等待买家付款）、TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、TRADE_SUCCESS（交易支付成功）、TRADE_FINISHED（交易结束，不可退款）
            String paymentStatus = (String) resultMap.get("paymentStatus");

            //根据查询的支付状态结果，判断是否进行下一次的延迟任务还是支付成功更新数据和后续任务
            if(StringUtils.isNotBlank(paymentStatus)&&paymentStatus.equals("TRADE_SUCCESS")){
                //支付成功，更新支付 发送支付队列

                PaymsPayment paymsPayment = new PaymsPayment();

                //这里的支付信息更新和保证金更新一样
                paymsPayment.setOrderSn(orderSnFinal);
                paymsPayment.setAlipayNo((String)resultMap.get("alipayNo"));
                paymsPayment.setPaymentStatus("1");
                paymsPayment.setCallbackContent((String) resultMap.get("callbackContent"));
                paymsPayment.setCallbackTime(new Date());

                //检查到支付宝中该笔订单已经支付然后更新本系统
                paymsPaymentService.updatePaymsPaymentFinal(paymsPayment);

                System.out.println("已经支付成功，支付成功，更新支付 发送支付队列");

            } else {

                //继续发送延迟检查任务，计算延迟时间等
                if(counter>0){
                    counter--;
                    paymsPaymentService.sendCheckPaymentStatusDelayMessageQueue(orderSnFinal, counter);
                } else{
                    //检查次数用尽
                    System.out.println("检查次数用尽");
                }
            }

        }

    }

}
