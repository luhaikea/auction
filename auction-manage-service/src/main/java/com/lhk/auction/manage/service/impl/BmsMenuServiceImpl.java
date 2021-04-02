package com.lhk.auction.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lhk.auction.bean.BmsMenu;
import com.lhk.auction.manage.mapper.BmsMenuMapper;
import com.lhk.auction.service.BmsMenuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Service
public class BmsMenuServiceImpl implements BmsMenuService {

    @Autowired
    BmsMenuMapper bmsMenuMapper;

    @Override
    public BmsMenu getBmsMenuById(String menuId) {

        BmsMenu bmsMenu = bmsMenuMapper.selectByPrimaryKey(menuId);
        return bmsMenu;

    }

    @Override
    public List<BmsMenu> getAllMenu(String parentId, String name, Integer offset, Integer pageSize) {
        return bmsMenuMapper.selectMenuList(parentId, name, offset, pageSize);
    }


}
