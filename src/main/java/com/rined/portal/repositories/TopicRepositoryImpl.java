package com.rined.portal.repositories;

import com.rined.portal.model.Topic;
import com.rined.portal.repositories.projections.TopicBrief;
import com.rined.portal.repositories.projections.TopicInfoBrief;
import com.rined.portal.repositories.projections.TopicInfoWithTags;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.ObjectOperators.ObjectToArray.valueOfToArray;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@RequiredArgsConstructor
public class TopicRepositoryImpl implements TopicRepositoryCustom {

    private final MongoTemplate template;

    //    db.topic.aggregate([
//            {$project: {topicId: "$_id", title: "$info.title", tags: "$info.tags", author: "$info.author", date: "$date", vote: "$vote"}},
//            {$unwind: "$tags"},
//            {$project: {topicId: 1, title: 1, date: 1, vote: 1, tag_map: {$objectToArray: "$tags"}, author_map: {$objectToArray: "$author"}}},
//            {$project: {topicId: 1, title: 1, date: 1, vote: 1, tag_id: {$arrayElemAt: ["$tag_map.v", 1]}, author_id: {$arrayElemAt: ["$author_map.v", 1]}}},
//            {$lookup: {from: "tags", localField: "tag_id", foreignField: "_id", as: "tag"}},
//            {$lookup: {from: "user", localField: "author_id", foreignField: "_id", as: "author"}},
//            {$unwind: "$author"},
//            {$unwind: "$tag"},
//            {$group: {"_id": "$topicId", topicTitle: {$first: "$title"}, topicDate: {$first: "$date"}, topicVote: {$first: "$vote"}, tags: {$addToSet: "$tag.tag"}, authorName: {$first: "$author.name"}, authorId: {$first: "$author._id"}}},
//            {$match: {tags: {$elemMatch:{$eq: "JavaEE"}}}},
//            {$project: {_id: 0}}
//          ]);
    @Override
    public List<TopicInfoWithTags> getTopicsWithTagsByTag(String tag) {
        Aggregation aggregation = Aggregation.newAggregation(
                project().andInclude("date").andInclude("vote").and("_id").as("topicId")
                        .and("info.title").as("title")
                        .and("info.tags").as("tags")
                        .and("info.author").as("author"),
                unwind("tags"),
                project().andInclude("topicId").andInclude("title").andInclude("date").andInclude("vote")
                        .and(valueOfToArray("tags")).as("tag_map")
                        .and(valueOfToArray("author")).as("author_map"),
                project().andInclude("topicId").andInclude("title").andInclude("date").andInclude("vote")
                        .and("tag_map.v").arrayElementAt(1).as("tag_id")
                        .and("author_map.v").arrayElementAt(1).as("author_id"),
                lookup("tags", "tag_id", "_id", "tag"),
                lookup("user", "author_id", "_id", "author"),
                unwind("author"),
                unwind("tag"),
                group("topicId")
                        .first("title").as("topicTitle")
                        .first("date").as("topicDate")
                        .first("vote").as("vote")
                        .addToSet("tag.tag").as("tags")
                        .first("author.name").as("authorName")
                        .first("author._id").as("authorId"),
                match(where("tags").is(tag)),
                project().andExclude("_id")
        );
        return template.aggregate(aggregation, "topic", TopicInfoWithTags.class).getMappedResults();
    }

    //    db.topic.aggregate([
//      {$project: {title: "$info.title", tags: "$info.tags", author: "$info.author", date: "$date"}},
//      {$unwind: "$tags"},
//      {$project: {title: 1, date: 1, tag_map: {$objectToArray: "$tags"}, author_map: {$objectToArray: "$author"}}},
//      {$project: {title: 1, date: 1, tag_id: {$arrayElemAt: ["$tag_map.v", 1]}, author_id: {$arrayElemAt: ["$author_map.v", 1]}}},
//      {$lookup: {from: "tags", localField: "tag_id", foreignField: "_id", as: "tag"}},
//      {$match: {"tag.tag": "JavaEE"}},
//      {$lookup: {from: "user", localField: "author_id", foreignField: "_id", as: "author"}},
//      {$unwind: "$author"},
//      {$project: {_id: 0, topicTitle: "$title", authorName: "$author.name", authorId: "$author._id", topicDate: "$date"}}
//    ]);
    @Override
    public List<TopicInfoBrief> getTopicsByTag(String tag) {
        Aggregation aggregation = Aggregation.newAggregation(
                project().andInclude("date")
                        .and("info.title").as("title")
                        .and("info.tags").as("tags")
                        .and("info.author").as("author"),
                unwind("tags"),
                project().andInclude("title").andInclude("date")
                        .and(valueOfToArray("tags")).as("tag_map")
                        .and(valueOfToArray("author")).as("author_map"),
                project().andInclude("title").andInclude("date")
                        .and("tag_map.v").arrayElementAt(1).as("tag_id")
                        .and("author_map.v").arrayElementAt(1).as("author_id"),
                lookup("tags", "tag_id", "_id", "tag"),
                match(where("tag.tag").is(tag)),
                lookup("user", "author_id", "_id", "author"),
                unwind("author"),
                project().andExclude("_id").and("title").as("topicTitle")
                        .and("author.name").as("authorName")
                        .and("author._id").as("authorId")
                        .and("date").as("topicDate")
        );
        return template.aggregate(aggregation, "topic", TopicInfoBrief.class).getMappedResults();
    }

    //    db.topic.aggregate([
//      {$project: {topicId: "$_id", title: "$info.title", tags: "$info.tags", author: "$info.author", date: "$date", vote: "$vote"}},
//      {$unwind: "$tags"},
//      {$project: {topicId: 1, title: 1, date: 1, vote: 1, tag_map: {$objectToArray: "$tags"}, author_map: {$objectToArray: "$author"}}},
//      {$project: {topicId: 1, title: 1, date: 1, vote: 1, tag_id: {$arrayElemAt: ["$tag_map.v", 1]}, author_id: {$arrayElemAt: ["$author_map.v", 1]}}},
//      {$lookup: {from: "tags", localField: "tag_id", foreignField: "_id", as: "tag"}},
//      {$lookup: {from: "user", localField: "author_id", foreignField: "_id", as: "author"}},
//      {$unwind: "$author"},
//      {$unwind: "$tag"},
//      {$group: {"_id": "$topicId", topicTitle: {$first: "$title"}, topicDate: {$first: "$date"}, topicVote: {$first: "$vote"}, tags: {$addToSet: "$tag.tag"}, authorName: {$first: "$author.name"}, authorId: {$first: "$author._id"}}},
//      {$sort: {vote: -1}},
//      {$skip: 1},
//      {$limit: 1},
//      {$project: {_id: 0}}
//    ]);
    @Override
    public Page<TopicInfoWithTags> findPageableAll(Pageable pageable, long count) {
        Aggregation aggregation = Aggregation.newAggregation(
                project().andInclude("vote").and("_id").as("topicId")
                        .and("info.title").as("title")
                        .and("info.tags").as("tags")
                        .and("info.author").as("author"),
                unwind("tags"),
                project().andInclude("topicId").andInclude("title").andInclude("vote")
                        .and(valueOfToArray("tags")).as("tag_map")
                        .and(valueOfToArray("author")).as("author_map"),
                project().andInclude("topicId").andInclude("title").andInclude("vote")
                        .and("tag_map.v").arrayElementAt(1).as("tag_id")
                        .and("author_map.v").arrayElementAt(1).as("author_id"),
                lookup("tags", "tag_id", "_id", "tag"),
                lookup("user", "author_id", "_id", "author"),
                unwind("author"),
                unwind("tag"),
                group("topicId")
                        .first("title").as("topicTitle")
                        .first("vote").as("vote")
                        .addToSet("tag.tag").as("tags")
                        .first("author.name").as("authorName")
                        .first("author._id").as("authorId"),
                sort(pageable.getSort()),
                skip((long) pageable.getPageNumber() * pageable.getPageSize()),
                limit(pageable.getPageSize()),
                project().andExclude("_id")
        );
        List<TopicInfoWithTags> topicsInfo =
                template.aggregate(aggregation, "topic", TopicInfoWithTags.class)
                        .getMappedResults();
        return new PageImpl<>(topicsInfo, pageable, count);
    }

    //  db.topic.aggregate([
//    {$match: {"info.title": "Awesome JPA"}},
//    {$project: {"id": "$_id", "title": "$info.title", "content": "$content.text", "keywords": "$info.keywords", "tags": "$info.tags"}},
//    {$unwind: "$tags"},
//    {$project: {id:1, title: 1, content:1, keywords: 1, "tag_map": {$objectToArray: "$tags"}}},
//    {$project: {id:1, title:1, content:1, keywords:1, "tag_id": {$arrayElemAt: ["$tag_map.v", 1]}}},
//    {$lookup: {from: "tags", localField: "tag_id", foreignField: "_id", as: "tag"}},
//    {$unwind: "$tag"},
//    {$group: {"_id": "$id", title: {$first: "$title"}, content: {$first: "$content"}, keywords: {$first: "$keywords"}, tags: {$addToSet: "$tag.tag"}}}
//  ]).pretty();
    @Override
    public TopicBrief findTopicBriefByTopicName(String topic) {
        Aggregation aggregation = Aggregation.newAggregation(
                match(where("info.title").is(topic)),
                project().and("_id").as("id")
                        .and("info.title").as("title")
                        .and("content.text").as("content")
                        .and("info.keywords").as("keywords")
                        .and("info.tags").as("tags"),
                unwind("tags"),
                project().andInclude("id").andInclude("title").andInclude("content").andInclude("keywords")
                        .and(valueOfToArray("tags")).as("tag_map"),
                project().andInclude("id").andInclude("title").andInclude("content").andInclude("keywords")
                        .and("tag_map.v").arrayElementAt(1).as("tag_id"),
                lookup("tags", "tag_id", "_id", "tag"),
                unwind("tag"),
                group("id")
                        .first("title").as("title")
                        .first("content").as("content")
                        .first("keywords").as("keywords")
                        .addToSet("tag.tag").as("tags")
        );
        return template.aggregate(aggregation, "topic", TopicBrief.class).getUniqueMappedResult();
    }

    @Override
    public void removeTagArrayElementsById(String id) {
        Query query = Query.query(Criteria.where("$id").is(new ObjectId(id)));
        Update update = new Update().pull("info.tags", query);
        template.updateMulti(new Query(), update, Topic.class);
    }

    @Override
    public void removeUserElementsById(String id) {
        Query commentQuery = Query.query(Criteria.where("author.$id").is(new ObjectId(id)));
        Update commentUpdate = new Update().pull("comments", commentQuery);
        template.updateMulti(new Query(), commentUpdate, Topic.class);

        //db.topic.remove({ "info.author.$id" : new ObjectId("5da26f4ec391892d846b70f5")})
        Query query = Query.query(Criteria.where("info.author.$id").is(new ObjectId(id)));
        template.remove(query, Topic.class);
    }
}
