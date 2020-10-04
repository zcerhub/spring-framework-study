package com.example.myspring.beans;


import org.apache.commons.lang3.StringUtils;

/*
* Bean定义，用来指定Bean构建信息接口
* */
public interface BeanDefinition {

    String SCOPE_SINGLETON = "singleton";
    String SCOPE_PROTOTYPE = "prototype";

    /*
     * 获取Bean的类名
     * */
    Class<?> getBeanClass();

    void setBeanClass(Class<?> beanClass);

    String getScope();
    void setScope(String scope);

    boolean isSingleton();
    boolean isPrototype();

    String getFactoryMethodName();

    void setFactoryMethodName(String factoryMethodName);

    String getFactoryBeanName();

    void setFactoryBeanName(String factoryBeanName);

    void setInitMethod(String initMethod);

    String getInitMethod();

    void setDestroyMethod(String destroyMethod);

    String getDestoryMethod();

    default boolean validate() {
//        没有beanclass信息，只能通过成员工厂来构建对象
        if (getBeanClass() == null) {
            if (!StringUtils.isNotBlank(getFactoryBeanName()) || !StringUtils.isNotBlank(getFactoryMethodName())) {
                return false;
            }
        }

        return true;
    }

}
