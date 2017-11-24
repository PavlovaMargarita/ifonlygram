package ifonlygram.dto;

import java.util.ArrayList;
import java.util.List;

public class Profile {
    private String name;
    private String profilePicture;
    private List<Publication> publications = new ArrayList<Publication>();

    public Profile() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public List<Publication> getPublications() {
        return publications;
    }

    public void setPublications(List<Publication> publications) {
        this.publications = publications;
    }
}
