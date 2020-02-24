package com.rined.portal.repositories.projections;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@RequiredArgsConstructor
public class TopicBrief {

    private final String id;

    private final String title;

    private final String content;

    private final List<String> keywords;

    private final List<String> tags;

}
