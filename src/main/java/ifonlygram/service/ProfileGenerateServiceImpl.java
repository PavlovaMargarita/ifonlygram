package ifonlygram.service;

import ifonlygram.dto.InfoWiki;
import ifonlygram.dto.Profile;
import ifonlygram.dto.Publication;
import ifonlygram.dto.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProfileGenerateServiceImpl implements ProfileGenerateService {

    @Autowired
    private PublicationGenerateService publicationGenerateService;

    @Override
    public Profile generateProfile(RequestInfo requestInfo) {

        InfoWiki infoWiki = getInfoWiki();

        List<Publication> allPublication = new ArrayList<Publication>();

        List<String> wikiParameters = getParametersFromInfoWiki(infoWiki);
        for(String wikiParameter : wikiParameters) {
            String tag = getRandomTag();
            List<Publication> publications = publicationGenerateService.generatePublications(tag, requestInfo.getName(), wikiParameter);
            allPublication.addAll(publications);
        }

        Profile profile = new Profile();
        profile.setName(requestInfo.getName());
        profile.setPublications(allPublication);

        return profile;
    }

    private List<String> getParametersFromInfoWiki(InfoWiki infoWiki) {
        final List<String> wikiParameters = new ArrayList<String>();
        wikiParameters.add(infoWiki.getYearOfBirth().toString());
        wikiParameters.add(infoWiki.getYearOfDeath().toString());
        wikiParameters.add(infoWiki.getJob());
        wikiParameters.addAll(infoWiki.getImportantPeople());
        wikiParameters.addAll(infoWiki.getPlaces());
        return wikiParameters;
    }

    private String getRandomTag() {
        return null;
    }

    private InfoWiki getInfoWiki() {
        return null;
    }
}
