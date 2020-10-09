package com.example.myspring.context;

import org.springframework.core.io.InputStreamSource;

import java.io.File;

/*
* 输入流资源扩展，资源接口
* */
public interface Resource extends InputStreamSource {

    //    classpath形式的xml配置文件
    String CLASS_PATH_PREFIX = "classpath:";
    //    系统文件形式的xml配置文件
    String FILE_SYSTEM_PREFIX = "file:";

    /*
     * 该资源是否存在
     * */
    boolean exists();

    /*
     * 是否可读
     * */
    boolean isReadable();

    /*
     * 是否打开
     * */
    boolean isOpen();

    /*
     * 资源文件
     * */
    File getFile();
}
