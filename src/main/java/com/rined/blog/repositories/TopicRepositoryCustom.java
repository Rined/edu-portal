package com.rined.blog.repositories;

import com.rined.blog.repositories.projections.TopicBrief;
import com.rined.blog.repositories.projections.TopicInfoBrief;
import com.rined.blog.repositories.projections.TopicInfoWithTags;
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
