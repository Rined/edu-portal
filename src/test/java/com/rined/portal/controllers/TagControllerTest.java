package com.rined.portal.controllers;

import com.rined.portal.dialect.object.CookieObjectDialect;
import com.rined.portal.dto.TagBriefDto;
import com.rined.portal.dto.TagDto;
import com.rined.portal.model.Tag;
import com.rined.portal.properties.ErrorViewTemplateProperties;
import com.rined.portal.repositories.projections.TagUsageCount;
import com.rined.portal.services.TagService;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static com.rined.portal.utils.MvcTestUtils.*;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TagController.class)
@ExtendWith(SpringExtension.class)
@DisplayName("Контроллер тегов должен ")
@Import({ErrorViewTemplateProperties.class, CookieObjectDialect.class, ErrorViewTemplateProperties.class})
class TagControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ErrorViewTemplateProperties properties;

    @MockBean
    private TagService tagService;

    @Test
    @DisplayName("возвращать теги и их количество")
    void tagsShouldReturnTagsAndItCount() throws Exception {
        val resultData = new TagUsageCount("Spring-Data", 123);
        val result = singletonList(resultData);

        given(tagService.getTagsWithUsageCount())
                .willReturn(result);

        mvc.perform(get("/tags"))
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("tag-list"))
                .andExpect(model().attribute("tags", result))
                .andExpect(content().string(containsString("Tag list")))
                .andExpect(content().string(containsString("Total number of tags: " + result.size())))
                .andExpect(content().string(containsString("Create new tag")))
                .andExpect(content().string(containsHref("/tags/create")))
                .andExpect(content().string(containsHref("/topics/tags", resultData.getTag())))
                .andExpect(content().string(containsString(resultData.getTag())))
                .andExpect(content().string(containsString(String.valueOf(resultData.getCount()))));
    }

    @Test
    @DisplayName("возвращать форму для заполнения список используемых тегов")
    void createTagViewShouldReturnViewWithFormsAndUsedTagList() throws Exception {
        val tag = new Tag("aTag");
        val result = Collections.singletonList(tag);

        given(tagService.allTags()).willReturn(result);

        mvc.perform(get("/tags/create"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("tag-create"))
                .andExpect(model().attribute("tags", result))
                .andExpect(content().string(containsFormAction("/tags/create")))
                .andExpect(content().string(containsHref("/topics/tags", tag.getTag())))
                .andExpect(content().string(containsTagValue(tag.getTag())));
    }


    @Test
    @DisplayName("возвращать форму для изменения тега")
    void tagChangeViewShouldReturnTagChangeView() throws Exception {
        val result = new TagDto("aId", "aTag");

        given(tagService.getTagByName(anyString())).willReturn(result);

        mvc.perform(get("/tags/{tag}/change", "jaxb"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("tag-change"))
                .andExpect(model().attribute("tag", result))
                .andExpect(content().string(containsFormActionTemplate("/tags/%s/change", result.getTag())));
    }

    @Test
    @DisplayName("возвращать оишбку, если не проходит валидацию")
    void createTagValidationShouldReturnErrorPage() throws Exception {
        val url = "/tags/create";
        mvc.perform(post(url).param("newTag", ""))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(view().name(properties.getName()))
                .andExpect(model().attribute(properties.getCodeAlias(), "400 Bad request!"))
                .andExpect(model().attribute(properties.getDescriptionAlias(), "Tag name is mandatory!"));
    }

    @Test
    @DisplayName("возвращать корректный код и view")
    void createTagShouldReturnCreateViewAndReturnCreateCode() throws Exception {
        val url = "/tags/create";
        val tagBrief = new TagBriefDto("aNewTag");
        val aTagResult = new Tag("aId", tagBrief.getNewTag());
        val tagsResult = Collections.singletonList(aTagResult);

        doNothing().when(tagService)
                .create(any());

        given(tagService.allTags())
                .willReturn(tagsResult);

        mvc.perform(post(url).param("newTag", tagBrief.getNewTag()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(view().name("tag-create"))
                .andExpect(model().attribute("tags", tagsResult))
                .andExpect(content().string(containsFormAction("/tags/create")))
                .andExpect(content().string(containsHref("/topics/tags", aTagResult.getTag())));
    }

    @Test
    @DisplayName("должен редиректить после удаления")
    void deleteTagShouldRedirectAfterDelete() throws Exception {
        val id = "123";

        doAnswer(invocation -> {
            String methodId = invocation.getArgument(0);
            assertThat(methodId).isEqualTo(id);
            return null;
        }).when(tagService).deleteById(any());

        mvc.perform(post("/tags/delete").param("id", id))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tags"));
    }

    @Test
    @DisplayName("возвращать форму для обновления тега")
    void updateTag() throws Exception {
        val id = "aId";
        val newTag = "newTag";
        val oldTag = "oldTag";

        doAnswer(invocation -> {
            TagDto dto = invocation.getArgument(0);
            assertThat(dto)
                    .hasFieldOrPropertyWithValue("id", id)
                    .hasFieldOrPropertyWithValue("tag", newTag);
            return null;
        }).when(tagService).update(any());

        mvc.perform(post("/tags/{tag}/change", oldTag).param("id", id).param("tag", newTag))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(cookie().value("update", oldTag))
                .andExpect(cookie().path("update", String.format("/tags/%s/change", newTag)))
                .andExpect(redirectedUrlTemplate("/tags/{tag}/change", newTag));
    }
}