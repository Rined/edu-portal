package com.rined.portal.repositories;

import com.rined.portal.repositories.projections.TopicBrief;
import com.rined.portal.repositories.projections.TopicInfoBrief;
import com.rined.portal.repositories.projections.TopicInfoWithTags;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TopicRepositoryCustom {

    List<TopicInfoWithTags> getTopicsWithTagsByTag(String tag);

    List<TopicInfoBrief> getTopicsByTag(String tag);

    Page<TopicInfoWithTags> findPageableAll(Pageable pageable, long count);

    TopicBrief findTopicBriefByTopicName(String topic);

    void removeTagArrayElementsById(String id);

    void removeUserElementsById(String id);
}
