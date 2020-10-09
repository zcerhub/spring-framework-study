package com.example.myspring.aop.advisor;

/*
* 用户构建切面的接口，组合Advice和Pointcut
* */
public interface Advisor {

    /*
     * 通知bean的名称
     * */
    String getAdviceBeanName();

    /*
     * 表达式
     * */
    String getExpression();

}
