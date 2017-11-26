package ifonlygram.util;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

public class SearchTags {
    public List searchTags(String text) throws IOException {
        return lastWords(sortByValue(searchWords(stopWord(), text)));
    }

    private Map<String, Integer> searchWords(List<String> listWords, String text) throws IOException {
        String[] words = text.toLowerCase().replaceAll("[-.?!)(,:]", "").split("\\s");
        Map<String, Integer> counterMap = new HashMap<>();
        for (String word : words) {
            if (!word.isEmpty() && word.length() > 1) {
                if (!listWords.contains(word)) {
                    Integer count = counterMap.get(word);
                    if (count == null) {
                        count = 0;
                    }
                    counterMap.put(word, ++count);
                }
            }
        }
        return counterMap;
    }

    private List<String> stopWord() throws IOException {
        String message = "(предлоги) для, на, по, со, из, от, до, без, над, под, за, при, после, во\n" +
                "(частицы)не, же, то, бы, всего, итого, даже, да, нет\n" +
                "(союзы) или, но, дабы, затем, потом, коли, лишь только\n" +
                "(наречия) как, так, еще, тот, откуда, зачем, почему, значительно\n" +
                "(местоимения) он, мы, его, вы, вам, вас, ее, что, который/ая/ых/ое, их, все, они, я, весь, мне, меня, таким, весь, всех\n" +
                "(сокращения) кб, мб, дн, руб, ул, кв, дн, гг\n" +
                "(междометия) ой, ого, эх, браво, здравствуйте, спасибо, извините, любом\n" +
                "(неопределенные) что-то, какой-то, где-то, как-то, зачем-то, из-за, дальше, ближе, раньше, позже, когда-то\n" +
                "(вводные) скажем, может, допустим, честно говоря, например, на самом деле, однако, вообще, в общем, вероятно\n" +
                "(обобщения и неточные определения) всего, почти, примерно, около, где-то, порядка\n" +
                "(усилители) очень, минимально, максимально, абсолютно, огромный, предельно, сильно, слабо, наиболее, наименьшее, самый\n" +
                "(оценочные) красивый, мягкий, удобный, дорогой, эффективный\n" +
                "(клише и штампы) масса ярких впечатлений, в лучших традициях, ударными темпами, трезвый взгляд, шаг за шагом, так или иначе, сплошь и рядом, направо и налево, туда и сюда, доверие клиентов, решать задачи бизнеса, расширить географию продаж, в настоящее время, в наши дни, в это столетие, в нашем веке, век высоких технологий, сегодня, сейчас\n" +
                "(слабые глаголы) является, есть, иметь, хотеть, содержаться, существует\n" +
                "(фразы с отглагольными существительными) осуществлять, оказывается\n" +
                "(фразы с модальным глаголом) можно продолжать, можно заказать";
        String[] words = message.toLowerCase().replaceAll("[-.?!)(,:/]", "").split("\\s");
        List<String> listWords = new ArrayList<>();
        for (String word : words) {
            if (!word.isEmpty() && word.length() > 1) {
                listWords.add(word);
            }
        }
        return listWords;
    }

    private static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap<>();
        Stream<Map.Entry<K, V>> st = map.entrySet().stream();
        st.sorted(Comparator.comparing(Map.Entry::getValue)).forEach(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }

    private static List lastWords(Map<String, Integer> wordsSortByValue) {
        List<String> listTags = new ArrayList<>();
        List<String> superTags = new ArrayList<>();
        for (Map.Entry<String, Integer> e : wordsSortByValue.entrySet()) {
            listTags.add(e.getKey());
        }
        for (int i = listTags.size(); i >= listTags.size() - 4; ) {
            superTags.add(listTags.get(--i));
        }
        return superTags;
    }
}
