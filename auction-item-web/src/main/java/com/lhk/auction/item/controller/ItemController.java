package com.lhk.auction.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lhk.auction.annotations.LoginRequired;
import com.lhk.auction.bean.OmsOrder;
import com.lhk.auction.bean.PmsProductInfo;
import com.lhk.auction.service.OrderService;
import com.lhk.auction.service.PmsProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class ItemController {

    @Reference
    PmsProductService pmsProductService;
    @Reference
    OrderService orderService;


    @RequestMapping("{productId}.html")
    @LoginRequired(loginSuccess = false)
    public String getItem(@PathVariable String productId, ModelMap modelMap){

        PmsProductInfo pmsProductInfoById = pmsProductService.getPmsProductInfoById(productId);

        List<OmsOrder> omsOrders = orderService.getOrderByProductId(productId);
        pmsProductInfoById.setOmsOrders(omsOrders);
        BigDecimal maxPrice = new BigDecimal("0");
        for (OmsOrder omsOrder : omsOrders) {
            if(maxPrice.compareTo(omsOrder.getBidPrice())<0){
                maxPrice = omsOrder.getBidPrice();
            }
        }
        pmsProductInfoById.setNumBid(omsOrders.size());
        pmsProductInfoById.setTopPrice(maxPrice);
        modelMap.put("pmsProductInfo",pmsProductInfoById);

        return "item";
    }
}
