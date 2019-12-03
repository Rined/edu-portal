package com.rined.blog.dto;

import com.rined.blog.model.Tag;
import com.rined.blog.repositories.projections.TopicInfoWithTags;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class TopicExtendedDto {

    private final Tag tag;

    private final List<TopicInfoWithTags> topicsWithTagsByTag;
}
