package com.lhk.auction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.lhk.auction.member.mapper")
public class AuctionMemberServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuctionMemberServiceApplication.class, args);
    }

}
