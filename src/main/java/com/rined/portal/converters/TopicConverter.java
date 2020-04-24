package com.rined.portal.converters;

import com.rined.portal.dto.TopicBriefDto;
import com.rined.portal.dto.TopicDto;
import com.rined.portal.model.Topic;
import com.rined.portal.model.User;
import com.rined.portal.repositories.projections.TopicBrief;

public interface TopicConverter {

    TopicDto briefToDto(TopicBrief brief);

    Topic createFrom(TopicDto changedTopic, Topic oldTopic);

    Topic createFrom(TopicBriefDto briefDto, User author);
}
