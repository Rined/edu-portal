package com.rined.blog.repositories;

import com.rined.blog.AbstractMongoDBRepositoryTest;
import com.rined.blog.model.User;
import com.rined.blog.repositories.projections.UserCommentBrief;
import com.rined.blog.repositories.projections.UserTopicBrief;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозитори пользователей должен ")
class UserRepositoryImplTest extends AbstractMongoDBRepositoryTest {

    @Autowired
    private UserRepository repository;

    private String id;

    @BeforeEach
    void setUp() {
        Optional<User> user = repository.findUserByName("Rined");
        user.ifPresent(usr -> id = usr.getId());
    }

    @Test
    @DisplayName("корректно возвращать информацию по комментарию по id пользователя")
    void getUserCommentsByUserId() {
        List<UserCommentBrief> userCommentsByUserId = repository.getUserCommentsByUserId(id);
        assertThat(userCommentsByUserId).isNotEmpty()
                .allMatch(userComment -> Objects.nonNull(userComment.getCommentDate()))
                .allMatch(userComment -> Objects.nonNull(userComment.getTopicTitle()));
    }

    @Test
    @DisplayName("корректно возвращать информацию по топикам по id пользователя")
    void getUserTopicsByUserId() {
        List<UserTopicBrief> userTopicsByUserId = repository.getUserTopicsByUserId(id);
        assertThat(userTopicsByUserId).isNotEmpty()
                .allMatch(userTopic -> Objects.nonNull(userTopic.getTopicDate()))
                .allMatch(userTopic -> Objects.nonNull(userTopic.getTopicTitle()));
    }
}