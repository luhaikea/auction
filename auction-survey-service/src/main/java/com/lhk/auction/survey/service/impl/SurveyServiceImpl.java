package com.lhk.auction.survey.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lhk.auction.bean.PmsProductInfo;
import com.lhk.auction.bean.UmsReplayInfo;
import com.lhk.auction.bean.UmsSurveyInfo;
import com.lhk.auction.service.SurveyService;
import com.lhk.auction.survey.mapper.UmsSurveyInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class SurveyServiceImpl implements SurveyService {

    @Autowired
    UmsSurveyInfoMapper umsSurveyInfoMapper;

    @Override
    public int saveSurveyInfo(UmsSurveyInfo umsSurveyInfo) {
        umsSurveyInfo.setReplayStatus("0");
        int i = umsSurveyInfoMapper.insert(umsSurveyInfo);
        return i;
    }

    @Override
    public List<UmsSurveyInfo> getAllUmsSurveyInfo(String replayStatus) {
        if(replayStatus.equals("2")){
            List<UmsSurveyInfo> umsSurveyInfos = umsSurveyInfoMapper.selectAll();
            return umsSurveyInfos;
        }
        UmsSurveyInfo umsSurveyInfo = new UmsSurveyInfo();
        umsSurveyInfo.setReplayStatus(replayStatus);
        return umsSurveyInfoMapper.select(umsSurveyInfo);
    }

    @Override
    public UmsSurveyInfo getUmsSurveyInfoById(UmsSurveyInfo umsSurveyInfo) {

        UmsSurveyInfo umsSurveyInfo1 = umsSurveyInfoMapper.selectByPrimaryKey(umsSurveyInfo.getId());
        return umsSurveyInfo1;
    }

    @Override
    public void updateReplayStatus(String surveyId) {

        UmsSurveyInfo umsSurveyInfo = new UmsSurveyInfo();
        umsSurveyInfo.setReplayStatus("1");
        umsSurveyInfo.setId(surveyId);
        Example example = new Example(UmsSurveyInfo.class);
        example.createCriteria().andEqualTo("id", surveyId);
        umsSurveyInfoMapper.updateByExampleSelective(umsSurveyInfo, example);
    }
}