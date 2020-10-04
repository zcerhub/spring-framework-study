package com.example.springadvanceapply.event;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class PaymentService implements InitializingBean {

    @Autowired
    ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        System.out.println("@PostConstruct");
    }

    public void pay(int id, String status) {
//        ... 省略处理业务逻辑代码
        PaymentInfo paymentInfo = new PaymentInfo(id, status);
//        发布事件
        applicationContext.publishEvent(new PaymentStatusUpdateEvent(paymentInfo));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("afterPropertiesSet");
    }

}
