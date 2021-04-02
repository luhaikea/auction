package com.lhk.auction.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//在每个moudle的启动类扫描到拦截器的情况下【如果没有扫描到，则这个moudle的所有方法都不会走拦截器】加了这个注解的方法会走拦截器，不加的不会通过拦截器

//注解运行范围，此注解在方法上生效
@Target(ElementType.METHOD)
//此注解在虚拟机允许时也生效
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginRequired {

    boolean loginSuccess() default true;

}
