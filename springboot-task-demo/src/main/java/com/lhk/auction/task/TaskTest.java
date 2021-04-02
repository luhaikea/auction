package com.lhk.auction.task;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class TaskTest {

    //1分钟
    @Scheduled(fixedRate = 1000*60)
    public void scheduled1() {
        System.out.println("=====>>>>>使用fixedRate{}"+ System.currentTimeMillis());
    }

}
