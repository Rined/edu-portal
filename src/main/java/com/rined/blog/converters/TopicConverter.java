package com.rined.blog.converters;

import com.rined.blog.dto.TopicBriefDto;
import com.rined.blog.dto.TopicDto;
import com.rined.blog.model.Topic;
import com.rined.blog.repositories.projections.TopicBrief;

public interface TopicConverter {

    TopicDto briefToDto(TopicBrief brief);

    Topic createFrom(TopicDto changedTopic, Topic oldTopic);

    Topic createFrom(TopicBriefDto briefDto);
}
