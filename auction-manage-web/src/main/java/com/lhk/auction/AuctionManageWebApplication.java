package com.lhk.auction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class AuctionManageWebApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(AuctionManageWebApplication.class);
    }

    public static void main(String[] args) {
        //禁用qos
        System.setProperty("dubbo.qos.accept.foreign.ip","false");
        SpringApplication.run(AuctionManageWebApplication.class, args);
    }

}
