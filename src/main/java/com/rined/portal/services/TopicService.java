package com.rined.portal.services;

import com.rined.portal.dto.TopicBriefDto;
import com.rined.portal.dto.TopicDto;
import com.rined.portal.dto.TopicExtendedDto;
import com.rined.portal.model.Topic;
import com.rined.portal.repositories.projections.TopicInfoWithTags;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface TopicService {

    TopicExtendedDto getTopicsByTag(String tag);

    Optional<Topic> topicByTitle(String title);

    Page<TopicInfoWithTags> getAllPageableTopics(int page, int numberOfElementsOnPage);

    void createTopic(TopicBriefDto topicBrief);

    TopicDto getTopicDtoByName(String topic);

    void update(TopicDto changedTopic);

    void deleteById(String id);
}
