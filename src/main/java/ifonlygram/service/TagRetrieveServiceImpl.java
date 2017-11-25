package ifonlygram.service;

import ifonlygram.dto.BlogCategory;
import ifonlygram.dto.InstaTag;
import ifonlygram.insta.InstaFileParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class TagRetrieveServiceImpl implements TagRetrieveService {

    @Autowired
    InstaFileParser fileParser;


    //При неверных аргумениах или пустом результате возвразается null
    @Override
    public String getRandomTag(BlogCategory blogCategory) {

        if (blogCategory == null) {
            return null;
        }
        List<InstaTag> instaTags = fileParser.getAllTagList();
        InstaTag randomTag = null;
        if (instaTags != null && !instaTags.isEmpty()) {

            List<InstaTag> categoryListTag = instaTags.stream().filter(item -> item.getCategory() == blogCategory)
                    .collect(Collectors.toCollection(ArrayList::new));
            Random ran = new Random();
            int randomIndex = 0;
            if (categoryListTag.size() > 1) {
                randomIndex = ran.nextInt(categoryListTag.size() - 1);
            }
            randomTag = categoryListTag.get(randomIndex);
        }
        if (randomTag != null) {
            return randomTag.getTagName();
        }
        return null;

    }

    //При неверных аргумениах или пустом результате возвразается null
    @Override
    public List<String> getAllTags(BlogCategory blogCategory) {
        if (blogCategory == null) {
            return null;
        }

        List<InstaTag> instaTags = fileParser.getAllTagList();
        List<String> categoryListTag = null;
        if (instaTags != null && !instaTags.isEmpty()) {

            categoryListTag = instaTags.stream().filter(item -> item.getCategory() == blogCategory).map(InstaTag::getTagName)
                    .collect(Collectors.toCollection(ArrayList::new));
        }
        return categoryListTag;
    }

    //При неверных аргумениах или пустом результате возвразается null
    @Override
    public String getRandomDescriptionByTagAndBlogCategory(String tag, BlogCategory blogCategory) {

        if (tag == null || blogCategory == null) {
            return null;
        }
        List<InstaTag> instaTags = fileParser.getAllTagList();
        if (instaTags != null && !instaTags.isEmpty()) {
            InstaTag randomTag = null;
            List<InstaTag> categoryListTag = instaTags.stream().filter(item -> (item.getCategory() == blogCategory && (tag).equals(item.getTagName())))
                    .collect(Collectors.toCollection(ArrayList::new));
            if (categoryListTag != null && !categoryListTag.isEmpty()) {
                randomTag = categoryListTag.get(0);
                Random ran = new Random();

                int randomIndex = 0;
                if (randomTag.getDescription().size() > 1) {
                    randomIndex = ran.nextInt(randomTag.getDescription().size() - 1);
                }
                String randomDescription = randomTag.getDescription().get(randomIndex);
                return randomDescription;
            }

        }
        return null;

    }
}
