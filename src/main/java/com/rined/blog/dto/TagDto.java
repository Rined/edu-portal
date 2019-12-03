package com.rined.blog.dto;

import lombok.*;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TagDto {

    @NotNull
    @NotBlank(message = "Id can not be null")
    private String id;

    @NotNull
    @NotBlank(message = "Tag name is mandatory")
    private String tag;
}
