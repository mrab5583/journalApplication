package com.edigest.myFirstProject.service;

import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Value("${spring.mail.welcome.subject}")
    private String subject ;

    @Test
    void testSendMail() throws MessagingException {
        emailService.sendMail("abhikad5583@gmail.com","Shy",subject);
    }
}
