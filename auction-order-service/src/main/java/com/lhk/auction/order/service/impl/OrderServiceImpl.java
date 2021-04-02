package com.lhk.auction.order.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lhk.auction.bean.OmsOrder;
import com.lhk.auction.bean.PmsProductInfo;
import com.lhk.auction.order.mapper.OmsOrderMapper;
import com.lhk.auction.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OmsOrderMapper omsOrderMapper;

    @Override
    public List<OmsOrder> getOrderByProductId(String productId) {

        OmsOrder omsOrder = new OmsOrder();
        omsOrder.setProductId(productId);
        List<OmsOrder> omsOrders = omsOrderMapper.select(omsOrder);
        return omsOrders;
    }

    @Override
    public List<OmsOrder> getOrderByMemberId(String memberId) {

        OmsOrder omsOrder = new OmsOrder();
        omsOrder.setMemberId(memberId);
        List<OmsOrder> omsOrders = omsOrderMapper.select(omsOrder);
        return omsOrders;
    }

    @Override
    public void saveOmsOrder(OmsOrder omsOrder) {
        omsOrderMapper.insert(omsOrder);
    }

    @Override
    public OmsOrder getOrderByOrderSn(String orderSn) {

        System.out.println("getOrderByOrderSn  "+orderSn);
        OmsOrder omsOrder = new OmsOrder();
        omsOrder.setOrderSn(orderSn);
        OmsOrder omsOrderRtn = omsOrderMapper.selectOne(omsOrder);
        return omsOrderRtn;
    }

    @Override
    public OmsOrder getOrderByorderSnFinal(String orderSnFinal) {
        OmsOrder omsOrder = new OmsOrder();
        omsOrder.setOrderSnFinal(orderSnFinal);
        OmsOrder omsOrderRtn = omsOrderMapper.selectOne(omsOrder);
        return omsOrderRtn;
    }

    @Override
    public void earnestPaymentSuccessUpdateOrder(OmsOrder omsOrder) {

        Example example = new Example(OmsOrder.class);
        example.createCriteria().andEqualTo("orderSn", omsOrder.getOrderSn());

        omsOrder.setEarnestStatus("1");//已支付定金
        omsOrder.setAuctionStatus("1");//正在竞拍
        omsOrder.setFinalPayStatus("2");//未支付尾款
        omsOrderMapper.updateByExampleSelective(omsOrder, example);
    }

    @Override
    public void finalPaymentSuccessUpdateOrder(OmsOrder omsOrder) {

        Example example = new Example(OmsOrder.class);
        example.createCriteria().andEqualTo("orderSnFinal", omsOrder.getOrderSnFinal());
        omsOrder.setFinalPayStatus("1");//已支付尾款
        omsOrderMapper.updateByExampleSelective(omsOrder, example);

    }

    @Override
    public void updateOrder(OmsOrder omsOrder) {

        Example example = new Example(OmsOrder.class);
        example.createCriteria().andEqualTo("id", omsOrder.getId());

        int i = omsOrderMapper.updateByExampleSelective(omsOrder, example);
    }

    @Override
    public boolean getOrderByProductIdAndMemberId(String pmsProductInfoId, String memberId) {
        
        OmsOrder omsOrder = new OmsOrder();
        omsOrder.setProductId(pmsProductInfoId);
        omsOrder.setMemberId(memberId);
        List<OmsOrder> select = omsOrderMapper.select(omsOrder);
        if(select.size() == 0){
            return false;
        }
        return true;
    }

    @Override
    public OmsOrder getOrderById(String orderId) {

        OmsOrder omsOrder = omsOrderMapper.selectByPrimaryKey(orderId);
        return omsOrder;
    }
}
