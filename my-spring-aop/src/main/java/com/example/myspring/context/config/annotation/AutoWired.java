package com.example.myspring.context.config.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.CONSTRUCTOR,ElementType.METHOD,ElementType.PARAMETER,ElementType.FIELD,
ElementType.ANNOTATION_TYPE})
public @interface AutoWired {

    boolean required() default true;
}
