package com.rined.blog.converters;

import com.rined.blog.dto.UserBriefDto;
import com.rined.blog.dto.UserDto;
import com.rined.blog.model.User;
import com.rined.blog.model.UserInfo;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Import(UserConverterImpl.class)
@ExtendWith(SpringExtension.class)
@DisplayName("UserConverter должен")
class UserConverterTest {

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    private UserConverter converter;

    @Test
    @DisplayName("создавать User из UserDto и User")
    void createFromShouldCorrectConvertUserDtoAndUserToNewUser() {
        val userDto = new UserDto(
                "id", "Name",
                "email", "about",
                "firstName", "secondName"
        );
        val oldData = new User(
                "OldUser", "OldName", LocalDate.now(), LocalDateTime.now(),
                new UserInfo("aboutOld", "emailOld", "firstNameOld", "secondNameOld"),
                0
        );

        User newUser = converter.createFrom(userDto, oldData);
        assertAll(
                () -> assertThat(newUser).isNotNull(),

                () -> assertThat(newUser)
                        .isNotNull()
                        .hasFieldOrPropertyWithValue("id", oldData.getId())
                        .hasFieldOrPropertyWithValue("name", userDto.getName())
                        .hasFieldOrPropertyWithValue("registrationDate", oldData.getRegistrationDate())
                        .hasFieldOrPropertyWithValue("lastSeen", oldData.getLastSeen())
                        .hasFieldOrPropertyWithValue("reputation", oldData.getReputation()),

                () -> assertThat(newUser.getInfo())
                        .isNotNull()
                        .hasFieldOrPropertyWithValue("about", userDto.getAbout())
                        .hasFieldOrPropertyWithValue("email", userDto.getEmail())
                        .hasFieldOrPropertyWithValue("firstName", userDto.getFirstName())
                        .hasFieldOrPropertyWithValue("secondName", userDto.getSecondName())
        );

    }

    @Test
    @DisplayName("преобразовывать UserBriefDto в UserInfo")
    void dtoToBeanShouldConvertUserBriefDtoToUserInfo() {
        UserBriefDto dto = new UserBriefDto("name", "email",
                "about", "firstName", "secondName");

        assertThat(converter.dtoToBean(dto))
                .isNotNull()
                .hasFieldOrPropertyWithValue("about", dto.getAbout())
                .hasFieldOrPropertyWithValue("email", dto.getEmail())
                .hasFieldOrPropertyWithValue("firstName", dto.getFirstName())
                .hasFieldOrPropertyWithValue("secondName", dto.getSecondName());
    }


    @Test
    @DisplayName("преобразовывать User к UserDto")
    void beanToUserDtoShouldConvertUserToUserDto() {
        val user = new User(
                "user", "name", LocalDate.now(), LocalDateTime.now(),
                new UserInfo("about", "email", "first", "second"),
                0
        );

        assertThat(converter.beanToUserDto(user))
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", user.getId())
                .hasFieldOrPropertyWithValue("name", user.getName())
                .hasFieldOrPropertyWithValue("email", user.getInfo().getEmail())
                .hasFieldOrPropertyWithValue("about", user.getInfo().getAbout())
                .hasFieldOrPropertyWithValue("firstName", user.getInfo().getFirstName())
                .hasFieldOrPropertyWithValue("secondName", user.getInfo().getSecondName());
    }
}