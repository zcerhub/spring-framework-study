package com.example.myspring.v3.aop.aspectj.advice;

import com.example.myspring.aop.advice.MethodInterceptor;

import java.lang.reflect.Method;

public class MyAroungAdvice implements MethodInterceptor {

    @Override
    public Object invoke(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("进入环绕通知的前");
        Object returnValue=method.invoke(target, args);
        System.out.println("进入环绕通知的后");
        return returnValue;
    }
}
