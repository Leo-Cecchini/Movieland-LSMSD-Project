package it.unipi.movieland.model.Movie;

import java.util.List;

public class Platform {
    private List<String> platformList;

    public Platform(List<String> platformList) {
        this.platformList = platformList;
    }

    public List<String> getPlatformList() {
        return platformList;
    }

    public void setPlatformList(List<String> platformList) {
        this.platformList = platformList;
    }

    @Override
    public String toString() {
        return "Platform{" +
                "platformList=" + platformList +
                '}';
    }
}
