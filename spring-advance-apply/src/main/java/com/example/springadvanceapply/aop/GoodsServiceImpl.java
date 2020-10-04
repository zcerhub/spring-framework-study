package com.example.springadvanceapply.aop;

import org.springframework.stereotype.Service;

@Service
public class GoodsServiceImpl implements GoodsService{
    @Override
    public void sendGoods(String userName, String goodsName) {
        System.out.println(String.format("给用户[%s]，完成[%s]的发货",userName,goodsName));
    }
}
