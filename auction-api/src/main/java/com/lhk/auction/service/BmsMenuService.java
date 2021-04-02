package com.lhk.auction.service;

import com.lhk.auction.bean.BmsMenu;

import java.util.List;
import java.util.Map;

public interface BmsMenuService {
    BmsMenu getBmsMenuById(String menuId);

    List<BmsMenu> getAllMenu(String parentId, String name, Integer offset, Integer pageSize);
}
