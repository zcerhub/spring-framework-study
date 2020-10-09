package com.example.myspring.aop;

/*
* AOP代理接口，用来创建获得代理对象
* */
public interface AopProxy {

    Object getProxy();


    Object getProxy(ClassLoader classLoader);

}
