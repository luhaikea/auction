package com.lhk.auction.service;

import com.lhk.auction.bean.BmsRole;

import java.util.List;
import java.util.Map;

public interface BmsRoleService {
    BmsRole getRoleById(String roleId);

    List<BmsRole> findList(String name, Integer offset, Integer pageSize);

    int getTotal(String name);

    int addRole(BmsRole bmsrole);

    int deleteRole(String id);

    int updateRole(BmsRole bmsRole);
}
