package com.lhk.auction.search;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lhk.auction.bean.PmsProductInfo;
import com.lhk.auction.bean.PmsSearchProductInfo;
import com.lhk.auction.service.PmsProductService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class AuctionSearchServiceApplicationTests {

    @Reference
    PmsProductService pmsProductService;

    @Autowired
    JestClient jestClient;

    @Test
    public void put() throws IOException {

        // 查询mysql数据
        List<PmsProductInfo> pmsProductInfos= new ArrayList<>();
        pmsProductInfos = pmsProductService.getAllProduct();

        // 转化为es的数据结构
        List<PmsSearchProductInfo> pmsSearchProductInfos = new ArrayList<>();

        for (PmsProductInfo pmsProductInfo : pmsProductInfos) {
            PmsSearchProductInfo pmsSearchProductInfo = new PmsSearchProductInfo();
            BeanUtils.copyProperties(pmsProductInfo, pmsSearchProductInfo);

            pmsSearchProductInfos.add(pmsSearchProductInfo);

        }
        // 导入es  这里指定在elasticsearch中的document的主键为pmsSearchProductInfo.getId()  更新时直接写入会自动覆盖
        for (PmsSearchProductInfo pmsSearchProductInfo : pmsSearchProductInfos) {
            Index put = new Index.Builder(pmsSearchProductInfo).index("auction").type("PmsProductInfo").id(pmsSearchProductInfo.getId() + "").build();
            jestClient.execute(put);
        }
    }
}
