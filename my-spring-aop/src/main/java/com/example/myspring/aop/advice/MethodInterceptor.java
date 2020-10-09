package com.example.myspring.aop.advice;

import java.lang.reflect.Method;

/*
* 方法环绕增强接口
* */
public interface MethodInterceptor  extends  Advice{

    /*
     * 对方法进行环绕（前置、后置）增强，、异常处理，方法实现中需调用目标方法
     * method：被执行的方法
     * args：方法执行参数
     * target：执行方法的目标对象
     * */
    Object invoke(Method method, Object[] args, Object target) throws Throwable;

}
