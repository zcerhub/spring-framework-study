package com.example.myspring.context;

import com.example.myspring.beans.BeanFactory;
import com.example.myspring.beans.BeanPostProcessor;
import com.example.myspring.beans.PreBuildBeanFactory;

/*
* ApplicationContext抽象实现类
* */
public abstract class AbstractApplicationContext implements ApplicationContext{

    //    用组合的方式持有beanFactory，完成beanFactory接口的方法
    protected BeanFactory beanFactory;

    public AbstractApplicationContext() {
        super();
//        换可以通过构造函数传递一个beanFactory的实现
        this.beanFactory = new PreBuildBeanFactory();
    }

    public AbstractApplicationContext(BeanFactory beanFactory) {
        super();
        this.beanFactory = beanFactory;
    }

    @Override
    public Object getBean(String beanName) throws Exception {
        return beanFactory.getBean(beanName);
    }

    @Override
    public void registryBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        this.beanFactory.registryBeanPostProcessor(beanPostProcessor);
    }


}
