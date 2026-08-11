package com.edigest.myFirstProject.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendMail(String to, String username, String subject) throws MessagingException {

        try{

            Context context = new Context();
            context.setVariable("username", username);
            String htmlBody = templateEngine.process("welcome-email",context);

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            javaMailSender.send(message);

        } catch (Exception e) {
            log.error("Error occurred while sending the mail : ", e);
        }
    }
}
