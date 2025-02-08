package it.unipi.movieland.model.Celebrity;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Job {

    private String role;
    private String movie_id;
    private String movie_title;
    private String character;
    private String job_id;

    // Contatore separato per ogni celebrazione
    private static final Map<Integer, Integer> jobCounterMap = new HashMap<>();

    // Costruttore predefinito
    public Job() {
    }

    // Costruttore parametrizzato
    public Job(CelebrityMongoDB celebrity, String role, String movie_id, String movie_title, String character) {
        if (celebrity == null) {
            throw new IllegalArgumentException("Celebrity cannot be null");
        }
        this.role = role;
        this.movie_id = movie_id;
        this.movie_title = movie_title;
        if ("Director".equals(role)) {
            this.character = null;
        } else {
            this.character = character;
        }

        // Utilizzo del contatore separato per ogni celebrità
        this.job_id = generateJobId(celebrity.getId());
    }

    // Metodo per generare un ID job unico per la celebrità
    private String generateJobId(int celebrityId) {
        int jobNumber = jobCounterMap.getOrDefault(celebrityId, 0) + 1;
        jobCounterMap.put(celebrityId, jobNumber);
        return celebrityId + "_job" + jobNumber;
    }

    // Getters e Setters
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    public String getMovie_title() {
        return movie_title;
    }

    public void setMovie_title(String movie_title) {
        this.movie_title = movie_title;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    @Override
    public String toString() {
        return "Job{" +
                "role='" + role + '\'' +
                ", movieId='" + movie_id + '\'' +
                ", movieTitle='" + movie_title + '\'' +
                ", character='" + character + '\'' +
                ", jobId='" + job_id + '\'' +
                '}';
    }
}