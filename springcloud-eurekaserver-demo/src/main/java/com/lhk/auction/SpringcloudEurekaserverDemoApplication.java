package com.lhk.auction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class SpringcloudEurekaserverDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringcloudEurekaserverDemoApplication.class, args);
    }

}
