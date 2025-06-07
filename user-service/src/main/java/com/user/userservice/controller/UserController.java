package com.user.userservice.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.commen.commenmodule.entity.Order;
import com.commen.commenmodule.util.LoginUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.userservice.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RefreshScope
@RequestMapping("/user")
public class UserController {
    @Value("${config.redisTimeout}")
    private int redisTimeout;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @GetMapping("/login")
    public String login() throws JsonProcessingException {
        //验证账号密码（省略）
        //jwt生成token
        String token = LoginUtil.getToken("123");
        tokenUtil.setValue(token);
        tokenUtil.setAnInt(redisTimeout);
        tokenUtil.setTimeUnit(System.currentTimeMillis());
        //将token保存进redis
        String json = new ObjectMapper().writeValueAsString(tokenUtil);
        //这个逼方法的序列化方式是JDK序列化！！！
        redisTemplate.opsForValue().set(token, json);
        //客户端返回token
        System.out.println(redisTemplate.opsForValue().get(token));
        return token;
    }
    //超时熔断测试
    @GetMapping("/test/timeout")
    public String testTimeout(){
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            System.out.println("捕获到异常"+ e.getMessage());
            throw new RuntimeException("接口异常，发生熔断",e);

        }
        return "慢调用比例熔断测试";
    }
    //热点限流测试与降级测试（blockHandler属性）
    @SentinelResource(value = "hotKey", blockHandler = "blockHandler")
    @GetMapping("/test/hotKey")
    public String hotKey(@RequestParam(value = "p1",required = false) String p1,@RequestParam(value = "p2",required = false) String p2){
        return "热点限流测试";
    }
    //不是接口，只是上面方法的额外功能->降级
    //方法名要与@SentinelResource注解中的blockHandler属性值一样
    public String blockHandler(String p1, String p2, BlockException e){
        return "hotkey,热点流控接口触发降级功能";
    }


    @PostMapping("updateUser")
    public String updateUser(){
        return "用户更新成功！";
    }

    @PostMapping("/add")
    public String addUser(@RequestBody Order order){
        return "调用adduser成功！商品名称为：" + order.getProductionName();
    }
}
