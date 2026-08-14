package com.edigest.myFirstProject.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void testRedis(){

        redisTemplate.opsForValue().set("username","Jackson");

        Object username = redisTemplate.opsForValue().get("username");
        Assertions.assertNotNull(username);
        System.out.println(username);

        Assertions.assertNotNull(username);
        Assertions.assertEquals("Jackson", username);
    }

    @Test
    void testSendMail() {
        redisTemplate.opsForValue().set("email","gmail@email.com");
        Object salary = redisTemplate.opsForValue().get("salary");
    }
}
