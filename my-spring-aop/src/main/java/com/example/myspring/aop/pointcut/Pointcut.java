package com.example.myspring.aop.pointcut;

import java.lang.reflect.Method;

/*
* 切点抽象接口
* */
public interface Pointcut {

    /*
     * 匹配类
     * targetClass： 将被匹配的目标类
     *
     * */
    boolean matchsClass(Class<?> targetClass);


    /*
     * 匹配方法
     * targetClass： 将被匹配的目标类
     * method： 将被匹配的方法
     *
     * */
    boolean matchsMethod(Method method,Class<?> targetClass);

}
