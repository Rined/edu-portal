package com.rined.portal.repositories;

import com.rined.portal.repositories.projections.UserCommentBrief;
import com.rined.portal.repositories.projections.UserTopicBrief;

import java.util.List;

public interface UserRepositoryCustom {

    List<UserCommentBrief> getUserCommentsByUserId(String id);

    List<UserTopicBrief> getUserTopicsByUserId(String id);
}
