package com.rined.portal.converters;

import com.rined.portal.dto.UserBriefDto;
import com.rined.portal.dto.UserDto;
import com.rined.portal.model.User;
import com.rined.portal.model.UserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@SuppressWarnings("UnmappedTargetProperties")
@Mapper(componentModel = "spring")
public interface UserConverter {

    @Mappings({
            @Mapping(target = "id", source = "oldData.id"),
            @Mapping(target = "name", source = "dto.name"),
            @Mapping(target = "registrationDate", source = "oldData.registrationDate"),
            @Mapping(target = "lastSeen", source = "oldData.lastSeen"),
            @Mapping(target = "info.about", source = "dto.about"),
            @Mapping(target = "info.email", source = "dto.email"),
            @Mapping(target = "info.firstName", source = "dto.firstName"),
            @Mapping(target = "info.secondName", source = "dto.secondName"),
            @Mapping(target = "reputation", source = "oldData.reputation")
    })
    User createFrom(UserDto dto, User oldData);

    @Mappings({
            @Mapping(target = "about", source = "dto.about"),
            @Mapping(target = "email", source = "dto.email"),
            @Mapping(target = "firstName", source = "dto.firstName"),
            @Mapping(target = "secondName", source = "dto.secondName")
    })
    UserInfo dtoToBean(UserBriefDto dto);

    @Mappings({
            @Mapping(target = "id", source = "user.id"),
            @Mapping(target = "name", source = "user.name"),
            @Mapping(target = "email", source = "user.info.email"),
            @Mapping(target = "about", source = "user.info.about"),
            @Mapping(target = "firstName", source = "user.info.firstName"),
            @Mapping(target = "secondName", source = "user.info.secondName")
    })
    UserDto beanToUserDto(User user);

}
