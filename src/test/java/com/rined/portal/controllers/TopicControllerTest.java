package com.rined.portal.controllers;

import com.rined.portal.dialect.object.CookieObjectDialect;
import com.rined.portal.dto.TopicDto;
import com.rined.portal.dto.TopicExtendedDto;
import com.rined.portal.model.*;
import com.rined.portal.properties.ErrorViewTemplateProperties;
import com.rined.portal.repositories.projections.TopicInfoWithTags;
import com.rined.portal.services.TopicService;
import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.rined.portal.utils.MvcTestUtils.*;
import static java.util.Collections.singletonList;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TopicController.class)
@ExtendWith(SpringExtension.class)
@DisplayName("Контроллер топиков должен ")
@Import({ErrorViewTemplateProperties.class, CookieObjectDialect.class, ErrorViewTemplateProperties.class})
class TopicControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TopicService topicService;

    private static final String topicTitle = "SOME_TOPIC";

    private static Tag tag;
    private static User user;
    private static Topic topic;
    private static Content content;
    private static Comment comment;
    private static TopicInfo topicInfo;

    @BeforeAll
    static void setUp() {
        tag = new Tag("TAG_ID", "TAG_NAME");
        content = new Content("CONTENT");

        user = new User(
                "USERID", "USER_NAME",
                LocalDate.now(), LocalDateTime.now(),
                new UserInfo(
                        "AboutUser", "userEmail@email.com",
                        "firstUserName", "secondUserName"
                ),
                50
        );

        comment = new Comment(user, "CommentText", LocalDateTime.now(), 50);

        topicInfo = new TopicInfo(
                topicTitle, Collections.singletonList("kw1"),
                Collections.singletonList(tag),
                user
        );

        topic = new Topic(
                "topicId", topicInfo, content,
                Collections.singletonList(comment), LocalDateTime.now(), 100
        );

    }

    @Test
    @DisplayName("возвращать топик по наименованию")
    void topicByTitleShouldReturnTopicByTitle() throws Exception {
        given(topicService.topicByTitle(anyString()))
                .willReturn(Optional.of(topic));

        mvc.perform(get("/topics/{topic}", topicTitle))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("topic"))
                .andExpect(model().attribute("topic", topic))
                .andExpect(content().string(containsTagValue(topicTitle)))
                .andExpect(content().string(containsHrefTemplate("/user?id=%s", user.getId())))
                .andExpect(content().string(containsTagValue(user.getName())))
                .andExpect(content().string(containsHref("/topics/tags", tag.getTag())))
                .andExpect(content().string(containsHrefTemplate("/topics/%s/change", topicTitle)))
                .andExpect(content().string(containsFormAction("/topics/delete")))
                .andExpect(content().string(containsString(content.getText())))
                .andExpect(content().string(containsString(comment.getText())));
    }

    @Test
    @DisplayName("возвраать информацию о топиках с тегами")
    void topicsShouldReturnTopicInfoWithTags() throws Exception {
        val page = new PageImpl<>(Collections.singletonList(
                new TopicInfoWithTags(topicTitle, topic.getVote(),
                        topicInfo.getTags().stream().map(Tag::getTag).collect(Collectors.toList()),
                        user.getName(), user.getId())));

        given(topicService.getAllPageableTopics(anyInt(), anyInt()))
                .willReturn(page);

        mvc.perform(get("/topics"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("topic-list"))
                .andExpect(model().attribute("topicPage", page))
                .andExpect(content().string(containsHref("/topics/create")))
                .andExpect(content().string(containsHref("/topics", topicTitle)))
                .andExpect(content().string(containsTagValue(topicTitle)))
                .andExpect(content().string(containsHref("/topics/tags", tag.getTag())))
                .andExpect(content().string(containsHrefTemplate("/user?id=%s", user.getId())))
                .andExpect(content().string(containsTagValue(user.getName())));
    }

    @Test
    @DisplayName("возвращать топики по тегу")
    void tagsTopicShouldReturnTopicsByTag() throws Exception {
        TopicExtendedDto bean = new TopicExtendedDto(
                new Tag("main-tag"),
                singletonList(new TopicInfoWithTags("aTitle", 0,
                        singletonList("aTag"), "authorName", "authorId")));

        given(topicService.getTopicsByTag(anyString()))
                .willReturn(bean);

        TopicInfoWithTags topic = bean.getTopicsWithTagsByTag().get(0);
        mvc.perform(get("/topics/tags/{tag}", "jaxb"))
                .andExpect(status().isOk())
                .andExpect(view().name("tag"))
                .andExpect(model().attribute("tagWithTopics", bean))
                .andExpect(content().string(containsFormAction("/tags/delete")))
                .andExpect(content().string(containsHrefTemplate("/tags/%s/change", bean.getTag().getTag())))
                .andExpect(content().string(containsHref("/topics/tags", topic.getTags().get(0))))
                .andExpect(content().string(containsHref("/topics", topic.getTopicTitle())))
                .andExpect(content().string(containsString(bean.getTag().getTag())))
                .andExpect(content().string(containsString(topic.getAuthorId())))
                .andExpect(content().string(containsString(topic.getAuthorName())))
                .andExpect(content().string(containsString(topic.getTopicTitle())))
                .andExpect(content().string(containsString(String.valueOf(topic.getVote()))))
                .andExpect(content().string(containsString(topic.getTags().get(0))));
    }

    @Test
    @DisplayName("возвращать view для создания топика")
    void topicCreateViewShouldReturnCreateTopicView() throws Exception {
        mvc.perform(get("/topics/create"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("topic-create"))
                .andExpect(content().string(containsFormAction("/topics/create")));
    }

    @Test
    @DisplayName("позволять создать топик и возвращать view")
    void topicCreateShouldCreateTopicAndReturnView() throws Exception {
        doNothing().when(topicService).createTopic(any());

        mvc.perform(post("/topics/create")
                .param("title", topicTitle)
                .param("keywords", String.join(",", topicInfo.getKeywords()))
                .param("tags", tag.getTag())
                .param("userName", user.getName())
                .param("content", content.getText()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(view().name("topic-create"))
                .andExpect(content().string(containsFormAction("/topics/create")))
                .andExpect(content().string(containsString("Topic was created successfully!")));
    }

    @Test
    @DisplayName("возвращать форму для редактирования топика")
    void topicChangeViewShouldReturnFormToChangeTopic() throws Exception {
        val topicDto = new TopicDto(
                topic.getId(), topicInfo.getTitle(), String.join(",", topicInfo.getKeywords()),
                topicInfo.getTags().stream().map(Tag::getTag).collect(Collectors.joining(",")),
                content.getText()
        );

        given(topicService.getTopicDtoByName(any()))
                .willReturn(topicDto);


        mvc.perform(get("/topics/{topic}/change", topicTitle))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("topic-change"))
                .andExpect(model().attribute("topic", topicDto))
                .andExpect(content().string(containsFormActionTemplate("/topics/%s/change", topicTitle)))
                .andExpect(content().string(containsString(topicDto.getTitle())))
                .andExpect(content().string(containsString(topicDto.getContent())))
                .andExpect(content().string(containsString(topicDto.getKeywords())))
                .andExpect(content().string(containsString(topicDto.getTags())))
                .andExpect(content().string(containsString(topicDto.getId())));
    }

    @Test
    @DisplayName("прнимать запрос на обновление топика и делать редирект")
    void updateTopicShouldAcceptRequestAndRedirect() throws Exception {
        doNothing().when(topicService).update(any());

        mvc.perform(post("/topics/{topic}/change", topicTitle)
                .param("id", topic.getId())
                .param("title", topicTitle)
                .param("keywords", String.join(",", topicInfo.getKeywords()))
                .param("tags", tag.getTag())
                .param("userName", user.getName())
                .param("content", content.getText()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(String.format("/topics/%s/change", topicTitle)));
    }

    @Test
    @DisplayName("удалять топик и делать редирект")
    void deleteTopic() throws Exception {
        doNothing().when(topicService).deleteById(anyString());

        mvc.perform(post("/topics/delete").param("id", topic.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/topics"));
    }
}
