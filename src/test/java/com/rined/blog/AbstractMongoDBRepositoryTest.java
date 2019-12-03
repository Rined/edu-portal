package com.rined.blog;

import com.rined.blog.changelogs.config.MongockConfiguration;
import com.rined.blog.properties.MongoConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"com.rined.blog.repositories"})
@Import({MongockConfiguration.class, MongoConfigurationProperties.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class AbstractMongoDBRepositoryTest {
}
