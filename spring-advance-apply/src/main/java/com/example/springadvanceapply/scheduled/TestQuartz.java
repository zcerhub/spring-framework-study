package com.example.springadvanceapply.scheduled;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

/*
* 创建任务类TestQuartz，该类主要是继承QuartzJobBean
* */
public class TestQuartz extends QuartzJobBean {


    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        System.out.println("quartz task "+new Date());
    }
}
