package com.edigest.myFirstProject.controller;

import com.edigest.myFirstProject.entity.JournalEntry;
import com.edigest.myFirstProject.entity.User;
import com.edigest.myFirstProject.repository.UserRepo;
import com.edigest.myFirstProject.service.JournalEntryService;
import com.edigest.myFirstProject.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;


    @PutMapping
    public ResponseEntity<User> updateEntry(@RequestBody User newUser){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        System.out.println("Logged-in user from security context : " + username);
        User oldUser = userService.findUserByUsername(username);

        if(oldUser != null){
            oldUser.setUsername(oldUser.getUsername()!= null & !oldUser.getUsername().equals("")? newUser.getUsername() : oldUser.getUsername());
            oldUser.setPassword(oldUser.getPassword()!= null & !oldUser.getPassword().equals("")? newUser.getPassword() : oldUser.getPassword());
            userService.addNewUsername(oldUser);
            return new ResponseEntity<>(oldUser, HttpStatus.OK);
        }
        return new ResponseEntity<>( HttpStatus.NOT_FOUND);
       }

       @DeleteMapping
        public ResponseEntity<?> deleteUserById(){

           Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
           userRepo.deleteByUsername(authentication.getName());
           return new ResponseEntity<>(HttpStatus.NO_CONTENT);
       }
}
