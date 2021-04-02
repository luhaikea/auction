package com.lhk.auction.order.bidder;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lhk.auction.bean.OmsOrder;
import com.lhk.auction.bean.PmsProductInfo;
import com.lhk.auction.service.OrderService;
import com.lhk.auction.service.PmsProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
@EnableScheduling
public class GetBidder {

    @Reference
    PmsProductService pmsProductService;

    @Autowired
    OrderService orderService;

    //10分钟
    @Scheduled(fixedRate = 1000*60)
    public void getBidder() throws ParseException {

        List<PmsProductInfo> allProduct = pmsProductService.getAllPmsProductInfo();
        for (PmsProductInfo pmsProductInfo : allProduct) {

            String buyer = pmsProductInfo.getBuyer();
            String endDateString = pmsProductInfo.getEndTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date endDate = dateFormat.parse(endDateString);
            //当一个物品的拍卖人为空并且拍卖结束【当前时间大于物品拍卖的结束时间】
            if(StringUtils.isBlank(buyer) && endDate.getTime()<new Date().getTime()){

                //找到出价最高的客户【不可能出现两个出价相同的客户，一个客户的出价都是基于当前最高出价进行出价的】
                List<OmsOrder> ordersByProductId = orderService.getOrderByProductId(pmsProductInfo.getId());
                String buyerId = "";
                BigDecimal maxBidPrice = new BigDecimal("0");
                for (OmsOrder omsOrder : ordersByProductId) {
                    //必须对定金已支付的进行比较
                    if(omsOrder.getEarnestStatus().equals("1")){
                        if(maxBidPrice.compareTo(omsOrder.getBidPrice())<0){
                            maxBidPrice = omsOrder.getBidPrice();
                            buyerId = omsOrder.getMemberId();
                        }
                    }

                }
                //更新物品的出价人
                if(StringUtils.isBlank(buyerId)){
                    //buyerId为空 即无人出价
                    pmsProductInfo.setBuyer("0");
                    pmsProductService.updatePmsProductInfo(pmsProductInfo);
                } else{
                    pmsProductInfo.setBuyer(buyerId);
                    pmsProductService.updatePmsProductInfo(pmsProductInfo);
                }

                //更新所有关于该物品的订单
                for (OmsOrder omsOrder : ordersByProductId) {

                    if(omsOrder.getEarnestStatus().equals("1")) {
                        if (omsOrder.getMemberId().equals(buyerId)) {
                            omsOrder.setAuctionStatus("2");
                            orderService.updateOrder(omsOrder);
                        } else {
                            omsOrder.setAuctionStatus("3");
                            orderService.updateOrder(omsOrder);
                        }
                    }

                }

            }
        }

    }


}
