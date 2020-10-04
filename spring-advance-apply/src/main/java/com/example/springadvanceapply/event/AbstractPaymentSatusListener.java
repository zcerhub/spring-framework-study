package com.example.springadvanceapply.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;

/*
* 有序监听器，抽象类实现事件源以及事件的通用判断
*
* */
public abstract class AbstractPaymentSatusListener implements SmartApplicationListener {

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return eventType==PaymentStatusUpdateEvent.class;
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return sourceType==PaymentInfo.class;
    }


}
