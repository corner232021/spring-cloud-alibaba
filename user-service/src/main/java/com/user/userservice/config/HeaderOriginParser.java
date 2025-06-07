package com.user.userservice.config;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

//未被授权访问本包接口的请求会被拦截,这里我只设置了网关服务请求白名单
//所以http://localhost:9999/user/login能够访问
//http://localhost:8081/user/login直接访问接口就不行
@Component
public class HeaderOriginParser implements RequestOriginParser {
    @Override
    public String parseOrigin(HttpServletRequest httpServletRequest) {
        String origin = httpServletRequest.getHeader("origin");
        if (StringUtils.isEmpty(origin)){
            return "空白头";
        }
        return origin;
    }
}
