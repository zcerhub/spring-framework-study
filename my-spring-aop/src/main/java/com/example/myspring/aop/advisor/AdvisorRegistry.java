package com.example.myspring.aop.advisor;

import com.example.myspring.aop.advice.Advice;

import java.util.List;

/*
* 通知者注册接口
* */
public interface AdvisorRegistry {

    /*
     * 注册通知器
     * */
    void registryAdvisor(Advisor advisor);

    /*
     * 获得通知者列表
     * */
    List<Advisor> getAdvisort();

}
