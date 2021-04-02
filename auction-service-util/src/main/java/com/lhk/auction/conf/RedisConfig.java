package com.lhk.auction.conf;

import com.lhk.auction.util.RedisUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//还需要一个redis配置类将redis的连接池创建到spring容器中
@Configuration
public class RedisConfig {
    //只有在springboot项目中resources文件下的资源文件才能被读入 而gmall-service-util项目是一个pom工程他只提供一个jar包
    //每个springboot项目的配置文件还是从自己的配置文件中读取，但不影响gmall-manage-service引入gmall-service-util这个jar包
    //redis的配置文件应该配置在每个使用redis的工程中 这样也可以是每个工程读取不同服务器的redis
    @Value("${spring.redis.host:disabled}")
    private String host;

    //冒号后面是默认值
    @Value("${spring.redis.port:0}")
    private int port;

    @Value("${spring.redis.database:0}")
    private int database;

    @Bean
    public RedisUtil getRedisUtil(){
        if(host.equals("disabled")){
            return null;
        }
        RedisUtil redisUtil=new RedisUtil();
        redisUtil.initPool(host,port,database);
        return redisUtil;
    }
}
