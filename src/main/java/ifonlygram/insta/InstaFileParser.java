package ifonlygram.insta;

import ifonlygram.dto.BlogCategory;
import ifonlygram.dto.InstaTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kristina on 24-Nov-17.
 */
@Component
public class InstaFileParser implements IInstaFileParser {

    @Autowired
    IInstaFileReader fileReader;
    private static final String FILE_PATH = "instaData.txt";

    public List<InstaTag> getAllTagList() {
        List<String> content = fileReader.readLinesFromFile(FILE_PATH);
        List<InstaTag> allTags = new ArrayList<>();
        BlogCategory category = BlogCategory.FASHION;

        for (String item : content) {
            //Ксли строка обознчает тип, то перерисываем тип в переенную category и пропускаем 1 виток цикла
            if (BlogCategory.isContainsElement(item.trim().toUpperCase())) {
                category = BlogCategory.valueOf(item.trim().toUpperCase());
                continue;
            }
            if (("").equals(item.trim())) {
                continue;
            }

            InstaTag newTag = new InstaTag();
            newTag.setCategory(category);
            String[] args = item.split("\\|\\|");
            newTag.setTagName(args[0].trim());

            List<String> descList = new ArrayList<>();
            if (args.length > 1) {
                for (int i = 1; i < args.length; i++) {
                    descList.add(args[i].trim());
                }
            }
            newTag.setDescription(descList);
            allTags.add(newTag);
        }
        return allTags;
    }

}
