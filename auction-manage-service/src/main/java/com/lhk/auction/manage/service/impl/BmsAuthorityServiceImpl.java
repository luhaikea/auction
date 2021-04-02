package com.lhk.auction.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lhk.auction.bean.BmsAuthority;
import com.lhk.auction.manage.mapper.BmsAuthorityMapper;
import com.lhk.auction.service.BmsAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service
public class BmsAuthorityServiceImpl implements BmsAuthorityService {

    @Autowired
    BmsAuthorityMapper bmsAuthorityMapper;

    @Override
    public List<BmsAuthority> getAuthorityListByRoleId(String id) {

        BmsAuthority bmsAuthority = new BmsAuthority();
        bmsAuthority.setRoleId(id);
        List<BmsAuthority> select = bmsAuthorityMapper.select(bmsAuthority);
        return select;
    }

    @Override
    public List<BmsAuthority> findAuthorityListByRoleId(String roleId) {

        BmsAuthority bmsAuthority = new BmsAuthority();
        bmsAuthority.setRoleId(roleId);

        List<BmsAuthority> select = bmsAuthorityMapper.select(bmsAuthority);
        return select;
    }

    @Override
    public void deleteByRoleId(String roleId) {

        BmsAuthority bmsAuthority = new BmsAuthority();
        bmsAuthority.setRoleId(roleId);

        bmsAuthorityMapper.delete(bmsAuthority);
    }

    @Override
    public void addAuthority(BmsAuthority authority) {

        bmsAuthorityMapper.insertSelective(authority);

    }
}
