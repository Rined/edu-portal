package com.rined.portal.repositories;

import com.rined.portal.AbstractMongoDBRepositoryTest;
import com.rined.portal.repositories.projections.TagUsageCount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.rined.portal.utils.PrimitiveUtil.notZero;
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