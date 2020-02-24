package com.rined.portal.repositories;

import com.rined.portal.repositories.projections.UserCommentBrief;
import com.rined.portal.repositories.projections.UserTopicBrief;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.ObjectOperators.ObjectToArray.valueOfToArray;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final MongoTemplate template;

    //  db.topic.aggregate([
    //    {$unwind: "$comments"},
    //    {$project: {"topicTitle": "$info.title", "commentVote": "$comments.vote", "commentDate": "$comments.date", comment_map: {$objectToArray: "$comments.author"}}},
    //    {$project: {_id: 0, "topicTitle": 1, "commentVote": 1, "commentDate": 1, "comment.author_id": {$arrayElemAt: ["$comment_map.v", 1]}}},
    //    {$match: {"comment.author_id": new ObjectId("5d90aa61c3918942382a23f8")}},
    //    {$project: {"topicTitle": 1, "commentVote": 1, "commentDate": 1}}
    //  ]);
    @Override
    public List<UserCommentBrief> getUserCommentsByUserId(String id) {
        Aggregation aggregation = Aggregation.newAggregation(
                unwind("comments"),
                project().and("info.title").as("topicTitle")
                        .and("comments.vote").as("commentVote")
                        .and("comments.date").as("commentDate")
                        .and(valueOfToArray("comments.author")).as("comment_map"),
                project().andExclude("_id").andInclude("topicTitle", "commentVote", "commentDate")
                        .and("comment_map.v").arrayElementAt(1).as("comment.author_id"),
                match(where("comment.author_id").is(new ObjectId(id))),
                project().andInclude("topicTitle", "commentVote", "commentDate")
        );
        return template.aggregate(aggregation, "topic", UserCommentBrief.class).getMappedResults();
    }

    //    db.topic.aggregate([
    //      {$project: {"info.title": 1, "vote": 1, "date": 1, topic_author_map: {$objectToArray: "$info.author"}}},
    //      {$project: {_id: 0, "info.title": 1, "vote": 1, "date": 1, "topic_author": {$arrayElemAt: ["$topic_author_map.v", 1]}}},
    //      {$match: {"topic_author": new ObjectId("5d90aa61c3918942382a23f7")}},
    //      {$project: {"topicTitle": "$info.title", "topicVote": "$vote", "topicDate": "$date"}}
    //    ]);
    @Override
    public List<UserTopicBrief> getUserTopicsByUserId(String id) {
        Aggregation aggregation = Aggregation.newAggregation(
                project().and("info.title").as("topicTitle")
                        .and("vote").as("topicVote")
                        .and("date").as("topicDate")
                        .and(valueOfToArray("info.author")).as("topic_author_map"),
                project().andExclude("_id").andInclude("topicTitle", "topicVote", "topicDate")
                        .and("$topic_author_map.v").arrayElementAt(1).as("topic_author"),
                match(where("topic_author").is(new ObjectId(id))),
                project().andInclude("topicTitle", "topicVote", "topicDate")
        );
        return template.aggregate(aggregation, "topic", UserTopicBrief.class).getMappedResults();
    }
}
