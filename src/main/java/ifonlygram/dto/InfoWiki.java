package ifonlygram.dto;

import java.util.List;

public class InfoWiki {
    private Integer yearObBirth;
    private Integer yearObDeath;
    private List<String> places;
    private String job;
    private List<String> importantPeople;

    public Integer getYearObBirth() {
        return yearObBirth;
    }

    public void setYearObBirth(Integer yearObBirth) {
        this.yearObBirth = yearObBirth;
    }

    public Integer getYearObDeath() {
        return yearObDeath;
    }

    public void setYearObDeath(Integer yearObDeath) {
        this.yearObDeath = yearObDeath;
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

