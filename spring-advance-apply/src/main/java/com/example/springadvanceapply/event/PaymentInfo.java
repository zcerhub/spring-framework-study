package com.example.springadvanceapply.event;

/*
* 支持实体类，将作为事件实体
* */
public class PaymentInfo {

    private int id;
    private String status;

    public PaymentInfo(int id, String status) {
        this.id = id;
        this.status = status;
    }
}
