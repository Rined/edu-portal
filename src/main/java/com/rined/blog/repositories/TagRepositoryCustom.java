package com.rined.blog.repositories;

import com.rined.blog.repositories.projections.TagUsageCount;

import java.util.List;

public interface TagRepositoryCustom {

    List<TagUsageCount> tagUsageAndCount();

}
