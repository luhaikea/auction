package com.lhk.auction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;
@SpringBootApplication
@MapperScan(basePackages = "com.lhk.auction.survey.mapper")
public class AuctionSurveyServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuctionSurveyServiceApplication.class, args);
    }

}
