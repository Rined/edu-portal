package com.rined.blog.services;

import com.rined.blog.dto.UserBriefDto;
import com.rined.blog.dto.UserDto;
import com.rined.blog.model.User;
import com.rined.blog.repositories.projections.UserProfileInfo;
import org.springframework.data.domain.Page;

public interface UserService {

    UserDto getUserDtoById(String id);

    UserProfileInfo getUserProfileInfo(String id);

    Page<User> getAllPageableUsers(int page, int numberOfElementsOnPage);

    void createUser(UserBriefDto userBrief);

    User updateAndGetOld(UserDto userDto);

    void deleteById(String userId);
}
