package com.rined.portal.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TagBriefDto {

    @NotNull
    @NotBlank(message = "Tag name is mandatory")
    private String newTag;

}
