package com.lhk.auction.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lhk.auction.bean.UmsReplayInfo;
import com.lhk.auction.bean.UmsSurveyInfo;
import com.lhk.auction.service.ReplayService;
import com.lhk.auction.service.SurveyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
public class ReplayController {

    @Reference
    SurveyService surveyService;
    @Reference
    ReplayService replayService;

    @RequestMapping("/getReplay")
    public String getReplay(){
        return "replay";
    }

    @RequestMapping("/getUmsSurveyInfoById")
    @ResponseBody
    public UmsSurveyInfo getUmsSurveyInfoById(@RequestBody UmsSurveyInfo umsSurveyInfo){
        return surveyService.getUmsSurveyInfoById(umsSurveyInfo);
    }

    @RequestMapping("/sendUmsReplayInfo")
    @ResponseBody
    public Map<String, String> sendUmsReplayInfo(@RequestBody UmsReplayInfo umsReplayInfo){
        Map<String, String> ret = new HashMap<>();
        umsReplayInfo.setCreateTime(new Date());
        replayService.saveUmsReplayInfo(umsReplayInfo);
        //跟新反馈已回复
        surveyService.updateReplayStatus(umsReplayInfo.getSurveyId());
        ret.put("type","success");
        ret.put("msg","发送成功");
        return ret;
    }

    @RequestMapping("/getAllUmsSurveyInfo")
    @ResponseBody
    public List<UmsSurveyInfo> getAllUmsSurveyInfo(String replayStatus){

        if(replayStatus == null ){
            return null;
        }
        List<UmsSurveyInfo> allUmsSurveyInfo = surveyService.getAllUmsSurveyInfo(replayStatus);
        return changeSatisfaction(allUmsSurveyInfo);
    }

    private List<UmsSurveyInfo> changeSatisfaction(List<UmsSurveyInfo> allUmsSurveyInfo) {
        for (UmsSurveyInfo umsSurveyInfo : allUmsSurveyInfo) {
            if(umsSurveyInfo.getSatisfaction().equals("1")){
                umsSurveyInfo.setSatisfaction("非常满意");
            }
            if(umsSurveyInfo.getSatisfaction().equals("2")){
                umsSurveyInfo.setSatisfaction("满意");
            }
            if(umsSurveyInfo.getSatisfaction().equals("3")){
                umsSurveyInfo.setSatisfaction("一般");
            }
            if(umsSurveyInfo.getSatisfaction().equals("4")){
                umsSurveyInfo.setSatisfaction("不满意");
            }
            if(umsSurveyInfo.getSatisfaction().equals("5")){
                umsSurveyInfo.setSatisfaction("非常不满意");
            }
        }
        return allUmsSurveyInfo;
    }

    @RequestMapping("/getReplayStatus")
    @ResponseBody
    public List<Map<String,String>> getReplayStatus(){

        List<Map<String,String>> ret = new ArrayList<>();
        Map<String,String> m1 = new HashMap<>();
        m1.put("id","2");
        m1.put("name","全部");
        ret.add(m1);
        Map<String,String> m2 = new HashMap<>();
        m2.put("id","0");
        m2.put("name","未回复");
        ret.add(m2);
        Map<String,String> m3 = new HashMap<>();
        m3.put("id","1");
        m3.put("name","已回复");
        ret.add(m3);
        return ret;
    }
}
