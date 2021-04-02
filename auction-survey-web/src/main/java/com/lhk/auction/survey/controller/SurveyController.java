package com.lhk.auction.survey.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lhk.auction.annotations.LoginRequired;
import com.lhk.auction.bean.UmsReplayInfo;
import com.lhk.auction.bean.UmsSurveyInfo;
import com.lhk.auction.service.ReplayService;
import com.lhk.auction.service.SurveyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SurveyController {

    @Reference
    SurveyService surveyService;
    @Reference
    ReplayService replayService;

    @LoginRequired(loginSuccess = true)
    @RequestMapping(value = "getSurvey", method = RequestMethod.GET)
    public String getSurvey(HttpServletRequest request, ModelMap modelMap){
        String memberId = (String) request.getAttribute("memberId");
        String nickname = (String) request.getAttribute("nickname");
        modelMap.put("nickname",nickname);
        return "survey";
    }

    @RequestMapping(value = "saveSurveyInfo", method = RequestMethod.POST)
    @LoginRequired(loginSuccess = true)
    @ResponseBody
    public Map<String, String> saveSurveyInfo(@RequestBody UmsSurveyInfo umsSurveyInfo, HttpServletRequest request){

        String memberId = (String) request.getAttribute("memberId");
        String nickname = (String) request.getAttribute("nickname");
        umsSurveyInfo.setCreateTime(new Date());
        umsSurveyInfo.setMemberId(memberId);
        Map<String, String> ret = new HashMap<>();
        int i = surveyService.saveSurveyInfo(umsSurveyInfo);
        if(i>0){
            ret.put("type","success");
            ret.put("msg","提交成功");

        } else{
            ret.put("type","success");
            ret.put("msg","提交失败");
        }
        return ret;
    }

    @RequestMapping("/getMyMessage")
    @LoginRequired(loginSuccess = true)
    public String getMyMessage(HttpServletRequest request, ModelMap modelMap){

        String memberId = (String) request.getAttribute("memberId");
        String nickname = (String) request.getAttribute("nickname");
        List<UmsReplayInfo> umsReplayInfos = replayService.getUmsReplayInfoByMemberId(memberId);
        modelMap.put("umsReplayInfos",umsReplayInfos);
        return "message";
    }
}
