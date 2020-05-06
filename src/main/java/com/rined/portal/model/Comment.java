package com.rined.portal.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @DBRef
    @Field("author")
    private User author;

    @Field("text")
    private String text;

    @Field("date")
    private LocalDateTime dateTime;

    @Field("vote")
    private long vote;

}
