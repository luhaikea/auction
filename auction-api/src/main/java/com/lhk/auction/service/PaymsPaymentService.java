package com.lhk.auction.service;

import com.lhk.auction.bean.PaymsPayment;

import java.util.Map;

public interface PaymsPaymentService {


    void savePaymsPayment(PaymsPayment paymsPayment);

    void updatePaymsPayment(PaymsPayment paymsPayment);

    void sendCheckPaymentStatusDelayMessageQueue(String orderSn, int counter);

    Map<String, Object> checkPaymentResult(String orderSn);

    void sendCheckPaymentFinalStatusDelayMessageQueue(String orderSnFinal, int i);

    Map<String, Object> checkPaymentFinalResult(String orderSnFinal);

    void updatePaymsPaymentFinal(PaymsPayment paymsPayment);
}
