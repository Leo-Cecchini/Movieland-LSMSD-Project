package it.unipi.movieland.repository.Celebrity;

import it.unipi.movieland.model.Celebrity.Job;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JobRepository {

    private List<Job> jobList = new ArrayList<>();

    // Ottieni tutti i lavori
    public List<Job> getAllJobs() {
        return jobList;
    }

    // Ottieni un lavoro per movieId
    public Optional<Job> getJobByMovieId(String movieId) {
        return jobList.stream()
                .filter(job -> job.getMovieId().equals(movieId))
                .findFirst();
    }

    // Aggiungi un nuovo lavoro
    public Job addJob(Job job) {
        jobList.add(job);
        return job;
    }

    // Modifica un lavoro esistente
    public Job updateJob(String movieId, Job updatedJob) {
        Job existingJob = getJobByMovieId(movieId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        existingJob.setRole(updatedJob.getRole());
        existingJob.setMovieTitle(updatedJob.getMovieTitle());
        existingJob.setCharacter(updatedJob.getCharacter());
        return existingJob;
    }

    // Elimina un lavoro
    public void deleteJob(String movieId) {
        Job job = getJobByMovieId(movieId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        jobList.remove(job);
    }
}
