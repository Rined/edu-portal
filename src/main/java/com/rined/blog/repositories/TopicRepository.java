package com.rined.blog.repositories;

import com.rined.blog.model.Topic;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TopicRepository extends MongoRepository<Topic, String>, TopicRepositoryCustom {

    Optional<Topic> findTopicByInfoTitle(String title);

    boolean existsByInfoTitle(String title);

    Topic getById(String id);

}
