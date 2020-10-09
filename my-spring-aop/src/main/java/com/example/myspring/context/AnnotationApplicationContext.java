package com.example.myspring.context;

/*
* 注解配置方式的ApplicationContext实现
* */
public class AnnotationApplicationContext extends AbstractApplicationContext{

    private ClassPathBeanDefinitionScanner scanner;

    @Override
    public Resource getResource(String location) {
        return null;
    }
}
