package com.example.springadvanceapply.event;

import org.springframework.context.ApplicationEvent;

/*
* 支付状态更新的事件，以paymentInfo作为传输的载体
* */
public class PaymentStatusUpdateEvent extends ApplicationEvent {


    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public PaymentStatusUpdateEvent(PaymentInfo source) {
        super(source);
    }
}
