package com.example.myspring.aop;

import com.example.myspring.aop.advisor.Advisor;
import com.example.myspring.beans.BeanFactory;

import java.util.List;

/*
* AOP代理接口的工厂模式接口
* */
public interface AopProxyFactory {

    /*
     * 根据参数获得AOP代理接口的实现
     * */
    AopProxy createAopProxy(Object bean, String beanName, List<Advisor> matchAdvisors, BeanFactory beanFactory);

    /*
     * 获得默认的AopProxyFactory实例
     *
     * */
    static AopProxyFactory getDefaultAopProxyFactory() {
        return new DefaultAopProxyFactory();
    }
}
