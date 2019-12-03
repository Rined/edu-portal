package com.rined.blog.repositories;

import com.rined.blog.AbstractMongoDBRepositoryTest;
import com.rined.blog.repositories.projections.TagUsageCount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.rined.blog.utils.PrimitiveUtil.notZero;
import static java.util.Objects.nonNull;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий тегов должен ")
class TagRepositoryImplTest extends AbstractMongoDBRepositoryTest {

    @Autowired
    private TagRepository repository;

    @Test
    @DisplayName("корректно возвращать все теги с количеством статей, в которых они используются")
    void countOfTopicsByTag() {
        List<TagUsageCount> tagUsageCounts = repository.tagUsageAndCount();
        assertThat(tagUsageCounts).isNotEmpty()
                .allMatch(tagUsageCount ->
                        notZero(tagUsageCount.getCount()), "Тег используется")
                .allMatch(tagUsageCount ->
                        nonNull(tagUsageCount.getTag()), "Наименование тега не null");
    }
}