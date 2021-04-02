package com.lhk.auction.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lhk.auction.bean.PmsAttrInfo;
import com.lhk.auction.bean.PmsAttrValue;
import com.lhk.auction.service.PmsAttrService;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AttrController {

    @Reference
    PmsAttrService pmsAttrService;

    @RequestMapping("/getattrValueByattrId")
    @ResponseBody
    public List<PmsAttrValue> getattrValueByattrId(String attrId){
        return pmsAttrService.getattrValueByattrId(attrId);
    }

    @RequestMapping("/getAttrInfo")
    @ResponseBody
    public PmsAttrInfo getAttrInfo(@RequestBody PmsAttrInfo attrInfo){

        PmsAttrInfo pmsAttrInfo = pmsAttrService.getAttrInfo(attrInfo);

        return pmsAttrInfo;
    }


    @RequestMapping(value = "/attrList", method = RequestMethod.GET)
    public String attrList(){
        return "attrList";
    }

    @RequestMapping("/attrInfoList")
    @ResponseBody
    public List<PmsAttrInfo> attrInfoList(String catalog3Id){

        if(catalog3Id == null)
            return null;

        return pmsAttrService.attrInfoList(catalog3Id);
    }

    @RequestMapping("/attrInfoListAll")
    @ResponseBody
    public List<PmsAttrInfo> attrInfoListAll(@RequestBody PmsAttrInfo pmsAttrInfo){

        if(pmsAttrInfo == null)
            return null;

        return pmsAttrService.attrInfoListAll(pmsAttrInfo);
    }

    @RequestMapping("/saveAttrInfo")
    @ResponseBody
    public Map<String, String> saveAttrInfo(@RequestBody PmsAttrInfo pmsAttrInfo){

        Map<String, String> ret = new HashMap<String, String>();
        if(pmsAttrInfo == null){
            ret.put("type", "error");
            ret.put("msg", "请填写正确的属性信息！");
            return ret;
        }
        String success = pmsAttrService.saveAttrInfo(pmsAttrInfo);
        if(success == null){
            ret.put("type", "error");
            ret.put("msg", "失败!");
            return ret;
        }
        ret.put("type", "success");
        ret.put("msg", "成功！");
        return ret;
    }

    @RequestMapping("/attrDelete")
    @ResponseBody
    public Map<String, String> attrDelete(String id){

        Map<String, String> ret = new HashMap<String, String>();

        String success = pmsAttrService.deleteAttrInfo(id);
        if(success == null){
            ret.put("type", "error");
            ret.put("msg", "失败!");
            return ret;
        }
        ret.put("type", "success");
        ret.put("msg", "成功！");
        return ret;
    }




}
