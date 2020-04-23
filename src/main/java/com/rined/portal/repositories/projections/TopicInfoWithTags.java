package com.rined.portal.repositories.projections;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
@RequiredArgsConstructor
public class TopicInfoWithTags {

    private final String topicTitle;

    private final long vote;

    private final List<String> tags;

    private final String authorName;

    private final String authorId;

}