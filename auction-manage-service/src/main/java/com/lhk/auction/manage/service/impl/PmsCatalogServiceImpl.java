package com.lhk.auction.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lhk.auction.bean.PmsCatalog1;
import com.lhk.auction.bean.PmsCatalog2;
import com.lhk.auction.bean.PmsCatalog3;
import com.lhk.auction.manage.mapper.PmsCatalog1Mapper;
import com.lhk.auction.manage.mapper.PmsCatalog2Mapper;
import com.lhk.auction.manage.mapper.PmsCatalog3Mapper;
import com.lhk.auction.service.PmsCatalogService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class PmsCatalogServiceImpl implements PmsCatalogService {

    @Autowired
    PmsCatalog1Mapper pmsCatalog1Mapper;

    @Autowired
    PmsCatalog2Mapper pmsCatalog2Mapper;

    @Autowired
    PmsCatalog3Mapper pmsCatalog3Mapper;

    @Override
    public List<PmsCatalog1> getCatalog1() {

        List<PmsCatalog1> pmsCatalog1List = pmsCatalog1Mapper.selectAll();
        return pmsCatalog1List;
    }

    @Override
    public List<PmsCatalog2> getCatalog2(String catalog1Id) {

        PmsCatalog2 pmsCatalog2 = new PmsCatalog2();
        pmsCatalog2.setCatalog1Id(catalog1Id);
        List<PmsCatalog2> select = pmsCatalog2Mapper.select(pmsCatalog2);
        return select;
    }

    @Override
    public List<PmsCatalog3> getCatalog3(String catalog2Id) {

        PmsCatalog3 pmsCatalog3 = new PmsCatalog3();
        pmsCatalog3.setCatalog2Id(catalog2Id);
        List<PmsCatalog3> select = pmsCatalog3Mapper.select(pmsCatalog3);
        return select;
    }
}
