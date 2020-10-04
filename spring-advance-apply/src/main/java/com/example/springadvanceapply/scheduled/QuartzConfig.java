package com.example.springadvanceapply.scheduled;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
*
* 创建配置类QuartzConfig
* */

//@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail testJobDetail() {
        return JobBuilder.newJob(TestQuartz.class).withIdentity("testQuartz").storeDurably().build();
    }

    @Bean
    public Trigger testQuartzTrigger() {
        SimpleScheduleBuilder simpleScheduleBuilder=SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(10)//设置时间同步
                .repeatForever();
        return TriggerBuilder.newTrigger().forJob(testJobDetail())
                .withIdentity("testQuartz")
                .withSchedule(simpleScheduleBuilder)
                .build();
    }
}
