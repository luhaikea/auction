package com.lhk.auction.service;

import com.lhk.auction.bean.PmsAttrInfo;
import com.lhk.auction.bean.PmsAttrValue;

import java.util.List;
import java.util.Set;

public interface PmsAttrService {
    List<PmsAttrInfo> attrInfoList(String catalog3Id);

    String saveAttrInfo(PmsAttrInfo pmsAttrInfo);

    PmsAttrInfo getAttrInfo(PmsAttrInfo attrInfo);

    List<PmsAttrInfo> attrInfoListAll(PmsAttrInfo pmsAttrInfo);

    List<PmsAttrValue> getattrValueByattrId(String attrId);

    List<PmsAttrInfo> getAttrListByValueId(Set<String> valueIdSet);

    String getAttrValueNameById(String valueId);

    String deleteAttrInfo(String id);
}
