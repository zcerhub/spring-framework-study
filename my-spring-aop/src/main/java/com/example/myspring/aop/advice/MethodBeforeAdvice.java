package com.example.myspring.aop.advice;

import java.lang.reflect.Method;

/*
* 前置增强接口
* */
public interface MethodBeforeAdvice extends  Advice{

    /*
     * 前置增强方法
     * method：被执行的方法
     * args：方法执行参数
     * target：执行方法的目标对象
     * */
    void afterReturing(Object returnValue, Method method, Object[] args, Object target);

}
