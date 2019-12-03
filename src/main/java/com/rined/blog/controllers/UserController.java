package com.rined.blog.controllers;

import com.rined.blog.dto.UserBriefDto;
import com.rined.blog.dto.UserDto;
import com.rined.blog.model.User;
import com.rined.blog.repositories.projections.UserProfileInfo;
import com.rined.blog.services.UserService;
import com.rined.blog.utils.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user")
    public String userById(@RequestParam("id") String id, Model model) {
        UserProfileInfo userProfileInfo = userService.getUserProfileInfo(id);
        model.addAttribute("userProfile", userProfileInfo);
        return "user";
    }

    @GetMapping("/users")
    public String allUsers(@RequestParam(value = "page", required = false, defaultValue = "0") int pageNumber,
                           @Value("${view.users.numberOfElementsOnPage}") int pageElements,
                           Model model) {
        Page<User> allUsers = userService.getAllPageableUsers(pageNumber, pageElements);
        model.addAttribute("userPage", allUsers);
        return "user-list";
    }

    @GetMapping("/users/create")
    public String createUserView() {
        return "user-create";
    }

    @PostMapping("/users/create")
    @ResponseStatus(code = HttpStatus.CREATED)
    public String createUser(@Valid UserBriefDto userBrief) {
        userService.createUser(userBrief);
        return "user-create";
    }

    @GetMapping("/users/{userId}/change")
    public String userChangeView(@PathVariable("userId") String id, Model model) {
        UserDto userDtoById = userService.getUserDtoById(id);
        model.addAttribute("user", userDtoById);
        return "user-change";
    }

    @PostMapping("/users/{userId}/change")
    public String updateUser(@Valid UserDto userDto,
                             HttpServletResponse response,
                             HttpServletRequest request,
                             @PathVariable("userId") String userId) {
        User previousUser = userService.updateAndGetOld(userDto);
        CookieUtil.addTransformCookieValueToPath(response, CookieUtil.UPDATE_COOKIE, previousUser.getName(),
                CookieUtil::encode,
                request.getRequestURI());
        return String.format("redirect:/users/%s/change", userId);
    }

    @PostMapping("/users/delete")
    public String deleteUser(@RequestParam(value = "id") String id) {
        userService.deleteById(id);
        return "redirect:/users";
    }
}
