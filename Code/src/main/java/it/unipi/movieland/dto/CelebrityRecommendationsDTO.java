package it.unipi.movieland.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CelebrityRecommendationsDTO {
    private Integer id;
    private String name;
    private String poster;
    private Integer followers;
    private Integer numRelations;
}
