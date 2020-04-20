package com.rined.portal.controllers;

import com.rined.portal.dto.TagBriefDto;
import com.rined.portal.dto.TagDto;
import com.rined.portal.model.Tag;
import com.rined.portal.repositories.projections.TagUsageCount;
import com.rined.portal.services.TagService;
import com.rined.portal.utils.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TagController {
    private static final String COOKIE_PATH_TEMPLATE = "/tags/%s/change";

    private final TagService tagService;

    @GetMapping("/tags")
    public String tags(Model model) {
        List<TagUsageCount> tagsWithUsageCount = tagService.getTagsWithUsageCount();
        model.addAttribute("tags", tagsWithUsageCount);
        return "tag-list";
    }

    @GetMapping("/tags/create")
    public String createTagView(Model model) {
        List<Tag> tags = tagService.allTags();
        model.addAttribute("tags", tags);
        return "tag-create";
    }

    @PostMapping("/tags/create")
    @ResponseStatus(code = HttpStatus.CREATED)
    public String createTag(@Valid TagBriefDto newTag, Model model) {
        tagService.create(newTag);
        List<Tag> tags = tagService.allTags();
        model.addAttribute("tags", tags);
        return "tag-create";
    }

    @GetMapping("/tags/{tag}/change")
    public String tagChangeView(@PathVariable("tag") String tag, Model model) {
        TagDto tagByName = tagService.getTagByName(tag);
        model.addAttribute("tag", tagByName);
        return "tag-change";
    }

    @PostMapping("/tags/{tag}/change")
    public String updateTag(@Valid TagDto newTag,
                            HttpServletResponse response,
                            @PathVariable("tag") String tag) {
        tagService.update(newTag);
        String newTagName = newTag.getTag();
        CookieUtil.addTransformCookieValueToPath(
                response,
                CookieUtil.UPDATE_COOKIE,
                tag,
                CookieUtil::encode,
                COOKIE_PATH_TEMPLATE,
                newTagName
        );
        return String.format("redirect:/tags/%s/change", newTagName);
    }

    @PostMapping("/tags/delete")
    public String deleteTag(@RequestParam(value = "id") String id) {
        tagService.deleteById(id);
        return "redirect:/tags";
    }
}
