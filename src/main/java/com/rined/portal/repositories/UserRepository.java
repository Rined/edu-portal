package com.rined.portal.repositories;

import com.rined.portal.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String>, UserRepositoryCustom {

    Optional<User> findUserByName(String name);

    User getById(String id);

    boolean existsByName(String name);

}
