package ifonlygram.service;

import ifonlygram.dto.BlogCategory;

import java.util.List;

public interface TagRetrieveService {
    String getRandomTag(BlogCategory blogCategory);
    List<String> getAllTags(BlogCategory blogCategory);
    String getRandomDescriptionByTagAndBlogCategory(String tag, BlogCategory blogCategory);
}
