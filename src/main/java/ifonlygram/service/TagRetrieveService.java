package ifonlygram.service;

import ifonlygram.dto.BlogCategory;

public interface TagRetrieveService {
    String getRandomTag(BlogCategory blogCategory);
    String getAllTags(BlogCategory blogCategory);
    String getDescriptionByTagAndBlogCategory(String tag, BlogCategory blogCategory);
}
