package it.unipi.movieland.model.Celebrity;

import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import java.util.ArrayList;

//
@Document(collection = "Celebrities")
public class CelebrityMongoDB {

    private int _id;
    private String name;
    private List <Job> jobs;
    private int followers;
    private String Poster;

    public CelebrityMongoDB() {
    }

    //Costruttore parametrizzato
    public CelebrityMongoDB(int id, String name, String poster) {
        this._id = id;
        this.name = name;
        this.jobs = new ArrayList<>();
        this.followers = 0;
        this.Poster = poster;
    }

    //Getters e Setters
    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public String getPoster() {
        return Poster;
    }

    public void setPoster(String poster) {
        this.Poster = poster;
    }

    @Override
    public String toString() {
        return "Celebrity{" +
                "id=" + _id +
                ", name='" + name + '\'' +
                ", jobs=" + jobs +
                ", followers=" + followers +
                ", poster='" + Poster + '\'' +
                '}';
    }
}