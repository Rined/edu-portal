package com.rined.portal.controllers;

import com.rined.portal.dto.UserBrief;
import com.rined.portal.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class RegistrationController {
    private final UserService userService;

    @PostMapping("/user/registration")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void registration(@Valid UserBrief user) {
        userService.createUser(user);
    }
}
