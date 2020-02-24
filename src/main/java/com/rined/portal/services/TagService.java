package com.rined.portal.services;

import com.rined.portal.converters.TagConverter;
import com.rined.portal.dto.TagBriefDto;
import com.rined.portal.dto.TagDto;
import com.rined.portal.exceptions.AlreadyExistException;
import com.rined.portal.exceptions.NotFoundException;
import com.rined.portal.model.Tag;
import com.rined.portal.repositories.TagRepository;
import com.rined.portal.repositories.projections.TagUsageCount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final TagConverter converter;

    public List<TagUsageCount> getTagsWithUsageCount() {
        List<TagUsageCount> tagUsageCounts = tagRepository.tagUsageAndCount();
        List<TagUsageCount> allTags = new ArrayList<>(tagUsageCounts);
        List<String> collect = tagUsageCounts.stream().map(TagUsageCount::getTag).collect(Collectors.toList());
        tagRepository.getAllByTagNotIn(collect).forEach(tag -> allTags.add(new TagUsageCount(tag.getTag(), 0)));
        return allTags;
    }

    public List<Tag> allTags() {
        return tagRepository.findAll();
    }

    public TagDto getTagByName(String tagName) {
        Optional<Tag> optionalTag = tagRepository.findByTag(tagName);
        if (!optionalTag.isPresent()) {
            throw new NotFoundException(String.format("Tag with name \"%s\" not found", tagName));
        }
        Tag tag = optionalTag.get();
        return converter.beanToDto(tag);
    }

    public void create(TagBriefDto newTag) {
        String tagName = newTag.getNewTag();
        if (tagRepository.existsByTag(tagName)) {
            throw new AlreadyExistException(String.format("Tag with name \"%s\" already exists", tagName));
        }
        Tag tag = converter.dtoBriefToBean(newTag);
        tagRepository.save(tag);
    }

    public void update(TagDto newTag) {
        boolean existedId = tagRepository.existsById(newTag.getId());
        if (!existedId) {
            throw new NotFoundException(String.format("Tag \"%s\" not found", newTag.getTag()));
        }

        boolean existsTagName = tagRepository.existsByTag(newTag.getTag());
        if (existsTagName) {
            throw new AlreadyExistException(String.format("Tag with name %s already exists", newTag.getTag()));
        }

        tagRepository.save(converter.dtoToBean(newTag));
    }

    public void deleteById(String id) {
        tagRepository.deleteById(id);
    }
}
