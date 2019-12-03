package com.rined.blog.services;

import com.rined.blog.converters.TopicConverter;
import com.rined.blog.dto.TopicBriefDto;
import com.rined.blog.dto.TopicDto;
import com.rined.blog.dto.TopicExtendedDto;
import com.rined.blog.exceptions.AlreadyExistException;
import com.rined.blog.exceptions.NotFoundException;
import com.rined.blog.model.Tag;
import com.rined.blog.model.Topic;
import com.rined.blog.repositories.TagRepository;
import com.rined.blog.repositories.TopicRepository;
import com.rined.blog.repositories.projections.TopicBrief;
import com.rined.blog.repositories.projections.TopicInfoWithTags;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {
    private final TopicRepository topicRepository;
    private final TagRepository tagRepository;
    private final TopicConverter converter;

    public TopicExtendedDto getTopicsByTag(String tag) {
        if (!tagRepository.existsByTag(tag)) {
            throw new NotFoundException(String.format("Tag %s not found!", tag));
        }
        Tag byTag = tagRepository.getByTag(tag);
        List<TopicInfoWithTags> topicsWithTagsByTag = topicRepository.getTopicsWithTagsByTag(tag);
        return new TopicExtendedDto(byTag, topicsWithTagsByTag);
    }

    public Optional<Topic> topicByTitle(String title) {
        if (!topicRepository.existsByInfoTitle(title)) {
            throw new NotFoundException(String.format("Title %s not found!", title));
        }
        return topicRepository.findTopicByInfoTitle(title);
    }

    public Page<TopicInfoWithTags> getAllPageableTopics(int page, int numberOfElementsOnPage) {
        long count = topicRepository.count();
        if ((count < page * numberOfElementsOnPage) || page < 0)
            throw new NotFoundException("Page %d not found", page);
        Pageable requestPageable = PageRequest.of(page, numberOfElementsOnPage, Sort.by("vote").descending());
        return topicRepository.findPageableAll(requestPageable, count);
    }

    public void createTopic(TopicBriefDto topicBrief) {
        String title = topicBrief.getTitle();
        if (topicRepository.existsByInfoTitle(title)) {
            throw new AlreadyExistException(String.format("Topic with name %s already exists", title));
        }
        Topic newTopic = converter.createFrom(topicBrief);
        topicRepository.save(newTopic);
    }

    public TopicDto getTopicDtoByName(String topic) {
        TopicBrief topicBriefByTag = topicRepository.findTopicBriefByTopicName(topic);
        return converter.briefToDto(topicBriefByTag);
    }

    public void update(TopicDto changedTopic) {
        String id = changedTopic.getId();
        boolean existedId = topicRepository.existsById(id);
        if (!existedId) {
            throw new NotFoundException(String.format("Topic with id \"%s\" not found", id));
        }
        Topic oldTopic = topicRepository.getById(id);
        Topic topic = converter.createFrom(changedTopic, oldTopic);
        topicRepository.save(topic);
    }

    public void deleteById(String id) {
        topicRepository.deleteById(id);
    }

}
