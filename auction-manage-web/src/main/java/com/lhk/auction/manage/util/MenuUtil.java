package com.lhk.auction.manage.util;

import com.lhk.auction.bean.BmsMenu;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * 关于菜单操作的一些公用方法
 * @author llq
 *
 */
public class MenuUtil {
    /**
     * 从给定的菜单中返回所有顶级菜单
     * @param menuList
     * @return
     */
    public static List<BmsMenu> getAllTopMenu(List<BmsMenu> menuList){

        List<BmsMenu> ret = new ArrayList<BmsMenu>();

        for(BmsMenu menu:menuList){
            if(menu.getParentId().equals("0")){
                ret.add(menu);
            }
        }
        return ret;
    }

    /**
     * 获取所有的二级菜单
     * @param menuList
     * @return
     */
    public static List<BmsMenu> getAllSecondMenu(List<BmsMenu> menuList){

        List<BmsMenu> ret = new ArrayList<BmsMenu>();

        List<BmsMenu> allTopMenu = getAllTopMenu(menuList);

        for(BmsMenu menu:menuList){
            for(BmsMenu topMenu:allTopMenu){
                if(menu.getParentId().equals(topMenu.getId())){
                    ret.add(menu);
                    break;
                }
            }
        }
        return ret;
    }

    /**
     * 获取某个二级菜单下的按钮
     * @param menuList
     * @param
     * @return
     */
    public static List<BmsMenu> getAllThirdMenu(List<BmsMenu> menuList, String secondMenuId){
        List<BmsMenu> ret = new ArrayList<BmsMenu>();
        for(BmsMenu menu:menuList){
            if(menu.getParentId().equals(secondMenuId)){
                ret.add(menu);
            }
        }
        return ret;
    }
}
