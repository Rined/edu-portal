package com.rined.portal.converters;

import com.rined.portal.dto.TagBriefDto;
import com.rined.portal.dto.TagDto;
import com.rined.portal.model.Tag;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@Import({TagConverterImpl.class})
@ExtendWith(SpringExtension.class)
@DisplayName("TagConverter должен ")
class TagConverterTest {

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    private TagConverter converter;

    @Test
    @DisplayName("корректно преобразовывать TagBriefDto к Tag")
    void dtoBriefToBeanShouldConvertTagBriefDtoToTag() {
        val tagName = "TAG";
        val tagBriefDto = new TagBriefDto(tagName);
        assertThat(converter.dtoBriefToBean(tagBriefDto))
                .isNotNull()
                .hasFieldOrPropertyWithValue("tag", tagName);

    }

    @Test
    @DisplayName("корректно преобразовывать TagDto к Tag")
    void dtoToBeanShouldConvertTagDtoToTag() {
        val tagName = "TAG";
        val tagId = "ID";
        val tagDto = new TagDto(tagId, tagName);
        assertThat(converter.dtoToBean(tagDto))
                .isNotNull()
                .hasFieldOrPropertyWithValue("tag", tagName)
                .hasFieldOrPropertyWithValue("id", tagId);
    }

    @Test
    @DisplayName("корректно преобразовывать Tag к TagDto")
    void beanToDtoShouldConvertTagToTagDto() {
        val tagName = "TAG";
        val tagId = "ID";
        val tag = new Tag(tagId, tagName);
        assertThat(converter.beanToDto(tag))
                .isNotNull()
                .hasFieldOrPropertyWithValue("tag", tagName)
                .hasFieldOrPropertyWithValue("id", tagId);
    }
}