package applicationMovieland.controller.Celebrity;

import applicationMovieland.model.Celebrity.Job;
import applicationMovieland.service.Celebrity.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    // Endpoint per ottenere tutti i lavori
    @GetMapping
    public List<Job> getAllJobs() {
        return jobService.getAllJobs();
    }

    // Endpoint per ottenere un lavoro per movieId
    @GetMapping("/{movieId}")
    public ResponseEntity<Job> getJobByMovieId(@PathVariable String movieId) {
        Optional<Job> job = jobService.getJobByMovieId(movieId);
        return job.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint per aggiungere un nuovo lavoro
    @PostMapping
    public ResponseEntity<Job> addJob(@RequestBody Job job) {
        Job addedJob = jobService.addJob(job);
        return ResponseEntity.ok(addedJob);
    }

    // Endpoint per aggiornare un lavoro esistente
    @PutMapping("/{movieId}")
    public ResponseEntity<Job> updateJob(@PathVariable String movieId, @RequestBody Job updatedJob) {
        Job updated = jobService.updateJob(movieId, updatedJob);
        return ResponseEntity.ok(updated);
    }

    // Endpoint per eliminare un lavoro
    @DeleteMapping("/{movieId}")
    public ResponseEntity<Void> deleteJob(@PathVariable String movieId) {
        jobService.deleteJob(movieId);
        return ResponseEntity.noContent().build();
    }
}
