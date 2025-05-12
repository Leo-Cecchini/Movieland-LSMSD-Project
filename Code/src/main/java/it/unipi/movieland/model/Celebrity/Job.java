package it.unipi.movieland.model.Celebrity;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Job {

    private String role;

    @Field("movie_id")
    private String movieId;

    @Field("movie_title")
    private String movieTitle;

    private String character;

    @Field("job_id")
    private String jobId;

    private static final Map<Integer, Integer> jobCounterMap = new HashMap<>();

    public Job() { }

    public Job(CelebrityMongoDB celebrity, String role, String movieId, String movieTitle, String character) {
        if (celebrity == null) {
            throw new IllegalArgumentException("Celebrity cannot be null");
        }
        this.role = role;
        this.movieId = movieId;
        this.movieTitle = movieTitle;

        if ("Director".equals(role)) {
            this.character = null;
        } else {
            this.character = character;
        }

        this.jobId = generateJobId(celebrity.getId());
    }

    private String generateJobId(int celebrityId) {
        int jobNumber = jobCounterMap.getOrDefault(celebrityId, 0) + 1;
        jobCounterMap.put(celebrityId, jobNumber);
        return celebrityId + "_job" + jobNumber;
    }

    //GETTERS AND SETTERS
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    public String getMovieId() {
        return movieId;
    }
    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }
    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getCharacter() {
        return character;
    }
    public void setCharacter(String character) {
        this.character = character;
    }

    public String getJobId() {
        return jobId;
    }
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    @Override
    public String toString() {
        return "Job{" +
                "role='" + role + '\'' +
                ", movieId='" + movieId + '\'' +
                ", movieTitle='" + movieTitle + '\'' +
                ", character='" + character + '\'' +
                ", jobId='" + jobId + '\'' +
                '}';
    }
}