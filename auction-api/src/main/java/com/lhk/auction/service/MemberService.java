package com.lhk.auction.service;

import com.lhk.auction.bean.UmsMember;
import com.lhk.auction.bean.UmsMemberReceiveAddress;

import java.util.List;

public interface MemberService {
    UmsMember login(UmsMember umsMember);

    void saveUserTokenToCache(String token, String memberId);

    List<UmsMemberReceiveAddress> getUmsMemberReceiveAddressByMemberId(String memberId);

    void addUmsMemberReceiveAddress(UmsMemberReceiveAddress umsMemberReceiveAddress);

    UmsMember checkWeiboMember(String idstr);

    UmsMember saveUmsMember(UmsMember weiboUmsMember);

    int registUmsMember(UmsMember umsMember);

    boolean usernameExitCheck(String username);

    UmsMember getUmsMemberById(String buyer);
}
