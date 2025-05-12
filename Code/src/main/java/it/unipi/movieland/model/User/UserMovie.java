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

    //GETTERS AND SETTERS
    public String getPoster() {
        return poster;
    }
    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getMovieId() {
        return movieId;
    }
    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "LikedMovie{" +
                "filmId=" + movieId +
                "title=" + title +
                "poster=" + poster +
                '}';
    }
}