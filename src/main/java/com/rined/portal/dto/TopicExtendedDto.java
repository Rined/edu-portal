package com.rined.portal.dto;

import com.rined.portal.model.Tag;
import com.rined.portal.repositories.projections.TopicInfoWithTags;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class TopicExtendedDto {

    private final Tag tag;

    private final List<TopicInfoWithTags> topicsWithTagsByTag;
}
