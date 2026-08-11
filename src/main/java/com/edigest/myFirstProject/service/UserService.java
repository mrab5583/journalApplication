package com.edigest.myFirstProject.service;

import com.edigest.myFirstProject.entity.JournalEntry;
import com.edigest.myFirstProject.entity.User;
import com.edigest.myFirstProject.repository.JournalEntryRepo;
import com.edigest.myFirstProject.repository.UserRepo;
import org.bson.types.ObjectId;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Value("${spring.mail.welcome.subject}")
    private String subject ;

    @Autowired
    private EmailService emailService;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final Logger logger = (Logger) LoggerFactory.getLogger(UserService.class);

    public void addNewUser(User user){

        User newUser = userRepo.save(user);
        System.out.println("New User ADDED successfully");
    }

    public void addNewUsername(User user){
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER"));
            User newUser = userRepo.save(user);
            System.out.println("New User ADDED successfully");

            if(checkMailConfigured(newUser.getUsername())){ // Sending mails ONLY if email is configured
                emailService.sendMail(newUser.getEmail(), newUser.getUsername(),subject);
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }

    private boolean checkMailConfigured(String username){

        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        query.addCriteria(Criteria.where("email").ne(null));

        List<User> users = mongoTemplate.find(query, User.class);
        if(!users.isEmpty()){
            System.out.println("Users with email configured : " + List.of(users));
            return true;
        }
        return false;
    }

    public void addNewAdminUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER","ADMIN"));
        User newUser = userRepo.save(user);
        System.out.println("New ADMIN User ADDED successfully");
    }

    public List<User> getAllUsers(){
        return userRepo.findAll();
    }

    public User findUserByUsername(String username){

       return userRepo.findByUsername(username);
    }

    public void removeUserEntry(ObjectId id){
         userRepo.deleteById(id);
    }
}
