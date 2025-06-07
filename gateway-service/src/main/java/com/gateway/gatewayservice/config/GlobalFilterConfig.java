package com.gateway.gatewayservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class GlobalFilterConfig implements GlobalFilter, Ordered {

    @Autowired
    private StringRedisTemplate template;

    private static final String HEADER_NAME = "access_token";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("============拦截器=============");
        //获取请求对象
        ServerHttpRequest request = exchange.getRequest();
        //获取相应对象
        ServerHttpResponse response = exchange.getResponse();
        //获取请求地址
        String url = request.getURI().getPath();
        //获取token信息
        String token = request.getHeaders().getFirst(HEADER_NAME);
        //判断是否为白名单请求以及不需要检验的请求（包括登录请求）
        //如果当前请求中包含token信息的令牌不为空，则验证令牌合法性，这能保证中的用户信息被正常访问
        //如果token令牌为空，则过滤非白名单请求
        //白名单请求的接口直接转发，但无法获取到用户的token数据
        if(this.shouldNotFilter(url)){
            return chain.filter(exchange);
        }
        //验证token不为空且redis中存在该token
        if (StringUtils.isEmpty(token)){
            return unAuthorize(exchange);
        }
        if (!template.hasKey(token)){
            return unAuthorize(exchange);
        }
        //验证通过将exchange放回过滤链中
        ServerHttpRequest newRequest = request.mutate().header(HEADER_NAME,token).build();
        ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
        return chain.filter(newExchange);

    }

    //判断是否为白名单接口（是则放行）
    private boolean shouldNotFilter(String url) {
//        return url.startsWith("/point/testGateWay") ||
//                url.startsWith("/user/login");
        return true;
    }
    //返回token不存在提示,登录失败
    private Mono<Void> unAuthorize(ServerWebExchange exchange){
        //设置状态吗为401
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        //返回信息为json类型
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        //定义错误信息
        String ereorMsg = "{\"error\":\"" + "用户登录超时，请重新登录" + "\"}";
        //将错误信息写入响应体
        return exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(ereorMsg.getBytes())));
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
