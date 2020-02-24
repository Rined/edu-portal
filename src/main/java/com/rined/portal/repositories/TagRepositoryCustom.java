package com.rined.portal.repositories;

import com.rined.portal.repositories.projections.TagUsageCount;

import java.util.List;

public interface TagRepositoryCustom {

    List<TagUsageCount> tagUsageAndCount();

}
