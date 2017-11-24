package ifonlygram.dto;

import java.util.List;

public class InfoWiki {
    private Integer yearOfBirth;
    private Integer yearOfDeath;
    private List<String> places;
    private String job;
    private List<String> importantPeople;

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

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public List<String> getImportantPeople() {
        return importantPeople;
    }

    public void setImportantPeople(List<String> importantPeople) {
        this.importantPeople = importantPeople;
    }
}

