package it.unipi.movieland.dto;

import it.unipi.movieland.model.User.UserMovie;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WatchlistDTO {
    private List<UserMovie> movies;

    public WatchlistDTO(List<UserMovie> movies) {
        this.movies = movies;
    }
}
