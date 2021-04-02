package com.lhk.auction.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lhk.auction.bean.BmsRole;
import com.lhk.auction.manage.mapper.BmsRoleMapper;
import com.lhk.auction.service.BmsRoleService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service
public class BmsRoleServiceImpl implements BmsRoleService {

    @Autowired
    BmsRoleMapper bmsRoleMapper;

    @Override
    public BmsRole getRoleById(String roleId) {

        BmsRole bmsRole = new BmsRole();
        bmsRole.setId(roleId);
        BmsRole bmsRole1 = bmsRoleMapper.selectOne(bmsRole);
        return bmsRole1;
    }

    @Override
    public List<BmsRole> findList(String name, Integer offset, Integer pageSize) {

        return bmsRoleMapper.findList(name, offset, pageSize);
    }

    @Override
    public int getTotal(String name) {

        return bmsRoleMapper.getTotal(name);
    }

    @Override
    public int addRole(BmsRole bmsrole) {

        return bmsRoleMapper.insert(bmsrole);

    }

    @Override
    public int deleteRole(String id) {
        return bmsRoleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateRole(BmsRole bmsRole) {

        Example example = new Example(BmsRole.class);
        example.createCriteria().andEqualTo("name", bmsRole.getName());
        return bmsRoleMapper.updateByExampleSelective(bmsRole, example);

    }
}
