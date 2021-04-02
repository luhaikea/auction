package com.lhk.auction.service;

import com.lhk.auction.bean.BmsUser;

public interface BmsUserService {

    BmsUser getByUsername(String username);

    int editPassword(BmsUser user);
}
