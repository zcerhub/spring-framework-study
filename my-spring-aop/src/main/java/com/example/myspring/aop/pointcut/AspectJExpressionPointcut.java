package com.example.myspring.aop.pointcut;

import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.ShadowMatch;

import java.lang.reflect.Method;

/*
* AspectJ表达式切点实现类
* */
public class AspectJExpressionPointcut implements Pointcut{
    //    先获得切点解析器
    private static PointcutParser parser = PointcutParser.getPointcutParserSupportingAllPrimitivesAndUsingContextClassloaderForResolution();

//  切点表达式中的字符串形式
    private String expression;

//    aspectJ中的切点表达式实例pe
    private PointcutExpression pe;

    public AspectJExpressionPointcut(String expression) {
        super();
        this.expression = expression;
        pe = parser.parsePointcutExpression(expression);
    }
    @Override
    public boolean matchsClass(Class<?> targetClass) {
        return pe.couldMatchJoinPointsInType(targetClass);
    }

    @Override
    public boolean matchsMethod(Method method, Class<?> targetClass) {
        ShadowMatch sm = pe.matchesMethodExecution(method);
//        System.out.println("method="+method+","+sm.alwaysMatches()+","+expression);
        return sm.alwaysMatches();
    }

    public String getExpression() {
        return expression;
    }

}
