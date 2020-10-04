package com.example.springadvanceapply.aop;

/*
* 织入增强的方式触发先后顺序：
* BeforeAround --》 Before --》 ProcessAround --》 After --》 AfterReturning --》 AfterThrowing
*
*
* 用代码描述
* 前置增强
* try{
*       //TODO 业务方法
*        //最终增强
* }catch(Exception e){
*       e.printStackTrace();
* // 异常增强
* }finnaly{
*   //后置增强
*}
*
* */

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Aspect
@Component
public class CustomerAspect {

    /*
     * 前置增强
     * */
    @Before("execution(public * com.example.springadvanceapply.aop.*.*(..))")
    public void before(JoinPoint joinpoint) {
        Map params = AopHelper.getMethodParams(joinpoint);
        params.forEach((key, value) -> {
            if (Objects.isNull(value)) {
                throw new RuntimeException("参数" + key + "不能为空");
            }
        });
        System.out.println("【前置增强】" + joinpoint.getTarget().getClass().getName() + "." + joinpoint.getSignature().getName());
    }

    /*
     * 最终增强
     * */
    @After("target(com.example.springadvanceapply.aop.CustomerServiceImpl)")
    public void after(JoinPoint joinpoint) {
        Map params = AopHelper.getMethodParams(joinpoint);
        params.forEach((key, value) -> {
            if (Objects.isNull(value)) {
                throw new RuntimeException("参数" + key + "不能为空");
            }
        });
        System.out.println("【后置增强】" + joinpoint.getTarget().getClass().getName() + "." + joinpoint.getSignature().getName());
    }

    /*
     * 环绕增强，比前置增强更加强大，能够通过procceed调用被代理对象方法获取执行结果
     * */
    @Around("@annotation(com.example.springadvanceapply.aop.AroundTag)")
    public Object around(ProceedingJoinPoint joinpoint) throws Throwable {
        System.out.println("【进入环绕增强】执行代理方法前" + joinpoint.getTarget().getClass().getName() + "." + joinpoint.getSignature().getName());
        long s = System.currentTimeMillis();
        Object result = null;
        result = joinpoint.proceed();
        System.out.println("【进入环绕增强】执行代理方法后" + joinpoint.getTarget().getClass().getName() + "." + joinpoint.getSignature().getName() + ",总共用时：" + (System.currentTimeMillis() - s));
        return result;
    }

    /*
     * 后置增强。和最终增强类似，在业务方法执行后执行
     * 区别：
     * 1.执行顺序在最终执行的后面  afterReturning---》After
     * 2.程序发生异常，后置增强不会发生，最终异常会正常执行
     * */
    @AfterReturning("execution(public * com.example.springadvanceapply.aop.*.*(..))")
    public void afterReturning(JoinPoint joinpoint) {
        Map params = AopHelper.getMethodParams(joinpoint);
        params.forEach((key, value) -> {
            if (Objects.isNull(value)) {
                throw new RuntimeException("参数" + key + "不能为空");
            }
        });
        System.out.println("【afterReturning增强】" + joinpoint.getTarget().getClass().getName() + "." + joinpoint.getSignature().getName());
    }

    /*
     * 异常增强
     * */
    @AfterThrowing(value = "execution(public * com.example.springadvanceapply.aop.*.*(..))", throwing = "e")
    public void afterThrowing(JoinPoint joinpoint, Exception e) {
        Map params = AopHelper.getMethodParams(joinpoint);
        params.forEach((key, value) -> {
            if (Objects.isNull(value)) {
                throw new RuntimeException("参数" + key + "不能为空");
            }
        });
        System.out.println("【AfterThrowing增强】" + joinpoint.getTarget().getClass().getName() + "." + joinpoint.getSignature().getName() + "\n异常信息为：" + e.getMessage());
    }

    /*
     * 引入增强，在不修改代码的情况下，将一个已经代理的类，引入新的方法
     * 这里将CustomerServiceImpl引入GoodsService的功能
     * “+”表示CustomService的所有子类；defaultImpl表示默认需要添加的新的类
     * */
    @DeclareParents(value = "com.example.springadvanceapply.aop.CustomerServiceImpl", defaultImpl = GoodsServiceImpl.class)
    public GoodsService goodsService;
}
