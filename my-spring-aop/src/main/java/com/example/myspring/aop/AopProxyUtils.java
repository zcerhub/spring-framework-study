package com.example.myspring.aop;

import com.example.myspring.aop.advisor.Advisor;
import com.example.myspring.aop.advisor.PointcutAdvisor;
import com.example.myspring.beans.BeanFactory;
import org.apache.commons.collections4.CollectionUtils;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/*
* AOP代理工具类
* */
public class AopProxyUtils {

    /*
    * 对方法应用advices增强，获得最终返回结果
    * */
    public static Object applyAdvices(Object target, Method method, Object[] args, List<Advisor> matchAdvisors, Object proxy, BeanFactory beanFactory) throws Throwable {
//        1.获取要对当前方法
        List<Object> advices = AopProxyUtils.getShouldApplyAdvices(target.getClass(), method, matchAdvisors, beanFactory);
//        2如有增强的advice，责任链式增强执行
        if (CollectionUtils.isEmpty(advices)) {
            return method.invoke(target, args);
        }else{
//            责任链式执行
            AopAdviceChainInvocation chain = new AopAdviceChainInvocation(proxy, target,method, args, advices);
            return chain.invoke();
        }
    }

    /*
    * 获取与方法匹配的切面的advice Bean对象列表
    * */
    public static List<Object> getShouldApplyAdvices(Class<?> beanClass, Method method, List<Advisor> matchAdvisors, BeanFactory beanFactory) throws Exception {
        if (CollectionUtils.isEmpty(matchAdvisors)) {
            return null;
        }
        List<Object> advices = new ArrayList<>();
        for (Advisor ad : matchAdvisors) {
            if (ad instanceof PointcutAdvisor) {
                if (((PointcutAdvisor) ad).getPointcut().matchsMethod(method, beanClass)) {
                    advices.add(beanFactory.getBean(ad.getAdviceBeanName()));
                }
            }
        }
        return advices;
    }


}
