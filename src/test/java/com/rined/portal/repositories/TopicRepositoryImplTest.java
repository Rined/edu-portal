package com.rined.portal.repositories;

import com.rined.portal.AbstractMongoDBRepositoryTest;
import com.rined.portal.repositories.projections.TopicBrief;
import com.rined.portal.repositories.projections.TopicInfoWithTags;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static java.util.Objects.nonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@DisplayName("Репозитори топиков должен ")
class TopicRepositoryImplTest extends AbstractMongoDBRepositoryTest {

    @Autowired
    private TopicRepository repository;

    @Test
    @DisplayName("корректно возвращать информацию по топику по определенному тегу")
    void getTopicsByTag() {
        val jee = repository.getTopicsByTag("JavaEE");
        assertThat(jee).isNotEmpty()
                .allMatch(topicInfoBrief -> nonNull(topicInfoBrief.getAuthorId()))
                .allMatch(topicInfoBrief -> nonNull(topicInfoBrief.getAuthorName()))
                .allMatch(topicInfoBrief -> nonNull(topicInfoBrief.getTopicDate()))
                .allMatch(topicInfoBrief -> nonNull(topicInfoBrief.getTopicTitle()));
    }


    @Test
    @DisplayName("корректно возвращать список топиков с тегами")
    void getTopicsWithTagsByTagShouldReturnCorrectTopicRepresentation() {
        val tag = "JAXB";
        val jaxb = repository.getTopicsWithTagsByTag(tag);
        assertThat(jaxb).isNotEmpty()
                .allMatch(topicInfoWithTags -> nonNull(topicInfoWithTags.getAuthorId()))
                .allMatch(topicInfoWithTags -> nonNull(topicInfoWithTags.getAuthorName()))
                .allMatch(topicInfoWithTags -> nonNull(topicInfoWithTags.getTopicTitle()))
                .allMatch(topicInfoWithTags -> topicInfoWithTags.getTags().contains(tag));
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    @DisplayName("возвращать все топики с пагинацией")
    void findPageableAll() {
        val pageNumber = 0;
        val elementOnPage = 1;
        val totalCount = 2;
        val requestPageable = PageRequest.of(pageNumber, elementOnPage, Sort.by("vote").descending());

        Page<TopicInfoWithTags> pageableAll = repository.findPageableAll(requestPageable, totalCount);
        assertAll(
                () -> assertThat(pageableAll.getContent()).isNotNull().hasSize(elementOnPage),
                () -> assertThat(pageableAll.getTotalPages()).isEqualTo(totalCount % elementOnPage == 0
                        ? totalCount / elementOnPage : totalCount / elementOnPage + 1),
                () -> assertThat(pageableAll.getTotalElements()).isEqualTo(totalCount)
        );
    }


    @Test
    @DisplayName("возвращать корректно сформированный объект TopicBrief")
    void findTopicBriefByTopicNameShouldReturnCorrectTopicBrief() {
        val title = "Awesome JPA";
        val existedTag = "JPA";
        TopicBrief awesomeJpa = repository.findTopicBriefByTopicName(title);
        assertAll(
                () -> assertThat(awesomeJpa).isNotNull(),
                () -> assertThat(awesomeJpa.getTags()).contains(existedTag),
                () -> assertThat(awesomeJpa.getId()).isNotNull(),
                () -> assertThat(awesomeJpa.getTitle()).isEqualTo(title)
        );

    }
}