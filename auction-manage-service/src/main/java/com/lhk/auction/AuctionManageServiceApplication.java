package com.lhk.auction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan(basePackages = "com.lhk.auction.manage.mapper")
@SpringBootApplication
public class AuctionManageServiceApplication {

    public static void main(String[] args) {
        System.setProperty("dubbo.qos.accept.foreign.ip","false");
        SpringApplication.run(AuctionManageServiceApplication.class, args);
    }

}
