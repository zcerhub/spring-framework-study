package com.example.myspring.v3.aop.aspectj.advice;

import com.example.myspring.aop.advice.MethodBeforeAdvice;

import java.lang.reflect.Method;

public class MyBeforeAdvice implements MethodBeforeAdvice {
    @Override
    public void before(Method method, Object[] args, Object target) {
        System.out.println(this+" 对"+target+" 进行了前置增强！");
    }
}
