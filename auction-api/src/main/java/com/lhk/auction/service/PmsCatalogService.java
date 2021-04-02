package com.lhk.auction.service;

import com.lhk.auction.bean.PmsCatalog1;
import com.lhk.auction.bean.PmsCatalog2;
import com.lhk.auction.bean.PmsCatalog3;

import java.util.List;

public interface PmsCatalogService {
    List<PmsCatalog1> getCatalog1();

    List<PmsCatalog2> getCatalog2(String catalog1Id);

    List<PmsCatalog3> getCatalog3(String catalog2Id);
}
