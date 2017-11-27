package ifonlygram.insta;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kristina on 24-Nov-17.
 */
@Component
public class InstaFileReader implements IInstaFileReader {

    public List<String> readLinesFromFile(String filePath) {
         List<String> allFileStrings= new ArrayList<>();
        try {
            allFileStrings = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8).collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Error in InstaFileReader. Can not read info from file");
        }
        return allFileStrings;
    }
}
