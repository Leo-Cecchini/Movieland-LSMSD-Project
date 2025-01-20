package it.unipi.movieland.applicationMovieland.model.Movie;

import java.util.List;

public class Genre {
    private List<String> genreList;

    public Genre(List<String> genreList) {
        this.genreList = genreList;
    }

    public List<String> getGenreList() {
        return genreList;
    }

    public void setGenreList(List<String> genreList) {
        this.genreList = genreList;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "genreList=" + genreList +
                '}';
    }
}