package com.lhk.auction.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lhk.auction.bean.BmsAuthority;
import com.lhk.auction.bean.BmsMenu;
import com.lhk.auction.bean.BmsRole;
import com.lhk.auction.manage.util.MenuUtil;
import com.lhk.auction.manage.util.Page;
import com.lhk.auction.service.BmsAuthorityService;
import com.lhk.auction.service.BmsMenuService;
import com.lhk.auction.service.BmsRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class RoleController {

    @Reference
    BmsRoleService bmsRoleService;

    @Reference
    BmsMenuService bmsMenuService;

    @Reference
    BmsAuthorityService bmsAuthorityService;

    @RequestMapping(value="/addAuthority",method=RequestMethod.POST)
    @ResponseBody
    public Map<String, String> addAuthority(@RequestParam(name="ids",required=true) String ids, @RequestParam(name="roleId",required=true) String roleId){

        Map<String,String> ret = new HashMap<String, String>();
        if(StringUtils.isEmpty(ids)){
            ret.put("type", "error");
            ret.put("msg", "请选择相应的权限！");
            return ret;
        }
        if(roleId == null){
            ret.put("type", "error");
            ret.put("msg", "请选择相应的角色！");
            return ret;
        }
        if(ids.contains(",")){
            ids = ids.substring(0,ids.length()-1);
        }
        String[] idArr = ids.split(",");
        //先清除所有权限
        if(idArr.length > 0){
            bmsAuthorityService.deleteByRoleId(roleId);
        }
        for(String id:idArr){
            BmsAuthority authority = new BmsAuthority();
            authority.setMenuId(id);
            authority.setRoleId(roleId);
            bmsAuthorityService.addAuthority(authority);
        }
        ret.put("type", "success");
        ret.put("msg", "权限编辑成功！");
        return ret;
    }

    @RequestMapping(value="/getRoleAuthority",method=RequestMethod.POST)
    @ResponseBody
    public List<BmsAuthority> getRoleAuthority(@RequestParam(name="roleId",required=true) String roleId){

        return bmsAuthorityService.findAuthorityListByRoleId(roleId);
    }

    @RequestMapping(value="/getAllMenu",method=RequestMethod.POST)
    @ResponseBody
    public List<BmsMenu> getAllMenu(){

        return bmsMenuService.getAllMenu(null,null,0,9999);

    }

    @RequestMapping(value="/roleEdit",method=RequestMethod.POST)
    @ResponseBody
    public Map<String, String> edit(BmsRole bmsRole){

        Map<String, String> ret = new HashMap<String, String>();
        if(bmsRole == null){
            ret.put("type", "error");
            ret.put("msg", "请填写正确的角色信息！");
            return ret;
        }
        if(StringUtils.isEmpty(bmsRole.getName())){
            ret.put("type", "error");
            ret.put("msg", "请填写角色名称！");
            return ret;
        }
        if(bmsRoleService.updateRole(bmsRole) <= 0){
            ret.put("type", "error");
            ret.put("msg", "角色修改失败，请联系管理员！");
            return ret;
        }
        ret.put("type", "success");
        ret.put("msg", "角色修改成功！");
        return ret;
    }

    @RequestMapping(value="/roleDelete",method=RequestMethod.POST)
    @ResponseBody
    public Map<String, String> delete(String id){

        Map<String, String> ret = new HashMap<String, String>();
        if(id == null){
            ret.put("type", "error");
            ret.put("msg", "请选择要删除的角色！");
            return ret;
        }
        try {
            if(bmsRoleService.deleteRole(id) <= 0){
                ret.put("type", "error");
                ret.put("msg", "删除失败，请联系管理员！");
                return ret;
            }
        } catch (Exception e) {
            // TODO: handle exception
            ret.put("type", "error");
            ret.put("msg", "该角色下存在权限或者用户信息，不能删除！");
            return ret;
        }
        ret.put("type", "success");
        ret.put("msg", "角色删除成功！");
        return ret;
    }

    @RequestMapping(value="/roleAdd",method=RequestMethod.POST)
    @ResponseBody
    public Map<String, String> roleAdd(BmsRole bmsrole){
        Map<String, String> ret = new HashMap<String, String>();
        if(bmsrole == null){
            ret.put("type", "error");
            ret.put("msg", "请填写正确的角色信息！");
            return ret;
        }
        if(StringUtils.isEmpty(bmsrole.getName())){
            ret.put("type", "error");
            ret.put("msg", "请填写角色名称！");
            return ret;
        }
        if(bmsRoleService.addRole(bmsrole) <= 0){
            ret.put("type", "error");
            ret.put("msg", "角色添加失败，请联系管理员！");
            return ret;
        }
        ret.put("type", "success");
        ret.put("msg", "角色添加成功！");
        return ret;
    }


    //有index页面调用
    @GetMapping(value = "/roleList")
    public String getRoleList(@RequestParam(name = "secondMenudId") String secondMenudId, ModelMap modelMap, HttpServletRequest request){

        if(StringUtils.isNotBlank(secondMenudId)){

            List<BmsMenu> bmsMenuList = (List<BmsMenu>)request.getSession().getAttribute("bmsMenus");

            List<BmsMenu> thirdMenuList = MenuUtil.getAllThirdMenu(bmsMenuList ,secondMenudId);
            modelMap.put("thirdMenuList", thirdMenuList);

        }

        return "roleList";
    }

    @RequestMapping(value="/roleList",method= RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getList(Page page, @RequestParam(name="name",required=false,defaultValue="") String name){

        Map<String, Object> ret = new HashMap<String, Object>();

        ret.put("rows", bmsRoleService.findList(name, page.getOffset(), page.getRows()));
        ret.put("total", bmsRoleService.getTotal(name));
        return ret;
    }

}
