package com.lhk.auction.manage.mapper;

import com.lhk.auction.bean.BmsMenu;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BmsMenuMapper extends Mapper<BmsMenu> {
    List<BmsMenu> selectMenuList(@Param("parentId") String parentId, @Param("name") String name, @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);
}
