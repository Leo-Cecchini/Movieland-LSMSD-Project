package it.unipi.movieland.model.User;

import org.springframework.data.mongodb.core.mapping.Field;

public class UserMovie {
    @Field("film_id")
    private String movieId;
    private String title;
    private String poster;

    public UserMovie(String movieId, String title, String poster) {
        this.movieId = movieId;
        this.title = title;
        this.poster = poster;
    }

    @Override
    public String toString() {
        return "LikedMovie{" +
                "filmId=" + movieId +
                "title=" + title +
                "poster=" + poster +
                '}';
    }

    public String getPoster() {
        return poster;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}