package com.example.springadvanceapply;

import com.example.springadvanceapply.event.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SpringEventTests {

    @Autowired
    private PaymentService service;

    @Test
    void test() {
        service.pay(1,"支付成功");
    }

}
