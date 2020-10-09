package com.example.myspring.context;

import java.io.InputStream;

/*
* 配置方式的最终同意结果接口
* */
public interface InputStreamSource {
    /*
    * 最终需要获得的是输入流
    * */
    InputStream getInputStream();
}
