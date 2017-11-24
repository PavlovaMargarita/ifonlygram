package ifonlygram.service;

import ifonlygram.dto.Publication;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ifonlygram.util.SearchImage.findImageUrlByParameters;

@Component
public class PublicationGenerateServiceImpl implements PublicationGenerateService {
    @Override
    public List<Publication> generatePublications(final String tag, final String name, final String infoWiki) {

        List<String> requestParameters;
        if (shouldWeUseNameInSearch()) {
            requestParameters = Arrays.asList(name, tag, infoWiki);
        } else {
            requestParameters = Arrays.asList(tag, infoWiki);
        }

        List<String> urls = findImageUrlByParameters(requestParameters);

        List<Publication> publicationList = new ArrayList<Publication>();
        urls.forEach(url -> {
            Publication publication = new Publication();
            publication.setImageUrl(url);
            publication.getTags().add(tag);
            publicationList.add(publication);
        });

        return publicationList;
    }

    private boolean shouldWeUseNameInSearch() {
        return true;
    }
}
