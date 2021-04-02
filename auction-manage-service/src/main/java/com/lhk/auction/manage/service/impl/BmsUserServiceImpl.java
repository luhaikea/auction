package com.lhk.auction.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lhk.auction.bean.BmsUser;
import com.lhk.auction.manage.mapper.BmsUserMapper;
import com.lhk.auction.service.BmsUserService;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class BmsUserServiceImpl implements BmsUserService {

    @Autowired
    BmsUserMapper bmsUserMapper;

    @Override
    public BmsUser getByUsername(String username) {

        BmsUser bmsUser = new BmsUser();
        bmsUser.setUsername(username);
        BmsUser bmsUser1 = bmsUserMapper.selectOne(bmsUser);
        return bmsUser1;
    }

    @Override
    public int editPassword(BmsUser user) {

        int i = bmsUserMapper.updateByPrimaryKeySelective(user);
        return i;
    }
}
