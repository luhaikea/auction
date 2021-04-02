package com.lhk.auction.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lhk.auction.bean.BmsAuthority;
import com.lhk.auction.bean.BmsMenu;
import com.lhk.auction.bean.BmsRole;
import com.lhk.auction.bean.BmsUser;
import com.lhk.auction.manage.util.CpachaUtil;
import com.lhk.auction.manage.util.MenuUtil;
import com.lhk.auction.service.BmsAuthorityService;
import com.lhk.auction.service.BmsMenuService;
import com.lhk.auction.service.BmsRoleService;
import com.lhk.auction.service.BmsUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Controller
public class LoginController {

    @Reference
    BmsMenuService bmsMenuService;

    @Reference
    BmsAuthorityService bmsAuthorityService;

    @Reference
    BmsRoleService bmsRoleService;

    @Reference
    BmsUserService bmsUserService;


    @RequestMapping(value="/editPassword",method=RequestMethod.GET)
    public String editPassword(HttpServletRequest request,ModelMap modelMap){

        BmsUser user = (BmsUser)request.getSession().getAttribute("findByUsername");
        modelMap.put("admin", user);
        return "editPassword";
    }

    @RequestMapping(value="/editPassword",method=RequestMethod.POST)
    @ResponseBody
    public Map<String, String> editPasswordAct(String newpassword,String oldpassword,HttpServletRequest request){

        Map<String, String> ret = new HashMap<String, String>();
        if(StringUtils.isEmpty(newpassword)){
            ret.put("type", "error");
            ret.put("msg", "请填写新密码！");
            return ret;
        }

        BmsUser user = (BmsUser)request.getSession().getAttribute("findByUsername");

        if(!user.getPassword().equals(oldpassword)){
            ret.put("type", "error");
            ret.put("msg", "原密码错误！");
            return ret;
        }

        user.setPassword(newpassword);
        if(bmsUserService.editPassword(user) <= 0){
            ret.put("type", "error");
            ret.put("msg", "密码修改失败，请联系管理员！");
            return ret;
        }
        ret.put("type", "success");
        ret.put("msg", "密码修改成功！");

        return ret;
    }

    @RequestMapping(value = "welcome", method = RequestMethod.GET)
    public String welcome(){

        return "welcome";
    }


    @RequestMapping(value="/logout",method=RequestMethod.GET)
    public String logout(HttpServletRequest request){

        HttpSession session = request.getSession();
        session.setAttribute("findByUsername", null);
        session.setAttribute("bmsRole", null);
        request.getSession().setAttribute("bmsMenus", null);
        return "redirect:getLogin";

    }

    //由login页面发起请求
    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String index(HttpServletRequest request, ModelMap modelMap){

        List<BmsMenu> bmsMenus = (List<BmsMenu>)request.getSession().getAttribute("bmsMenus");
        BmsUser findByUsername = (BmsUser) request.getSession().getAttribute("findByUsername");
        BmsRole bmsRole = (BmsRole) request.getSession().getAttribute("bmsRole");

        if(findByUsername == null){
            return "redirect:getLogin";
        }

        modelMap.put("topMenuList", MenuUtil.getAllTopMenu(bmsMenus));
        modelMap.put("secondMenuList", MenuUtil.getAllSecondMenu(bmsMenus));


        modelMap.put("findByUsername", findByUsername);
        modelMap.put("bmsRole", bmsRole);

        return "index";
    }

    @RequestMapping(value = "getLogin", method = RequestMethod.GET)
    public String getLogin(){
        return "login";
    }

    @RequestMapping(value="login",method=RequestMethod.POST)
    @ResponseBody
    public Map<String, String> login(BmsUser user, String cpacha, HttpServletRequest request){

        Map<String, String> ret = new HashMap<String, String>();

        if(user == null){
            ret.put("type", "error");
            ret.put("msg", "请填写用户信息！");
            return ret;
        }

        if(StringUtils.isBlank(cpacha)){
            ret.put("type", "error");
            ret.put("msg", "请填写验证码！");
            return ret;
        }
        if(StringUtils.isBlank(user.getUsername())){
            ret.put("type", "error");
            ret.put("msg", "请填写用户名！");
            return ret;
        }
        if(StringUtils.isBlank(user.getPassword())){
            ret.put("type", "error");
            ret.put("msg", "请填写密码！");
            return ret;
        }
        Object loginCpacha = request.getSession().getAttribute("loginCpacha");
        if(loginCpacha == null){
            ret.put("type", "error");
            ret.put("msg", "会话超时，请刷新页面！");
            return ret;
        }
        if(!cpacha.toUpperCase().equals(loginCpacha.toString().toUpperCase())){
            ret.put("type", "error");
            ret.put("msg", "验证码错误！");
            return ret;
        }

        BmsUser findByUsername = bmsUserService.getByUsername(user.getUsername());
        if(findByUsername == null){
            ret.put("type", "error");
            ret.put("msg", "该用户名不存在！");
            return ret;
        }
        if(!user.getPassword().equals(findByUsername.getPassword())){
            ret.put("type", "error");
            ret.put("msg", "密码错误！");

            return ret;
        }

        //说明用户名密码及验证码都正确
        //此时需要查询用户的角色权限

        BmsRole bmsRole = bmsRoleService.getRoleById(findByUsername.getRoleId());

        List<BmsAuthority> bmsAuthorityList = bmsAuthorityService.getAuthorityListByRoleId(bmsRole.getId());//根据角色获取权限列表

        List<BmsMenu> bmsMenus = new ArrayList<>();

        for(BmsAuthority authority:bmsAuthorityList){
            String menuId = authority.getMenuId();
            BmsMenu bmsMenu = bmsMenuService.getBmsMenuById(menuId);
            bmsMenus.add(bmsMenu);
        }


        //把角色信息、菜单信息放到session中
        request.getSession().setAttribute("findByUsername", findByUsername);
        request.getSession().setAttribute("bmsRole", bmsRole);
        request.getSession().setAttribute("bmsMenus", bmsMenus);

        ret.put("type", "success");
        ret.put("msg", "登录成功！");

        return ret;
    }

    @RequestMapping(value="/getCpacha",method= RequestMethod.GET)
    public void getCpacha(HttpServletRequest request, HttpServletResponse response){

        CpachaUtil cpachaUtil = new CpachaUtil(4, 150, 40);
        String generatorVCode = cpachaUtil.generatorVCode();

        request.getSession().setAttribute("loginCpacha", generatorVCode);

        BufferedImage generatorRotateVCodeImage = cpachaUtil.generatorRotateVCodeImage(generatorVCode, true);

        try {
            ImageIO.write(generatorRotateVCodeImage, "gif", response.getOutputStream());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
