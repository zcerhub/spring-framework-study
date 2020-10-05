package com.example.myspring.v3.aop.aspectj;

import com.example.myspring.beans.DefaultBeanFactory;
import com.example.myspring.v2.MagicGirl;
import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.ShadowMatch;
import org.aspectj.weaver.tools.TypePatternMatcher;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class AspectJTest {

    @Test
    public void pointcutParsse() throws Exception {
//        获取切点解析器
        PointcutParser pointcutParser = PointcutParser.
                getPointcutParserSupportingAllPrimitivesAndUsingContextClassloaderForResolution();
//        切点解析器根据规则，解析出一个类型匹配器
        TypePatternMatcher tpm = pointcutParser.parseTypePattern("v2.di..*");
//        根据表达式生成一个切点表达式
        PointcutExpression pe = pointcutParser.parsePointcutExpression(
                "execution(* com.example.myspring.beans.BeanFactory.get*(..))"
        );

//        匹配MagicGirl类的getName方法
        Class<?> cl = MagicGirl.class;
        Method aMethod = cl.getMethod("getName", null);
//        匹配方法执行
        ShadowMatch sm = pe.matchesMethodExecution(aMethod);
        System.out.println("是否匹配到方法执行：" + sm.alwaysMatches());

        System.out.println(cl.getName() + "是否匹配表达式：" + pe.couldMatchJoinPointsInType(cl));
        System.out.println(DefaultBeanFactory.class.getName() + "是否匹配表达式：" + pe.couldMatchJoinPointsInType(DefaultBeanFactory.class));

        System.out.println("\r\n" + cl.getName() + "下的方法有：");
        for (Method m : cl.getMethods()) {
            System.out.println(m.getName());
        }
    }

}
