package it.unipi.movieland.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GenreRecommendationsDTO {
    private String _id;
    private List<MovieDTO> recommended_movies;


    @Setter
    @Getter
    public static class MovieDTO {


        private String _id;
        private String title;
        private String poster_path;
        private Integer imdb_votes;
        private String match_score;

    }
}
