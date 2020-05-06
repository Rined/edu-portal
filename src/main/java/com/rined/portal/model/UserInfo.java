package com.rined.portal.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    @Field("about")
    private String about;

    @Field("email")
    private String email;

    @Field("firstName")
    private String firstName;

    @Field("secondName")
    private String secondName;

}
