package com.lhk.auction.bean;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

public class UmsReplayInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private String id;
    @Column
    private String surveyId;
    @Column
    private String memberId;
    @Column
    private String replayTitle;
    @Column
    private String replayMsg;
    @Column
    private Date createTime;

    public String getReplayTitle() {
        return replayTitle;
    }

    public void setReplayTitle(String replayTitle) {
        this.replayTitle = replayTitle;
    }

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getReplayMsg() {
        return replayMsg;
    }

    public void setReplayMsg(String replayMsg) {
        this.replayMsg = replayMsg;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
