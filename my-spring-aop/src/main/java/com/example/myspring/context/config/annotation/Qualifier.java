package com.example.myspring.context.config.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Target({ElementType.FIELD,ElementType.TYPE,
        ElementType.METHOD,ElementType.PARAMETER,ElementType.ANNOTATION_TYPE})
public @interface Qualifier {

    String value() default "";
}
