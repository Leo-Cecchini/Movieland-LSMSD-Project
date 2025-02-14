package it.unipi.movieland.dto;

import it.unipi.movieland.model.CountryEnum;
import lombok.Getter;
import lombok.Setter;

public class UserRecommendationsDTO {
    private String username;
    private CountryEnum country;
    private Integer followers;
    private Integer numRelations;

    public String getUsername() {
        return username;
    }

    public CountryEnum getCountry() {
        return country;
    }

    public Integer getFollowers() {
        return followers;
    }

    public Integer getNumRelations() {
        return numRelations;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCountry(CountryEnum country) {
        this.country = country;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public void setNumRelations(Integer numRelations) {
        this.numRelations = numRelations;
    }

    public UserRecommendationsDTO() {}

    public UserRecommendationsDTO(String username, CountryEnum country, Integer followers, Integer numRelations) {
        this.username = username;
        this.country = country;
        this.followers = followers;
        this.numRelations = numRelations;
    }
}
