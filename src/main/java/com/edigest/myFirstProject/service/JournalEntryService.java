package com.edigest.myFirstProject.service;

import com.edigest.myFirstProject.entity.JournalEntry;
import com.edigest.myFirstProject.entity.User;
import com.edigest.myFirstProject.repository.JournalEntryRepo;
import com.edigest.myFirstProject.repository.UserRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepo journalEntryRepo;

    @Autowired
    private UserRepo userRepo;

    @Transactional
    public void addNewEntry(JournalEntry journalEntry,String username){
        User userFound = userRepo.findByUsername(username);
        journalEntry.setDate(LocalDateTime.now());
        JournalEntry saved = journalEntryRepo.save(journalEntry);

        userFound.getJournal_entries().add(saved);
        userRepo.save(userFound);
        System.out.println("Entry saved successfully");
    }

    public List<JournalEntry> getAllEntires(){
        return journalEntryRepo.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id){

       return journalEntryRepo.findById(id);
    }

    @Transactional
    public void removeJournalEntry(ObjectId id,String username){
        try {
            User userFound = userRepo.findByUsername(username);
            boolean removed = userFound.getJournal_entries().removeIf(x -> x.getId().equals(id));
            if(removed) {
                userRepo.save(userFound);
                journalEntryRepo.deleteById(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error occurred while deleting the journal entry");
        }
    }
}
