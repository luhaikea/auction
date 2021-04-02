package com.lhk.auction.payment.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.lhk.auction.bean.PaymsPayment;
import com.lhk.auction.mq.ActiveMQUtil;
import com.lhk.auction.payment.mapper.PaymsPaymentMapper;
import com.lhk.auction.service.PaymsPaymentService;
import org.apache.activemq.ScheduledMessage;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import javax.jms.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymsPaymentServiceImpl implements PaymsPaymentService {

    @Autowired
    AlipayClient alipayClient;

    @Autowired
    PaymsPaymentMapper paymsPaymentMapper;

    @Autowired
    ActiveMQUtil activeMQUtil;
    
    @Override
    public void savePaymsPayment(PaymsPayment paymsPayment) {

        paymsPaymentMapper.insertSelective(paymsPayment);
    }

    private void sendPaymentFinalSuccessMsg(String orderSnFinal) {
        // 1、尾款支付完成（支付服务）
        // PAYMENTFINAL_SUCCESS_QUEUE：由订单服务消费【PAYMENTFINAL_SUCCESS_QUEUE由支付服务产生，由订单服务消费，为了引起尾款支付已完成的业务】
        Connection connection = null;
        Session session = null;
        try {
            connection = activeMQUtil.getConnectionFactory().createConnection();
            session = connection.createSession(true, Session.SESSION_TRANSACTED);
        } catch (JMSException jmsException) {
            jmsException.printStackTrace();
        }

        try {
            Queue payment_check_queue = session.createQueue("PAYMENTFINAL_SUCCESS_QUEUE");
            MessageProducer producer = session.createProducer(payment_check_queue);
            MapMessage mapMessage = new ActiveMQMapMessage();
            mapMessage.setString("orderSnFinal", orderSnFinal);
            producer.send(mapMessage);
            session.commit();
        } catch (Exception ex) {
            try {
                session.rollback();
            } catch (JMSException jmsException) {
                jmsException.printStackTrace();
            }
        } finally {
            try {
                connection.close();
            } catch (JMSException jmsException) {
                jmsException.printStackTrace();
            }
        }
    }

    public void sendPaymentSuccessMsg(String orderSn){
        // 1、支付完成（支付服务）
        // PAYMENT_SUCCESS_QUEUE：由订单服务消费【PAYMENT_SUCCESS_QUEUE由支付服务产生，由订单服务消费，为了引起支付已完成的业务】
        Connection connection = null;
        Session session = null;
        try {
            connection = activeMQUtil.getConnectionFactory().createConnection();
            session = connection.createSession(true, Session.SESSION_TRANSACTED);
        } catch (JMSException jmsException) {
            jmsException.printStackTrace();
        }

        try {

            Queue payment_check_queue = session.createQueue("PAYMENT_SUCCESS_QUEUE");
            MessageProducer producer = session.createProducer(payment_check_queue);
            MapMessage mapMessage = new ActiveMQMapMessage();
            mapMessage.setString("orderSn", orderSn);
            producer.send(mapMessage);
            session.commit();
        } catch (Exception ex) {
            try {
                session.rollback();
            } catch (JMSException jmsException) {
                jmsException.printStackTrace();
            }
        } finally {
            try {
                connection.close();
            } catch (JMSException jmsException) {
                jmsException.printStackTrace();
            }
        }
    }

    @Override
    public void updatePaymsPayment(PaymsPayment paymsPayment) {
        //先检验是否已经更新
        PaymsPayment paymsPaymentCheckUpadte = new PaymsPayment();
        paymsPaymentCheckUpadte.setOrderSn(paymsPayment.getOrderSn());
        PaymsPayment paymsPaymentCheckUpadteRes = paymsPaymentMapper.selectOne(paymsPaymentCheckUpadte);
        if(paymsPaymentCheckUpadteRes.getPaymentStatus().equals("0")){
            //未更新
            Example e = new Example(PaymsPayment.class);
            e.createCriteria().andEqualTo("orderSn", paymsPayment.getOrderSn());
            paymsPaymentMapper.updateByExampleSelective(paymsPayment, e);
            sendPaymentSuccessMsg(paymsPayment.getOrderSn());
        } else {
            //已更新 直接返回
            return;
        }
    }

    @Override
    public void updatePaymsPaymentFinal(PaymsPayment paymsPayment) {
        //先检验是否已经更新
        PaymsPayment paymsPaymentFinalCheckUpadte = new PaymsPayment();
        paymsPaymentFinalCheckUpadte.setOrderSn(paymsPayment.getOrderSn());
        PaymsPayment paymsPaymentFinalCheckUpadteRes = paymsPaymentMapper.selectOne(paymsPaymentFinalCheckUpadte);
        if(paymsPaymentFinalCheckUpadteRes.getPaymentStatus().equals("0")){
            //未更新
            Example e = new Example(PaymsPayment.class);
            e.createCriteria().andEqualTo("orderSn", paymsPayment.getOrderSn());
            paymsPaymentMapper.updateByExampleSelective(paymsPayment, e);
            sendPaymentFinalSuccessMsg(paymsPayment.getOrderSn());
        } else {
            //已更新 直接返回
            return;
        }
    }

    @Override
    public void sendCheckPaymentFinalStatusDelayMessageQueue(String orderSnFinal, int counter) {

        Connection connection = null;
        Session session = null;
        try {
            connection = activeMQUtil.getConnectionFactory().createConnection();
            session = connection.createSession(true, Session.SESSION_TRANSACTED);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        try{
            Queue payhmentfinall_check_queue = session.createQueue("PAYMENTFINAL_CHECK_QUEUE");
            MessageProducer producer = session.createProducer(payhmentfinall_check_queue);

            MapMessage mapMessage = new ActiveMQMapMessage();// hash结构

            mapMessage.setString("orderSnFinal",orderSnFinal);
            mapMessage.setInt("counter",counter);  //计数器

            //延迟1000*120毫秒后会被监听到
            mapMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY,1000*120);
            producer.send(mapMessage);
            session.commit();
        }catch (Exception ex){
            try {
                session.rollback();
            } catch (JMSException e1) {
                e1.printStackTrace();
            }
        }finally {
            try {
                connection.close();
            } catch (JMSException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void sendCheckPaymentStatusDelayMessageQueue(String orderSn, int counter) {

        System.out.println("发送延迟队列:"+counter);
        Connection connection = null;
        Session session = null;
        try {
            connection = activeMQUtil.getConnectionFactory().createConnection();
            session = connection.createSession(true, Session.SESSION_TRANSACTED);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        try{
            Queue payhment_success_queue = session.createQueue("PAYMENT_CHECK_QUEUE");
            MessageProducer producer = session.createProducer(payhment_success_queue);

            MapMessage mapMessage = new ActiveMQMapMessage();// hash结构

            mapMessage.setString("orderSn",orderSn);
            mapMessage.setInt("counter",counter);  //计数器

            //延迟1000*120毫秒后会被监听到
            mapMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY,1000*120);
            producer.send(mapMessage);
            session.commit();
        }catch (Exception ex){
            try {
                session.rollback();
            } catch (JMSException e1) {
                e1.printStackTrace();
            }
        }finally {
            try {
                connection.close();
            } catch (JMSException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public Map<String, Object> checkPaymentResult(String orderSn) {

        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("out_trade_no", orderSn);
        request.setBizContent(JSON.toJSONString(requestMap));
        AlipayTradeQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        Map<String, Object> resultMap = new HashMap<>();
        if(response.isSuccess()){
            resultMap.put("orderSn", response.getOutTradeNo());
            resultMap.put("alipayNo", response.getTradeNo());
            resultMap.put("paymentStatus",response.getTradeStatus());
            requestMap.put("callbackContent", response.getMsg());
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> checkPaymentFinalResult(String orderSnFinal) {

        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("out_trade_no", orderSnFinal);
        request.setBizContent(JSON.toJSONString(requestMap));
        AlipayTradeQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        Map<String, Object> resultMap = new HashMap<>();
        if(response.isSuccess()){
            resultMap.put("orderSnFinal", response.getOutTradeNo());
            resultMap.put("alipayNo", response.getTradeNo());
            resultMap.put("paymentStatus",response.getTradeStatus());
            requestMap.put("callbackContent", response.getMsg());
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
        return resultMap;
    }

}
