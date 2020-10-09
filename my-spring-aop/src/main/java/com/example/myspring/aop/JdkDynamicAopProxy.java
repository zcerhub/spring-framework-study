package com.example.myspring.aop;

import com.example.myspring.aop.advisor.Advisor;
import com.example.myspring.beans.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;


/*
* jdk动态代理实现
*
* */
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(JdkDynamicAopProxy.class);

//    bean名称
    private String beanName;
//    bean对象，需要被代理的对象
    private Object target;
//    通知列表，需要被增强的一些列功能
    private List<Advisor> matchAdvisors;
//    bean工厂
    private BeanFactory beanFactory;


    public JdkDynamicAopProxy(String beanName, Object target, List<Advisor> matchAdvisors, BeanFactory beanFactory) {
        this.beanName = beanName;
        this.target = target;
        this.matchAdvisors = matchAdvisors;
        this.beanFactory = beanFactory;
    }

    @Override
    public Object getProxy() {
        return this.getProxy(target.getClass().getClassLoader());
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("为"+target+"创建代理。");
        }
        return Proxy.newProxyInstance(classLoader, target.getClass().getInterfaces(), this);
    }

    /*
    * 进行dialing功能增强后的返回实例额结果
    * */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        调用代理增强
        return AopProxyUtils.applyAdvices(target, method, args, matchAdvisors, proxy, beanFactory);
    }
}
