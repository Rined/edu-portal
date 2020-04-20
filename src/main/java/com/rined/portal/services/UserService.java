package com.rined.portal.services;

import com.rined.portal.dto.UserBriefDto;
import com.rined.portal.dto.UserDto;
import com.rined.portal.model.User;
import com.rined.portal.repositories.projections.UserProfileInfo;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserDto getUserDtoById(String id);

    UserProfileInfo getUserProfileInfo(String id);

    Page<User> getAllPageableUsers(int page, int numberOfElementsOnPage);

    void createUser(UserBriefDto userBrief);

    User updateAndGetOld(UserDto userDto);

    void deleteById(String userId);
}
