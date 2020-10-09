package com.example.myspring.context;

/*
* 将Resource资源解析成Bean定义的接口
* */
public interface BeanDefinitionReader {

    void loadBeanDefinition(Resource resource);

    void loadBeanDefinitions(Resource... resources);

}
