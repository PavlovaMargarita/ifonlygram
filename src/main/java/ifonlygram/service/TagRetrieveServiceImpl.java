package ifonlygram.service;

import ifonlygram.dto.BlogCategory;
import org.springframework.stereotype.Component;

@Component
public class TagRetrieveServiceImpl implements TagRetrieveService{
    @Override
    public String getRandomTag(BlogCategory blogCategory) {
        return null;
    }

    @Override
    public String getAllTags(BlogCategory blogCategory) {
        return null;
    }

    @Override
    public String getDescriptionByTagAndBlogCategory(String tag, BlogCategory blogCategory) {
        return null;
    }
}
