package ifonlygram.service;

import ifonlygram.dto.BlogCategory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TagRetrieveServiceImpl implements TagRetrieveService{
    @Override
    public String getRandomTag(BlogCategory blogCategory) {
        return null;
    }

    @Override
    public List<String> getAllTags(BlogCategory blogCategory) {
        return null;
    }

    @Override
    public String getRandomDescriptionByTagAndBlogCategory(String tag, BlogCategory blogCategory) {
        return null;
    }
}
