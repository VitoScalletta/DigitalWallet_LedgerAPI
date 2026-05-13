package com.microcommerce.digitalwallet_ledgerapi.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class IdempotencyInterceptor implements HandlerInterceptor{
    private final StringRedisTemplate redisTemplate;
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {
        System.out.println("----> GÜMRÜK GÖREVLİSİ UYANDI, İSTEK GELDİ! <----");
        if(request.getMethod().equalsIgnoreCase("GET")){
            return true;
        }
        String idempotencyKey = request.getHeader("Idempotency-Key");

        if (idempotencyKey == null|| idempotencyKey.trim().isEmpty()) {
            throw new RuntimeException("Güvenlik ihlali: Idempotency-Key is null or empty");
        }

        String redisKey = "idempotencyKey : "+idempotencyKey;
        Boolean isFirstTime = redisTemplate.opsForValue()
                .setIfAbsent(redisKey,"PROCEED",24, TimeUnit.HOURS);

        if(Boolean.FALSE.equals(isFirstTime)){
            throw new RuntimeException("Çifte işlem engellendi");
        }

        return true;
    }
}
