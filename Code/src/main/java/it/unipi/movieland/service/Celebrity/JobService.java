package applicationMovieland.service.Celebrity;

import applicationMovieland.model.Celebrity.Job;
import applicationMovieland.repository.Celebrity.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    // Ottieni tutti i lavori
    public List<Job> getAllJobs() {
        return jobRepository.getAllJobs();
    }

    // Ottieni un lavoro per movieId
    public Optional<Job> getJobByMovieId(String movieId) {
        return jobRepository.getJobByMovieId(movieId);
    }

    // Aggiungi un nuovo lavoro
    public Job addJob(Job job) {
        return jobRepository.addJob(job);
    }

    // Modifica un lavoro esistente
    public Job updateJob(String movieId, Job updatedJob) {
        return jobRepository.updateJob(movieId, updatedJob);
    }

    // Elimina un lavoro
    public void deleteJob(String movieId) {
        jobRepository.deleteJob(movieId);
    }
}
