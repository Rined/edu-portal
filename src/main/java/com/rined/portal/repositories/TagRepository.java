package com.rined.portal.repositories;

import com.rined.portal.model.Tag;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends MongoRepository<Tag, String>, TagRepositoryCustom {

    boolean existsByTag(String tag);

    Optional<Tag> findByTag(String tag);

    Tag getByTag(String tag);

    List<Tag> getAllByTagNotIn(List<String> tags);

}
