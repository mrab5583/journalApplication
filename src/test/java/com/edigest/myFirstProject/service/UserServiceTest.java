package com.edigest.myFirstProject.service;

import com.edigest.myFirstProject.entity.User;
import org.assertj.core.api.Assert;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {


    @Autowired
    private UserService userService;


    @Test
    public void test_emails(){
        User user = new User();
        user.setEmail("abhikad5583@gmail.com");
        user.setUsername("Shy");
        user.setId(new ObjectId("6890a0c3e4b0b0d365224578"));
        user.setPassword("Shanky");
        userService.addNewUsername(user);
    }
}

