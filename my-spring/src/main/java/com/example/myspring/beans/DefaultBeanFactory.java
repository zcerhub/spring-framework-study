package com.example.myspring.beans;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.Provider;
import java.util.*;
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

    public Object doGetBean(String beanName) throws Exception {
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
                bean = createBeanByConstructor(beanDefinition);
            }else{
//                通过静态工厂方式构建对象
                bean = createBeanByStaticFactoryMethod(beanClass, beanDefinition);
            }
        }else{
//            成员工厂方式构建对象
            bean = createBeanByFactoryMethod(beanDefinition);
        }

//        开始bean声明周期
        if (StringUtils.isNotBlank(beanDefinition.getInitMethod())) {
            invokeInitMethod(bean, beanDefinition);
        }

//        完成属性注入
        setPropertyValueDIValues(beanDefinition, bean);

//        对单例bean的处理
        if (beanDefinition.isSingleton()) {
            beanMap.put(beanName, bean);
        }
        return bean;
    }

    private void setPropertyValueDIValues(BeanDefinition beanDefinition, Object bean) throws Exception {
        if (CollectionUtils.isEmpty(beanDefinition.getPropertyValues())) {
            return;
        }
        for (PropertyValue pv : beanDefinition.getPropertyValues()) {
            if (StringUtils.isBlank(pv.getName())) {
                continue;
            }
            Field field=bean.getClass().getDeclaredField(pv.getName());
            field.setAccessible(true);
            Object rv = pv.getValue();
            Object v=null;
            if (rv == null) {
                v=null;
            } else if (rv instanceof BeanReference) {
                v = doGetBean(((BeanReference) rv).getBeanName());
            } else if (rv instanceof Object[]) {
                // TODO 处理集合中的bean引用
            } else if (rv instanceof Collection) {
                // TODO 处理集合中的bean引用
            } else if (rv instanceof Properties) {
                // TODO 处理properties中的bean引用
            } else if (rv instanceof Map) {
                // TODO 处理Map中的bean引用
            } else {
                v = rv;
            }
            field.set(bean,v);
        }
    }


    private void invokeInitMethod(Object bean, BeanDefinition beanDefinition) throws Exception {
        Method method=bean.getClass().getMethod(beanDefinition.getInitMethod(), null);
        method.invoke(bean, null);
    }

    private Object createBeanByFactoryMethod(BeanDefinition beanDefinition) throws Exception {
        Object factoryBean=beanMap.get(beanDefinition.getFactoryBeanName());
        Object[] realArgs = getRealValues(beanDefinition.getConstructorArgumentValues());
        Method method = determinStaticFactoryMethod(beanDefinition, realArgs,factoryBean.getClass());
        return method.invoke(factoryBean, realArgs);
    }


    private Object createBeanByStaticFactoryMethod(Class<?> beanClass, BeanDefinition bd) throws Exception {
        Class<?> type=bd.getBeanClass();
        Object[] realArgs = getRealValues(bd.getConstructorArgumentValues());
        Method method = determinStaticFactoryMethod(bd, realArgs, type);
        return method.invoke(beanClass, realArgs);
    }

    private Method determinStaticFactoryMethod(BeanDefinition bd, Object[] realArgs, Class<?> type) throws Exception {
        if (type == null) {
            type = bd.getBeanClass();
        }
        String methodName = bd.getFactoryMethodName();
        if (realArgs == null) {
            return type.getMethod(methodName, null);
        }
//        对应原型bean，从第二次开始获取bean实例时可直接获得对此缓存的构造方法
        Method factoryMethod = bd.getFactoryMethod();
        if (factoryMethod != null) {
            return factoryMethod;
        }
//      根据参数类型获取精确匹配的方法
        Class[] paramTypeFactoryMethod=new Class[realArgs.length];
        for (int i = 0; i < realArgs.length; i++) {
            paramTypeFactoryMethod[i] = realArgs[i].getClass();
        }
        try {
            factoryMethod = type.getMethod(bd.getFactoryMethodName(), paramTypeFactoryMethod);
        } catch (NoSuchMethodException e) {
            factoryMethod=null;
        }
        if (factoryMethod == null) {
//            遍历所有的工厂方法
            Method[] candidateMethods = bd.getBeanClass().getMethods();
        outer:  for (Method cadidateMethod : candidateMethods) {
//                先判断个数是否符合，
                Class<?>[] paramTypeFactoryMethods = cadidateMethod.getParameterTypes();
                if (paramTypeFactoryMethods.length != realArgs.length) {
                    continue outer;
                }
            for (int i = 0; i < realArgs.length; i++) {
                if (!paramTypeFactoryMethods[i].isAssignableFrom(realArgs[i].getClass())) {
                    continue outer;
                }
            }
            factoryMethod = cadidateMethod;
            break outer;
            }
        }
        if (factoryMethod != null) {
            if (bd.isPrototype()) {
//                将获取到的工厂方法放入到缓存中
                bd.setFactoryMethod(factoryMethod);
            }
        }
        return factoryMethod;
    }

    private Object createBeanByConstructor(BeanDefinition beanDefinition) throws Exception {
        Object instance = null;
        if (CollectionUtils.isEmpty(beanDefinition.getConstructorArgumentValues())) {
            instance=beanDefinition.getBeanClass().newInstance();
        }else{
            Object[] args = getConstructorArgumentValues(beanDefinition);
            if (args == null) {
                instance = beanDefinition.getBeanClass().newInstance();
            }else{
                instance = determinConstructor(beanDefinition, args).newInstance(args);
            }
        }
        return instance;
    }

    private Constructor determinConstructor(BeanDefinition beanDefinition, Object[] args) throws Exception {
        if (args == null) {
            return beanDefinition.getBeanClass().getConstructor(null);
        }
//        对于原型Bean，从第二次开始获取Bean实例时，可以直接从第一次缓存中获取构造方法
        Constructor constructor = beanDefinition.getConstructor();

//        获取参数的类型
        Class[] paramTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            paramTypes[i] = args[i].getClass();
        }
        constructor = beanDefinition.getBeanClass().getConstructor(paramTypes);

//        如果不能直接匹配，则遍历所有的构造函数进行匹配
        if (constructor == null) {
            Constructor[] constructors = beanDefinition.getBeanClass().getConstructors();
//            判断逻辑，先通过参数个数进行匹配，然后再通过参数类型进行匹配
            outer:    for (Constructor ct : constructors) {
                Class[] paramterTypesConstrutor = ct.getParameterTypes();
//                参数个数不一样，跳过
                if (paramterTypesConstrutor.length != args.length) {
                    continue;
                }
                for (int i = 0; i < args.length; i++) {
                    if (!paramterTypesConstrutor[i].isAssignableFrom(args[i].getClass())) {
                        continue outer;
                    }
                }
                constructor=ct;
            }
        }

        if (constructor != null) {
            if (!beanDefinition.isSingleton()) {
                beanDefinition.setConstructor(constructor);
            }
        }else{
            throw new Exception("抱歉，找不到对应的构造函数");
        }
        return constructor;
    }

    private Object[] getConstructorArgumentValues(BeanDefinition beanDefinition) throws Exception {
        List<?> args = beanDefinition.getConstructorArgumentValues();
        return getRealValues(args);
    }

    private Object[] getRealValues(List<?> args) throws Exception {
        if (CollectionUtils.isEmpty(args)) {
            return null;
        }
        Object v=null;
        Object[] values = new Object[args.size()];
        for (int i = 0; i < values.length; i++) {
            Object rv = args.get(i);
            if (rv == null) {
                v=rv;
            } else if (rv instanceof BeanReference) {
                v = doGetBean( ((BeanReference) rv).getBeanName());
            } else if (rv instanceof Collection) {
//                TODO 处理集合中的BeanReference
            }else if (rv instanceof Properties) {
//                TODO 处理Properties中的BeanReference
            }else if (rv instanceof Map) {
//                TODO 处理Map中的BeanReference
            }else if (rv instanceof Object[]) {
//                TODO 处理Object[]中的BeanReference
            }else {
                v=rv;
            }
            values[i]=v;
        }
        return values;
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
