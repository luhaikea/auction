package com.lhk.auction.password.controller;

import com.alibaba.fastjson.JSON;
import com.lhk.auction.annotations.LoginRequired;
import com.lhk.auction.password.util.CpachaUtil;
import com.lhk.auction.util.HttpclientUtil;
import com.lhk.auction.util.JwtUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.lhk.auction.bean.UmsMember;
import com.lhk.auction.service.MemberService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PasswordController {

    @Reference
    MemberService memberService;

    @RequestMapping("login1")
    public String login1(){

        return "login1";
    }

    @RequestMapping("toRegist")
    public String toRegist(){
        return "regist";
    }

    @RequestMapping("usernameExitCheck")
    @ResponseBody
    public Map<String,String> usernameExitCheck(@RequestBody String username){

        Map<String, String> ret = new HashMap<String, String>();
        boolean result = memberService.usernameExitCheck(username);
        //存在
        if(result == true){
            ret.put("type","fail");
        }
        //不存在
        else{
            ret.put("type","success");
        }
        return ret;
    }

    @RequestMapping("regist")
    @ResponseBody
    public Map<String, String> regist(@RequestBody UmsMember umsMember){

        //记一个出现的问题   异常：SQLIntegrityConstraintViolationException
        //在一个字段上建了索引后这个字段就有了unique 不能插入在这个字段上有相同值的两条记录

        Map<String, String> ret = new HashMap<String, String>();

        umsMember.setCreateTime(new Date());
        int i = memberService.registUmsMember(umsMember);

        if(i!=0){
            ret.put("type","success");
        } else {
            ret.put("type","fail");
        }
        return ret;
    }

    //微博登录页回调这个方法必须返回到一个新的页面，不然微博登录页会一直回调这个页面
    @RequestMapping("redirect")
    public String weiBoredirect(String code, HttpServletRequest request){

        System.out.println("回调"+code);
        String accessTokenStr = getAccessToken(code);
        System.out.println("accessTokenStr"+accessTokenStr);
        Map<String, String> accessTokenMap = JSON.parseObject(accessTokenStr,Map.class);
        String accessToken = null;
        if(accessTokenMap != null) {
            accessToken = (String) accessTokenMap.get("access_token");
        } else{
            return "redirect:http://search.auction.com:8883/index";
        }
        String uid = (String) accessTokenMap.get("uid");

        Map<String, Object> userInfoMap = getUserInfo(accessToken, uid);
        System.out.println("用户信息:"+userInfoMap.get("name"));

        UmsMember weiboMember = getWeiboMember(userInfoMap,code,accessToken);
        System.out.println("微博用户信息:"+weiboMember.toString());
        //生成token,并且重定向到首页，携带该token
        String token = "";

        if(weiboMember!=null){

            String memberId = weiboMember.getId();
            String nickname = weiboMember.getNickname();
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("memberId", memberId);
            userMap.put("nickname", nickname);

            String ip = request.getHeader("x-forwarded-for");
            if(StringUtils.isBlank(ip)){
                ip = request.getRemoteAddr();
                if(StringUtils.isBlank(ip)){
                    ip = "127.0.0.1";
                }
            }
            token = JwtUtil.encode("2021auction", userMap, ip);

            System.out.println(token);
            memberService.saveUserTokenToCache(token, memberId);

        } else{
            token = "fail";
        }

        return "redirect:http://search.auction.com:8883/index?token="+token;
    }

    private String getAccessToken(String code){
        //https://api.weibo.com/oauth2/access_token?client_id=2486814460&client_secret=1a04eb5cce2dda9b2eaafa4fc43fbc48&grant_type=authorization_code&redirect_uri=http://passport.auction.com:8885/redirect&code=CODE
        //授权码换取access_token
        String getAccessTokenUrl = "https://api.weibo.com/oauth2/access_token";

        Map<String, String> getAccessTokenParam = new HashMap();
        getAccessTokenParam.put("client_id", "2486814460");
        getAccessTokenParam.put("client_secret", "1a04eb5cce2dda9b2eaafa4fc43fbc48");
        getAccessTokenParam.put("grant_type", "authorization_code");
        getAccessTokenParam.put("redirect_uri", "http://passport.auction.com:8885/redirect");
        getAccessTokenParam.put("code", code);

        String accessTokenStr = HttpclientUtil.doPost(getAccessTokenUrl, getAccessTokenParam);
        return accessTokenStr;
    }

    private Map<String, Object> getUserInfo(String accessToken, String uid){
        //access_token换取用户信息
        String getUserUrl = "https://api.weibo.com/2/users/show.json?access_token="+accessToken+"&uid="+uid;
        //得到一个JSON字符串
        String userStr = HttpclientUtil.doGet(getUserUrl);
        Map<String, Object> userInfoMap = JSON.parseObject(userStr, Map.class);
        return userInfoMap;
    }

    private UmsMember getWeiboMember(Map<String, Object> userInfoMap, String code, String accessToken) {
        //首先用idstr检查该微博用户是否已经在本系统保存
        UmsMember weiboMember = memberService.checkWeiboMember((String)userInfoMap.get("idstr"));
        System.out.println(weiboMember == null);
        if(weiboMember == null){
            //将用户信息保存数据库，用户类型设置为微博用户
            UmsMember weiboUmsMember = new UmsMember();
            weiboUmsMember.setUsername((String)userInfoMap.get("screen_name"));
            weiboUmsMember.setNickname((String)userInfoMap.get("name"));
            weiboUmsMember.setStatus(1);
            weiboUmsMember.setCreateTime(new Date());
            weiboUmsMember.setIcon((String)userInfoMap.get("profile_image_url"));//用户头像

            //本系统：0->未知；1->男；2->女   微博：gender	string	性别，m：男、f：女、n：未知
            String gender = (String)userInfoMap.get("gender");
            if(gender.equals("m")){
                weiboUmsMember.setGender("1");
            } else if(gender.equals("f")){
                weiboUmsMember.setGender("2");
            } else {
                weiboUmsMember.setGender("0");
            }

            weiboUmsMember.setSourceUid((String)userInfoMap.get("idstr"));
            weiboUmsMember.setSourceType("2");
            weiboUmsMember.setAccessCode(code);
            weiboUmsMember.setAccessToken(accessToken);

            weiboMember = memberService.saveUmsMember(weiboUmsMember);
            System.out.println("结果 "+weiboMember.toString());
        }
        return weiboMember;
    }

    //直接访问登录页面必须携带返回地址
    @RequestMapping("getLogin")
    public String getLogin(String ReturnUrl, ModelMap modelMap, HttpServletRequest request){

        String queryString =  request.getQueryString();
        String qS = queryString.substring(10);
        modelMap.put("ReturnUrl", qS);
        return "login";
    }

    //对应第一种情况
    @RequestMapping("getToken")
    @ResponseBody
    public Map<String, String> getToken(@RequestBody Map<String,String> param, HttpServletRequest request){

        String username = param.get("username");
        String password = param.get("password");
        String cpacha = param.get("cpacha");
        Map<String, String> ret = new HashMap<String, String>();
        System.out.println(username+" "+password+" "+cpacha);
        Object loginCpacha = request.getSession().getAttribute("loginCpacha");
        if(loginCpacha == null){
            ret.put("type", "fail");
            ret.put("msg", "会话超时，请刷新页面！");
            return ret;
        }
        if(!cpacha.toUpperCase().equals(loginCpacha.toString().toUpperCase())){
            ret.put("type", "fail");
            ret.put("msg", "验证码错误！");
            return ret;
        }

        UmsMember umsMember = new UmsMember();
        umsMember.setUsername(username);
        umsMember.setPassword(password);
        String token = "";
        //调用用户服务验证用户名和密码
        UmsMember umsMemberLogin= memberService.login(umsMember);
        if(umsMemberLogin!=null){
            //登录成功
            //用jwt制作token
            String memberId = umsMemberLogin.getId();
            String nickname = umsMemberLogin.getNickname();
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("memberId", memberId);
            userMap.put("nickname", nickname);

            //获得IP  这个方法获得的是直接发送请求的服务器地址，如果有负载均衡的话得到的就是负载均衡服务器的IP
            //String remoteAddr = request.getRemoteAddr();

            String ip = request.getHeader("x-forwarded-for");//通过nginx代理转发的客户端ip
            if(StringUtils.isBlank(ip)){
                ip = request.getRemoteAddr();
                if(StringUtils.isBlank(ip)){
                    ip = "127.0.0.1";
                }
            }
            //需要对参数加密后生成token
            token = JwtUtil.encode("2021auction", userMap, ip);
            ret.put("type","success");
            ret.put("token",token);
            //将token存入redis一份  由于cookie中的token的过期时间可能被篡改
            memberService.saveUserTokenToCache(token, memberId);

        } else{
            //登录失败
            ret.put("type","fail");
            ret.put("msg", "用户名或者密码错误");
        }

        return ret;
    }

    /*
      注意这里必须使用currentIp不能用request的IP，这里的流程是浏览器访问某个业务，被这个业务的拦截器拦截
      ，再由这个业务的拦截器对verify方法发送http请求，所以这个request的IP是业务的IP，不是浏览器的IP,这里需要浏览器的IP
      【这里可以通过测试得出】
   */
    @RequestMapping("verify")
    @ResponseBody
    public String verify(String token, String currentIp){

        //通过jwt校验token真假
        Map<String, String> map = new HashMap<>();

        Map<String, Object> decode = JwtUtil.decode(token, "2021auction", currentIp);
        if(decode!=null){
            map.put("status", "success");
            map.put("memberId", (String) decode.get("memberId"));
            map.put("nickname", (String) decode.get("nickname"));

        } else{
            map.put("status", "fail");

        }

        return JSON.toJSONString(map);
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
