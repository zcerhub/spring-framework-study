package com.example.myspring.v2;

public class BoyFactory {

    public static Boy getBean(String name, Money money) {
        return new Lad(name, money);
    }

}
