package ifonlygram.parser;

import ifonlygram.dto.InfoWiki;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ifonlygram.parser.WikipediaParserConstants.*;

@Component
public class WikipediaParser {

    public InfoWiki parse(String name) {
        Document doc;
        try {
            doc = Jsoup.connect(SEARCH_URL + name).timeout(TIMEOUT).get();
        } catch (IOException exc) {
            System.out.println(exc);
            return new InfoWiki();
        }

        //Get info table from wiki page
        Elements infobox = doc.body().getElementsByClass(INFOBOX);
        Element infoTable = infobox.get(ZERO);

        InfoWiki infoWiki = new InfoWiki();

        //place of birth
        String birthPlace = getPlaceByPropertyId(infoTable, PLACE_OF_BIRTH_CODE);
        infoWiki.addPlace(birthPlace);

        //place of death
        String deathPlace = getPlaceByPropertyId(infoTable, PLACE_OF_DEATH_CODE);
        infoWiki.addPlace(deathPlace);

        //year of birth
        Integer birthYear = getYearByPropertyId(infoTable, YEAR_OF_BIRTH_CODE);
        infoWiki.setYearOfBirth(birthYear);

        //year of death
        Integer deathYear = getYearByPropertyId(infoTable, YEAR_OF_DEATH_CODE);
        infoWiki.setYearOfDeath(deathYear);

        //job
        List<String> jobs = getJobByPropertyId(infoTable, JOB_CODE);
        infoWiki.setJobs(jobs);

        //family
        List<String> importantPeople = getFamily(infoTable);
        infoWiki.setImportantPeople(importantPeople);

        return infoWiki;
    }

    private List<String> getFamily(Element infoTable) {
        //get rows from table
        Elements rows = infoTable.select("tr");
        for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
            Element row = rows.get(rowIndex);

            Elements rowHeader = row.select("th");
            if (rowHeader.isEmpty()) {
                continue;
            }

            String headerOwnText = rowHeader.get(ZERO).ownText();
            boolean isWife = headerOwnText.contains(WIFE);
            boolean isHusband = headerOwnText.contains(HUSBAND);
            if (isWife || isHusband) {
                Elements familyCells = row.select("td");
                List<String> familyMembersList = new ArrayList();
                for (int j = 0; j < familyCells.size(); j++) {
                    Elements paragraphs = familyCells.get(j).getElementsByTag("p");
                    if (!paragraphs.isEmpty()) {
                        familyMembersList.add(paragraphs.get(ZERO).ownText());
                        continue;
                    }

                    Elements links = familyCells.get(j).getElementsByTag("a");
                    if (!links.isEmpty()) {
                        familyMembersList.add(links.get(ZERO).ownText());
                    }
                }
                return familyMembersList;
            }
        }
        return Collections.EMPTY_LIST;
    }

    private String getPlaceByPropertyId(Element infoTable, String propertyId) {
        Elements dataByWikidataPropertyId = infoTable.getElementsByAttributeValue(DATA_WIKIDATA_PROPERTY_ID, propertyId);
        if (dataByWikidataPropertyId.isEmpty()) {
            return StringUtils.EMPTY;
        }
        Elements titlesInDataWikidataPropertyId = dataByWikidataPropertyId.get(ZERO).getElementsByAttribute(TITLE);
        return titlesInDataWikidataPropertyId.attr(TITLE);
    }

    private List<String> getJobByPropertyId(Element infoTable, String propertyId) {
        Elements dataByWikidataPropertyId = infoTable.getElementsByAttributeValue(DATA_WIKIDATA_PROPERTY_ID, propertyId);
        if (dataByWikidataPropertyId.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        Elements jobsTitles = dataByWikidataPropertyId.get(ZERO).getElementsByAttribute(TITLE);
        List<String> jobs = new ArrayList<String>();
        for (int i = 0; i < jobsTitles.size(); i++) {
            Element job = jobsTitles.get(i);
            jobs.add(job.attr(TITLE));
        }
        return jobs;
    }

    private Integer getYearByPropertyId(Element infoTable, String propertyId) {
        Elements dataByWikidataPropertyId = infoTable.getElementsByAttributeValue(DATA_WIKIDATA_PROPERTY_ID, propertyId);
        if (dataByWikidataPropertyId.isEmpty()) {
            return ZERO;
        }
        Elements titleInDataWikidataPropertyId = dataByWikidataPropertyId.get(ZERO).getElementsByAttribute(TITLE);
        Elements elementsWithTitleYear = titleInDataWikidataPropertyId.attr(TITLE, YEAR);
        for (int i = 0; i < elementsWithTitleYear.size(); i++) {
            String ownText = elementsWithTitleYear.get(i).ownText();
            boolean isYear = ownText.matches(YEAR_REG_EXPRESSION);
            if (isYear) {
                return Integer.valueOf(ownText);
            }
        }
        return ZERO;
    }
}
