package com.rined.blog.repositories;

import com.rined.blog.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String>, UserRepositoryCustom {

    Optional<User> findUserByName(String name);

    User getById(String id);

}
