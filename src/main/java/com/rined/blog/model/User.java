package com.rined.blog.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@ToString
@Setter
@Document("user")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("registrationDate")
    private LocalDate registrationDate;

    @Field("lastSeen")
    private LocalDateTime lastSeen;

    @Field("info")
    private UserInfo info;

    @Field("reputation")
    private int reputation;

    public User(String name) {
        this.name = name;
        this.registrationDate = LocalDate.now();
        this.lastSeen = LocalDateTime.now();
    }

    public User(String name, LocalDate registrationDate, LocalDateTime lastSeen, int reputation) {
        this.name = name;
        this.registrationDate = registrationDate;
        this.lastSeen = lastSeen;
        this.reputation = reputation;
    }

    public User(String name, LocalDate registrationDate, LocalDateTime lastSeen, UserInfo info, int reputation) {
        this.name = name;
        this.registrationDate = registrationDate;
        this.lastSeen = lastSeen;
        this.reputation = reputation;
        this.info = info;
    }

    public User(String name, UserInfo userInfo) {
        this.name = name;
        this.info = userInfo;
        this.registrationDate = LocalDate.now();
        this.lastSeen = LocalDateTime.now();
    }
}
