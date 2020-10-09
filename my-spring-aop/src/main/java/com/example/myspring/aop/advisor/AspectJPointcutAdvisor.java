package com.example.myspring.aop.advisor;

/*
* 基于aspectj表达式切点的统治者实现
* */

import com.example.myspring.aop.pointcut.AspectJExpressionPointcut;
import com.example.myspring.aop.pointcut.Pointcut;

public class AspectJPointcutAdvisor implements PointcutAdvisor{

//    通知bean名称
    private String adviceBeanName;

//    切点表达式
    private String expression;

    //    aspectJ切点实现
    private Pointcut pointcut;

    public AspectJPointcutAdvisor(String adviceBeanName, String expression) {
        this.adviceBeanName = adviceBeanName;
        this.expression = expression;
        this.pointcut = new AspectJExpressionPointcut(this.expression);
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public String getAdviceBeanName() {
        return this.adviceBeanName;
    }

    @Override
    public String getExpression() {
        return this.expression;
    }
}
