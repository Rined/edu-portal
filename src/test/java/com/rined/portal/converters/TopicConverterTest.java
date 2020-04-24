package com.rined.portal.converters;

import com.rined.portal.dto.TopicBriefDto;
import com.rined.portal.dto.TopicDto;
import com.rined.portal.model.*;
import com.rined.portal.repositories.TagRepository;
import com.rined.portal.repositories.UserRepository;
import com.rined.portal.repositories.projections.TopicBrief;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@Import({TopicConverterImpl.class})
@ExtendWith(SpringExtension.class)
@DisplayName("TopicConverter должен ")
class TopicConverterTest {

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    private TopicConverter converter;

    @MockBean
    private TagRepository tagRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    @DisplayName("корректно преобразовывать TagBriefDto к Tag")
    void briefToDtoShouldConvertTopicBriefToTopicDto() {
        val id = "id";
        val title = "title";
        val keywords = Arrays.asList("kw1", "kw2");
        val tags = Arrays.asList("tag1", "tag2");
        val content = "content";

        TopicDto topicDto = converter.briefToDto(new TopicBrief(id, title, content, keywords, tags));
        assertAll(
                () -> assertThat(topicDto).isNotNull(),
                () -> assertThat(topicDto.getContent()).isEqualTo(content),
                () -> assertThat(topicDto.getTitle()).isEqualTo(title),
                () -> assertThat(topicDto.getTags()).contains(tags),
                () -> assertThat(topicDto.getKeywords()).contains(keywords)
        );
    }

    @Test
    @DisplayName("корректно преобразовывать TopicDto и Topic к Topic")
    void createFromShouldConvertTopicDtoAndOldTopicToTopic() {
        val oldTopic = new Topic(
                "id",
                new TopicInfo("OLD_TITLE", Collections.emptyList(),
                        Collections.emptyList(), new User("hate-haters")),
                new Content("content_text"),
                Collections.emptyList(),
                LocalDateTime.now(),
                0
        );
        val changedTopic = new TopicDto("ID", "title", "kw1, kw2",
                "tag1, tag2", "content");

        given(tagRepository.existsByTag(any()))
                .willReturn(false);

        Topic topic = converter.createFrom(changedTopic, oldTopic);
        assertAll(
                () -> assertThat(topic).isNotNull(),
                () -> assertThat(topic.getId()).isEqualTo(oldTopic.getId()),
                () -> assertThat(topic.getDate()).isEqualTo(oldTopic.getDate()),
                () -> assertThat(topic.getContent()).isNotNull()
                        .hasFieldOrPropertyWithValue("text", changedTopic.getContent()),
                () -> assertThat(topic.getComments()).isNotNull()
                        .containsAll(oldTopic.getComments()),
                () -> assertThat(topic.getInfo()).isNotNull()
                        .hasFieldOrPropertyWithValue("title", changedTopic.getTitle())
                        .hasFieldOrPropertyWithValue("author", oldTopic.getInfo().getAuthor()),
                () -> assertThat(topic.getInfo().getTags().stream().map(Tag::getTag).collect(Collectors.toList()))
                        .containsOnly(Arrays.stream(changedTopic.getTags().split(","))
                                .map(String::trim).toArray(String[]::new)),
                () -> assertThat(topic.getInfo().getKeywords())
                        .containsOnly(Arrays.stream(changedTopic.getKeywords().split(","))
                                .map(String::trim).toArray(String[]::new)),
                () -> assertThat(topic.getComments()).isEqualTo(oldTopic.getComments())
        );
    }

    @Test
    @DisplayName("корректно преобразовывать TagBriefDto к Tag")
    void createFromShouldConvertTopicBriefDtoToTopic() {
        val topicBriefDto = new TopicBriefDto("title", "kw1, kw2, kw3",
                "tag1,tag2", "CONTENT!");

        given(userRepository.findUserByName(any()))
                .willReturn(Optional.of(new User("USER")));

        given(tagRepository.existsByTag(any()))
                .willReturn(false);

        Topic topic = converter.createFrom(topicBriefDto, new User());
        assertAll(
                () -> assertThat(topic).isNotNull(),
                () -> assertThat(topic.getContent()).isNotNull()
                        .hasFieldOrPropertyWithValue("text", topicBriefDto.getContent()),
                () -> assertThat(topic.getInfo()).isNotNull()
                        .hasFieldOrPropertyWithValue("title", topicBriefDto.getTitle()),
                () -> assertThat(topic.getInfo().getTags().stream().map(Tag::getTag).collect(Collectors.toList()))
                        .containsOnly(Arrays.stream(topicBriefDto.getTags().split(","))
                                .map(String::trim).toArray(String[]::new)),
                () -> assertThat(topic.getInfo().getKeywords())
                        .containsOnly(Arrays.stream(topicBriefDto.getKeywords().split(","))
                                .map(String::trim).toArray(String[]::new))
        );
    }
}