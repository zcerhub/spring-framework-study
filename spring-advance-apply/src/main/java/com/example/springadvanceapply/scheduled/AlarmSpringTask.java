package com.example.springadvanceapply.scheduled;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@EnableAsync
public class AlarmSpringTask {

    /*
     * 默认是fixedDelay，上一次执行完毕时间后执行下一个
     * */
//    @Scheduled(cron = "0/5 * * * * *")
    @Async
    public void run() throws InterruptedException {
        Thread.sleep(6000);
        System.out.println(Thread.currentThread().getName()+"=============>" +(System.currentTimeMillis() / 1000));
    }


    /*
     * 默认是fixedRate，上一次执开始执行时间点之后5秒再执行
     * */
//    @Scheduled(fixedRate = 5000)
    @Async
    public void run1() throws InterruptedException {
        Thread.sleep(6000);
        System.out.println(Thread.currentThread().getName()+"=============>" +(System.currentTimeMillis() / 1000));
    }


    /*
     * 默认是fixedRate，上一次执开始执行时间点之后5秒再执行
     * */
    @Scheduled(fixedDelay = 5000)
//    @Async
    public void run2() throws InterruptedException {
        Thread.sleep(6000);
        System.out.println(Thread.currentThread().getName()+"=============>run2:" +(System.currentTimeMillis() / 1000));
    }
}
