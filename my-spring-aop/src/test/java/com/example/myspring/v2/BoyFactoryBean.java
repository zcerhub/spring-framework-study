package com.example.myspring.v2;

public class BoyFactoryBean {

    public Boy buildBoy(String name, MagicGirl girl) {
        return new Lad(name, girl);
    }

}
