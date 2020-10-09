package com.example.myspring.aop;

import com.example.myspring.aop.advisor.Advisor;
import com.example.myspring.aop.advisor.AdvisorRegistry;
import com.example.myspring.aop.advisor.PointcutAdvisor;
import com.example.myspring.aop.pointcut.Pointcut;
import com.example.myspring.beans.BeanFactory;
import com.example.myspring.beans.BeanFactoryAware;
import com.example.myspring.beans.BeanPostProcessor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.*;

/*
* 功能增强的实现，用户和框架交互的核心类。
* 用户：通过Advisor提供方面，像DefaultBeanFactory注入改下该实现
* 框架内部：DefaultBeanFactory注入ioc容器
* DefaultBeanFactory调动BeanPostProcessor接口相关方法
* 进行功能增强。
*
* 实现：自动创建代理、通知者注册、Bean处理者、Bean工厂通知接口。
*
* */
public class AdvisorAutoProxyCreator implements AdvisorRegistry, BeanPostProcessor, BeanFactoryAware {

    //    通知者列表
    private List<Advisor> advisors;
    //    当前的bean工厂
    private BeanFactory beanFactory;

    public AdvisorAutoProxyCreator() {
        this.advisors = new ArrayList<>();
    }

//    注入通知器，用户定义得切面内容
    @Override
    public void registryAdvisor(Advisor advisor) {
        this.advisors.add(advisor);
    }

//    返回通知者列表
    @Override
    public List<Advisor> getAdvisort() {
        return this.advisors;
    }

//    aware接口的实现，获得Bean工厂实例
    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }


    /*
    * 通过AOPProxyFactory工厂去完成选择和创建代理对象的工厂
    * */
    private Object createProxy(Object bean, String beanName, List<Advisor> matchAdvisors) {
//        默认的代理工厂实现中获得代理工厂实现
        return AopProxyFactory.getDefaultAopProxyFactory()
//                根据参数信息，选择创建代理工厂具体实现
                .createAopProxy(bean, beanName, matchAdvisors, beanFactory)
//                从选择的代理工厂中，获得代理对象
                .getProxy();
    }

    private List<Advisor> getMatchAdvisors(Object bean, String beanName) {
        if (CollectionUtils.isEmpty(advisors)) {
            return null;
        }

//        得到类、所有的方法
        Class<?> beanClass = bean.getClass();
        List<Method> allMethods = this.getAllMethodForClass(beanClass);

//        存放匹配的Advisor的list
        List<Advisor> matchAdvisors = new ArrayList<>();
//        遍历Advisor来找匹配的
        for (Advisor ad : this.advisors) {
            if (ad instanceof PointcutAdvisor) {
                if (isPointcutMatchBean((PointcutAdvisor) ad, beanClass, allMethods)) {
                    matchAdvisors.add(ad);
                }
            }
        }
        return matchAdvisors;
    }

    /*
    * 判断制定了类
    * */
    private boolean isPointcutMatchBean(PointcutAdvisor pa, Class<?> beanClass, List<Method> allMethods) {
        Pointcut p = pa.getPointcut();

//        首先判断类是否匹配
        if (!p.matchsClass(beanClass)) {
            return false;
        }

//        在判断方法是否匹配
        for (Method method : allMethods) {
            if (p.matchsMethod(method, beanClass)) {
                return true;
            }
        }
        return false;
    }

    private List<Method> getAllMethodForClass(Class<?> beanClass) {
        List<Method> allMethods = new LinkedList<>();
        Set<Class<?>> classes = new LinkedHashSet<>(ClassUtils.getAllInterfacesForClassAsSet(beanClass));
        classes.add(beanClass);
        for (Class<?> clazz : classes) {
//            通过spring frameworkd提供的工具类找出所有方法，包括从父类继承而来的方法
            Method[] methods = ReflectionUtils.getAllDeclaredMethods(clazz);
            for (Method m : methods) {
                allMethods.add(m);
            }
        }
        return allMethods;
    }


    /*
     *
     * */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        //      在此判断bean是否需要进行切面增强，及获得增强的通知实现
        List<Advisor> matchAdvisors = getMatchAdvisors(bean, beanName);
//        如果需要进行增强，再返回增强对象
        if (CollectionUtils.isNotEmpty(matchAdvisors)) {
            bean = this.createProxy(bean, beanName, matchAdvisors);
        }
        return bean;
    }
}
