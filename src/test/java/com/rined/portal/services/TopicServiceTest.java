package com.rined.portal.services;

import com.rined.portal.converters.TopicConverter;
import com.rined.portal.dto.TopicBriefDto;
import com.rined.portal.dto.TopicDto;
import com.rined.portal.dto.TopicExtendedDto;
import com.rined.portal.exceptions.AlreadyExistException;
import com.rined.portal.exceptions.NotFoundException;
import com.rined.portal.model.*;
import com.rined.portal.repositories.TagRepository;
import com.rined.portal.repositories.TopicRepository;
import com.rined.portal.repositories.projections.TopicBrief;
import com.rined.portal.repositories.projections.TopicInfoWithTags;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@Import({TopicServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisplayName("TopicService должен ")
class TopicServiceTest {

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    private TopicService service;

    @MockBean
    private TopicConverter converter;

    @MockBean
    private TopicRepository topicRepository;

    @MockBean
    private TagRepository tagRepository;

    @Test
    @DisplayName("бросать NotFoundException если тег не найден")
    void getTopicsByTagShouldThrowNotFoundExceptionIfUserNotFound() {
        val exceptionMessageTemplate = "Tag %s not found!";
        val tag = UUID.randomUUID().toString();

        given(tagRepository.existsByTag(tag))
                .willReturn(false);

        assertThatThrownBy(() -> service.getTopicsByTag(tag))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(String.format(exceptionMessageTemplate, tag));
    }


    @Test
    @DisplayName("корректно возвращать объект тега и список с информацией о статье по тегу")
    void getTopicsByTagShouldReturnCorrectTopicAndListOfTopicsInfo() {
        val tag = "SOME-TAG";
        val title = "some-title";
        val authorName = "author-name";
        val authorId = "some-author-id";

        val topicInfoBrief = new TopicInfoWithTags(
                title,
                0L,
                Collections.singletonList(tag),
                authorName,
                authorId
        );

        given(tagRepository.existsByTag(anyString()))
                .willReturn(true);

        given(tagRepository.getByTag(anyString()))
                .willReturn(new Tag(tag));

        given(topicRepository.getTopicsWithTagsByTag(anyString()))
                .willReturn(singletonList(topicInfoBrief));

        TopicExtendedDto topicsByTag = service.getTopicsByTag(anyString());
        assertAll(
                () -> assertThat(topicsByTag)
                        .isNotNull(),

                () -> assertThat(topicsByTag.getTag())
                        .isNotNull().hasFieldOrPropertyWithValue("tag", tag),

                () -> assertThat(topicsByTag.getTopicsWithTagsByTag()).isNotNull().hasSize(1)
                        .allMatch(topicInfoWithTags -> topicInfoWithTags.getAuthorId().equals(authorId))
                        .allMatch(topicInfoWithTags -> topicInfoWithTags.getAuthorName().equals(authorName))
                        .allMatch(topicInfoWithTags -> topicInfoWithTags.getTopicTitle().equals(title))
        );
    }

    @Test
    @DisplayName("бросать NotFoundException если топик не найден")
    void topicByTitleShouldThrowNotFoundExceptionIfTopicNotFound() {
        val exceptionMessageTemplate = "Title %s not found!";
        val title = "SOME-TITLE";

        given(topicRepository.existsByInfoTitle(title))
                .willReturn(false);

        assertThatThrownBy(() -> service.topicByTitle(title))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(String.format(exceptionMessageTemplate, title));
    }

    @Test
    @DisplayName("коректно возвращать топик по наименованию топика")
    void topicByTitleShouldReturnCorrectTopic() {
        val title = "SOME-TITLE";
        val topic = new Topic(
                new TopicInfo(
                        "SOME-TITLE",
                        Collections.singletonList("keywords"),
                        Collections.singletonList(new Tag("TAG")),
                        new User("USER_NAME")),
                new Content("CONTENT_TEXT")
        );

        given(topicRepository.existsByInfoTitle(title))
                .willReturn(true);

        given(topicRepository.findTopicByInfoTitle(title))
                .willReturn(Optional.of(topic));

        val topicOptional = service.topicByTitle(title);
        assertAll(
                () -> assertThat(topicOptional).isNotNull().isPresent(),
                () -> assertThat(topicOptional).get().isEqualTo(topic)
        );
    }

    @Test
    @DisplayName("бросать NotFoundException, если количество Topic-ов меньше чем номер страницы умноженное на количество элементов на странице")
    void getAllPageableTopicsShouldThrowNotFoundExceptionIfTopicCountLessThanMultiplyOfPageNumberAndNumberOfElements() {
        val exceptionMessageTemplate = "Page %d not found";
        val pageNumber = 0xFFFFFFFF;

        given(topicRepository.count())
                .willReturn(1L);

        assertThatThrownBy(() -> service.getAllPageableTopics(pageNumber, 2))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(String.format(exceptionMessageTemplate, pageNumber));
    }

    @Test
    @SuppressWarnings("ConstantCondsitions")
    @DisplayName("возвращать корректно сформированное представление страницы")
    void getAllPageableTopicsShouldReturnCorrectPage() {
        val title = "TITLE";
        val pageNumber = 1;
        val numberOfElements = 10;
        val totalElementCount = 21;
        val pageRequest = PageRequest.of(pageNumber, numberOfElements);

        val offset = pageNumber * numberOfElements;
        val remainder = totalElementCount - offset;
        val calcCollectionSize = remainder / numberOfElements > 0 ? numberOfElements : remainder % numberOfElements;

        given(topicRepository.count())
                .willReturn((long) totalElementCount);

        given(topicRepository.findPageableAll(any(PageRequest.class), anyLong()))
                .willReturn(new PageImpl<>(
                        Collections.nCopies(calcCollectionSize, new TopicInfoWithTags(
                                title, 0,
                                Collections.singletonList("TAG"), "AUTHOR_NAME", "AUTHOR_ID")
                        ), pageRequest, totalElementCount));
    }

    @Test
    @DisplayName("бросать AlreadyExistsException, если title уже есть")
    void createTopicShouldThrowAlreadyExistsExceptionIfTitleAlreadyExists() {
        val title = "TITLE";
        val exceptionMessageTemplate = "Topic with name %s already exists";
        val topicBriefDto = new TopicBriefDto(
                title,
                "kw1",
                "tag",
                "content"
        );

        given(topicRepository.existsByInfoTitle(any()))
                .willReturn(true);

        assertThatThrownBy(() -> service.createTopic(topicBriefDto, new User()))
                .isInstanceOf(AlreadyExistException.class)
                .hasMessage(String.format(exceptionMessageTemplate, title));
    }

    @Test
    @DisplayName("должен вызывать метод save репозитория")
    void createTopic() {
        given(converter.createFrom(any(), new User()))
                .willReturn(new Topic());

        given(topicRepository.save(any()))
                .willReturn(new Topic());

        service.createTopic(new TopicBriefDto(), new User());

        verify(topicRepository, times(1))
                .save(any());
    }

    @Test
    @DisplayName("корректно возвращать объект TopicDto")
    void getTopicDtoByName() {
        val title = "title";
        val topicDto = new TopicDto();
        val topicBrief = new TopicBrief(
                UUID.randomUUID().toString(),
                "title",
                "content",
                Collections.singletonList("keywords"),
                Collections.singletonList("tags")
        );

        given(topicRepository.findTopicBriefByTopicName(anyString()))
                .willReturn(topicBrief);
        given(converter.briefToDto(topicBrief))
                .willReturn(topicDto);

        assertThat(service.getTopicDtoByName(title))
                .isNotNull()
                .isEqualTo(topicDto);
    }

    @Test
    @DisplayName("бросать NotFoundException если топик не найден")
    void updateShouldThrowNotFoundExceptionIfTopicExists() {
        val id = "id";
        val exceptionMessageTemplate = "Topic with id \"%s\" not found";
        val dto = new TopicDto(id, "title", "keywords", "tags", "content");

        given(topicRepository.existsById(any()))
                .willReturn(false);

        assertThatThrownBy(() -> service.update(dto))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(String.format(exceptionMessageTemplate, id));
    }

    @Test
    @DisplayName("вызывать метод save репозитория")
    void updateShouldCallTopicRepositorySaveMethod() {
        given(topicRepository.existsById(any()))
                .willReturn(true);
        service.update(new TopicDto());
        verify(topicRepository, times(1))
                .save(any());
    }

    @Test
    @DisplayName("вызывать метод deleteById репозитория")
    void deleteById() {
        doNothing().when(topicRepository)
                .deleteById(anyString());
        service.deleteById("ID");
        verify(topicRepository, times(1))
                .deleteById(any());
    }
}