package com.example.myspring.v3.aop.aspectj;

import com.example.myspring.aop.AdvisorAutoProxyCreator;
import com.example.myspring.aop.advisor.AspectJPointcutAdvisor;
import com.example.myspring.beans.*;
import com.example.myspring.v2.Boy;
import com.example.myspring.v2.Lad;
import com.example.myspring.v2.MagicGirl;
import com.example.myspring.v2.RenminBi;
import com.example.myspring.v3.aop.aspectj.advice.MyAfterReturingAdvice;
import com.example.myspring.v3.aop.aspectj.advice.MyAroungAdvice;
import com.example.myspring.v3.aop.aspectj.advice.MyBeforeAdvice;
import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.ShadowMatch;
import org.aspectj.weaver.tools.TypePatternMatcher;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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

    static PreBuildBeanFactory bf = new PreBuildBeanFactory();

    @Test
    public void testAdvice() throws Exception {

        GenericBeanDefinition bd = new GenericBeanDefinition();
        bd.setBeanClass(Lad.class);
        List<Object> args = new ArrayList<>();
        args.add("sunwukong");
        args.add(new BeanReference("baigujing"));
        bd.setConstructorArgumentValues(args);
        bf.registryBeanDefinition("swk", bd);

        bd = new GenericBeanDefinition();
        bd.setBeanClass(MagicGirl.class);
        args = new ArrayList<>();
        args.add("baigujing");
        bd.setConstructorArgumentValues(args);
        bf.registryBeanDefinition("baigujing", bd);

        bd = new GenericBeanDefinition();
        bd.setBeanClass(RenminBi.class);
        bf.registryBeanDefinition("renminbi", bd);

//        前置增强advice bean注册
        bd = new GenericBeanDefinition();
        bd.setBeanClass(MyBeforeAdvice.class);
        bf.registryBeanDefinition("myBeforeAdvice", bd);

        //        环绕增强advice bean注册
        bd = new GenericBeanDefinition();
        bd.setBeanClass(MyAroungAdvice.class);
        bf.registryBeanDefinition("myAroungAdvice", bd);

//        后置增强advice bean注册
        bd = new GenericBeanDefinition();
        bd.setBeanClass(MyAfterReturingAdvice.class);
        bf.registryBeanDefinition("myAfterReturingAdvice", bd);

//        往BeanFactory中注册AOP的BeanPostProcessor
        AdvisorAutoProxyCreator aaps = new AdvisorAutoProxyCreator();
        bf.registryBeanPostProcessor(aaps);
//        向AdvisorAutoProxyCreator注册Advisor
        aaps.registryAdvisor(new AspectJPointcutAdvisor("myBeforeAdvice",
                "execution(* com.example.myspring.v2.MagicGirl.*(..))"));
        aaps.registryAdvisor(new AspectJPointcutAdvisor("myAroungAdvice",
                "execution(* com.example.myspring.v2.Lad.say*(..))"));
       /* aaps.registryAdvisor(new AspectJPointcutAdvisor("myAfterReturingAdvice",
                "execution(* com.example.myspring.v2.RenminBi.*(..))"));*/
        aaps.registryAdvisor(new AspectJPointcutAdvisor("myAfterReturingAdvice",
                "execution(public void com.example.myspring.v2.RenminBi.pay())"));
//        bf.preInstantiateSingletons();

        System.out.println("--------------------------myBeforeAdvice----------------------");
        MagicGirl girl = (MagicGirl) bf.getBean("baigujing");
        girl.getFriend();
        girl.getName();

        System.out.println("---------------------------myMethodInterceptor----------------");
        Boy boy = (Boy) bf.getBean("swk");
        boy.sayLove();

        System.out.println("---------------------------myAfterReturningAdvice");
        RenminBi rmb = (RenminBi) bf.getBean("renminbi");
        rmb.pay();
    }

}
