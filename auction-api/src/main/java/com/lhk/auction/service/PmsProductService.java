package com.lhk.auction.service;

import com.lhk.auction.bean.PmsProductInfo;

import java.util.List;

public interface PmsProductService {
    List<PmsProductInfo> getPmsProductInfo(String catalog3Id);

    String savePmsProductInfo(PmsProductInfo pmsProductInfo);

    PmsProductInfo getPmsProductInfoById(String productId);

    List<PmsProductInfo> getAllProduct();

    List<PmsProductInfo> getAllPmsProductInfo();

    void updatePmsProductInfo(PmsProductInfo pmsProductInfo);

    PmsProductInfo getPmsProductInfoByIdAllInfo(String pmsProductId);

    String editPmsProductInfo(PmsProductInfo pmsProductInfo);

    String aggreeApprove(PmsProductInfo pmsProductInfo);

    void sendUpdateSearchMsg(String id);

    PmsProductInfo getSearchProductById(String productId);
}
