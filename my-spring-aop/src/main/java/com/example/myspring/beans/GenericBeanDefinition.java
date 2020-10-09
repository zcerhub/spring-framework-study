package com.example.myspring.beans;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

public class GenericBeanDefinition implements BeanDefinition{

    private Class<?> beanClass;
    private String factoryMethodName;
    private String factoryBeanName;
    private String initMethod;
    private String destroyMethod;
    private String scope = BeanDefinition.SCOPE_SINGLETON;
//    保存构造参数的实参值
    private List<?> constructorArgumentValues;
//    缓存选中的构造函数
    private Constructor constructor;
//    缓存取得的工厂方法
    private Method factoryMethod;
//  保存属性值
    private List<PropertyValue> propertyValues;
    private ThreadLocal<Object[]> realConstructorArgumentValues = new ThreadLocal<>();

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
    public List<?> getConstructorArgumentValues() {
        return constructorArgumentValues;
    }

    @Override
    public void setConstructorArgumentValues(List<?> constructorArgumentValues) {
        this.constructorArgumentValues = constructorArgumentValues;
    }

    @Override
    public Constructor getConstructor() {
        return constructor;
    }

    @Override
    public void setConstructor(Constructor constructor) {
        this.constructor = constructor;
    }

    @Override
    public Method getFactoryMethod() {
        return factoryMethod;
    }

    @Override
    public void setFactoryMethod(Method method) {
        this.factoryMethod = method;
    }

    @Override
    public List<PropertyValue> getPropertyValues() {
        return propertyValues;
    }

    @Override
    public void setPropertyValues(List<PropertyValue> propertyValues) {
        this.propertyValues = propertyValues;
    }

    @Override
    public Object[] getConstructorArgumentRealValues() {
        return realConstructorArgumentValues.get();
    }

    @Override
    public void setConstructorArgumentRealValues(Object[] values) {
            realConstructorArgumentValues.set(values);
    }

}
