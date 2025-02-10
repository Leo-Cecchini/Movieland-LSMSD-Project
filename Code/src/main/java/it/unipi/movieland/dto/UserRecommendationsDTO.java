package it.unipi.movieland.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRecommendationsDTO {
    private String username;
    private String country;
    private Integer followers;
    private Integer numRelations;
}
