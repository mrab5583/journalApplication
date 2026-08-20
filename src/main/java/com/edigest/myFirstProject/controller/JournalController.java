package com.edigest.myFirstProject.controller;

import com.edigest.myFirstProject.entity.JournalEntry;
import com.edigest.myFirstProject.entity.User;
import com.edigest.myFirstProject.exception.ResourceNotFoundException;
import com.edigest.myFirstProject.service.JournalEntryService;
import com.edigest.myFirstProject.service.RateLimiterService;
import com.edigest.myFirstProject.service.RedisService;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalController {


    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RateLimiterService rateLimiterService;

    @GetMapping
    public ResponseEntity<?> getAllJournalEntriesForUser(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userfromSC = authentication.getName();
        System.out.println("Logged-in user from security context : " + userfromSC);

        User userFound = userService.findUserByUsername(userfromSC);
        if(userFound != null) {
            List<JournalEntry> journalEntries = userFound.getJournal_entries();
            if (journalEntries != null) {
                return new ResponseEntity<>(journalEntries, HttpStatus.OK);
            }
        }
        return  new ResponseEntity<>( HttpStatus.NOT_FOUND);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getSingleEntry(@PathVariable ObjectId id){

        String key = "rate_limt:id:" + id.toString();
        if(!rateLimiterService.isAllowed(key)){
            return ResponseEntity
                    .status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Rate limit exceeds");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        System.out.println("Logged-in user from security context : " + username);
        JournalEntry journalEntryFromCache = redisService.get("journal:" +id.toString(),JournalEntry.class);

        if(journalEntryFromCache != null){
            redisService.get(id.toString(), JournalEntry.class);
            return new ResponseEntity<>(journalEntryFromCache, HttpStatus.OK);
        }else {
            User userByUsername = userService.findUserByUsername(username);
            List<JournalEntry> listOfJournalEntries = userByUsername.getJournal_entries().stream().filter(x -> x.getId().equals(id)).collect(Collectors.toList());
            if (!listOfJournalEntries.isEmpty()) {
                Optional<JournalEntry> journalEntry = journalEntryService.findById(id);
                if (journalEntry.isPresent()) {
                    redisService.set("journal:" + id.toString(),journalEntry.get(), 3000L);
                    return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
                }
            }else{
                throw new ResourceNotFoundException("Resource NOT found for this id : " + id);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PostMapping
    public ResponseEntity<JournalEntry> createNewJournalEntry(@RequestBody JournalEntry journalEntry){
        try{
            journalEntry.setDate(LocalDateTime.now());
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            System.out.println("Logged-in user from security context : " + username);

            journalEntryService.addNewEntry(journalEntry,username);
            return new ResponseEntity<>(journalEntry, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/id/{id}")
    public ResponseEntity<JournalEntry> updateEntry(@PathVariable ObjectId id, @RequestBody JournalEntry newEntry){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        System.out.println("Logged-in user from security context : " + username);
        User userByUsername = userService.findUserByUsername(username);
        List<JournalEntry> listOfJournalEntries = userByUsername.getJournal_entries().stream().filter(x -> x.getId().equals(id)).collect(Collectors.toList());

        if(!listOfJournalEntries.isEmpty()) {
            JournalEntry oldEntry = journalEntryService.findById(id).orElse(null);
            if(oldEntry != null){
                oldEntry.setContent(newEntry.getContent()!= null & !newEntry.getContent().equals("")? newEntry.getContent() : oldEntry.getContent());
                oldEntry.setTitle(newEntry.getTitle()!= null & !newEntry.getTitle().equals("")? newEntry.getTitle() : oldEntry.getTitle());
                oldEntry.setLastUpdated(LocalDateTime.now());
                journalEntryService.addNewEntry(oldEntry,username);
                return new ResponseEntity<>(oldEntry, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>( HttpStatus.NOT_FOUND);
       }

       @DeleteMapping("/id/{id}")
        public ResponseEntity<?> deleteEntryForUser(@PathVariable ObjectId id){

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            System.out.println("Logged-in user from security context : " + username);
            journalEntryService.removeJournalEntry(id, username);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
       }
}
