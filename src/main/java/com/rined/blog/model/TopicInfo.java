package com.rined.blog.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TopicInfo {

    @Field("title")
    private String title;

    @Field("keywords")
    private List<String> keywords;

    @DBRef
    @Field("tags")
    private List<Tag> tags;

    @DBRef
    @Field("author")
    private User author;
}
