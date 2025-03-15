package it.unipi.movieland.controller.Celebrity;

import it.unipi.movieland.exception.CelebrityNotFoundException;
import it.unipi.movieland.exception.CelebrityNotFoundInMongoException;
import it.unipi.movieland.exception.CelebrityNotFoundInNeo4jException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import it.unipi.movieland.service.Celebrity.CelebrityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import it.unipi.movieland.dto.Celebrity.CelebrityNeo4JDto;
import it.unipi.movieland.dto.Celebrity.CelebrityMongoDto;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/celebrities")
public class CelebrityController {

    private final CelebrityService celebrityService;

    @Autowired
    public CelebrityController(CelebrityService celebrityService) {
        this.celebrityService = celebrityService;
    }

    //ENDPOINT TO RETRIEVE ALL CELEBRITIES WITH PAGINATION SUPPORT (MONGODB)
    @GetMapping("/")
    public ResponseEntity<Page<CelebrityMongoDto>> getAllCelebritiesMongo(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Max(100) int size) {

        Page<CelebrityMongoDto> result = celebrityService.getAllCelebritiesMongo(PageRequest.of(page, size));
        return ResponseEntity.ok(result);
    }

    //ENDPOINT TO RETRIEVE A CELEBRITY BY THEIR PERSON ID (MONGODB)
    @GetMapping("/{celebrityId}")
    public ResponseEntity<Object> getCelebrityByIdMongo(@PathVariable int celebrityId) {
        try {
            CelebrityMongoDto celebrityDto = celebrityService.getCelebrityByIdMongo(celebrityId);
            return ResponseEntity.ok(celebrityDto);
        } catch (CelebrityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    // ENDPOINT TO SEARCH FOR A CELEBRITY BY KEYWORD OR NAME (MONGODB)
    @GetMapping("/search")
    public ResponseEntity<Object> searchMongo(@RequestParam String text) {
        try
        {
            List<CelebrityMongoDto> celebrities = celebrityService.searchActorsByCharacterMongo(text);
            return ResponseEntity.ok(celebrities);
        }
        catch (RuntimeException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    //ENDPOINT TO CREATE A CELEBRITY
    @PostMapping("/")
    public ResponseEntity<Object> createCelebrity(
            @RequestParam int id,
            @RequestParam String name,
            @RequestParam (required = false) String poster) {
        try {

            String responseMessage = celebrityService.createCelebrityInBothDatabases(id, name, poster);

            Map<String, Object> response = new HashMap<>();
            response.put("message", responseMessage);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(response);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "FAILED TO CREATE CELEBRITY", "details", e.getMessage()));
        }
    }

    //ENDPOINT TO DELETE A CELEBRITY
    @DeleteMapping("/delete/{celebrityId}")
    public ResponseEntity<Object> deleteCelebrityById(@PathVariable int celebrityId) {
        try {
            celebrityService.deleteCelebrityInBothDatabases(celebrityId);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("message", "CELEBRITY WITH ID " + celebrityId + " HAS BEEN DELETED FROM BOTH DATABASES"));

        } catch (CelebrityNotFoundInMongoException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));

        } catch (CelebrityNotFoundInNeo4jException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "FAILED TO DELETE CELEBRITY FROM BOTH DATABASES", "details", e.getMessage()));
        }
    }

    //ENDPOINT TO ADD JOBS TO AN ACTOR
    @PostMapping("/{celebrityId}/jobs/actor")
    public ResponseEntity<Object> updateJobToActor(
            @PathVariable int celebrityId,
            @RequestParam String movie_id,
            @RequestParam String character) {

        return celebrityService.addJobToActor(celebrityId, movie_id, character);
    }

    //ENDPOINT TO ADD JOBS TO A DIRECTOR
    @PostMapping("/{celebrityId}/jobs/director")
    public ResponseEntity<Object> updateJobToDirector(
            @PathVariable int celebrityId,
            @RequestParam String movie_id) {

        return celebrityService.addJobToDirector(celebrityId, movie_id);
    }

    //ENDPOINT TO DELETE A JOB FOR A DIRECTOR OR ACTOR
    @DeleteMapping("/{celebrityId}/jobs/{jobId}")
    public ResponseEntity<Object> deleteJobById(
            @PathVariable int celebrityId,
            @PathVariable String jobId) {

        return celebrityService.removeJobById(celebrityId, jobId);
    }

    //ENDPOINT TO GET A CELEBRITY'S JOBS
    @GetMapping("/{celebrityId}/jobs")
    public ResponseEntity<Object> getJobsForCelebrity(@PathVariable int celebrityId) {
        return celebrityService.getJobsForCelebrity(celebrityId);
    }

    //ENDPOINT FOR UPDATE THE CELEBRITY
    @PutMapping("/update/{celebrityId}")
    public ResponseEntity<Object> updateCelebrity(
            @PathVariable String celebrityId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String poster) {
        return celebrityService.updateCelebrity(celebrityId, name, poster);
    }
}