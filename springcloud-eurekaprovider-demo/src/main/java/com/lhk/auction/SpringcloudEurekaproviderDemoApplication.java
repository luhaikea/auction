package com.lhk.auction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class SpringcloudEurekaproviderDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringcloudEurekaproviderDemoApplication.class, args);
    }

}
