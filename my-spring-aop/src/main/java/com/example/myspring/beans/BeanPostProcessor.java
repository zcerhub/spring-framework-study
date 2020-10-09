package com.example.myspring.beans;

/*
* 后置处理器
* Bean实例化完毕后及依赖注入完成后触发。
* */
public interface BeanPostProcessor {

    /*
     * bean初始化前的处理
     * */
    default Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    /*
     * bean初始化后的处理
     * */
    default Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }
}
