package com.rined.blog.services;

import com.rined.blog.converters.TagConverter;
import com.rined.blog.dto.TagBriefDto;
import com.rined.blog.dto.TagDto;
import com.rined.blog.exceptions.AlreadyExistException;
import com.rined.blog.exceptions.NotFoundException;
import com.rined.blog.model.Tag;
import com.rined.blog.repositories.TagRepository;
import com.rined.blog.repositories.projections.TagUsageCount;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

import static com.rined.blog.utils.PrimitiveUtil.isSame;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@Import({TagService.class})
@ExtendWith(SpringExtension.class)
@DisplayName("TagService должен ")
class TagServiceTest {

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    private TagService service;

    @MockBean
    private TagRepository repository;

    @MockBean
    private TagConverter converter;

    @Test
    @DisplayName("должен корректно возвращать все теги с количеством статей, в которых они используются")
    void getTagsWithUsageCount() {
        val tagName = "Spring-Shell";
        val count = 1L;
        given(repository.tagUsageAndCount())
                .willReturn(singletonList(new TagUsageCount(tagName, count)));
        assertThat(service.getTagsWithUsageCount()).hasSize(1)
                .allMatch(tagUsageCount -> isSame(count, tagUsageCount.getCount()))
                .allMatch(tagUsageCount -> Objects.equals(tagName, tagUsageCount.getTag()));
    }

    @Test
    @DisplayName("корректно возвращать список тегов")
    void allTagsShouldReturnTagsFromRepository() {
        val tag = "tag";
        val tagList = Collections.singletonList(new Tag(tag));

        given(repository.findAll())
                .willReturn(tagList);
        assertThat(service.allTags())
                .isNotNull()
                .hasSize(1)
                .allMatch(tagObj -> tagObj.getTag().equals(tag));
    }

    @Test
    @DisplayName("бросать NotFoundException если тег не найден")
    void getTagByNameShouldThrowNotFoundExceptionIfTagNotFound() {
        val title = "TITLE";
        val exceptionMessageTemplate = "Tag with name \"%s\" not found";

        given(repository.findByTag(anyString()))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> service.getTagByName(title))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(String.format(exceptionMessageTemplate, title));
    }

    @Test
    @DisplayName("корректно возвращать TagDto")
    void getTagByNameShouldReturnCorrectTagDto() {
        val title = "title";
        val optionalTag = Optional.of(new Tag(title));

        given(repository.findByTag(anyString()))
                .willReturn(optionalTag);

        given(converter.beanToDto(any()))
                .willReturn(new TagDto("id", title));

        assertThat(service.getTagByName(anyString()))
                .isNotNull()
                .hasFieldOrPropertyWithValue("tag", title);
    }

    @Test
    @DisplayName("бросать AlreadyExistsException если таг уже существует")
    void createShouldThrowAlreadyExceptionIfTagAlreadyExists() {
        val tag = "tag";
        val exceptionMessageTemplate = "Tag with name \"%s\" already exists";

        given(repository.existsByTag(any()))
                .willReturn(true);

        assertThatThrownBy(() -> service.create(new TagBriefDto(tag)))
                .isInstanceOf(AlreadyExistException.class)
                .hasMessage(String.format(exceptionMessageTemplate, tag));
    }

    @Test
    @DisplayName("вызывать метод репозитория")
    void createShouldCallRepositorySaveMethod() {
        val tag = "tag";
        given(repository.existsByTag(any()))
                .willReturn(false);
        given(converter.dtoBriefToBean(any()))
                .willReturn(new Tag(tag));
        service.create(new TagBriefDto(tag));
        verify(repository, times(1))
                .save(any());
    }

    @Test
    @DisplayName("должен бросать NotFoundException если id не найден")
    void updateShouldThrowNotFoundExceptionIfIdNotFound() {
        val exceptionMessageTemplate = "Tag \"%s\" not found";
        val tag = "TAG";
        val tagDto = new TagDto("ID", tag);

        given(repository.existsById(any()))
                .willReturn(false);

        assertThatThrownBy(() -> service.update(tagDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(String.format(exceptionMessageTemplate, tag));
    }

    @Test
    @DisplayName("должен бросать NotFoundException если тег не найден")
    void updateShouldThrowAlreadyExistsExceptionIfTagAlreadyExists() {
        val exceptionMessageTemplate = "Tag with name %s already exists";
        val tag = "TAG";
        val tagDto = new TagDto("ID", tag);

        given(repository.existsById(any()))
                .willReturn(true);
        given(repository.existsByTag(any()))
                .willReturn(true);

        assertThatThrownBy(() -> service.update(tagDto))
                .isInstanceOf(AlreadyExistException.class)
                .hasMessage(String.format(exceptionMessageTemplate, tag));
    }

    @Test
    @DisplayName("вызывать метод save репозитория")
    void updateShouldCallRepositorySaveMethod() {
        val tag = new Tag("tag");
        given(repository.existsById(any()))
                .willReturn(true);
        given(repository.existsByTag(any()))
                .willReturn(false);
        given(converter.dtoToBean(any()))
                .willReturn(tag);
        service.update(new TagDto());
        verify(repository, times(1))
                .save(any());
    }

    @Test
    @DisplayName("вызывать метод deleteById репозитория")
    void deleteById() {
        doNothing().when(repository)
                .deleteById(anyString());
        service.deleteById("ID");
        verify(repository, times(1))
                .deleteById(any());
    }
}