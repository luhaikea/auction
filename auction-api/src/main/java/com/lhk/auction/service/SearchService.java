package com.lhk.auction.service;

import com.lhk.auction.bean.PmsSearchParam;
import com.lhk.auction.bean.PmsSearchProductInfo;

import java.io.IOException;
import java.util.List;

public interface SearchService {
    List<PmsSearchProductInfo> list(PmsSearchParam pmsSearchParam);

    void updateSearch(String productId) throws IOException;
}
