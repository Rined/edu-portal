package com.rined.portal.converters;

import com.rined.portal.dto.TagBriefDto;
import com.rined.portal.dto.TagDto;
import com.rined.portal.model.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
@SuppressWarnings("UnmappedTargetProperties")
public interface TagConverter {

    @Mappings({
            @Mapping(target = "tag", source = "dto.newTag")
    })
    Tag dtoBriefToBean(TagBriefDto dto);

    @Mappings({
            @Mapping(target = "id", source = "dto.id"),
            @Mapping(target = "tag", source = "dto.tag")
    })
    Tag dtoToBean(TagDto dto);

    @Mappings({
            @Mapping(target = "id", source = "tag.id"),
            @Mapping(target = "tag", source = "tag.tag")
    })
    TagDto beanToDto(Tag tag);

}
