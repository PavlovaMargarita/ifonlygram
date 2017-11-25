package ifonlygram.service;

import ifonlygram.dto.InfoWiki;
import ifonlygram.parser.WikipediaParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InfoWikiRetrieveServiceImpl implements InfoWikiRetrieveService {

    @Autowired
    private WikipediaParser wikipediaParser;

    @Override
    public InfoWiki getInfoWikiByName(String name) {
        return wikipediaParser.parse(name);
    }
}
