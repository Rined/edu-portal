package com.rined.blog.repositories.projections;

import com.rined.blog.model.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class UserProfileInfo {

    private final User user;

    private final List<UserTopicBrief> topics;

    private final List<UserCommentBrief> comments;

}
