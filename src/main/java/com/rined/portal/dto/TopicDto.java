package com.rined.portal.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TopicDto {

    @NotNull(message = "Id can not be null")
    @NotBlank(message = "Id can not be empty")
    private String id;

    @NotNull(message = "Title can not be null")
    @NotBlank(message = "Title can not be empty")
    private String title;

    @NotNull(message = "Keywords can not be null")
    @NotBlank(message = "Keywords can not be empty")
    private String keywords;

    @NotNull(message = "Tags can not be null")
    @NotBlank(message = "Tags can not be empty")
    private String tags;

    @NotNull(message = "Content can not be null")
    @NotBlank(message = "Content can not be empty")
    private String content;
}
