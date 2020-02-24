package com.rined.portal.repositories.listener;

import com.rined.portal.model.Tag;
import com.rined.portal.repositories.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TagEventListener extends AbstractMongoEventListener<Tag> {

    private final TopicRepository topicRepository;

    @Override
    public void onAfterDelete(AfterDeleteEvent<Tag> event) {
        Document source = event.getSource();
        String id = source.get("_id").toString();
        topicRepository.removeTagArrayElementsById(id);
    }
}
