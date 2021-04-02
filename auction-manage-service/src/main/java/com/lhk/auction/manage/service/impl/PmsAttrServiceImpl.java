package com.lhk.auction.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lhk.auction.bean.PmsAttrInfo;
import com.lhk.auction.bean.PmsAttrValue;
import com.lhk.auction.manage.mapper.PmsAttrInfoMapper;
import com.lhk.auction.manage.mapper.PmsAttrValueMapper;
import com.lhk.auction.service.PmsAttrService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Set;

@Service
public class PmsAttrServiceImpl implements PmsAttrService {

    @Autowired
    PmsAttrInfoMapper pmsAttrInfoMapper;

    @Autowired
    PmsAttrValueMapper pmsAttrValueMapper;

    @Override
    public List<PmsAttrInfo> attrInfoList(String catalog3Id) {

        PmsAttrInfo pmsAttrInfo = new PmsAttrInfo();
        pmsAttrInfo.setCatalog3Id(catalog3Id);
        List<PmsAttrInfo> pmsAttrInfoList = pmsAttrInfoMapper.select(pmsAttrInfo);

        for (PmsAttrInfo attrInfo : pmsAttrInfoList) {
            PmsAttrValue pmsAttrValue = new PmsAttrValue();
            pmsAttrValue.setAttrId(attrInfo.getId());
            List<PmsAttrValue> pmsAttrValues = pmsAttrValueMapper.select(pmsAttrValue);
            attrInfo.setAttrValueList(pmsAttrValues);
        }

        return pmsAttrInfoList;
    }

    @Override
    public String saveAttrInfo(PmsAttrInfo pmsAttrInfo) {

        String id = pmsAttrInfo.getId();
        if(StringUtils.isBlank(id)){
            //id为空，保存
            //保存属性    insert insertSelective 是否将null插入数据库
            pmsAttrInfoMapper.insertSelective(pmsAttrInfo);

            //保存属性值
            List<PmsAttrValue> attrValueList = pmsAttrInfo.getAttrValueList();
            for (PmsAttrValue pmsAttrValue : attrValueList) {
                pmsAttrValue.setAttrId(pmsAttrInfo.getId());
                pmsAttrValueMapper.insertSelective(pmsAttrValue);
            }

            return "success";
        } else{
            //id不为空，修改
            //修改属性
            Example example = new Example(PmsAttrInfo.class);
            example.createCriteria().andEqualTo("id",pmsAttrInfo.getId());
            pmsAttrInfoMapper.updateByExampleSelective(pmsAttrInfo,example);
            //修改属性值
            //先按照属性id删除所有属性值
            PmsAttrValue pmsAttrValueDel = new PmsAttrValue();
            pmsAttrValueDel.setAttrId(pmsAttrInfo.getId());
            pmsAttrValueMapper.delete(pmsAttrValueDel);

            List<PmsAttrValue> attrValueList = pmsAttrInfo.getAttrValueList();
            for (PmsAttrValue pmsAttrValue : attrValueList) {
                pmsAttrValue.setAttrId(pmsAttrInfo.getId());
                pmsAttrValueMapper.insertSelective(pmsAttrValue);
            }
            return "success";
        }

    }

    @Override
    public PmsAttrInfo getAttrInfo(PmsAttrInfo attrInfo) {

        PmsAttrInfo select = pmsAttrInfoMapper.selectOne(attrInfo);

        PmsAttrValue pmsAttrValue = new PmsAttrValue();
        pmsAttrValue.setAttrId(attrInfo.getId());
        List<PmsAttrValue> pmsAttrValueList = pmsAttrValueMapper.select(pmsAttrValue);

        select.setAttrValueList(pmsAttrValueList);

        return select;
    }

    @Override
    public List<PmsAttrInfo> attrInfoListAll(PmsAttrInfo pmsAttrInfo) {


        List<PmsAttrInfo> pmsAttrInfoList = pmsAttrInfoMapper.select(pmsAttrInfo);

        for (PmsAttrInfo attrInfo : pmsAttrInfoList) {
            PmsAttrValue pmsAttrValue = new PmsAttrValue();
            pmsAttrValue.setAttrId(attrInfo.getId());
            List<PmsAttrValue> pmsAttrValues = pmsAttrValueMapper.select(pmsAttrValue);
            attrInfo.setAttrValueList(pmsAttrValues);
        }

        return pmsAttrInfoList;

    }

    @Override
    public List<PmsAttrValue> getattrValueByattrId(String attrId) {

        PmsAttrValue pmsAttrValue = new PmsAttrValue();
        pmsAttrValue.setAttrId(attrId);
        List<PmsAttrValue> select = pmsAttrValueMapper.select(pmsAttrValue);

        return select;
    }

    @Override
    public List<PmsAttrInfo> getAttrListByValueId(Set<String> valueIdSet) {

        String valueIdStr = StringUtils.join(valueIdSet, ",");
        List<PmsAttrInfo> pmsBaseAttrInfos = pmsAttrInfoMapper.selectAttrInfoListByValueId(valueIdStr);
        return pmsBaseAttrInfos;
    }

    @Override
    public String getAttrValueNameById(String valueId) {
        PmsAttrValue pmsAttrValue = pmsAttrValueMapper.selectByPrimaryKey(valueId);
        return pmsAttrValue.getValueName();
    }

    @Override
    public String deleteAttrInfo(String id) {

        //删除属性值
        PmsAttrValue pmsAttrValue = new PmsAttrValue();
        pmsAttrValue.setAttrId(id);
        pmsAttrValueMapper.delete(pmsAttrValue);
        //删除属性
        PmsAttrInfo pmsAttrInfo = new PmsAttrInfo();
        pmsAttrInfo.setId(id);
        int delete = pmsAttrInfoMapper.delete(pmsAttrInfo);

        return "success";
    }


}
