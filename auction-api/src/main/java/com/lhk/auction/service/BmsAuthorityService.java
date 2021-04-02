package com.lhk.auction.service;

import com.lhk.auction.bean.BmsAuthority;

import java.util.List;

public interface BmsAuthorityService {
    List<BmsAuthority> getAuthorityListByRoleId(String id);

    List<BmsAuthority> findAuthorityListByRoleId(String roleId);

    void deleteByRoleId(String roleId);

    void addAuthority(BmsAuthority authority);
}
