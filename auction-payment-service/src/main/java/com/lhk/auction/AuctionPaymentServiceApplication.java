package com.lhk.auction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.lhk.auction.payment.mapper")
public class AuctionPaymentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuctionPaymentServiceApplication.class, args);
    }

}
