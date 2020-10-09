package com.example.myspring.context;

import com.example.myspring.beans.BeanFactory;

/*
* 用来构建整个应用的环境接口，用来完成Bean的配置解析。
* 1.为减少用户对框架类接口的依赖，扩展BeanFactory接口，这样Bean配置和Bean的获取都能够通过ApplicationContext接口蓝完成
* 2.配置资源有xml和annotation两种方式。存在xml和annotation的两种类型的子类实现
* 3.Bean配置解析首先需要加载，故实现了配置资源Resource的记载接口ResourceLoader
* */
public interface ApplicationContext extends ResouceLoader, BeanFactory {
}
