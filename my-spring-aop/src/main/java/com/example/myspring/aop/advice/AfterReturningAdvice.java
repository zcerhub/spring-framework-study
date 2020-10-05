package com.example.myspring.aop.advice;

import java.lang.reflect.Method;

/*
* 后置增强接口
* */
public interface AfterReturningAdvice  extends  Advice{

    /*
     * 方法执行完毕后的增强接口
     * returnValue:方法执行完毕的返回值
     * method：被执行的方法
     * args：方法执行参数
     * target：执行方法的对象
     * */
    void afterReturing(Object returnValue, Method method, Object[] args, Object target);

}
