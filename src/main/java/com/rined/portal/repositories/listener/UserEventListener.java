package com.rined.portal.repositories.listener;

import com.rined.portal.model.User;
import com.rined.portal.repositories.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventListener extends AbstractMongoEventListener<User> {

    private final TopicRepository topicRepository;

    @Override
    public void onAfterDelete(AfterDeleteEvent<User> event) {
        Document source = event.getSource();
        String id = source.get("_id").toString();
        topicRepository.removeUserElementsById(id);
    }
}
