package com.cmcc.demo.demo.service.impl;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SchedulingServiceImpl {

    /**
     * second, minute, hour, day of month, month, day of week.
     * For example, {@code "0 * * * * MON-FRI"}
     */
    @Scheduled(cron = "0 * * * * MON-FRI")
    @Scheduled(cron = "0,1,2 * * * * MON-FRI")//枚举时间点指定
    @Scheduled(cron = "0/3 * * * * MON-FRI")//每三秒一次
    @Scheduled(cron = "0-3 * * * * MON-FRI")//时间段指定
    @Scheduled(cron = "0 * * * * MON-FRI")
    public void scheduledTask(){
        System.err.println("定时任务执行...");
    }

}
