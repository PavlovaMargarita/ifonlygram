package ifonlygram.util;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static ifonlygram.parser.WikipediaParserConstants.TIMEOUT;

@Component
public class BingSearch {

    private static final String SEARCH_URL = "https://www.bing.com/images?q=";
    private static final String IUSC_CLASS = "iusc";
    private static final int PHOTO_COUNT = 5;

    public static List<String> findImageUrlByParameters(final List<String> param) {
        try {
            String query = String.join("+", param);

            Document doc = Jsoup.connect(SEARCH_URL + query).timeout(TIMEOUT).get();

            List<String> urls = new ArrayList<>();
            //Get info table from wiki page
            Elements elements = doc.body().getElementsByClass(IUSC_CLASS);
            for (int i = 0; i < PHOTO_COUNT; i++) {
                String photoInfo = elements.get(i).attr("m");
                JSONObject jsonObject = (JSONObject) JSONValue.parseWithException(photoInfo);
                String url = (String) jsonObject.get("murl");
                urls.add(url);
            }

            return urls;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

}
