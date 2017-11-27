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

import static ifonlygram.parser.WikipediaParserConstants.AVATAR_CODE;
import static ifonlygram.parser.WikipediaParserConstants.BING_SEARCH_URI;
import static ifonlygram.parser.WikipediaParserConstants.B_RESULTS;
import static ifonlygram.parser.WikipediaParserConstants.DATA_WIKIDATA_PROPERTY_ID;
import static ifonlygram.parser.WikipediaParserConstants.EMPTY_STRING;
import static ifonlygram.parser.WikipediaParserConstants.HUSBAND;
import static ifonlygram.parser.WikipediaParserConstants.INFOBOX;
import static ifonlygram.parser.WikipediaParserConstants.JOB_CODE;
import static ifonlygram.parser.WikipediaParserConstants.JOB_STYLE;
import static ifonlygram.parser.WikipediaParserConstants.PLACE_OF_BIRTH_CODE;
import static ifonlygram.parser.WikipediaParserConstants.PLACE_OF_DEATH_CODE;
import static ifonlygram.parser.WikipediaParserConstants.STYLE;
import static ifonlygram.parser.WikipediaParserConstants.TIMEOUT;
import static ifonlygram.parser.WikipediaParserConstants.TITLE;
import static ifonlygram.parser.WikipediaParserConstants.WIFE;
import static ifonlygram.parser.WikipediaParserConstants.WIKIPEDIA;
import static ifonlygram.parser.WikipediaParserConstants.YEAR;
import static ifonlygram.parser.WikipediaParserConstants.YEAR_OF_BIRTH_CODE;
import static ifonlygram.parser.WikipediaParserConstants.YEAR_OF_DEATH_CODE;
import static ifonlygram.parser.WikipediaParserConstants.YEAR_REG_EXPRESSION;
import static ifonlygram.parser.WikipediaParserConstants.ZERO;

@Component
public class WikipediaParser {

    public InfoWiki parse(String name) {
        String wikipediaSearchURI =  getWikiSearchURI(name);
        Document doc;
        try {
            doc = Jsoup.connect(wikipediaSearchURI).timeout(TIMEOUT).get();
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

        //avatar URI
        String avatarURI = getAvatarUrl(infoTable);
        infoWiki.setAvatarURI(avatarURI);

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

    private List<String> getPlacesByPropertyId(Element infoTable, String propertyId) {
        Elements dataByWikidataPropertyId = infoTable.getElementsByAttributeValue(DATA_WIKIDATA_PROPERTY_ID, propertyId);
        if (dataByWikidataPropertyId.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        Elements titlesInDataWikidataPropertyId = dataByWikidataPropertyId.get(ZERO).getElementsByAttribute(TITLE);
        if (titlesInDataWikidataPropertyId == null || titlesInDataWikidataPropertyId.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
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
        if (jobsTitles == null) {
            return Collections.EMPTY_LIST;
        }
        List<String> jobs = new ArrayList<>();
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

    private List<String> getJobsByStyleFilter(Element infoTable) {
        Elements dataByWikidataPropertyId = infoTable.getElementsByAttributeValue(STYLE, JOB_STYLE);
        if (dataByWikidataPropertyId == null || dataByWikidataPropertyId.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        List<String> jobs = new ArrayList();
        for (int k = 0; k < dataByWikidataPropertyId.size(); k++) {
            Elements elem = dataByWikidataPropertyId.get(k).select("a");
            if (elem.isEmpty()) {
                jobs.add(dataByWikidataPropertyId.get(k).ownText());
            } else {
                jobs.add(elem.get(ZERO).ownText());
            }
        }
        return jobs;
    }

    private String getAvatarUrl(Element infoTable) {
        Elements dataByWikidataPropertyId = infoTable.getElementsByAttributeValue(DATA_WIKIDATA_PROPERTY_ID, AVATAR_CODE);
        if (dataByWikidataPropertyId == null && dataByWikidataPropertyId.isEmpty()) {
            return EMPTY_STRING;
        }
        Elements imageTag = dataByWikidataPropertyId.get(0).getElementsByTag("img");
        if (imageTag == null || imageTag.isEmpty()) {
            return EMPTY_STRING;
        }
        return imageTag.get(ZERO).attr("src");
    }

    private String getWikiSearchURI(String name) {
        Document doc;
        try {
            doc = Jsoup.connect(BING_SEARCH_URI + name).timeout(TIMEOUT).get();
        } catch (IOException exc) {
            System.out.println(exc);
            return EMPTY_STRING;
        }
        Element bResultsElement = doc.body().getElementById(B_RESULTS);
        if (bResultsElement == null) {
            return EMPTY_STRING;
        }

        Elements listItems = bResultsElement.getElementsByTag("li");
        if (listItems == null) {
            return EMPTY_STRING;
        }
        for (int k = 0; k < listItems.size(); k++) {
            Element elem = listItems.get(k);
            Elements links = elem.getElementsByTag("a");
            if (links == null || links.isEmpty()) {
                continue;
            }
            String href = links.get(0).attr("href");
            if (href != null && href.contains(WIKIPEDIA)) {
                return href;
            }
        }
        return EMPTY_STRING;
    }
}
