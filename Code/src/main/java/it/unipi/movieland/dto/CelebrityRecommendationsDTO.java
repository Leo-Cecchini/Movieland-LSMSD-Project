package it.unipi.movieland.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CelebrityRecommendationsDTO {
    private String id;
    private String name;
    private String poster;
    private Integer followers;
    private Integer numRelations;

    public String getPoster() {
        return poster;
    }

    public Integer getNumRelations() {
        return numRelations;
    }

    public Integer getFollowers() {
        return followers;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setNumRelations(Integer numRelations) {
        this.numRelations = numRelations;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CelebrityRecommendationsDTO() {}

    public CelebrityRecommendationsDTO(String id, String name, Integer followers, Integer numRelations) {
        this.id = id;
        this.name = name;
        this.followers = followers;
        this.numRelations = numRelations;
    }
}
