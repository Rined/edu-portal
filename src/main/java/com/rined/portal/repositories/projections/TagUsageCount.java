package com.rined.portal.repositories.projections;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TagUsageCount {

    private final String tag;

    private final long count;

}
