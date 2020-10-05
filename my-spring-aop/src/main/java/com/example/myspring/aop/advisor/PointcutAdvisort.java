package com.example.myspring.aop.advisor;

import com.example.myspring.aop.pointcut.Pointcut;

/*
* 切点通知者，继承自Advisor扩展了Pointcut
* */
public interface PointcutAdvisort extends Advisor {

    Pointcut getPointcut();

}
