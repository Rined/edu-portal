package com.rined.portal.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TopicBriefDto {

    @NotNull
    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotNull
    @NotBlank(message = "Keywords is mandatory")
    private String keywords;

    @NotNull
    @NotBlank(message = "Tags is mandatory")
    private String tags;

    @NotNull
    @NotBlank(message = "Topic content is mandatory")
    private String content;

}
