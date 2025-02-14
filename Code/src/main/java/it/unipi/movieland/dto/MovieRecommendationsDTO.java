package it.unipi.movieland.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieRecommendationsDTO {
    private String id;
    private String title;
    private String poster;
    private Integer votes;
    private Integer numRelations;
    private Integer commonGenres;

    public Integer getNumRelations() {
        return numRelations;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster() {
        return poster;
    }

    public Integer getCommonGenres() {
        return commonGenres;
    }

    public Integer getVotes() {
        return votes;
    }

    public String getId() {
        return id;
    }

    public void setNumRelations(Integer numRelations) {
        this.numRelations = numRelations;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setCommonGenres(Integer commonGenres) {
        this.commonGenres = commonGenres;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public MovieRecommendationsDTO() {}

    public MovieRecommendationsDTO(String id, String title, String poster, Integer votes, Integer numRelations, Integer commonGenres) {
        this.id = id;
        this.title = title;
        this.poster = poster;
        this.votes = votes;
        this.numRelations = numRelations;
        this.commonGenres = commonGenres;
    }
}
