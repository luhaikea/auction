package com.lhk.auction.config;

import com.lhk.auction.interceptors.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/*
单点登录可以跨顶级域名认证 所以就不能把登录信息放到session中 session不能跨顶级域名使用
 */
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        //spring boot默认有一个全局的异常处理（一个error方法）
        //排除error这个请求是由于：当页面加载不到某一个图片时就会对springboot发送一个error错误请求
        registry.addInterceptor(authInterceptor).addPathPatterns("/**").excludePathPatterns("/error");
        super.addInterceptors(registry);
    }
}
