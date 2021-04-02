package com.lhk.auction.service;

import com.lhk.auction.bean.UmsReplayInfo;

import java.util.List;

public interface ReplayService {
    void saveUmsReplayInfo(UmsReplayInfo umsReplayInfo);

    List<UmsReplayInfo> getUmsReplayInfoByMemberId(String memberId);
}
