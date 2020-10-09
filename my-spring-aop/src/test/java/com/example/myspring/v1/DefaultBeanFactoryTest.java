package com.example.myspring.v1;

import com.example.myspring.beans.BeanDefinition;
import com.example.myspring.beans.DefaultBeanFactory;
import com.example.myspring.beans.GenericBeanDefinition;
import org.junit.jupiter.api.Test;

public class DefaultBeanFactoryTest {

    static DefaultBeanFactory defaultBeanFactory = new DefaultBeanFactory();

    @Test
    public void testRegist() throws Exception {
        GenericBeanDefinition bd = new GenericBeanDefinition();
        bd.setBeanClass(Lad.class);
        bd.setScope(BeanDefinition.SCOPE_SINGLETON);
        bd.setInitMethod("init");
        bd.setDestroyMethod("destroy");
        defaultBeanFactory.registryBeanDefinition("lad",bd);

        System.out.println("构造方法------------");
        for (int i = 0; i < 3; i++) {
            Boy boy = (Boy) defaultBeanFactory.getBean("lad");
            boy.sayLove();
        }
    }


    @Test
    public void testFactoryStaticMethod() throws Exception {
        GenericBeanDefinition bd = new GenericBeanDefinition();
        bd.setBeanClass(BoyFactory.class);
        bd.setFactoryMethodName("getBean");
        bd.setScope(BeanDefinition.SCOPE_PROTOTYPE);
//        bd.setScope(BeanDefinition.SCOPE_SINGLETON);
        bd.setInitMethod("init");
        bd.setDestroyMethod("destroy");
        defaultBeanFactory.registryBeanDefinition("lad",bd);

        System.out.println("静态工厂方法------------");
        for (int i = 0; i < 3; i++) {
            Boy boy = (Boy) defaultBeanFactory.getBean("lad");
            boy.sayLove();
        }
    }

    @Test
    public void testFactoryBeanMethod() throws Exception {
        GenericBeanDefinition bd = new GenericBeanDefinition();
        bd.setBeanClass(BoyFactoryBean.class);
        bd.setScope(BeanDefinition.SCOPE_SINGLETON);
        defaultBeanFactory.registryBeanDefinition("boyFactoryBean",bd);
//        加载工厂bean
        defaultBeanFactory.getBean("boyFactoryBean");
        GenericBeanDefinition boyBeanDefinition = new GenericBeanDefinition();
        boyBeanDefinition.setFactoryBeanName("boyFactoryBean");
        boyBeanDefinition.setFactoryMethodName("buildBoy");
        boyBeanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
        defaultBeanFactory.registryBeanDefinition("lad",boyBeanDefinition);

        System.out.println("工厂bean方法------------");
        for (int i = 0; i < 3; i++) {
            Boy boy = (Boy) defaultBeanFactory.getBean("lad");
            boy.sayLove();
        }
    }

}
