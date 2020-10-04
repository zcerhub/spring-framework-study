package com.example.myspring.beans;

public class GenericBeanDefinition implements BeanDefinition{

    private Class<?> beanClass;
    private String factoryMethodName;
    private String factoryBeanName;
    private String initMethod;
    private String destroyMethod;
    private String scope = BeanDefinition.SCOPE_SINGLETON;

    @Override
    public Class<?> getBeanClass() {
        return beanClass;
    }

    @Override
    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public String getScope() {
        return scope;
    }

    @Override
    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public boolean isSingleton() {
        return BeanDefinition.SCOPE_SINGLETON.equals(scope);
    }

    @Override
    public boolean isPrototype() {
        return BeanDefinition.SCOPE_PROTOTYPE.equals(scope);
    }

    @Override
    public String getFactoryMethodName() {
        return factoryMethodName;
    }

    @Override
    public void setFactoryMethodName(String factoryMethodName) {
        this.factoryMethodName = factoryMethodName;
    }

    @Override
    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    @Override
    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }

    @Override
    public void setInitMethod(String initMethod) {
        this.initMethod = initMethod;
    }

    @Override
    public String getInitMethod() {
        return initMethod;
    }

    @Override
    public void setDestroyMethod(String destroyMethod) {
        this.destroyMethod = destroyMethod;
    }

    @Override
    public String getDestoryMethod() {
        return destroyMethod;
    }

    @Override
    public String toString() {
        return "GenericBeanDefinition{" +
                "beanClass=" + beanClass +
                ", factoryMethodName='" + factoryMethodName + '\'' +
                ", factoryBeanName='" + factoryBeanName + '\'' +
                ", initMethod='" + initMethod + '\'' +
                ", destroyMethod='" + destroyMethod + '\'' +
                ", scope='" + scope + '\'' +
                '}';
    }
}
