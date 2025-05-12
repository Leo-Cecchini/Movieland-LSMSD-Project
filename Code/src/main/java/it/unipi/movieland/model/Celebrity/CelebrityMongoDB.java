package it.unipi.movieland.model.Celebrity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.ArrayList;

@Document(collection = "Celebrities")
public class CelebrityMongoDB {

    @Id
    @Field("_id")
    private int id;

    private String name;
    private List <Job> jobs;
    private int followers;

    @Field("Poster")
    private String poster;

    public CelebrityMongoDB() {}

    public CelebrityMongoDB(int id, String name, String poster) {
        this.id = id;
        this.name = name;
        this.jobs = new ArrayList<>();
        this.followers = 0;
        this.poster = poster;
    }

    //GETTERS AND SETTERS
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Job> getJobs() { return jobs; }
    public void setJobs(List<Job> jobs) { this.jobs = jobs; }

    public int getFollowers() { return followers; }
    public void setFollowers(int followers) { this.followers = followers; }

    public String getPoster() { return poster; }
    public void setPoster(String poster) { this.poster = poster; }

    @Override
    public String toString() {
        return "Celebrity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", jobs=" + jobs +
                ", followers=" + followers +
                ", poster='" + poster + '\'' +
                '}';
    }
}