package com.edigest.myFirstProject.entity;

import lombok.Data;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "user")
@Data
public class User {

    @Id
    private ObjectId id;

    @NonNull
    @Indexed(unique = true)
    private String username ;

    @NonNull
    private String password;

    @DBRef
    private List<JournalEntry> journal_entries = new ArrayList<>();

    private List<String> roles;
}
