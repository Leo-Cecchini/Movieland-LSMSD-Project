package it.unipi.movieland.dtotemp;


import it.unipi.movieland.model.Celebrity.Job;
import java.util.List;

public class CelebrityMongoDBDTO {
    private int id;  // ID come int per MongoDB
    private String name;
    private String poster;
    private List<Job> jobs;  // Lista di Job associati alla celebrity

    // Costruttore predefinito
    public CelebrityMongoDBDTO() {}

    // Costruttore con parametri
    public CelebrityMongoDBDTO(int id, String name, String poster, List<Job> jobs) {
        this.id = id;
        this.name = name;
        this.poster = poster;
        this.jobs = jobs;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }
}