package com.user.userservice.utils;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class TokenUtil {
    private Object value;
    private int anInt;
    private Long timeUnit;
}
