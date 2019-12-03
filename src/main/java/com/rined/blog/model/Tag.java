package com.rined.blog.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Setter
@Getter
@Document("tags")
@NoArgsConstructor
@AllArgsConstructor
public class Tag {

    @Id
    private String id;

    @Field("tag")
    private String tag;

    public Tag(String tag) {
        this.tag = tag;
    }
}
