package ifonlygram.service;

import ifonlygram.dto.BlogCategory;
import ifonlygram.dto.InfoWiki;
import ifonlygram.dto.Profile;
import ifonlygram.dto.Publication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Component
public class ProfileGenerateServiceImpl implements ProfileGenerateService {
    private static final int TAG_COUNT = 15;

    @Autowired
    private PublicationGenerateService publicationGenerateService;

    @Autowired
    private InfoWikiRetrieveService infoWikiRetrieveService;

    @Autowired
    private TagRetrieveService tagRetrieveService;

    @Autowired
    private CreateAvatarService createAvatarService;

    @Override
    public Profile generateProfile(String name, BlogCategory blogCategory) {

        InfoWiki infoWiki = infoWikiRetrieveService.getInfoWikiByName(name);

        List<String> allTagsForBlogCategory = tagRetrieveService.getAllTags(blogCategory);

        List<Publication> allPublication = new ArrayList<Publication>();

        List<String> wikiParameters = getParametersFromInfoWiki(infoWiki);
        for (String wikiParameter : wikiParameters) {
            String tag = getRandomTag(blogCategory);
            List<Publication> publications = publicationGenerateService.generatePublications(tag, name, wikiParameter); //contain image url and one tag

            // set description, location and add tags
            publications.forEach(post -> {
                //set description
                post.setDescription(tagRetrieveService.getRandomDescriptionByTagAndBlogCategory(tag, blogCategory));

                //set location
                if (infoWiki.getPlaces().contains(wikiParameter)) {
                    post.setLocation(wikiParameter);
                }

                //add tags
                post.getTags().addAll(getRandomTags(allTagsForBlogCategory, TAG_COUNT));

            });

            allPublication.addAll(publications);
        }

        Collections.shuffle(allPublication);
        Profile profile = new Profile();
        profile.setName(name);
        profile.setProfilePicture(createAvatarService.createAvatar(""));
        profile.setPublications(allPublication);

        return profile;
    }

    private List<String> getParametersFromInfoWiki(InfoWiki infoWiki) {
        final List<String> wikiParameters = new ArrayList<String>();
        wikiParameters.add(infoWiki.getYearOfBirth().toString());
        wikiParameters.add(infoWiki.getYearOfDeath().toString());
        wikiParameters.addAll(infoWiki.getJobs());
        wikiParameters.addAll(infoWiki.getImportantPeople());
        wikiParameters.addAll(infoWiki.getPlaces());
        return wikiParameters;
    }

    private String getRandomTag(BlogCategory blogCategory) {
        return tagRetrieveService.getRandomTag(blogCategory);
    }

    private List<String> getRandomTags(List<String> allTagsForBlogCategory, int count) {
        List<String> copy = new LinkedList<String>(allTagsForBlogCategory);
        Collections.shuffle(copy);
        int currentTagCount = allTagsForBlogCategory.size();
        return copy.subList(0, currentTagCount < count ? currentTagCount : count);
    }
}
