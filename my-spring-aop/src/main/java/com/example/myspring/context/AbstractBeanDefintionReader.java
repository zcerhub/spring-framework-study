package com.example.myspring.context;

import com.example.myspring.beans.BeanDefinitionRegistry;

/*
* BeanDefinitionReader抽象实现
* 解读出来后的BeanDefinition最终需要注册到BeanFactory中，通过BeanDefinitionRegistry接口来完成
* */
public abstract class AbstractBeanDefintionReader implements BeanDefinitionReader{

    //    持有BeanDefinitionRegistry接口实例，以便完成注册到BeanFactory中
    protected BeanDefinitionRegistry registry;

    public AbstractBeanDefintionReader(BeanDefinitionRegistry registry) {
        super();
        this.registry = registry;
    }

}
