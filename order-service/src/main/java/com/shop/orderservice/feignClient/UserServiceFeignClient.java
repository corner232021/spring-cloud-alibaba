package com.shop.orderservice.feignClient;

import com.commen.commenmodule.entity.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "point-service")
public interface UserServiceFeignClient {
    @PostMapping(value = "/point/add")
    String addUser(@RequestBody Order order);
}
