package com.example.myspring.context.config.annotation;

import com.example.myspring.beans.BeanDefinition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {

    String value() default "";
    String name() default "";
    String scope() default BeanDefinition.SCOPE_SINGLETON;

    String factoryMethodName() default "";
    String factoryBeanName() default "";
    String initMethodName() default "";
    String destoryMethodName() default "";
}
