package com.rined.portal.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("view.error.template")
public class ErrorViewTemplateProperties {
    private String name;
    private String codeAlias;
    private String descriptionAlias;
}
