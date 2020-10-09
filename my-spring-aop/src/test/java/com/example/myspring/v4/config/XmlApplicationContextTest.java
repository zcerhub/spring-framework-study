package com.example.myspring.v4.config;

import com.example.myspring.context.ApplicationContext;
import com.example.myspring.context.XmlApplicationContext;
import com.example.myspring.v4.Boy;

import java.io.IOException;

public class XmlApplicationContextTest {

    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext = new XmlApplicationContext("classpath:/application.xml");
        Boy boy= (Boy) applicationContext.getBean("goodBoy");
        boy.sayLove();
    }

}
