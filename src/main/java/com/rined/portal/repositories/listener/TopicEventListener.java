package com.rined.portal.repositories.listener;

import com.rined.portal.model.Topic;
import com.rined.portal.model.TopicInfo;
import com.rined.portal.model.User;
import com.rined.portal.repositories.TagRepository;
import com.rined.portal.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@SuppressWarnings("NullableProblems")
public class TopicEventListener extends AbstractMongoEventListener<Topic> {

    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Topic> event) {
        Topic topic = event.getSource();
        TopicInfo info = topic.getInfo();

        User author = info.getAuthor();
        if (Objects.isNull(author.getId())) {
            userRepository.save(author);
        }

        info.getTags().stream().filter(tag -> Objects.isNull(tag.getId()))
                .forEach(tagRepository::save);
    }
}
