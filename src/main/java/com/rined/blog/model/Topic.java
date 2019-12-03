package com.rined.blog.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
@Document("topic")
@NoArgsConstructor
@AllArgsConstructor
public class Topic {
    @Id
    private String id;

    @Field("info")
    private TopicInfo info;

    @Field("content")
    private Content content;

    @Field("comments")
    private List<Comment> comments;

    @Field("date")
    private LocalDateTime date;

    @Field("vote")
    private int vote;

    public Topic(TopicInfo info, Content content, LocalDateTime date, int vote, List<Comment> comments) {
        this.info = info;
        this.content = content;
        this.date = date;
        this.vote = vote;
        this.comments = comments;
    }

    public Topic(TopicInfo topicInfo, Content content) {
        this.info = topicInfo;
        this.content = content;
        this.date = LocalDateTime.now();
    }
}
