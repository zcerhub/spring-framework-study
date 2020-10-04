package com.example.springadvanceapply.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailPaymentSatusUpdateListener extends AbstractPaymentSatusListener implements SmartApplicationListener {
    @Async
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        System.out.println("Thread:" + Thread.currentThread().getName()+
                "邮件服务，收到支付状态更新的通知。" + event);
    }

//    排序，数字越小执行的优先级越高
    @Override
    public int getOrder() {
        return 1;
    }
}
