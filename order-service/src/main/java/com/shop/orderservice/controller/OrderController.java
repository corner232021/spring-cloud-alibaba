package com.shop.orderservice.controller;

import com.commen.commenmodule.entity.Order;
import com.shop.orderservice.feignClient.UserServiceFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
@RequestMapping("/order")
public class OrderController {

    @Value("${config.info}")
    private String config;

    @Autowired
    private UserServiceFeignClient userServiceFeignClient;

    @GetMapping("/test")
    public String test(){
        return "欢迎使用订单系统！";
    }
    @RequestMapping("/nacos/test/getConfigInfo")
    public String getConfigInfo(){
        return "nacos接口测试成功！" + "/"
                 + config;
    }
    @PostMapping("/add")
    public String addOrder(){
        Order order = new Order();
        order.setId("123");
        order.setProductionName("都行！");
        return userServiceFeignClient.addUser(order);
    }
}
