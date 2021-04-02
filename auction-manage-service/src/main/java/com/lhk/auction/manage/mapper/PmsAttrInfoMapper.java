package com.lhk.auction.manage.mapper;

import com.lhk.auction.bean.PmsAttrInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PmsAttrInfoMapper extends Mapper<PmsAttrInfo> {
    List<PmsAttrInfo> selectAttrInfoListByValueId(@Param("valueIdStr") String valueIdStr);
}
