package it.unipi.movieland.dto.Celebrity;

import it.unipi.movieland.model.Celebrity.Job;

public class JobDto {
    private String jobId;
    private String role;
    private String movieId;
    private String movieTitle;
    private String character;

    public JobDto(String jobId, String role, String movieId, String movieTitle, String character) {
        this.jobId = jobId;
        this.role = role;
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.character = character;
    }

    //GETTER AND SETTER
    public String getJobId() { return jobId; }
    public void setJobId(String jobId) { this.jobId = jobId; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getMovieId() { return movieId; }
    public void setMovieId(String movieId) { this.movieId = movieId; }

    public String getMovieTitle() { return movieTitle; }
    public void setMovieTitle(String movieTitle) { this.movieTitle = movieTitle; }

    public String getCharacter() { return character; }
    public void setCharacter(String character) { this.character = character; }

    public static JobDto fromEntity(Job job) {
        return new JobDto(
                job.getJobId(),
                job.getRole(),
                job.getMovieId(),
                job.getMovieTitle(),
                job.getCharacter()
        );
    }
}