package com.rined.blog.repositories.projections;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@RequiredArgsConstructor
public class UserTopicBrief {

    private final String topicTitle;

    private final long topicVote;

    private final LocalDateTime topicDate;

}
