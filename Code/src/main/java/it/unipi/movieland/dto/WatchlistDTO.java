package it.unipi.movieland.dto;

import it.unipi.movieland.model.User.UserMovie;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class WatchlistDTO {
    private List<UserMovie> movies;

    public WatchlistDTO(List<UserMovie> movies) {
        this.movies = movies;
    }
    public WatchlistDTO() {
        this.movies = new ArrayList<>();
    }

    public List<UserMovie> getMovies() {
        return movies;
    }
    public void setMovies(List<UserMovie> movies) {}
}
