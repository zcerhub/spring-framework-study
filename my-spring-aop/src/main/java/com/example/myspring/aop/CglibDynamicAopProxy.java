package com.example.myspring.aop;

import com.example.myspring.aop.advisor.Advisor;
import com.example.myspring.beans.BeanDefinition;
import com.example.myspring.beans.BeanFactory;
import com.example.myspring.beans.DefaultBeanFactory;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

/*
* CGLIB动态代理
* */
public class CglibDynamicAopProxy implements AopProxy, MethodInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CglibDynamicAopProxy.class);
    private static Enhancer enhancer = new Enhancer();

    private String beanName;
    private Object target;

    private List<Advisor> matchAdvisors;

    private BeanFactory beanFactory;

    public CglibDynamicAopProxy(String beanName, Object target, List<Advisor> matchAdvisors, BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
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
            LOGGER.debug("为 "+target+"创建cglib代理");
        }
        Class<?> superClass = this.target.getClass();
        enhancer.setSuperclass(superClass);
        enhancer.setInterfaces(this.getClass().getInterfaces());
        enhancer.setCallback(this);
        Constructor<?> constructor = null;
        try {
            constructor = superClass.getConstructor(new Class[]{});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        if (constructor != null) {
            return enhancer.create();
        }else{
            BeanDefinition bd = ((DefaultBeanFactory) beanFactory).getBeanDefinition(beanName);
            return enhancer.create(bd.getConstructor().getParameterTypes(), bd.getConstructorArgumentRealValues());
        }
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        return AopProxyUtils.applyAdvices(target,method,args,matchAdvisors,proxy,beanFactory);
    }
}
