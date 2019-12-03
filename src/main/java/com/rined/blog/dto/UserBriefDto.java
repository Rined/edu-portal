package com.rined.blog.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserBriefDto {

    @NotNull
    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotNull
    @Email(message = "Need valid email address")
    @NotBlank(message = "Email is mandatory")
    private String email;

    private String about;

    private String firstName;

    private String secondName;

}
