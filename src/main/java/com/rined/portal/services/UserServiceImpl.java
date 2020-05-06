package com.rined.portal.services;

import com.rined.portal.converters.UserConverter;
import com.rined.portal.dto.UserBrief;
import com.rined.portal.dto.UserBriefDto;
import com.rined.portal.dto.UserDto;
import com.rined.portal.exceptions.AlreadyExistException;
import com.rined.portal.exceptions.NotFoundException;
import com.rined.portal.model.User;
import com.rined.portal.model.UserInfo;
import com.rined.portal.repositories.UserRepository;
import com.rined.portal.repositories.projections.UserCommentBrief;
import com.rined.portal.repositories.projections.UserProfileInfo;
import com.rined.portal.repositories.projections.UserTopicBrief;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;

    @Override
    public UserDto getUserDtoById(String id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userConverter.beanToUserDto(userOptional.get());
        } else throw new NotFoundException(String.format("User by id %s not found!", id));
    }

    @Override
    public UserProfileInfo getUserProfileInfo(String id) {
        Optional<User> userById = userRepository.findById(id);
        if (userById.isPresent()) {
            List<UserTopicBrief> userTopicsByUserId = userRepository.getUserTopicsByUserId(id);
            List<UserCommentBrief> userCommentsByUserId = userRepository.getUserCommentsByUserId(id);
            return new UserProfileInfo(userById.get(), userTopicsByUserId, userCommentsByUserId);
        } else throw new NotFoundException(String.format("User by id %s not found!", id));
    }

    @Override
    public Page<User> getAllPageableUsers(int page, int numberOfElementsOnPage) {
        long count = userRepository.count();
        if ((count < page * numberOfElementsOnPage) || page < 0)
            throw new NotFoundException("Page %d not found", page);
        return userRepository.findAll(PageRequest.of(page, numberOfElementsOnPage, Sort.by("reputation").descending()));
    }

    @Override
    public void createUser(UserBriefDto userBrief) {
        UserInfo userInfo = userConverter.dtoToBean(userBrief);
        User user = new User(userBrief.getName(), userInfo);
        userRepository.save(user);
    }

    @Override
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

    @Override
    public void deleteById(String userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public void createUser(UserBrief user) {
        if (userRepository.existsByName(user.getUsername())) {
            throw new AlreadyExistException("User with username %s already exists!", user.getUsername());
        }
        userRepository.save(new User(user.getUsername(), user.getPassword()));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByName(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with login %s not found!", username)));
    }
}
