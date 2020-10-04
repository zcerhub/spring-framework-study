package com.example.myspring.beans;

import org.apache.commons.lang3.StringUtils;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultBeanFactory implements BeanDefinitionRegistry,BeanFactory, Closeable {

    private Map<String, BeanDefinition> bdMap = new ConcurrentHashMap<>();
    private Map<String, Object> beanMap = new ConcurrentHashMap<>();

    @Override
    public void registryBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionRegisterException {
        Objects.requireNonNull(beanName, "注册BeanDefinition时beanName不能为空");
        Objects.requireNonNull(beanDefinition, "注册BeanDefinition时beanDefinition不能为空");
        if (!beanDefinition.validate()) {
            throw new BeanDefinitionRegisterException("抱歉，该beanDefinition不合法"+beanDefinition);
        }
        bdMap.put(beanName, beanDefinition);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return bdMap.get(beanName);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return bdMap.containsKey(beanName);
    }

    @Override
    public Object getBean(String beanName) throws Exception {
        return doGetBean(beanName);
    }

    private Object doGetBean(String beanName) throws Exception {
        Objects.requireNonNull(beanName, "获取bean时beanName不能为空");

//        先去缓存里面判断一下beanName对应的对象已经创建好了
        Object bean = beanMap.get(beanName);
        if (bean!=null) {
            return bean;
        }

//        构建的方式有三种：构造函数、静态工厂、成员工厂
        BeanDefinition beanDefinition = bdMap.get(beanName);
        Objects.requireNonNull(beanDefinition, "找不到【" + beanName + "】的bean定义信息");
        Class<?> beanClass = beanDefinition.getBeanClass();
        if (beanClass != null) {
//            通过构造函数
            if (beanDefinition.getFactoryMethodName() == null) {
                bean = createBeanByConstructor(beanClass);
            }else{
//                通过静态工厂方式构建对象
                bean = createBeanByStaticFactoryMethod(beanClass, beanDefinition.getFactoryMethodName());
            }
        }else{
//            成员工厂方式构建对象
            bean = createBeanByFactoryMethod(beanDefinition.getFactoryBeanName(), beanDefinition.getFactoryMethodName());
        }

//        开始bean声明周期
        if (StringUtils.isNotBlank(beanDefinition.getInitMethod())) {
            invokeInitMethod(bean, beanDefinition);
        }

//        对单例bean的处理
        if (beanDefinition.isSingleton()) {
            beanMap.put(beanName, bean);
        }
        return bean;
    }

    private void invokeInitMethod(Object bean, BeanDefinition beanDefinition) throws Exception {
        Method method=bean.getClass().getMethod(beanDefinition.getInitMethod(), null);
        method.invoke(bean, null);
    }

    private Object createBeanByFactoryMethod(String factoryBeanName, String factoryMethodName) throws Exception {
        Object factoryBean=beanMap.get(factoryBeanName);
        Method method=factoryBean.getClass().getMethod(factoryMethodName, null);
        return method.invoke(factoryBean, null);
    }

    private Object createBeanByStaticFactoryMethod(Class<?> beanClass, String factoryMethodName) throws Exception {
        Method method =beanClass.getMethod(factoryMethodName, null);
        return method.invoke(beanClass, null);
    }

    private Object createBeanByConstructor(Class<?> beanClass) throws Exception {
        return beanClass.newInstance();
    }


    @Override
    public void close() throws IOException {
        bdMap.forEach((key,value)->{
            if (value.isSingleton()&& StringUtils.isNotBlank(value.getDestoryMethod())) {
                Object bean=beanMap.get(key);
                try {
                    Method method = bean.getClass().getMethod(value.getDestoryMethod(), null);
                    method.invoke(bean, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
