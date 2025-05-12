package it.unipi.movieland.dto.Celebrity;

import it.unipi.movieland.model.Celebrity.CelebrityMongoDB;
import it.unipi.movieland.model.Celebrity.Job;

import java.util.List;

public class CelebrityMongoDto {
    private int _id;
    private String name;
    private List<Job> jobs;
    private int followers;
    private String poster;

    public CelebrityMongoDto(CelebrityMongoDB celebrityMongoDB) {
        this._id = celebrityMongoDB.getId();
        this.name = celebrityMongoDB.getName();
        this.jobs = celebrityMongoDB.getJobs();
        this.followers = celebrityMongoDB.getFollowers();
        this.poster = celebrityMongoDB.getPoster();
    }

    //GETTERS AND SETTERS
    public int getId() { return _id; }
    public void setId(int _id) { this._id = _id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Job> getJobs() { return jobs; }
    public void setJobs(List<Job> jobs) { this.jobs = jobs; }

    public int getFollowers() { return followers; }
    public void setFollowers(int followers) { this.followers = followers; }

    public String getPoster() { return poster; }
    public void setPoster(String poster) { this.poster = poster; }

    public static CelebrityMongoDto fromEntity(CelebrityMongoDB entity) {
        return new CelebrityMongoDto(entity);
    }
}