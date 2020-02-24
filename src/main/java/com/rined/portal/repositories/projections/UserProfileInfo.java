package com.rined.portal.repositories.projections;

import com.rined.portal.model.User;
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
