package com.rined.blog.services;

import com.rined.blog.converters.UserConverter;
import com.rined.blog.dto.UserBriefDto;
import com.rined.blog.dto.UserDto;
import com.rined.blog.exceptions.NotFoundException;
import com.rined.blog.model.User;
import com.rined.blog.model.UserInfo;
import com.rined.blog.repositories.UserRepository;
import com.rined.blog.repositories.projections.UserCommentBrief;
import com.rined.blog.repositories.projections.UserProfileInfo;
import com.rined.blog.repositories.projections.UserTopicBrief;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;

    public UserDto getUserDtoById(String id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userConverter.beanToUserDto(userOptional.get());
        } else throw new NotFoundException(String.format("User by id %s not found!", id));
    }

    public UserProfileInfo getUserProfileInfo(String id) {
        Optional<User> userById = userRepository.findById(id);
        if (userById.isPresent()) {
            List<UserTopicBrief> userTopicsByUserId = userRepository.getUserTopicsByUserId(id);
            List<UserCommentBrief> userCommentsByUserId = userRepository.getUserCommentsByUserId(id);
            return new UserProfileInfo(userById.get(), userTopicsByUserId, userCommentsByUserId);
        } else throw new NotFoundException(String.format("User by id %s not found!", id));
    }

    public Page<User> getAllPageableUsers(int page, int numberOfElementsOnPage) {
        long count = userRepository.count();
        if ((count < page * numberOfElementsOnPage) || page < 0)
            throw new NotFoundException("Page %d not found", page);
        return userRepository.findAll(PageRequest.of(page, numberOfElementsOnPage, Sort.by("reputation").descending()));
    }

    public void createUser(UserBriefDto userBrief) {
        UserInfo userInfo = userConverter.dtoToBean(userBrief);
        User user = new User(userBrief.getName(), userInfo);
        userRepository.save(user);
    }

    public User updateAndGetOld(UserDto userDto) {
        boolean existedId = userRepository.existsById(userDto.getId());
        if (!existedId) {
            throw new NotFoundException(String.format("User with id \"%s\" not found", userDto.getId()));
        }
        User oldUserData = userRepository.getById(userDto.getId());
        User newUser = userConverter.createFrom(userDto, oldUserData);
        userRepository.save(newUser);
        return oldUserData;
    }

    public void deleteById(String userId) {
        userRepository.deleteById(userId);
    }

}
