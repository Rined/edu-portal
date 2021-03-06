package com.rined.portal.converters;

import com.rined.portal.dto.TopicBriefDto;
import com.rined.portal.dto.TopicDto;
import com.rined.portal.model.*;
import com.rined.portal.repositories.TagRepository;
import com.rined.portal.repositories.projections.TopicBrief;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class TopicConverterImpl implements TopicConverter {
    private final TagRepository tagRepository;

    @Override
    public TopicDto briefToDto(TopicBrief brief) {
        return new TopicDto(
                brief.getId(),
                brief.getTitle(),
                String.join(", ", brief.getKeywords()),
                String.join(", ", brief.getTags()),
                brief.getContent()
        );
    }

    @Override
    public Topic createFrom(TopicDto changedTopic, Topic oldTopic) {
        List<Tag> tagList = getTagsFromString(changedTopic.getTags());
        List<String> keywordList = getKeyWordsFromString(changedTopic.getKeywords());

        return new Topic(
                oldTopic.getId(),
                new TopicInfo(
                        changedTopic.getTitle(),
                        keywordList,
                        tagList,
                        oldTopic.getInfo().getAuthor()
                ),
                new Content(changedTopic.getContent()),
                oldTopic.getComments(),
                oldTopic.getDate(),
                oldTopic.getVote()
        );
    }

    @Override
    public Topic createFrom(TopicBriefDto topicBrief, User author) {
        String title = topicBrief.getTitle();

        List<Tag> tagList = getTagsFromString(topicBrief.getTags());
        List<String> keywordList = getKeyWordsFromString(topicBrief.getKeywords());

        TopicInfo topicInfo = new TopicInfo(title, keywordList, tagList, author);
        return new Topic(topicInfo, new Content(topicBrief.getContent()));
    }


    private List<Tag> getTagsFromString(String tags) {
        List<Tag> tagList = new ArrayList<>();
        for (String tag : tags.split(",")) {
            String trimmedTag = tag.trim();
            if (tagRepository.existsByTag(trimmedTag)) {
                tagList.add(tagRepository.getByTag(trimmedTag));
                continue;
            }
            tagList.add(new Tag(trimmedTag));
        }
        return tagList;
    }

    private List<String> getKeyWordsFromString(String keywords) {
        return Stream.of(keywords.split(","))
                .map(String::trim).collect(Collectors.toList());
    }
}
