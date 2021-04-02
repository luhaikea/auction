package com.lhk.auction.interceptors;

import com.alibaba.fastjson.JSON;
import com.lhk.auction.annotations.LoginRequired;
import com.lhk.auction.util.CookieUtil;
import com.lhk.auction.util.HttpclientUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

//拦截器是基于每个springboot的模块
/*  老token：浏览器中的token         新token：url中的token 在登录页面得到token在由登录页面对携带的返回地址并携带token发起请求
                          老token空                      老token不空

    1、新token空     【从未登录过，是直接被浏览器拦截的】       2、【之前登录过，但访问每个其他的需要被拦截的页面都会进入这个分支】

    3、新token不空   【刚刚登录过，也就是直接点击登录按钮的情况】 4、【过期 也就是浏览器中有一个token但老token验证不过[验证不过即是过期]，又被踢回到认证中心，所以由携带了新token】

    情况2、3直接去验证token就可以了  情况4、是去验证新token这里的过期应该是jwt的过期

*/
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //放过其他的请求 比如对静态资源
        HandlerMethod handlerMethod = null;
        //获取执行方法上的注解
        if(handler instanceof  HandlerMethod){
            handlerMethod = (HandlerMethod) handler;
        }else{
            return true;
        }
        //获取执行方法上的注解 判断被拦截的请求去访问的方法的注解(是否是需要拦截的)
        LoginRequired methodAnnotation = handlerMethod.getMethodAnnotation(LoginRequired.class);

        if (methodAnnotation == null) {
            //也就是这个方法上没有@LoginRequired这个注解
            return true;
        }
        else {

            String token = "";
            String oldToken = CookieUtil.getCookieValue(request, "oldToken", true);
            if (StringUtils.isNotBlank(oldToken)) {
                token = oldToken;
            }
            String newToken = request.getParameter("token");
            if (StringUtils.isNotBlank(newToken)) {
                token = newToken;
            }
            //保存验证token的结果
            String success = "fail";
            Map<String,String> successMap = new HashMap<>();
            if(StringUtils.isNotBlank(token)){
                String ip = request.getHeader("x-forwarded-for");// 通过nginx转发的客户端ip
                if(StringUtils.isBlank(ip)){
                    ip = request.getRemoteAddr();// 从request中获取ip
                    if(StringUtils.isBlank(ip)){
                        ip = "127.0.0.1";
                    }
                }
                //通过验证之后successJson包含了验证的状态和用户信息
                String successJson  = HttpclientUtil.doGet("http://passport.auction.com:8885/verify?token=" + token+"&currentIp="+ip);
                successMap = JSON.parseObject(successJson,Map.class);
                success = successMap.get("status");
            }


            // 是否必须登录  也就是的到注解的值 获得该请求是否必登录成功
            boolean loginSuccess = methodAnnotation.loginSuccess();
            //@LoginRequired(loginSuccess = true)  //必须登录才可访问
            if (loginSuccess) {

                //token验证失败
                if (!success.equals("success")) {
                    //是由于token为空或者token过期验证不通过success为fail  重定向会passport登录
                    StringBuffer requestURL = request.getRequestURL();
                    //获得url后面的参数串
                    String queryString =  request.getQueryString();
                    String ReturnUrl = requestURL.toString();
                    if(StringUtils.isNotBlank(queryString)){
                        ReturnUrl += "?"+queryString;
                    }
                    System.out.println("拦截器 "+ReturnUrl);
                    response.sendRedirect("http://passport.auction.com:8885/getLogin?ReturnUrl="+ReturnUrl);
                    return false;
                }
                //token验证成功
                else {
                    // 需要将token携带的用户信息写入
                    request.setAttribute("memberId", successMap.get("memberId"));
                    request.setAttribute("nickname", successMap.get("nickname"));
                    //验证通过，覆盖cookie中的token  //刷新token
                    CookieUtil.setCookie(request,response,"oldToken",token,60*60*2,true);
                    return true;
                }

            }
            //@LoginRequired(loginSuccess = false)  //可以不登录去访问
            else{
                // 没有登录也能用，但是必须验证
                if (success.equals("success")) {
                    // 需要将token携带的用户信息写入
                    request.setAttribute("memberId", successMap.get("memberId"));
                    request.setAttribute("nickname", successMap.get("nickname"));
                    //验证通过，覆盖cookie中的token
                    CookieUtil.setCookie(request,response,"oldToken",token,60*60*2,true);
                }
                return true;
            }
        }

    }

}
