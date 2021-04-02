package com.lhk.auction.order.activemqListener;

import com.lhk.auction.bean.OmsOrder;
import com.lhk.auction.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.MapMessage;

@Component
public class OrderServiceActiveMqListener {

    @Autowired
    OrderService orderService;

    @JmsListener(destination = "PAYMENT_SUCCESS_QUEUE", containerFactory = "jmsQueueListener")
    public void consumePaymentSuccessQueue(MapMessage mapMessage){

        String orderSn = null;
        try {
            orderSn = mapMessage.getString("orderSn");
        } catch (JMSException e) {
            e.printStackTrace();
        }
        //更新订单状态业务
        OmsOrder omsOrder = new OmsOrder();
        omsOrder.setOrderSn(orderSn);
        //定金支付成功更新Order
        orderService.earnestPaymentSuccessUpdateOrder(omsOrder);
    }

    @JmsListener(destination = "PAYMENTFINAL_SUCCESS_QUEUE", containerFactory = "jmsQueueListener")
    public void consumePaymentFinalSuccessQueue(MapMessage mapMessage){

        String orderSnFinal = null;
        try {
            orderSnFinal = mapMessage.getString("orderSnFinal");
        } catch (JMSException e) {
            e.printStackTrace();
        }
        //更新订单状态业务
        OmsOrder omsOrder = new OmsOrder();
        omsOrder.setOrderSnFinal(orderSnFinal);
        //定金支付成功更新Order
        orderService.finalPaymentSuccessUpdateOrder(omsOrder);
    }

}
