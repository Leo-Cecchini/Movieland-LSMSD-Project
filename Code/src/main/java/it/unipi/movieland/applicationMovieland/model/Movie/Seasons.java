package it.unipi.movieland.applicationMovieland.model.Movie;

import java.util.List;

public class Seasons {
    private List<String> seasonList;

    public Seasons(List<String> seasonList) {
        this.seasonList = seasonList;
    }

    public List<String> getSeasonList() {
        return seasonList;
    }

    public void setSeasonList(List<String> seasonList) {
        this.seasonList = seasonList;
    }

    @Override
    public String toString() {
        return "Seasons{" +
                "seasonList=" + seasonList +
                '}';
    }
}
