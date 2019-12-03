package com.rined.blog.services;

import com.rined.blog.dto.TopicBriefDto;
import com.rined.blog.dto.TopicDto;
import com.rined.blog.dto.TopicExtendedDto;
import com.rined.blog.model.Topic;
import com.rined.blog.repositories.projections.TopicInfoWithTags;
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
