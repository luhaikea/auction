package com.lhk.auction.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lhk.auction.annotations.LoginRequired;
import com.lhk.auction.bean.*;
import com.lhk.auction.service.OrderService;
import com.lhk.auction.service.PmsAttrService;
import com.lhk.auction.service.PmsProductService;
import com.lhk.auction.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.*;

@Controller
public class SearchController {

    @Reference
    PmsAttrService pmsAttrService;

    @Reference
    SearchService searchService;

    @Reference
    OrderService orderService;

    @RequestMapping("/index")
    @LoginRequired(loginSuccess = false)
    public String getIndex() {
        return "index";
    }

    @RequestMapping("list")
    @LoginRequired(loginSuccess = false)
    public String list(PmsSearchParam pmsSearchParam, ModelMap map) {

        List<PmsSearchProductInfo> pmsSearchProductInfos = searchService.list(pmsSearchParam);

        for (PmsSearchProductInfo pmsSearchProductInfo : pmsSearchProductInfos) {

            List<OmsOrder> omsOrders = orderService.getOrderByProductId(pmsSearchProductInfo.getId());
            //该物品的出价次数
            pmsSearchProductInfo.setHotScore(omsOrders.size());
            BigDecimal maxPrice = new BigDecimal("0");
            for (OmsOrder omsOrder : omsOrders) {
                if(maxPrice.compareTo(omsOrder.getBidPrice())<0){
                    maxPrice = omsOrder.getBidPrice();
                }
            }
            //该物品的最高出价
            pmsSearchProductInfo.setStartingPrice(maxPrice);
        }

        map.put("pmsSearchProductInfos", pmsSearchProductInfos);

        //抽取检索结果所包含的平台属性集合 用于制作面包屑
        Set<String> valueIdSet = new HashSet<>();
        for (PmsSearchProductInfo pmsSearchProductInfo : pmsSearchProductInfos) {
            List<PmsProductAttrValue> pmsProductAttrValues = pmsSearchProductInfo.getPmsProductAttrValues();
            for (PmsProductAttrValue pmsProductAttrValue : pmsProductAttrValues) {
                String valueId = pmsProductAttrValue.getValueId();
                valueIdSet.add(valueId);
            }
        }

        //根据valueId将平台属性集合列表查出来
        List<PmsAttrInfo> pmsAttrInfos =  null;
        //这里不进行非空判断的话有可能会出现错误
        if(!valueIdSet.isEmpty()) {   //这里是用属性值反推属性
            //这里的到的属性对应的属性值并不是该属性的所有属性值，只是它出现在搜索到的物品所携带的属性值
            pmsAttrInfos = pmsAttrService.getAttrListByValueId(valueIdSet);
            map.put("pmsAttrInfos", pmsAttrInfos);
        }
        //对平台属性集合进一步处理，去掉当前条件中valueId所在的属性组
        //也就是减去pmsSearchParam中包含的属性    如果这个属性已经有属性值被选中了则这个属性就不需要被放到筛选条件中了
        String[] delValueIds = pmsSearchParam.getValueId();
        if(delValueIds != null){
            Iterator<PmsAttrInfo> iterator = pmsAttrInfos.iterator();

            while(iterator.hasNext()){
                PmsAttrInfo pmsAttrInfo = iterator.next();
                List<PmsAttrValue> pmsAttrValueList = pmsAttrInfo.getAttrValueList();
                for (PmsAttrValue pmsAttrValue : pmsAttrValueList) {
                    String valueId = pmsAttrValue.getId();
                    for (String delValueId : delValueIds) {
                        if(delValueId.equals(valueId)){
                            iterator.remove();
                        }
                    }
                }
            }
        }

        //当前请求  用于筛选条件形成其url
        String urlParam=getUrlParam(pmsSearchParam);
        map.put("urlParam",urlParam);

        //关键字  用于在筛选条件处显示关键字
        String keyword = pmsSearchParam.getKeyword();
        if(StringUtils.isNoneBlank(keyword)){
            map.put("keyword", keyword);
        }

        //面包屑  面包屑是减属性 在页面点击面包屑后会从url中减去该面包屑的valueId
        List<PmsSearchCrumb> pmsSearchCrumbs = new ArrayList<>();
        String[] valueIds = pmsSearchParam.getValueId();
        if(valueIds != null){
            //如果valueIds不为空，说明当前请求中包含属性参数，每一个属性参数都应该生产一个面包屑
            for (String valueId : valueIds) {

                PmsSearchCrumb pmsSearchCrumb = new PmsSearchCrumb();
                pmsSearchCrumb.setValueId(valueId);
                String valueName = pmsAttrService.getAttrValueNameById(valueId);
                pmsSearchCrumb.setValueName(valueName);
                //面包屑的url就是减去这个面包屑的valueId形成的url
                pmsSearchCrumb.setUrlParam(getUrlParamForCrumb(pmsSearchParam,valueId));
                pmsSearchCrumbs.add(pmsSearchCrumb);
            }
        }
        //已经选择的条件  面包屑
        map.put("pmsSearchCrumbs", pmsSearchCrumbs);

        return "list";
    }

    private String getUrlParamForCrumb(PmsSearchParam pmsSearchParam, String valueIdDel) {

        String urlParam="";

        String keyword = pmsSearchParam.getKeyword();
        String catalog3Id = pmsSearchParam.getCatalog3Id();
        String valueIds[] = pmsSearchParam.getValueId();

        if(StringUtils.isNoneBlank(keyword)){
            if(StringUtils.isNoneBlank(urlParam)){
                urlParam = urlParam + "&";
            }
            urlParam = urlParam+"keyword="+keyword;
        }

        if(StringUtils.isNoneBlank(catalog3Id)){
            if(StringUtils.isNoneBlank(urlParam)){
                urlParam = urlParam + "&";
            }
            urlParam = urlParam+"catalog3Id="+catalog3Id;
        }

        if(valueIds != null){
            for (String valueId : valueIds) {
                if(!valueId.equals(valueIdDel)) {
                    urlParam = urlParam + "&valueId=" + valueId;
                }

            }
        }
        return urlParam;
    }

    //重新点击面包屑后的链接也是基于上次请求的keyword和catalog3Id
    private String getUrlParam(PmsSearchParam pmsSearchParam) {

        String urlParam="";

        String keyword = pmsSearchParam.getKeyword();
        String catalog3Id = pmsSearchParam.getCatalog3Id();
        String valueIds[] = pmsSearchParam.getValueId();

        if(StringUtils.isNoneBlank(keyword)){
            if(StringUtils.isNoneBlank(urlParam)){
                urlParam = urlParam + "&";
            }
            urlParam = urlParam+"keyword="+keyword;
        }

        if(StringUtils.isNoneBlank(catalog3Id)){
            if(StringUtils.isNoneBlank(urlParam)){
                urlParam = urlParam + "&";
            }
            urlParam = urlParam+"catalog3Id="+catalog3Id;
        }

        if(valueIds != null){
            for (String valueId : valueIds) {
                urlParam = urlParam+"&valueId="+valueId;

            }
        }
        return urlParam;
    }
}