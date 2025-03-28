package it.unipi.movieland.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"movieMongoDb", "movieNeo4j", "userMongoDb", "userNeo4j", "celebrityMongoDb", "celebrityNeo4j", "reviewMongoDb", "reviewNeo4j"})
public class InconsistenciesDTO {
    private List<String> MovieMongoDb;
    private List<String> UserMongoDb;
    private List<String> CelebrityMongoDb;
    private List<String> ReviewMongoDb;
    private List<String> MovieNeo4j;
    private List<String> CelebrityNeo4j;
    private List<String> UserNeo4j;
    private List<String> ReviewNeo4j;

    public InconsistenciesDTO() {
        this.MovieMongoDb = new ArrayList<String>();
        this.UserMongoDb = new ArrayList<String>();
        this.CelebrityMongoDb = new ArrayList<String>();
        this.ReviewMongoDb = new ArrayList<String>();
        this.MovieNeo4j = new ArrayList<String>();
        this.CelebrityNeo4j = new ArrayList<String>();
        this.UserNeo4j = new ArrayList<String>();
        this.ReviewNeo4j = new ArrayList<String>();
    }
    public InconsistenciesDTO(List<String> movieMongoDb, List<String> userMongoDb, List<String> celebrityMongoDb, List<String> reviewMongoDb, List<String> movieNeo4j, List<String> celebrityNeo4j, List<String> userNeo4j, List<String> reviewNeo4j) {
        this.MovieMongoDb = movieMongoDb;
        this.UserMongoDb = userMongoDb;
        this.CelebrityMongoDb = celebrityMongoDb;
        this.ReviewMongoDb = reviewMongoDb;
        this.MovieNeo4j = movieNeo4j;
        this.CelebrityNeo4j = celebrityNeo4j;
        this.UserNeo4j = userNeo4j;
        this.ReviewNeo4j = reviewNeo4j;
    }

    public List<String> getCelebrityMongoDb() {
        return CelebrityMongoDb;
    }

    public List<String> getMovieMongoDb() {
        return MovieMongoDb;
    }

    public List<String> getMovieNeo4j() {
        return MovieNeo4j;
    }

    public List<String> getCelebrityNeo4j() {
        return CelebrityNeo4j;
    }

    public List<String> getReviewMongoDb() {
        return ReviewMongoDb;
    }

    public List<String> getUserMongoDb() {
        return UserMongoDb;
    }

    public List<String> getReviewNeo4j() {
        return ReviewNeo4j;
    }

    public List<String> getUserNeo4j() {
        return UserNeo4j;
    }

    public void setCelebrityMongoDb(List<String> celebrityMongoDb) {
        CelebrityMongoDb = celebrityMongoDb;
    }

    public void setCelebrityNeo4j(List<String> celebrityNeo4j) {
        CelebrityNeo4j = celebrityNeo4j;
    }

    public void setMovieMongoDb(List<String> movieMongoDb) {
        MovieMongoDb = movieMongoDb;
    }

    public void setMovieNeo4j(List<String> movieNeo4j) {
        MovieNeo4j = movieNeo4j;
    }

    public void setReviewMongoDb(List<String> reviewMongoDb) {
        ReviewMongoDb = reviewMongoDb;
    }

    public void setReviewNeo4j(List<String> reviewNeo4j) {
        ReviewNeo4j = reviewNeo4j;
    }

    public void setUserMongoDb(List<String> userMongoDb) {
        UserMongoDb = userMongoDb;
    }

    public void setUserNeo4j(List<String> userNeo4j) {
        UserNeo4j = userNeo4j;
    }
}
