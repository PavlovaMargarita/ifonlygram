package ifonlygram.util;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import static ifonlygram.parser.WikipediaParserConstants.TIMEOUT;

@Component
public class BingSearch {

    private static final String SEARCH_URL = "https://www.bing.com/images?q=";
    private static final String INFOBOX = "iusc";
    private static final int PHOTO_COUNT = 5;

    public static List<String> findImageUrlByParameters(final List<String> param) {
        try {
            String query = String.join("+", param);
            String formattedName = getFormatedName(query);

            Document doc = Jsoup.connect(SEARCH_URL + formattedName).timeout(TIMEOUT).get();

            List<String> urls = new ArrayList<>();
            //Get info table from wiki page
            Elements elements = doc.body().getElementsByClass(INFOBOX);
            for (int i = 0; i < PHOTO_COUNT; i++) {
                String infobox = elements.get(i).attr("m");
                JSONObject jsonObject = (JSONObject) JSONValue.parseWithException(infobox);
                String url = (String) jsonObject.get("murl");
                urls.add(url);
            }

            return urls;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }


    private static String getFormatedName(String name) {
        String lowerCaseName = name.toLowerCase();
        StringTokenizer stringTokenizer = new StringTokenizer(lowerCaseName);
        String formattedName = new String();
        while (stringTokenizer.hasMoreTokens()) {
            String token = stringTokenizer.nextToken();
            formattedName += token.substring(0, 1).toUpperCase() + token.substring(1) + " ";
        }
        return formattedName;
    }
}
