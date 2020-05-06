package com.rined.portal.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Setter
@Getter
@ToString
@Document("user")
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("password")
    private String password;

    @Field("registrationDate")
    private LocalDate registrationDate;

    @Field("lastSeen")
    private LocalDateTime lastSeen;

    @Field("info")
    private UserInfo info;

    @Field("reputation")
    private int reputation;

    @Field("roles")
    private List<Role> roles;

    public User(String name) {
        this.name = name;
        this.registrationDate = LocalDate.now();
        this.lastSeen = LocalDateTime.now();
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        this.registrationDate = LocalDate.now();
        this.lastSeen = LocalDateTime.now();
    }

    public User(String name, LocalDate registrationDate, LocalDateTime lastSeen, int reputation) {
        this.name = name;
        this.registrationDate = registrationDate;
        this.lastSeen = lastSeen;
        this.reputation = reputation;
    }

    public User(String name, LocalDate registrationDate, LocalDateTime lastSeen, int reputation, String password) {
        this.name = name;
        this.registrationDate = registrationDate;
        this.lastSeen = lastSeen;
        this.reputation = reputation;
        this.password = password;
    }


    public User(String id, String name, LocalDate registrationDate, LocalDateTime lastSeen, UserInfo info, int reputation) {
        this.id = id;
        this.name = name;
        this.registrationDate = registrationDate;
        this.lastSeen = lastSeen;
        this.info = info;
        this.reputation = reputation;
    }

    public User(String name, LocalDate registrationDate, LocalDateTime lastSeen, UserInfo info, int reputation) {
        this.name = name;
        this.registrationDate = registrationDate;
        this.lastSeen = lastSeen;
        this.reputation = reputation;
        this.info = info;
    }

    public User(String name, LocalDate registrationDate, LocalDateTime lastSeen, UserInfo info, int reputation, String password) {
        this.name = name;
        this.registrationDate = registrationDate;
        this.lastSeen = lastSeen;
        this.reputation = reputation;
        this.info = info;
        this.password = password;
    }

    public User(String name,
                LocalDate registrationDate,
                LocalDateTime lastSeen,
                UserInfo info,
                int reputation,
                String password,
                List<Role> roles) {
        this.name = name;
        this.registrationDate = registrationDate;
        this.lastSeen = lastSeen;
        this.reputation = reputation;
        this.info = info;
        this.password = password;
        this.roles = roles;
    }

    public User(String name, UserInfo userInfo) {
        this.name = name;
        this.info = userInfo;
        this.registrationDate = LocalDate.now();
        this.lastSeen = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
