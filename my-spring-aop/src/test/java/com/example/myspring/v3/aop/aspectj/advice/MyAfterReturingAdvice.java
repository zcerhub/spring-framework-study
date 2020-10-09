package com.example.myspring.v3.aop.aspectj.advice;

import com.example.myspring.aop.advice.AfterReturningAdvice;

import java.lang.reflect.Method;

public class MyAfterReturingAdvice implements AfterReturningAdvice {
    @Override
    public void afterReturing(Object returnValue, Method method, Object[] args, Object target) {
        System.out.println(this+" 对 "+target+" 做了后置增强，得到的返回值="+returnValue);
    }
}
