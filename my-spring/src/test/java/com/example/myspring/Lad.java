package com.example.myspring;

public class Lad implements Boy {
    @Override
    public void sayLove() {
        System.out.println("i love you ,my dear"+hashCode());
    }

    public void init() {
        System.out.println("lad init method");
    }

    public void destroy() {
        System.out.println("lad destroy method");
    }

}
