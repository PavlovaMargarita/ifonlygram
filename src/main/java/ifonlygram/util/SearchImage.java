package ifonlygram.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SearchImage {
    private static String WEATHER_URL = "https://api.qwant.com/api/search/images?count=10&offset=1&q=";

    public static List<String> findImageUrlByParameters(final List<String> param) {
        WEATHER_URL += String.join("+", param);
        System.out.println(WEATHER_URL);////////////////
        URL url = SearchImage.createUrl(WEATHER_URL); // создаем объект URL из указанной в параметре строки
        String resultJson = SearchImage.parseUrl(url);
        List linksIMG = SearchImage.parseCurrentIMGJson(resultJson);
        return linksIMG;
    }

    private static URL createUrl(String link) {
        try {
            return new URL(link);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String parseUrl(URL url) {
        if (url == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    private static List<String> parseCurrentIMGJson(String resultJson) {
        ArrayList<String> linksIMG = new ArrayList<>();
        try {
            JSONObject jsonObject = (JSONObject) JSONValue.parseWithException(resultJson);
            JSONObject dataJsonObject = (JSONObject) jsonObject.get("data");
            JSONObject resultJsonObject = (JSONObject) dataJsonObject.get("result");
            JSONArray itemsJsonArray = (JSONArray) resultJsonObject.get("items");
            for (JSONObject anItemsJsonArray : (Iterable<JSONObject>) itemsJsonArray) {
                linksIMG.add(String.valueOf(anItemsJsonArray.get("media")));
            }
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
            System.out.println("ERROR in <<<<<<<parseCurrentIMGJson>>>>>>>");
        }
        return linksIMG;
    }
}