package ifonlygram.parser;

import ifonlygram.dto.InfoWiki;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import static ifonlygram.parser.WikipediaParserConstants.*;

@Component
public class WikipediaParser {

    public InfoWiki parse(String name) {
        if (name == null || name.isEmpty()) {
            return new InfoWiki();
        }
        String formattedName = getFormatedName(name);

        Document doc;
        try {
            doc = Jsoup.connect(SEARCH_URL + formattedName).timeout(TIMEOUT).get();
        } catch (IOException exc) {
            System.out.println(exc);
            return new InfoWiki();
        }

        //Get info table from wiki page
        Elements infobox = doc.body().getElementsByClass(INFOBOX);
        if (infobox.isEmpty()) {
            return new InfoWiki();
        }
        Element infoTable = infobox.get(ZERO);

        InfoWiki infoWiki = new InfoWiki();

        //place of birth
        List<String> birthPlace = getPlacesByPropertyId(infoTable, PLACE_OF_BIRTH_CODE);
        infoWiki.addPlaces(birthPlace);

        //place of death
        List<String> deathPlace = getPlacesByPropertyId(infoTable, PLACE_OF_DEATH_CODE);
        infoWiki.addPlaces(deathPlace);

        //year of birth
        Integer birthYear = getYearByPropertyId(infoTable, YEAR_OF_BIRTH_CODE);
        infoWiki.setYearOfBirth(birthYear);

        //year of death
        Integer deathYear = getYearByPropertyId(infoTable, YEAR_OF_DEATH_CODE);
        infoWiki.setYearOfDeath(deathYear);

        //job
        List<String> jobsByCode = getJobByPropertyId(infoTable, JOB_CODE);
        infoWiki.addJobs(jobsByCode);
        List<String> jobsByStyleFilter = getJobsByStyleFilter(infoTable);
        infoWiki.addJobs(jobsByStyleFilter);

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
                        Elements links = paragraphs.get(ZERO).getElementsByTag("a");
                        if (links == null || links.isEmpty()) {
                            familyMembersList.add(paragraphs.get(ZERO).ownText());
                        } else {
                            links.get(ZERO).ownText();
                            continue;
                        }
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

    private static List<String> getPlacesByPropertyId(Element infoTable, String propertyId) {
        Elements dataByWikidataPropertyId = infoTable.getElementsByAttributeValue(DATA_WIKIDATA_PROPERTY_ID, propertyId);
        if (dataByWikidataPropertyId.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        Elements titlesInDataWikidataPropertyId = dataByWikidataPropertyId.get(ZERO).getElementsByAttribute(TITLE);
        List<String> places = new ArrayList();
        for (int i = 0; i < titlesInDataWikidataPropertyId.size(); i++) {
            places.add(titlesInDataWikidataPropertyId.get(i).ownText());
        }
        return places;
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

    private String getFormatedName(String name) {
        String lowerCaseName = name.toLowerCase();
        StringTokenizer stringTokenizer = new StringTokenizer(lowerCaseName);
        String formattedName = new String();
        while (stringTokenizer.hasMoreTokens()) {
            String token = stringTokenizer.nextToken();
            formattedName += token.substring(0, 1).toUpperCase() + token.substring(1) + " ";
        }
        return formattedName;
    }

    private List<String> getJobsByStyleFilter(Element infoTable) {
        Elements dataByWikidataPropertyId = infoTable.getElementsByAttributeValue(STYLE, JOB_STYLE);
        if (dataByWikidataPropertyId.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        List<String> jobs = new ArrayList();
        for (int k = 0; k < dataByWikidataPropertyId.size(); k++) {
            Elements elem = dataByWikidataPropertyId.get(k).select("a");
            if (elem.isEmpty()) {
                jobs.add(dataByWikidataPropertyId.get(k).ownText());
            } else {
                jobs.add(elem.get(0).ownText());
            }
        }
        return jobs;
    }

}
