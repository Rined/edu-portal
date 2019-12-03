package com.rined.blog.repositories.projections;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
@RequiredArgsConstructor
public class TopicInfoBrief {

    private final String topicTitle;

    private final String authorName;

    private final String authorId;

    private final LocalDate topicDate;

}
