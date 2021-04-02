package com.lhk.auction.manage.mapper;

import com.lhk.auction.bean.BmsRole;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.*;

public interface BmsRoleMapper extends Mapper<BmsRole> {

    public List<BmsRole> findList(@Param("name") String name, @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);
    public int getTotal(@Param("name") String name);

}
