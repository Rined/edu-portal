package com.rined.blog.repositories;


import com.rined.blog.repositories.projections.TagUsageCount;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.ObjectOperators.ObjectToArray.valueOfToArray;

@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepositoryCustom {
    private final MongoTemplate template;

    //db.topic.aggregate([
    //      {$project: {_id:0, tag: "$info.tags"}},
    //      {$unwind: "$tag"},
    //      {$group: {"_id": "$tag", "count":{"$sum": 1}}},
    //      {$project: {_id:0, count: 1, tag_map: {$objectToArray: "$_id"}}},
    //      {$project: {count:1, tag_id: {$arrayElemAt: ["$tag_map.v", 1]}}},
    //      {$lookup: {from: "tags", localField: "tag_id", foreignField: "_id", as: "tag"}},
    //      {$unwind: "$tag"},
    //      {$project: {count:1, tag: "$tag.tag"}}
    //]);
    @Override
    public List<TagUsageCount> tagUsageAndCount() {
        Aggregation aggregation = Aggregation.newAggregation(
                project().andExclude("_id").and("info.tags").as("tag"),
                unwind("tag"),
                group("tag").count().as("count"),
                project().andExclude("_id").andInclude("count").and(valueOfToArray("_id")).as("tag_map"),
                project().andInclude("count").and("tag_map.v").arrayElementAt(1).as("tag_id"),
                lookup("tags", "tag_id", "_id", "tag"),
                unwind("tag"),
                project().andInclude("count").and("tag.tag").as("tag"),
                sort(Sort.by("count").descending())
        );
        return template.aggregate(aggregation, "topic", TagUsageCount.class).getMappedResults();
    }
}
