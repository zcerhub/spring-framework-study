package com.example.myspring.v2;

import com.example.myspring.beans.BeanDefinitionRegisterException;
import com.example.myspring.beans.BeanReference;
import com.example.myspring.beans.GenericBeanDefinition;
import com.example.myspring.beans.PreBuildBeanFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class DITest {

    static PreBuildBeanFactory bf = new PreBuildBeanFactory();

    @Test
    public void testConstructorDI() throws Exception {
        GenericBeanDefinition bd = new GenericBeanDefinition();
        bd.setBeanClass(Lad.class);
        List<Object> args = new ArrayList<>();
        args.add("sunwukong");
        args.add(new BeanReference("magicGirl"));
        bd.setConstructorArgumentValues(args);
        bf.registryBeanDefinition("swk",bd);

        bd = new GenericBeanDefinition();
        bd.setBeanClass(MagicGirl.class);
        args = new ArrayList<>();
        args.add("baigujing");
        bd.setConstructorArgumentValues(args);
        bf.registryBeanDefinition("magicGirl",bd);

        bf.preInstantiateSingletons();

        Lad abean = (Lad) bf.getBean("swk");
        abean.sayLove();
    }

    @Test
    public void testStaticFactoryMethod() throws Exception {
        GenericBeanDefinition bd = new GenericBeanDefinition();
        bd.setBeanClass(BoyFactory.class);
        bd.setFactoryMethodName("getBean");
        List<Object> factoryMethodArgs = new ArrayList<>();
        factoryMethodArgs.add("悟空");
        factoryMethodArgs.add(new BeanReference("renminbi"));
        bd.setConstructorArgumentValues(factoryMethodArgs);
        bf.registryBeanDefinition("staticFactoryMethod", bd);

        GenericBeanDefinition renminBiBd = new GenericBeanDefinition();
        renminBiBd.setBeanClass(RenminBi.class);
        bf.registryBeanDefinition("renminbi",renminBiBd);

        Boy boy= (Boy) bf.getBean("staticFactoryMethod");
        boy.play();
    }


    @Test
    public void testFactoryBeanMethod() throws Exception{
        GenericBeanDefinition factoryBeanDb = new GenericBeanDefinition();
        factoryBeanDb.setBeanClass(BoyFactoryBean.class);
        bf.registryBeanDefinition("factoryBean",factoryBeanDb);

        GenericBeanDefinition beanBd = new GenericBeanDefinition();
        beanBd.setFactoryBeanName("factoryBean");
        beanBd.setFactoryMethodName("buildBoy");
        List<Object> args = new ArrayList<>();
        args.add("通过工厂实例产生bean");
        args.add(new BeanReference("xiaolongnv"));
        beanBd.setConstructorArgumentValues(args);
        bf.registryBeanDefinition("boyBean", beanBd);

        GenericBeanDefinition xiaolongnvBd = new GenericBeanDefinition();
        xiaolongnvBd.setBeanClass(MagicGirl.class);
        List<Object> constructorArg = new ArrayList<>();
        constructorArg.add("xiaolongnv");
        xiaolongnvBd.setConstructorArgumentValues(constructorArg);
        bf.registryBeanDefinition("xiaolongnv", xiaolongnvBd);
        bf.preInstantiateSingletons();

        Boy boy= (Boy) bf.getBean("boyBean");
        boy.sayLove();
    }


}
