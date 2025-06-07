package com.commen.commenmodule.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
//token生成工具类
public class LoginUtil {
    public static final String USERNAME =  "admin";
    private static final String KEY = "token_key";
    public static final int TOKEN_TIME_OUT = 1000 * 60 ;

    public static String getToken(String userId){
        JWTCreator.Builder jwt = JWT.create();
        Map<String,Object> headers = new HashMap<>();

        headers.put("type","jwt");
        headers.put("alg","hs256");
        String token = jwt.withHeader(headers)
                .withClaim("userId",userId)
                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_TIME_OUT))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withIssuer(USERNAME)
                .sign(Algorithm.HMAC256(KEY));
        System.out.println(token);
        return token;
    }
    public static boolean verify(String token){
        return true;
    }
}
