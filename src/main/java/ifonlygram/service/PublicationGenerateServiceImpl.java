package ifonlygram.service;

import ifonlygram.dto.Publication;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static ifonlygram.util.BingSearch.findImageUrlByParameters;

@Component
public class PublicationGenerateServiceImpl implements PublicationGenerateService {
    private static final Random random = new Random();
    private static final String UTF_8 = "UTF-8";

    @Override
    public List<Publication> generatePublications(final String tag, final String name, final String infoWiki) {
        try {
            String tagUTF8 = URLEncoder.encode(tag, UTF_8);
            String infoWikiUTF8 = URLEncoder.encode(infoWiki, UTF_8);

            List<String> requestParameters;
            if (shouldWeUseNameInSearch()) {
                String nameUTF8 = URLEncoder.encode(name, UTF_8);
                requestParameters = Arrays.asList(nameUTF8, tagUTF8, infoWikiUTF8);
            } else {
                requestParameters = Arrays.asList(tagUTF8, infoWikiUTF8);
            }

            List<String> urls = findImageUrlByParameters(requestParameters);

//            List<String> urls = new ArrayList<String>(
//                    Arrays.asList("https://upload.wikimedia.org/wikipedia/commons/thumb/9/92/CindyCrawfordOct09.jpg/250px-CindyCrawfordOct09.jpg",
//                            "https://i.ytimg.com/vi/gmVWrbrJbhQ/hqdefault.jpg",
//                            "http://myprodiets.info/wp-content/uploads/2017/05/polina-gagarina-pohudela.jpg"));

            List<Publication> publicationList = new ArrayList<Publication>();
            urls.forEach(url -> {
                Publication publication = new Publication();
                publication.setImageUrl(url);
                publication.getTags().add(tag);
                publicationList.add(publication);
            });

            return publicationList;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean shouldWeUseNameInSearch() {
        return random.nextBoolean();
    }
}
