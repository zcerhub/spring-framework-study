package com.example.myspring.beans;

/*
* Bean定义注册接口，用来完成Bean定义和Bean工厂之间沟通
* */
public interface BeanDefinitionRegistry {

    /*
     * Bean工厂，注册Bean定义信息
     * */
    void registryBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionRegisterException;

    /*
     * 获取已经注册的BeanDefinition
     * */
    BeanDefinition getBeanDefinition(String beanName);

    /*
     * 是否包含了已经定义了beanName
     * */
    boolean containsBeanDefinition(String beanName);
}
