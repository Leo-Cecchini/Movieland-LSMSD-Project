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
}
