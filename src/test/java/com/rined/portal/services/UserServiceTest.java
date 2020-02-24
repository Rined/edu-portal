package com.rined.portal.services;

import com.rined.portal.converters.UserConverter;
import com.rined.portal.dto.UserBriefDto;
import com.rined.portal.dto.UserDto;
import com.rined.portal.exceptions.NotFoundException;
import com.rined.portal.model.User;
import com.rined.portal.repositories.UserRepository;
import com.rined.portal.repositories.projections.UserCommentBrief;
import com.rined.portal.repositories.projections.UserTopicBrief;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@Import({UserServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisplayName("UserService должен ")
class UserServiceTest {

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    private UserService service;

    @MockBean
    private UserRepository repository;

    @MockBean
    private UserConverter converter;

    @Test
    @DisplayName("бросать NotFoundException если user не найден")
    void getUserDtoByIdShouldThrowExceptionIfUserNotFound() {
        val exceptionMessageTemplate = "User by id %s not found!";
        val id = UUID.randomUUID().toString();

        given(repository.findById(id))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> service.getUserDtoById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(String.format(exceptionMessageTemplate, id));
    }

    @Test
    @DisplayName("возвращать корректно преобразованный в UserDto объект")
    void getUserDtoByIdShouldReturnCorrectUserDto() {
        val id = UUID.randomUUID().toString();
        val userName = "USER_NAME";

        given(repository.findById(id))
                .willReturn(Optional.of(new User(userName)));

        given(converter.beanToUserDto(any()))
                .willReturn(new UserDto(null, userName, null, null, null, null));

        assertThat(service.getUserDtoById(id))
                .isNotNull().hasFieldOrPropertyWithValue("name", userName);
    }

    @Test
    @DisplayName("бросать NotFoundException если user не найден")
    void getUserProfileInfoShouldThrowExceptionIfUserNotFound() {
        val exceptionMessageTemplate = "User by id %s not found!";
        val id = UUID.randomUUID().toString();

        given(repository.findById(id))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> service.getUserProfileInfo(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(String.format(exceptionMessageTemplate, id));
    }

    @Test
    @DisplayName("возвращать корректно сформированный объект UserProfileInfo")
    void getUserProfileInfoShouldReturnCorrectUserProfileInfo() {
        val id = UUID.randomUUID().toString();
        val userName = "USER_NAME";

        val title = "TITLE";
        val dateTime = LocalDateTime.now();

        given(repository.findById(id))
                .willReturn(Optional.of(new User(userName)));

        val userTopicBrief = new UserTopicBrief(title, 0, dateTime);
        given(repository.getUserTopicsByUserId(id))
                .willReturn(Collections.singletonList(userTopicBrief));

        val userCommentBrief = new UserCommentBrief(title, 0, dateTime);
        given(repository.getUserCommentsByUserId(id))
                .willReturn(Collections.singletonList(userCommentBrief));

        val userProfileInfo = service.getUserProfileInfo(id);
        assertAll(
                () -> assertThat(userProfileInfo)
                        .isNotNull(),

                () -> assertThat(userProfileInfo.getUser())
                        .isNotNull().hasFieldOrPropertyWithValue("name", userName),

                () -> assertThat(userProfileInfo.getTopics())
                        .isNotNull().hasSize(1).containsOnly(userTopicBrief),

                () -> assertThat(userProfileInfo.getComments())
                        .isNotNull().hasSize(1).containsOnly(userCommentBrief)
        );
    }

    @Test
    @DisplayName("бросать NotFoundException, если номер страницы отрицательный")
    void getAllPageableUsersShouldThrowNotFoundExceptionIfPageIsNegative() {
        val exceptionMessageTemplate = "Page %d not found";
        val negativePageNumber = -1;

        given(repository.count())
                .willReturn(50L);

        assertThatThrownBy(() -> service.getAllPageableUsers(negativePageNumber, 2))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(String.format(exceptionMessageTemplate, negativePageNumber));
    }

    @Test
    @DisplayName("бросать NotFoundException, если количество User-ов меньше чем номер страницы умноженное на количество элементов на странице")
    void getAllPageableUsersShouldThrowNotFoundExceptionIfUserCountLessThanMultiplyOfPageNumberAndNumberOfElements() {
        val exceptionMessageTemplate = "Page %d not found";
        val pageNumber = 0xFFFFFFFF;

        given(repository.count())
                .willReturn(1L);

        assertThatThrownBy(() -> service.getAllPageableUsers(pageNumber, 2))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(String.format(exceptionMessageTemplate, pageNumber));
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    @DisplayName("возвращать корректно сформированное представление страницы")
    void getAllPageableUsersShouldReturnCorrectPage() {
        val userName = "USER_NAME";
        val pageNumber = 1;
        val numberOfElements = 10;
        val totalElementCount = 21;
        val pageRequest = PageRequest.of(pageNumber, numberOfElements);

        val offset = pageNumber * numberOfElements;
        val remainder = totalElementCount - offset;
        val calcCollectionSize = remainder / numberOfElements > 0 ? numberOfElements : remainder % numberOfElements;

        given(repository.count())
                .willReturn((long) totalElementCount);

        given(repository.findAll((PageRequest) any()))
                .willReturn(new PageImpl<>(Collections.nCopies(calcCollectionSize, new User(userName)),
                        pageRequest, totalElementCount));

        Page<User> allPageableUsers = service.getAllPageableUsers(pageNumber, numberOfElements);
        assertAll(
                () -> assertThat(allPageableUsers)
                        .isNotNull().isNotEmpty(),

                () -> assertThat(allPageableUsers.getTotalElements())
                        .isEqualTo(totalElementCount),

                () -> assertThat(allPageableUsers.getTotalPages()).isEqualTo(
                        (totalElementCount % numberOfElements == 0) ? totalElementCount / numberOfElements :
                                totalElementCount / numberOfElements + 1
                ),

                () -> assertThat(allPageableUsers.getNumber())
                        .isEqualTo(pageNumber),

                () -> assertThat(allPageableUsers.getContent())
                        .isNotNull()
                        .hasSize(calcCollectionSize)
                        .allMatch(Objects::nonNull)
                        .allMatch(usr -> usr.getName().equals(userName))
        );
    }

    @Test
    @DisplayName("вызывать метод save репозитория")
    void createUserShouldCallRepositorySaveMethod() {
        when(repository.save(any())).thenReturn(any());
        service.createUser(new UserBriefDto());
        verify(repository, times(1)).save(any());
    }

    @Test
    @DisplayName("выбрасывать NotFoundException если User не найден")
    void updateAndGetOldShouldThrowNotFoundExceptionIfUserDoesNotExists() {
        val exceptionMessageTemplate = "User with id \"%s\" not found";

        given(repository.existsById(anyString()))
                .willReturn(false);

        assertThatThrownBy(() -> service.updateAndGetOld(new UserDto()))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(String.format(exceptionMessageTemplate, (Object) null));
    }


    @Test
    @DisplayName("возвращать корректные предыдущие данные о пользователе и вызывать метод сохранения для новых данных")
    void updateAndGetOldShouldReturnCorrectPreviousDataAndCallRepositorySaveMethod() {
        val userName = "OLD_USER";

        given(repository.existsById(any()))
                .willReturn(true);

        given(repository.getById(any()))
                .willReturn(new User(userName));

        given(converter.createFrom(any(), any()))
                .willReturn(new User());

        val user = service.updateAndGetOld(new UserDto());
        assertAll(
                () -> assertThat(user).isNotNull()
                        .hasFieldOrPropertyWithValue("name", userName),
                () -> verify(repository, times(1)).save(any())
        );
    }

    @Test
    @DisplayName("вызывать метод deleteById репозитория")
    void deleteByIdShouldCallRepositoryDeleteByIdMethod() {
        doNothing().when(repository).deleteById(any());
        service.deleteById(any());
        verify(repository, times(1)).deleteById(any());
    }
}