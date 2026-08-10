package com.edigest.myFirstProject.entity;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Document(collection = "journal") // If not mentioned, it will create new collection : "journalentry"
@Data
public class JournalEntry {

    @Id
    private ObjectId id ;

    private String title ;

    private String content ;

    private LocalDateTime date ;

    private LocalDateTime lastUpdated;


}
