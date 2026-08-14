package com.edigest.myFirstProject.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper ;

    public <T> T get(String key, Class<T> entityClass) {
        try {
            Object obj = redisTemplate.opsForValue().get(key);
            if(obj != null){
                return objectMapper.readValue(obj.toString(), entityClass);
            }else {
                return null;
            }
        } catch (Exception e) {
            log.error("Exception while reading from Redis cache ", e);
            return null;
        }
    }

    public void set(String key, Object value, Long ttl) {
        try {
            String jsonValue = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, jsonValue, ttl, TimeUnit.SECONDS);
            log.info("Value cached successfully in cache. Key :{} , TTL: {} seconds",key,ttl);
        } catch (Exception e) {
            log.error("Exception while saving into Redis cache ", e);
        }
    }
}
