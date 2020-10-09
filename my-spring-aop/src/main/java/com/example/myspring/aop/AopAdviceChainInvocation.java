package com.example.myspring.aop;


import com.example.myspring.aop.advice.AfterReturningAdvice;
import com.example.myspring.aop.advice.MethodBeforeAdvice;
import com.example.myspring.aop.advice.MethodInterceptor;

import java.lang.reflect.Method;
import java.util.List;

/*
*
* AOP通知链调用类
*
* */
public class AopAdviceChainInvocation {

//    AOP调用链执行方法
    private static Method invokeMethod;

    static {
        try {
            invokeMethod = AopAdviceChainInvocation.class.getMethod("invoke", null);
        } catch (NoSuchMethodException | SecurityException exception) {
            exception.printStackTrace();
        }
    }

//    代理类对象
    private Object proxy;
//    目标类对象
    private Object target;
//    调用执行的对象方法
    private Method method;
//    执行方法的参数
    private Object[] args;
//    方法被增强的功能-通知列表
    private List<Object> advices;

    public AopAdviceChainInvocation(Object proxy, Object target, Method method,
                                    Object[] args, List<Object> advices) {
        this.proxy = proxy;
        this.target = target;
        this.method = method;
        this.args = args;
        this.advices = advices;
    }

//    责任链执行记录索引号
    private int i=0;

    public Object invoke() throws Throwable {
        if (i < this.advices.size()) {
            Object advice = this.advices.get(i++);
            if (advice instanceof MethodBeforeAdvice) {
//                执行前置增强
                ((MethodBeforeAdvice) advice).before(method, args, target);
            } else if (advice instanceof MethodInterceptor) {
//                执行环绕增强和异常处理增强，注意这里给如的method和对象时invoke方法和链对象
                return ((MethodInterceptor) advice).invoke(invokeMethod, null, this);
            } else if (advice instanceof AfterReturningAdvice) {
//                当时后置增强时，先得到结果，再执行后置增强逻辑
                Object returnValue = this.invoke();
                ((AfterReturningAdvice) advice).afterReturing(returnValue, method, args, target);
                return returnValue;
            }
            return this.invoke();//回调，遍历完advices列表
        }else{
            return method.invoke(target, args);
        }
    }

}
