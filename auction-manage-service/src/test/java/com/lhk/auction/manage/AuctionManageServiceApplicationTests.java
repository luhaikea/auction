package com.lhk.auction.manage;

import com.lhk.auction.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;

@SpringBootTest
class AuctionManageServiceApplicationTests {

    @Autowired
    RedisUtil redisUtil;

    @Test
    void contextLoads() {

        Jedis jedis = redisUtil.getJedis();
        jedis.close();

    }

}
