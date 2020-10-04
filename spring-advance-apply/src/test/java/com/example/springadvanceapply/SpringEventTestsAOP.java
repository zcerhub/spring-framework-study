package com.example.springadvanceapply;

import com.example.springadvanceapply.aop.CustomerServiceImpl;
import com.example.springadvanceapply.aop.GoodsService;
import com.example.springadvanceapply.event.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SpringEventTestsAOP {

    @Autowired
    private CustomerServiceImpl customerService;

    @Test
    void test() {
        customerService.addAddress(12, "hash", "整个世界都是我的");
//        引用增强验证：customerService引入GoodService的功能
        System.out.println("【引入增强】实现：customerService引入GoodsService的功能");
        GoodsService goodsService = (GoodsService) customerService;
        goodsService.sendGoods("hash","送你整个世界");
    }

}
