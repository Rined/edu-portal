package com.rined.portal.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class SearchController {

    @GetMapping("/search")
    public String search() {
        return "search";
    }


}
