package com.lhk.auction.service;

import com.lhk.auction.bean.OmsOrder;

import java.util.List;

public interface OrderService {
    List<OmsOrder> getOrderByProductId(String productId);

    List<OmsOrder> getOrderByMemberId(String memberId);

    void saveOmsOrder(OmsOrder omsOrder);

    OmsOrder getOrderByOrderSn(String orderSn);

    void earnestPaymentSuccessUpdateOrder(OmsOrder omsOrder);

    void updateOrder(OmsOrder omsOrder);

    boolean getOrderByProductIdAndMemberId(String pmsProductInfoId, String memberId);

    OmsOrder getOrderById(String orderId);

    OmsOrder getOrderByorderSnFinal(String orderSnFinal);

    void finalPaymentSuccessUpdateOrder(OmsOrder omsOrder);
}
