package com.rined.blog.controllers;

import com.rined.blog.dto.TopicBriefDto;
import com.rined.blog.dto.TopicDto;
import com.rined.blog.dto.TopicExtendedDto;
import com.rined.blog.model.Topic;
import com.rined.blog.repositories.projections.TopicInfoWithTags;
import com.rined.blog.services.TopicService;
import com.rined.blog.utils.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class TopicController {
    private static final String COOKIE_PATH_TEMPLATE = "/topics/%s/change";

    private final TopicService topicService;

    @GetMapping("/topics/{topic}")
    public String topicByTitle(@PathVariable("topic") String topicTitle, Model model) {
        Optional<Topic> topic = topicService.topicByTitle(topicTitle);
        topic.ifPresent(t -> model.addAttribute("topic", t));
        return "topic";
    }

    @GetMapping("/topics")
    public String topics(@RequestParam(value = "page", required = false, defaultValue = "0") int pageNumber,
                         @Value("${view.users.numberOfElementsOnPage}") int pageElements, Model model) {
        Page<TopicInfoWithTags> topics = topicService.getAllPageableTopics(pageNumber, pageElements);
        model.addAttribute("topicPage", topics);
        return "topic-list";
    }

    @GetMapping("/topics/create")
    public String topicCreateView() {
        return "topic-create";
    }

    @PostMapping("/topics/create")
    @ResponseStatus(code = HttpStatus.CREATED)
    public String topicCreate(@Valid TopicBriefDto topicBriefDto) {
        topicService.createTopic(topicBriefDto);
        return "topic-create";
    }

    @GetMapping("/topics/tags/{tag}")
    public String tagsTopic(@PathVariable("tag") String tag, Model model) {
        TopicExtendedDto topicsByTag = topicService.getTopicsByTag(tag);
        model.addAttribute("tagWithTopics", topicsByTag);
        return "tag";
    }

    @GetMapping("/topics/{topic}/change")
    public String topicChangeView(@PathVariable("topic") String topic, Model model) {
        TopicDto topicDto = topicService.getTopicDtoByName(topic);
        model.addAttribute("topic", topicDto);
        return "topic-change";
    }

    @PostMapping("/topics/{topic}/change")
    public String updateTopic(@Valid TopicDto changedTopic,
                            HttpServletResponse response,
                            @PathVariable("topic") String title) {
        topicService.update(changedTopic);
        String newTitle = changedTopic.getTitle();
        CookieUtil.addTransformCookieValueToPath(response, CookieUtil.UPDATE_COOKIE, title,
                CookieUtil::encode,
                COOKIE_PATH_TEMPLATE, newTitle);
        return String.format("redirect:/topics/%s/change", newTitle);
    }

    @PostMapping("/topics/delete")
    public String deleteTopic(@RequestParam(value = "id") String id) {
        topicService.deleteById(id);
        return "redirect:/topics";
    }
}