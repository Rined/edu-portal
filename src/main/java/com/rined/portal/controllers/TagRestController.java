package com.rined.portal.controllers;

import com.rined.portal.dto.TagDto;
import com.rined.portal.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TagRestController {
    private final TagService tagService;

    @GetMapping("/api/tag")
    public List<TagDto> tagStartWith(@RequestParam(name = "filter", required = false, defaultValue = "") String filter) {
        return tagService.getByTagStartWith(filter);
    }

}
