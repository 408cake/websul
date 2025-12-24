package com.web.bookstore.auth;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RefreshTokenService {

    private final StringRedisTemplate redis;

    public RefreshTokenService(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public void save(Long userId, String jti, long ttlSeconds) {
        String key = key(userId, jti);
        redis.opsForValue().set(key, "1", Duration.ofSeconds(ttlSeconds));
    }

    public boolean exists(Long userId, String jti) {
        return Boolean.TRUE.equals(redis.hasKey(key(userId, jti)));
    }

    public void delete(Long userId, String jti) {
        redis.delete(key(userId, jti));
    }

    private String key(Long userId, String jti) {
        return "rt:" + userId + ":" + jti;
    }
}
