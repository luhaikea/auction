package com.lhk.auction.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

//这个类将来会被gmall-manage-service中的启动类扫描 必须保证启动类的
//包层次关系大于等于RedisUtil类的包层次关系附则就要在启动类中扫描RedisUtil组件
//原本启动类在com.atguigu.gmall.manage下而RedisUtil在com.atguigu.gmall.conf下 这样会报错 要把启动类移动到com.atguigu.gmall
public class RedisUtil {

    private JedisPool jedisPool;

    public void initPool(String host, int port, int database){

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(200);
        poolConfig.setMaxIdle(30);
        poolConfig.setBlockWhenExhausted(true);
        poolConfig.setMaxWaitMillis(10*1000);
        poolConfig.setTestOnBorrow(true);
        jedisPool = new JedisPool(poolConfig, host, port, 20*1000);

    }
    public Jedis getJedis(){
        Jedis jedis = jedisPool.getResource();
        return jedis;
    }
    //还需要一个redis配置类将redis的连接池创建到spring容器中
}
