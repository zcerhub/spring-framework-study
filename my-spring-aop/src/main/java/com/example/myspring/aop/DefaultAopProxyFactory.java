package com.example.myspring.aop;

import com.example.myspring.aop.advisor.Advisor;
import com.example.myspring.beans.BeanFactory;

import java.util.List;

public class DefaultAopProxyFactory implements AopProxyFactory {

    @Override
    public AopProxy createAopProxy(Object bean, String beanName, List<Advisor> matchAdvisors, BeanFactory beanFactory) {
//        是该用JDk动态代理换是cglib
        if (shouldJDKDynamicProxy(bean, beanName)) {
            return new JdkDynamicAopProxy(beanName, bean, matchAdvisors, beanFactory);
        }else{
            return new CglibDynamicAopProxy(beanName, bean, matchAdvisors, beanFactory);
        }
    }

    private boolean shouldJDKDynamicProxy(Object bean, String beanName) {
//        如何判断
//        这样可以吗：有实现接口就用JDK，没有就用cglib？
//        TODO
        return false;
    }
}
