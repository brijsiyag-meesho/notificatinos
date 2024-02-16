package com.meesho.notification.service.sms;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SmsRedisBlacklistCacheService {
    private final RedisTemplate<String, Boolean> redisTemplate;

    @Autowired
    public SmsRedisBlacklistCacheService(RedisTemplate<String, Boolean> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    public void set(String key, Boolean value){
        redisTemplate.opsForValue().set(key, value);
    }
    public Optional<Boolean> get(String key){
        return Optional.ofNullable(redisTemplate.opsForValue().get(key));
    }

    public void delete(String key){
        redisTemplate.delete(key);
    }
}
