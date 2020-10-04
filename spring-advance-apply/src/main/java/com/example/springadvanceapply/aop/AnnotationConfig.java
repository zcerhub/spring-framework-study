package com.example.springadvanceapply.aop;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

//在spring容器中能注入已装载的bean
@Configuration
@ComponentScan
@EnableAspectJAutoProxy
public class AnnotationConfig {
}
