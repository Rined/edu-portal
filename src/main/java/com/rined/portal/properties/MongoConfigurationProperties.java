package com.rined.portal.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("spring.data.mongodb")
public class MongoConfigurationProperties {
    private int port;
    private String database;
    private String uri;
}
