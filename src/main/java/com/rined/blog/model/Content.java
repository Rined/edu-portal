package com.rined.blog.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Content {

    @Field("text")
    private String text;

}
