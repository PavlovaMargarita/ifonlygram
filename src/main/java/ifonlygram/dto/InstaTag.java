package ifonlygram.dto;

import java.util.List;

/**
 * Created by kristina on 24-Nov-17.
 */
public class InstaTag{

    private String tagName;
    private List<String> description;
    private BlogCategory category;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public BlogCategory getCategory() {
        return category;
    }

    public void setCategory(BlogCategory category) {
        this.category = category;
    }
}
