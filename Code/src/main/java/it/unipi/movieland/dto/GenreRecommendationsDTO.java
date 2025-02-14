package it.unipi.movieland.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class GenreRecommendationsDTO {
    private String _id;
    private List<MovieDTO> recommended_movies;

    public String get_id() {
        return _id;
    }

    public List<MovieDTO> getRecommended_movies() {
        return recommended_movies;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setRecommended_movies(List<MovieDTO> recommended_movies) {
        this.recommended_movies = recommended_movies;
    }

    public GenreRecommendationsDTO() {}

    public GenreRecommendationsDTO(String _id) {
        this._id = _id;
        this.recommended_movies = new ArrayList<MovieDTO>();
    }

    public static class MovieDTO {
        private String _id;
        private String title;
        private String poster_path;
        private Integer imdb_votes;
        private String match_score;

        public String get_id() {
            return _id;
        }

        public String getTitle() {
            return title;
        }

        public Integer getImdb_votes() {
            return imdb_votes;
        }

        public String getMatch_score() {
            return match_score;
        }

        public String getPoster_path() {
            return poster_path;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setImdb_votes(Integer imdb_votes) {
            this.imdb_votes = imdb_votes;
        }

        public void setMatch_score(String match_score) {
            this.match_score = match_score;
        }

        public void setPoster_path(String poster_path) {
            this.poster_path = poster_path;
        }

        public MovieDTO() {}

        public MovieDTO(String _id, String title, Integer imdb_votes, String match_score) {
            this._id = _id;
            this.title = title;
            this.imdb_votes = imdb_votes;
            this.match_score = match_score;
        }
    }
}
