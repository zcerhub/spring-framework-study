package com.example.myspring;

public class BoyFactoryBean {

    public Boy buildBoy() {
        return ()->{
            System.out.println("BoyFactoryBean buildBoy()");
        };
    }

}
