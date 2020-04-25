package com.rined.portal.controllers;

import com.rined.portal.dto.UserBrief;
import com.rined.portal.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class RegistrationController {
    private final UserService userService;

    @PostMapping("/user/registration")
    public String registration(@Valid UserBrief user) {
        userService.createUser(user);
        return "redirect:/login";
    }

}
