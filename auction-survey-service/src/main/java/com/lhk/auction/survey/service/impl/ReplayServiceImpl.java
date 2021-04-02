package com.lhk.auction.survey.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lhk.auction.bean.UmsReplayInfo;
import com.lhk.auction.service.ReplayService;
import com.lhk.auction.survey.mapper.UmsReplayInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class ReplayServiceImpl implements ReplayService {

    @Autowired
    UmsReplayInfoMapper umsReplayInfoMapper;

    @Override
    public void saveUmsReplayInfo(UmsReplayInfo umsReplayInfo) {
        umsReplayInfoMapper.insert(umsReplayInfo);
    }

    @Override
    public List<UmsReplayInfo> getUmsReplayInfoByMemberId(String memberId) {

        UmsReplayInfo umsReplayInfo = new UmsReplayInfo();
        umsReplayInfo.setMemberId(memberId);
        List<UmsReplayInfo> select = umsReplayInfoMapper.select(umsReplayInfo);
        return select;
    }
}
