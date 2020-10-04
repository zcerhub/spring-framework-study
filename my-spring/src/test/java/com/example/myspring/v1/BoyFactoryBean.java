package com.example.myspring.v1;

public class BoyFactoryBean {

    public Boy buildBoy() {
        return ()->{
            System.out.println("BoyFactoryBean buildBoy()");
        };
    }

}
