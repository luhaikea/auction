package com.lhk.auction.service;

import com.lhk.auction.bean.UmsSurveyInfo;

import java.util.List;

public interface SurveyService {
    int saveSurveyInfo(UmsSurveyInfo umsSurveyInfo);

    List<UmsSurveyInfo> getAllUmsSurveyInfo(String replayStatus);

    UmsSurveyInfo getUmsSurveyInfoById(UmsSurveyInfo umsSurveyInfo);

    void updateReplayStatus(String surveyId);
}
