package ifonlygram.dto;

import java.util.ArrayList;
import java.util.List;

public class InfoWiki {
    private Integer yearOfBirth;
    private Integer yearOfDeath;
    private List<String> places;
    private List<String> jobs;
    private List<String> importantPeople;
    private String avatarURI;

    public InfoWiki() {
        places = new ArrayList<>();
        jobs = new ArrayList<>();
        importantPeople = new ArrayList<>();
    }

    public Integer getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(Integer yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public Integer getYearOfDeath() {
        return yearOfDeath;
    }

    public void setYearOfDeath(Integer yearOfDeath) {
        this.yearOfDeath = yearOfDeath;
    }

    public List<String> getPlaces() {
        return places;
    }

    public void setPlaces(List<String> places) {
        this.places = places;
    }

    public List<String> getJobs() {
        return jobs;
    }

    public void setJobs(List<String> jobs) {
        this.jobs = jobs;
    }

    public String getAvatarURI() {
        return avatarURI;
    }

    public void setAvatarURI(String avatarURI) {
        this.avatarURI = avatarURI;
    }

    public List<String> getImportantPeople() {
        return importantPeople;
    }

    public void setImportantPeople(List<String> importantPeople) {
        this.importantPeople = importantPeople;
    }

    public void addPlaces(List<String> placeList) {
        for(String place:placeList) {
            places.add(place);
        }
    }

    public void addJobs(List<String> jobsList){
        for(String job:jobsList){
            jobs.add(job);
        }
    }
}

