package com.example.myspring.context;

import java.io.IOException;
import java.net.MalformedURLException;

/*
* 配置资源加载接口
* 不同的配置文件，有不同的加载过程，故需要抽象一个接口来拥抱变化的部分
* 虽然记载方式不同，但是返回的资源结果是一样的Resource
* */
public interface ResouceLoader {

    /*
    * 资源加载
    * */
    Resource getResource(String location) throws IOException;
}
