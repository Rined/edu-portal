package com.rined.blog.changelogs.config;

import com.github.cloudyrock.mongock.Mongock;
import com.github.cloudyrock.mongock.SpringMongockBuilder;
import com.mongodb.MongoClient;
import com.rined.blog.properties.MongoConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongockConfiguration {
    private static final String CHANGE_LOGS_PACKAGE = "com.rined.blog.changelogs";

    @Bean
    public Mongock mongock(MongoConfigurationProperties mongoProperties, MongoClient mongoClient) {
        System.out.println(mongoProperties);
        return new SpringMongockBuilder(mongoClient, mongoProperties.getDatabase(), CHANGE_LOGS_PACKAGE)
                .build();
    }
    
}
