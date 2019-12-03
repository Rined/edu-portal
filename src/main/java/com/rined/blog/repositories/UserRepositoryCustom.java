package com.rined.blog.repositories;

import com.rined.blog.repositories.projections.UserCommentBrief;
import com.rined.blog.repositories.projections.UserTopicBrief;

import java.util.List;

public interface UserRepositoryCustom {

    List<UserCommentBrief> getUserCommentsByUserId(String id);

    List<UserTopicBrief> getUserTopicsByUserId(String id);
}
