package com.rined.portal.controllers;

import com.rined.portal.model.User;
import com.rined.portal.model.UserInfo;
import com.rined.portal.properties.ErrorViewTemplateProperties;
import com.rined.portal.repositories.projections.UserCommentBrief;
import com.rined.portal.repositories.projections.UserProfileInfo;
import com.rined.portal.repositories.projections.UserTopicBrief;
import com.rined.portal.services.UserService;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

import static com.rined.portal.utils.MvcTestUtils.*;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ExtendWith(SpringExtension.class)
@DisplayName("Контроллер пользователей должен ")
@Import({ErrorViewTemplateProperties.class, ErrorViewTemplateProperties.class})
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("возвращать информацию о пользователе")
    void userByIdShouldReturnUserInfo() throws Exception {
        val userName = "MEGA USER";
        val title = "AwesomeTitle";
        val userId = "userId";
        val userProfileInfo = new UserProfileInfo(
                new User(userName),
                Collections.singletonList(new UserTopicBrief(title, 0, LocalDateTime.now())),
                Collections.singletonList(new UserCommentBrief(title, 0, LocalDateTime.now()))
        );

        given(userService.getUserProfileInfo(any()))
                .willReturn(userProfileInfo);

        mvc.perform(get("/user").param("id", userId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("user"))
                .andExpect(model().attribute("userProfile", userProfileInfo))
                .andExpect(content().string(containsFormAction("/users/delete")))
                .andExpect(content().string(containsHrefTemplate("/users/%s/change", userId)))
                .andExpect(content().string(containsHref("/topics", title)))
                .andExpect(content().string(containsTagValue(userName)));
    }


    @Test
    @DisplayName("возвращать пользователей и информацию о них")
    void allUsersShouldReturnUsersAndInformationAboutThem() throws Exception {
        val user = new User(
                "USER_ID", "USER_NAME", LocalDate.now(), LocalDateTime.now(),
                new UserInfo(
                        "ABOUT_USER", "mail@mail.ru",
                        "FirstUserName", "SecondUserName"
                ),
                100
        );
        val modelObj = new PageImpl<User>(Collections.singletonList(user));

        given(userService.getAllPageableUsers(anyInt(), anyInt()))
                .willReturn(modelObj);

        mvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("user-list"))
                .andExpect(model().attribute("userPage", modelObj))
                .andExpect(content().string(containsHref("/users/create")))
                .andExpect(content().string(containsHrefTemplate("/user?id=%s", user.getId())))
                .andExpect(content().string(containsTagValue(user.getName())))
                .andExpect(content().string(containsString(user.getLastSeen().toString())))
                .andExpect(content().string(containsString(String.valueOf(user.getReputation()))))
                .andExpect(content().string(containsTagValue(user.getInfo().getAbout())));
    }


    @Test
    @DisplayName("возврашать форму для создания пользователя")
    void createUserViewShouldReturnViewToCreateUser() throws Exception {
        mvc.perform(get("/users/create"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("user-create"))
                .andExpect(content().string(containsFormAction("/users/create")));
    }

    @Test
    @DisplayName("возвращать view для создания пользователей")
    void createUserShouldReturnCreateUserView() throws Exception {
        val name = "NAME";
        val email = "EMAIL@MAIL.MAIL";

        doNothing().when(userService)
                .createUser(any());

        mvc.perform(post("/users/create").param("name", name).param("email", email))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(view().name("user-create"))
                .andExpect(content().string(containsString("User was created successfully!")));
    }


    @Test
    @DisplayName("прнимать запрос на обновление пользователя и делать редирект")
    void updateUser() throws Exception {
        val userId = "USER_ID";
        val userName = "USER_NAME";
        val userEmail = "USER_EMAIL@EMAIL.EMAIL";

        val user = new User(
                userId, userName, LocalDate.now(), LocalDateTime.now(),
                new UserInfo("about", userEmail, "firstName", "secondName"),
                100
        );

        given(userService.updateAndGetOld(any()))
                .willReturn(user);

        mvc.perform(post("/users/{userId}/change", userId).param("id", userId)
                .param("name", userName).param("email", userEmail))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(String.format("/users/%s/change", userId)));
    }

    @Test
    @DisplayName("удалять пользлователя и делать редирект")
    void deleteUser() throws Exception {
        val userId = "USER_ID";

        doNothing().when(userService)
                .deleteById(any());

        mvc.perform(post("/users/delete").param("id", userId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
    }
}
