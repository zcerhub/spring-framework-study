package com.example.myspring.v2;

public class RenminBi implements Money {
    @Override
    public void pay() {
        System.out.println("使用人民币进行支付");
    }
}
