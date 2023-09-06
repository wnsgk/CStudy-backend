package com.CStudy.global.redis;

import com.nimbusds.openid.connect.sdk.federation.policy.operations.ValueOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    public void setValues(String token) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(token, token, Duration.ofDays(7));
    }

    public void setValues(String key, String value, Duration duration) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, value, duration);
    }

    public String getValues(String key) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(key);
    }

    public void delValues(String token) {
        redisTemplate.delete(token);
    }
}