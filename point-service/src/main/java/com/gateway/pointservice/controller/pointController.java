package com.gateway.pointservice.controller;

import com.commen.commenmodule.entity.Order;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

@RestController
@RefreshScope
@RequestMapping("/point")
public class pointController {

    @PostMapping("updatePoint")
    public String updateUser(){
        return "点击量更新成功！";
    }

    @PostMapping("/add")
    public String 调用addPoint成功(@RequestBody Order order){
        return "调用addPoint成功2222！商品名称为：" + order.getProductionName();
    }
}
