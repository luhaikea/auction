package com.lhk.auction.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lhk.auction.bean.PmsCatalog1;
import com.lhk.auction.bean.PmsCatalog2;
import com.lhk.auction.bean.PmsCatalog3;
import com.lhk.auction.service.PmsCatalogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class CatalogController {

    @Reference
    PmsCatalogService pmscatalogService;

    @RequestMapping(value = "/getCatalog1")
    @ResponseBody
    public List<PmsCatalog1> getCatalog1(){

        List<PmsCatalog1> catalog1s = pmscatalogService.getCatalog1();
        return catalog1s;

    }

    @RequestMapping(value = "/getCatalog2")
    @ResponseBody
    public List<PmsCatalog2> getCatalog2(String catalog1Id){
        List<PmsCatalog2> catalog2s = pmscatalogService.getCatalog2(catalog1Id);
        return catalog2s;
    }

    @RequestMapping(value = "/getCatalog3")
    @ResponseBody
    public List<PmsCatalog3> getCatalog3(String catalog2Id){
        List<PmsCatalog3> catalog3s = pmscatalogService.getCatalog3(catalog2Id);
        return catalog3s;
    }
}
