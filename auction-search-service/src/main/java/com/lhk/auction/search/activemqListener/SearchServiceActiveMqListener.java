package com.lhk.auction.search.activemqListener;

import com.lhk.auction.bean.OmsOrder;
import com.lhk.auction.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import java.io.IOException;

@Component
public class SearchServiceActiveMqListener {

    @Autowired
    SearchService searchService;

    @JmsListener(destination = "SEARCH_UPDATE_QUEUE", containerFactory = "jmsQueueListener")
    public void consumeSearchUpdateQueue(MapMessage mapMessage) throws IOException {

        String productId = null;
        try {
            productId = mapMessage.getString("productId");
        } catch (JMSException e) {
            e.printStackTrace();
        }

        //更新搜索引擎
        searchService.updateSearch(productId);
    }

}
